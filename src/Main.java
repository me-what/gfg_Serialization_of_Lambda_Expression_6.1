import java.io.*;
import java.util.function.Function;

public class Main {
    // 1
    private static void serialize(Serializable obj,
                                  String outputPath)
            throws IOException
    {
        File outputFile = new File(outputPath);
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }
        try (ObjectOutputStream outputStream
                     = new ObjectOutputStream(
                new FileOutputStream(outputFile))) {
            outputStream.writeObject(obj);
        }
    }


    // 2
    private static Object deserialize(String inputPath)
            throws IOException, ClassNotFoundException
    {
        File inputFile = new File(inputPath);
        try (ObjectInputStream inputStream
                     = new ObjectInputStream(
                new FileInputStream(inputFile))) {
            return inputStream.readObject();
        }
    }


    // 3
    private static void serializeAndDeserializeFunction()
            throws Exception
    {
        Function<Integer, String> fn
                = (Function<Integer, String> & Serializable)(n)
                -> "Hello " + n;
        System.out.println("Run original function: "
                + fn.apply(10));

        String path = "./serialized-fn";

        serialize((Serializable)fn, path);
        System.out.println("Serialized function to "
                + path);

        Function<Integer, String> deserializedFn
                = (Function<Integer, String>)deserialize(path);
        System.out.println("Deserialized function from "
                + path);
        System.out.println("Run deserialized function: "
                + deserializedFn.apply(11));
    }


    // 4
    private static void serializeAndDeserializeClass()
            throws Exception
    {
        String path = "./serialized-class";
        serialize(MyImpl.class, path);
        System.out.println("Serialized class to " + path);

        Class<?> myImplClass = (Class<?>)deserialize(path);
        System.out.println("Deserialized class from "
                + path);
        MyInterface instance
                = (MyInterface)myImplClass.newInstance();
        instance.hello("Java");
    }


    // 5
    public static void main(String[] args) throws Exception {
        serializeAndDeserializeFunction();
        serializeAndDeserializeClass();
    }
}