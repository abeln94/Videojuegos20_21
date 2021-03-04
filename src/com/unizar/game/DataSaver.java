package com.unizar.game;

import java.io.*;
import java.util.Base64;

public class DataSaver {

    private String savedData;

    public final void saveData(Data data) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(data);
            oos.close();
            savedData = Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final Data loadData() {
        try {
            byte[] data = Base64.getDecoder().decode(savedData);
            ObjectInputStream ois = new ObjectInputStream(
                    new ByteArrayInputStream(data));
            Object o = ois.readObject();
            ois.close();
            return (Data) o;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
