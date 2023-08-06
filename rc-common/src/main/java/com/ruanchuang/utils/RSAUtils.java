package com.ruanchuang.utils;

import com.ruanchuang.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * RSA加密工具类
 * @Author guopeixiong
 * @Date 2023/8/4
 * @Email peixiongguo@163.com
 */
@Slf4j
public class RSAUtils {

    /**
     * 公钥
     */
    private static final String PUBLIC_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCkEEX+vDChlA4o" +
            "RQVCdaqxfv8scfiaMOgsi5k4Z/kEWWUE6jI11ijyltOqhOl2fj4vCATQAv5MUj08" +
            "pX69OjKHh4cPAqY2WJAG1r/hAgDZtRlG7wePrEDjmAHF0ZfOteplWGefthXr3a5V" +
            "wZkSwRYwgrcjv0ADXA6t/5PAUzJolpqe7SwMYzsFOXipdUVtV3fPFaKXNljYDmPz" +
            "gyK9M/ClaPfXlJjaBKhvG5jT0KW74jFqHD5VC2CuurlqsCjBZG6FapsOoDgm9aPg" +
            "7RwyGnm7NxIcpfGNMfs4BFSjRWW9Gcs/9IkZ9tghmfeWhfD6u920ewTiIHEKH0H+" +
            "He/OXD4lAgMBAAECggEBAI9OO6WXgi8Q4RDhwQddOn0R/hg3cTxV7edDBNIVV5kp" +
            "8KhLXRJTf67uefLfaAH9oZl5OqLrim7mAuFfeYYWOJ7TjfBZkw+zahLfL/l9w3pm" +
            "yq6ACa3Ae2mRlDLTsfN4SUa+4rzJ0voUx7vWgDu2dwhmlUvfOS32EkiADvIWediK" +
            "Fg2YUUNdaQjJ0IW+dI9P9iuVGV1cBwfL2N1X3Pcqe45S5MJ9qU/ixy+rB56j/ug7" +
            "lPJhbCX5zb3RHrf4avJLyDbcTnLZACJd5wQchPBzv+KHMF+Ae/lyWXkU4x3UuH3j" +
            "4BX2qHIw+w5EBtwS211cKnMZL9gUYu5JQ8SgGUc6zKECgYEA10hXA8KjihKYoiLE" +
            "1GHol71P3NUhudonY76rDvzSXFNeZdtoihjQ+o9hYSfVTwB+FojW+8PuRtJl1Akx" +
            "ffSfrojHoHJ4PaHPfi4sdP4kQrjPlCkLHubR3HaGPXngtkYa+Gdt5Iaulo3CGoz4" +
            "LszQQ+aAtAv9qk+gYabMX8WJ5i0CgYEAwxf8WjAqeNPiR6pXFKG2aWuRuRmtLEEb" +
            "s1BbZ7oIjCWBzXFwjBtoA8d4ZIlUdqOEbGMNDbFKO9hbTCtkUGP+wQoU5b9Uub71" +
            "Xo9LzKSKnMg1sA2HlizensGSSQDi+A08O7p+O9G6OWEUwzzkxdmUk49807n3NJpJ" +
            "j5Kaxjc/6tkCgYB3p+OFWYegVJm1dHHGQe0Pec+LxIKMVC6UiAN3vxoE6umK35qJ" +
            "NyXgPhU39PXghTuPttsW8F3dy0+BvOJfSbn/PlMGYNloGCnFzlAzbpYk56eoh++A" +
            "a3a1fMM8JCO769dNvd8cp6ceUwhRzYycJoaTVuiuKgQBx2QzIoxxps/wuQKBgCI5" +
            "jfqgYoMJ4LNjzZz/9Yku5sSLHZca4OonGbHrr94uIkRRN0ZM9IVdM+CnOtsJOozt" +
            "H3gcdwJipTmKjzGPKTziMOJRG2LufgsVpl2DLBHvCYQWC4gr3O4K1UfbUPXlFIEB" +
            "6W1DEKFKR6MshpGy5t6wGFzoTda7jlYSiPZm8N6ZAoGADzqYoRbG3xGnkcRqRfYW" +
            "kC9tS0xpz3lr5wigcDP+NhzL2JJdG+vmLkW0mzSIOUNlVtwTVwyFJZI9ByvVgMq+" +
            "DlHkAVgehturqIyeVCX2lfI4fsru6088XxaXK4GCmx35VvcJ0Q8M8QhYg2pKDkhD" +
            "B80UJrcbfryEKrgNkjoy+YA=";

