package com.briup.cms;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * @author YuYan
 * @date 2023-11-30 16:24:23
 */
public class MD5Test {

    public static void main(String[] args) {
        // String salt = "adjiofjsaiodf";
        // String password = "123456";
        // 加密
        // String md5Str = DigestUtils.md5DigestAsHex((password + salt).getBytes(StandardCharsets.UTF_8));
        // System.out.println(md5Str);

        // 两个增加密码安全性的操作
        // 加密强度 到达10 以上会明显地影响性能
        int strength = 10;
        // 干扰因子
        String secret = "briup-123";
        SecureRandom secureRandom = new SecureRandom(secret.getBytes(StandardCharsets.UTF_8));

        // SpringSecurity
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(strength, secureRandom);
        String password = "123456";
        System.out.println(DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)));
        System.out.println(DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)));
        System.out.println(DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)));
        String encode1 = encoder.encode(password);
        String encode2 = encoder.encode(password);
        String encode3 = encoder.encode(password);
        System.out.println(encode1);
        System.out.println(encode2);
        System.out.println(encode3);

        String submitPassword = "123456";
        boolean b1 = encoder.matches(submitPassword, encode1);
        boolean b2 = encoder.matches(submitPassword, encode2);
        boolean b3 = encoder.matches(submitPassword, encode3);
        System.out.println(b1);
        System.out.println(b2);
        System.out.println(b3);
    }
}
