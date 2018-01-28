package org.infinispan.client.creson.serialization;


import org.infinispan.commons.marshall.Marshaller;
import org.infinispan.marshall.core.JBossMarshaller;

/**
 * @author Pierre Sutra
 */
public class Marshalling {

    private static boolean DEFAULT_BOOLEAN;
    private static byte DEFAULT_BYTE;
    private static short DEFAULT_SHORT;
    private static int DEFAULT_INT;
    private static long DEFAULT_LONG;
    private static float DEFAULT_FLOAT;
    private static double DEFAULT_DOUBLE;

    public static Object getDefaultValue(Class clazz) {
        if (clazz.equals(boolean.class)) {
            return DEFAULT_BOOLEAN;
        } else if (clazz.equals(byte.class)) {
            return DEFAULT_BYTE;
        } else if (clazz.equals(short.class)) {
            return DEFAULT_SHORT;
        } else if (clazz.equals(int.class)) {
            return DEFAULT_INT;
        } else if (clazz.equals(long.class)) {
            return DEFAULT_LONG;
        } else if (clazz.equals(float.class)) {
            return DEFAULT_FLOAT;
        } else if (clazz.equals(double.class)) {
            return DEFAULT_DOUBLE;
        } else {
            throw new IllegalArgumentException(
                    "Class type " + clazz + " not supported");
        }
    }

    public static byte[] marshall(Object object) {
        Marshaller marshaller = new JBossMarshaller();
        try {
            if (object instanceof byte[])
                return (byte[]) object;
            return marshaller.objectToByteBuffer(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object unmarshall(Object object) {
        Marshaller marshaller = new JBossMarshaller();
        try {
            if (object instanceof byte[])
                return marshaller.objectFromByteBuffer((byte[]) object);
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