    /**
     * 私钥
     */
    private static final String PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCkEEX+vDChlA4o" +
            "RQVCdaqxfv8scfiaMOgsi5k4Z/kEWWUE6jI11ijyltOqhOl2fj4vCATQAv5MUj08" +
            "pX69OjKHh4cPAqY2WJAG1r/hAgDZtRlG7wePrEDjmAHF0ZfOteplWGefthXr3a5V" +
            "wZkSwRYwgrcjv0ADXA6t/5PAUzJolpqe7SwMYzsFOXipdUVtV3fPFaKXNljYDmPz" +
            "gyK9M/ClaPfXlJjaBKhvG5jT0KW74jFqHD5VC2CuurlqsCjBZG6FapsOoDgm9aPg" +
            "7RwyGnm7NxIcpfGNMfs4BFSjRWW9Gcs/9IkZ9tghmfeWhfD6u920ewTiIHEKH0H+" +
            "He/OXD4lAgMBAAECggEBAI9OO6WXgi8Q4RDhwQddOn0R/hg3cTxV7edDBNIVV5kp" +
            "8KhLXRJTf67uefLfaAH9oZl5OqLrim7mAuFfeYYWOJ7TjfBZkw+zahLfL/l9w3pm" +
            "yq6ACa3Ae2mRlDLTsfN4SUa+4rzJ0voUx7vWgDu2dwhmlUvfOS32EkiADvIWediK" +
            "Fg2YUUNdaQjJ0IW+dI9P9iuVGV1cBwfL2N1X3Pcqe45S5MJ9qU/ixy+rB56j/ug7" +
            "lPJhbCX5zb3RHrf4avJLyDbcTnLZACJd5wQchPBzv+KHMF+Ae/lyWXkU4x3UuH3j" +
            "4BX2qHIw+w5EBtwS211cKnMZL9gUYu5JQ8SgGUc6zKECgYEA10hXA8KjihKYoiLE" +
            "1GHol71P3NUhudonY76rDvzSXFNeZdtoihjQ+o9hYSfVTwB+FojW+8PuRtJl1Akx" +
            "ffSfrojHoHJ4PaHPfi4sdP4kQrjPlCkLHubR3HaGPXngtkYa+Gdt5Iaulo3CGoz4" +
            "LszQQ+aAtAv9qk+gYabMX8WJ5i0CgYEAwxf8WjAqeNPiR6pXFKG2aWuRuRmtLEEb" +
            "s1BbZ7oIjCWBzXFwjBtoA8d4ZIlUdqOEbGMNDbFKO9hbTCtkUGP+wQoU5b9Uub71" +
            "Xo9LzKSKnMg1sA2HlizensGSSQDi+A08O7p+O9G6OWEUwzzkxdmUk49807n3NJpJ" +
            "j5Kaxjc/6tkCgYB3p+OFWYegVJm1dHHGQe0Pec+LxIKMVC6UiAN3vxoE6umK35qJ" +
            "NyXgPhU39PXghTuPttsW8F3dy0+BvOJfSbn/PlMGYNloGCnFzlAzbpYk56eoh++A" +
            "a3a1fMM8JCO769dNvd8cp6ceUwhRzYycJoaTVuiuKgQBx2QzIoxxps/wuQKBgCI5" +
            "jfqgYoMJ4LNjzZz/9Yku5sSLHZca4OonGbHrr94uIkRRN0ZM9IVdM+CnOtsJOozt" +
            "H3gcdwJipTmKjzGPKTziMOJRG2LufgsVpl2DLBHvCYQWC4gr3O4K1UfbUPXlFIEB" +
            "6W1DEKFKR6MshpGy5t6wGFzoTda7jlYSiPZm8N6ZAoGADzqYoRbG3xGnkcRqRfYW" +
            "kC9tS0xpz3lr5wigcDP+NhzL2JJdG+vmLkW0mzSIOUNlVtwTVwyFJZI9ByvVgMq+" +
            "DlHkAVgehturqIyeVCX2lfI4fsru6088XxaXK4GCmx35VvcJ0Q8M8QhYg2pKDkhD" +
            "B80UJrcbfryEKrgNkjoy+YA=";

    /**
     * 使用RSA私钥加密
     * @param data
     * @return
     */
    public static String encryptByRsa(String data) {
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(PRIVATE_KEY));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] result = cipher.doFinal(data.getBytes());
            return Base64.encodeBase64String(result);
        } catch (Exception e) {
            log.error("密码加密异常, 异常信息: '{}'", e.getMessage());
            throw new SystemException("密码加密异常");
        }
    }

    /**
     * 使用RSA私钥解密
     * @param data
     * @return
     */
    public static String decryptByRsa(String data) {
        byte[] result;
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(Base64.decodeBase64(PRIVATE_KEY));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec5);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            result = cipher.doFinal(Base64.decodeBase64(data));
        } catch (Exception e) {
            log.error("密码解密异常, 异常信息: '{}'", e.getMessage());
            throw new SystemException("密码解密异常");
        }
        return result == null ? null : new String(result);
    }

}
