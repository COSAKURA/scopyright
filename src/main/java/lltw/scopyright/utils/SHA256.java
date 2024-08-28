package lltw.scopyright.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author Sakura
 */
@Slf4j
public class SHA256 {

    // 盐值长度
    private static final int SALT_LENGTH = 16;

    // 生成 SHA-256 哈希
    public String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(salt);
        byte[] hash = digest.digest(password.getBytes());

        // 将哈希值转换为十六进制字符串
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // 验证密码
    public boolean verifyPassword(String inputPassword, String storedHash, byte[] salt) throws NoSuchAlgorithmException {
        String hashedInputPassword = hashPassword(inputPassword, salt);
        return hashedInputPassword.equals(storedHash);
    }

    // 生成随机盐值
    public static byte[] generateSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);
        return salt;
    }

    // Base64 转换盐值
    public static byte[] base64ToSalt(String base64Salt) {
        return Base64.getDecoder().decode(base64Salt);
    }

    // Base64 转换盐值
    public static String saltToBase64(byte[] salt) {
        return Base64.getEncoder().encodeToString(salt);
    }
}
