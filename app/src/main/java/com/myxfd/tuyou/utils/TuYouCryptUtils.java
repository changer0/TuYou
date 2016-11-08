package com.myxfd.tuyou.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Lulu on 2016/11/7.
 */

public class TuYouCryptUtils {
    ///////////////////////////////////////////////////////////////////////////
    // AES 方式1
    ///////////////////////////////////////////////////////////////////////////
    public static byte[] aesEncryptSimple(byte[] data, byte[] key){
        byte[] ret = null;
        if (data != null && key != null) {
            if(data.length > 0 && key.length == 16){
                // AES 128bit = 16bytes
                try {
                    Cipher cipher = Cipher.getInstance("AES");
                    SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
                    cipher.init(Cipher.ENCRYPT_MODE, keySpec);
                    ret = cipher.doFinal(data);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }
    public static byte[] aesDecryptSimple(byte[] data, byte[] key){
        byte[] ret = null;
        if (data != null && key != null) {
            if(data.length > 0 && key.length == 16){
                // AES 128bit = 16bytes
                try {
                    Cipher cipher = Cipher.getInstance("AES");
                    SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
                    cipher.init(Cipher.DECRYPT_MODE, keySpec);
                    ret = cipher.doFinal(data);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

}
