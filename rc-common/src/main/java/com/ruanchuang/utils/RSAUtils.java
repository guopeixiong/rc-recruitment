package com.ruanchuang.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

import java.nio.charset.StandardCharsets;

/**
 * RSA加密工具类
 * @Author guopeixiong
 * @Date 2023/8/4
 * @Email peixiongguo@163.com
 */
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
     * 使用RSA公钥加密
     * @param data
     * @return
     */
    public static String encryptByRsa(String data) {
        RSA rsa = SecureUtil.rsa(null, PUBLIC_KEY);
        return rsa.encryptBase64(data, StandardCharsets.UTF_8, KeyType.PublicKey);
    }

    /**
     * 使用RSA私钥解密
     * @param data
     * @return
     */
    public static String decryptByRsa(String data) {
        RSA rsa = SecureUtil.rsa(PRIVATE_KEY, null);
        return rsa.decryptStr(data, KeyType.PrivateKey, StandardCharsets.UTF_8);
    }

}
