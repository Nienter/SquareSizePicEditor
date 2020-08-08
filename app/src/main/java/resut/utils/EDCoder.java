package resut.utils;

import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import resut.ExtResConfig;

public class EDCoder {
    //    public static byte key = 0x33;
    public byte[] key = ExtResConfig.resKey; //128bit
    public Key aesKey = getKey(key);
    public final String cipherName = "AES/ECB/PKCS5Padding";
    public Cipher cipher;

    public static Key getKey(byte[] key) {
        try {
            Key key1 = new SecretKeySpec(key, "AES");
            return key1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String base64Encode(byte[] bytes) {
        return new String(Base64.encode(bytes, Base64.DEFAULT));
    }

    public byte[] decode(byte[] in, int s, int len) {
        try {
            byte[] result = getDecodeCipher().doFinal(in, s, len);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Cipher getDecodeCipher(){
        if(cipher == null) {
            try {
                cipher = Cipher.getInstance(cipherName);
                cipher.init(Cipher.DECRYPT_MODE, aesKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cipher;
    }

    public void releaseDecodeCipher(){
        cipher = null;
    }

    public byte[] decode(byte[] in) {
        return decode(in, 0, in.length);
    }

    public byte[] encode(byte[] in, int s, int len) {
        Key secretKey = aesKey;
        try {
            Cipher cipher = Cipher.getInstance(cipherName);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] result = cipher.doFinal(in, s, len);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] encode(byte[] in) {
        return encode(in, 0, in.length);
    }
}
