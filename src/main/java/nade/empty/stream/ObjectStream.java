package nade.empty.stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectStream {
    
    public static void write(File file, Object object) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            
            oos.writeObject(object);
            
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <E> E read(File file, Class<E> clazz) throws IOException, ClassNotFoundException {
        File playerdata = new File("C:\\Users\\case\\Desktop\\playerdata.dat");

        FileInputStream fis = new FileInputStream(playerdata);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        if (!clazz.isInstance(object)) {
            fis.close();
            ois.close();
            return null;
        }
        E result = clazz.cast(object);
        fis.close();
        ois.close();
        return result;
    }
}
