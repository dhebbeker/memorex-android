package info.hebbeker.david.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class StringSerialization
{
    /**
     * Serialize object to byte array and convert to String.
     */
    public static String serialize(final Serializable objectToSerialize) throws IOException
    {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(objectToSerialize);
        objectOutputStream.flush();
        objectOutputStream.close();
        return byteArrayOutputStream.toString();
    }

    /**
     * Decode String to byte array and deserialize.
     */
    public static Object deserialize(final String serializedObject) throws IOException, ClassNotFoundException
    {
        final byte serializedObjectBytes[] = serializedObject.getBytes();
        final ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(serializedObjectBytes));
        final Object deserializedObject = objectInputStream.readObject();
        objectInputStream.close();
        return deserializedObject;
    }
}
