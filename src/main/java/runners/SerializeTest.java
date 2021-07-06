package runners;

import java.io.*;

public class SerializeTest {
    public static void main(String[] args) {
        ClassToSerialize c = new SecondClass();
        //ClassToSerialize c = new ClassToSerialize();

        String filename = "src/main/resources/file.ser";

        c.nonSerializable.setI(758);

        // Serialization
        try
        {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(c);

            out.close();
            file.close();

            System.out.println("Object has been serialized");

        }

        catch(IOException ex)
        {
            ex.printStackTrace();
        }


        ClassToSerialize object1 = null;

        // Deserialization
        try
        {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            object1 = (ClassToSerialize) in.readObject();

            in.close();
            file.close();

            System.out.println("Object has been deserialized ");
            System.out.println(object1.get());
            System.out.println(object1.nonSerializable.getI());
        }

        catch(IOException ex)
        {
            ex.printStackTrace();
        }

        catch(ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
    }

    private static class ClassToSerialize implements Serializable {
        private String s = "abc";
        private float f = 2.65f;
        public NonSerializable nonSerializable = new NonSerializable(38);

        public int get(){
            return 10;
        }
    }

    private static class SecondClass extends ClassToSerialize{
        private int i = 23;

        public int get(){
            return i;
        }
    }

    private static class NonSerializable implements Serializable{
        private int i = 64;
        public NonSerializable(int i){
            this.i = i;
        }

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }
    }
}
