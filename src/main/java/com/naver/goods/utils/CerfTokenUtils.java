package com.naver.goods.utils;

import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CerfTokenUtils {

    public static String generateSignature(String clientId, String clientSecret, Long timestamp) {
        // 用下划线（ ）连接提供的值client_id和值。timestamp_
        //示例：如果client_id是“aaaabbbb”并且timestamp是“1643956077762”aaaabbbb_1643956077762
        String password = StringUtils.joinWith("_", clientId, timestamp);
        // bcrypt
        String hashedPw = BCrypt.hashpw(password, clientSecret);
        // base64
        return Base64.getUrlEncoder().encodeToString(hashedPw.getBytes(StandardCharsets.UTF_8));
    }
}
