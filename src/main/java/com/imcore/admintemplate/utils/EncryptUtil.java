package com.imcore.admintemplate.utils;

import com.imcore.admintemplate.base.enums.ResultCodeEnum;
import com.imcore.admintemplate.exception.model.ImcoreBusinessException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

/**
 * 加密工具类
 */
public class EncryptUtil {

    /**
     * aes加密
     * @param content 需要加密的内容
     * @param strKey  密钥
     * @return
     * @throws ImcoreBusinessException
     */
    public static String aes_encrypt(String content, String strKey) throws ImcoreBusinessException {
        try {
            SecretKey key = generateMySQLAESKey(strKey,"ASCII");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cleartext = content.getBytes("UTF-8");
            byte[] ciphertextBytes = cipher.doFinal(cleartext);
            return new String(Hex.encodeHex(ciphertextBytes));

        } catch (Exception e) {
            e.printStackTrace();
            throw new ImcoreBusinessException(ResultCodeEnum.SYS_ERROR);
        }
    }

    /**
     * aes解密
     * @param content 需要解密的内容
     * @param aesKey   密钥
     * @return
     * @throws ImcoreBusinessException
     */
    public static String aes_decrypt(String content, String aesKey) throws ImcoreBusinessException{
        try {
            SecretKey key = generateMySQLAESKey(aesKey,"ASCII");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] cleartext = Hex.decodeHex(content.toCharArray());
            byte[] ciphertextBytes = cipher.doFinal(cleartext);
            return new String(ciphertextBytes, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
            throw new ImcoreBusinessException(ResultCodeEnum.SYS_ERROR);
        }
    }

    public static SecretKeySpec generateMySQLAESKey(final String key, final String encoding) {
        try {
            final byte[] finalKey = new byte[16];
            int i = 0;
            for(byte b : key.getBytes(encoding))
                finalKey[i++%16] ^= b;
            return new SecretKeySpec(finalKey, "AES");
        } catch(UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        try{
            String str = aes_encrypt("16619710160", "$2a$10$zf.8eEzZv.IgaDNDiaIcyOnnbH13XNnZWHmNhm6nUJfZKV2QLUGzG");
            System.out.println("********************:"+str);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
