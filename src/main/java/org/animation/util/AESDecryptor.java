package org.animation.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESDecryptor {
  private static final String KEY = "2023062720230627";           // 16字节密钥
  private static final String IV = "Mann20230627daoo";            // 16字节IV

  public static String decrypt(String encryptedData) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
    IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes());
    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

    byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
    byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

    return new String(decryptedBytes, "UTF-8").trim();
  }
}