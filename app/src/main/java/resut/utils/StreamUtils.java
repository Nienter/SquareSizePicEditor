package resut.utils;

public class StreamUtils {
    public static byte[] arrayGrowth(byte[] b, int offset) {
        if (offset == b.length) {
            byte[] b2 = new byte[b.length * 2];
            System.arraycopy(b, 0, b2, 0, b.length);
            b = b2;
            b2 = null;
        }
        return b;
    }
}
