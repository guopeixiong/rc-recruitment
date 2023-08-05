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
    private static final String PUBLIC_KEY = "xxxxxxxxxxxx";

    /**
     * 私钥
     */
    private static final String PRIVATE_KEY = "1231232sdfwcsadrfxvcseeafzdvdsaf";

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
