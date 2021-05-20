package com.unizar.game;

import com.unizar.Main;

import java.io.*;

/**
 * Can save and load a Data class.
 * Only one save (currently)
 */
public class DataSaver {

    private static String FILE() {
        return Main.root + ".save";
    }

    /**
     * Saves the current data. Overrides existing ones.
     *
     * @param world data to save
     */
    public final void saveData(World world) {
        try {
            FileOutputStream fout = new FileOutputStream(FILE());
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(world);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the saved data, or null if no data is saved
     */
    public final World loadData() {
        try {
            ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(FILE()));
            Object o = ois.readObject();
            ois.close();
            return (World) o;
        } catch (FileNotFoundException ignored) {
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
