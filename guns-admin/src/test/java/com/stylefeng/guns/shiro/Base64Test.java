package com.stylefeng.guns.shiro;

import org.springframework.util.Base64Utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Base64Test {

    /**
     * Shiro 记住密码采用的是AES加密，AES key length 需要是16位，该方法生成16位的key
     */
    public static void main(String[] args) {
    	
        String keyStr = "guns";
        
        byte[] keys;
        keys = keyStr.getBytes(StandardCharsets.UTF_8);
        System.out.println(Base64Utils.encodeToString(Arrays.copyOf(keys, 16)));

    }

}
