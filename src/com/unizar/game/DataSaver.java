package com.unizar.game;

import java.io.*;
import java.util.Base64;

/**
 * Can save and load a Data class.
 * Only one save (currently)
 */
public class DataSaver {

    /**
     * The saved data
     */
    private String savedData = null;

    /**
     * Saves the current data. Overrides existing ones.
     *
     * @param world data to save
     */
    public final void saveData(World world) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(world);
            oos.close();
            savedData = Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the saved data, or null if no data is saved
     */
    public final World loadData() {
        if (savedData == null) return null;
        try {
            byte[] data = Base64.getDecoder().decode(savedData);
            ObjectInputStream ois = new ObjectInputStream(
                    new ByteArrayInputStream(data));
            Object o = ois.readObject();
            ois.close();
            return (World) o;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
