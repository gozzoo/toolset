package toolset.security;

import java.security.*;
import java.util.*;

import toolset.HexString;

public class PasswordUtils {
    static int DEFAULT_PASSWORD_LENGTH  = 8;
    private static final char[] CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@?-_/[]".toCharArray();
    
    static String ALGORITHM = "md5"; // "SHA"

    public static String encrpytPasswd(String password) {
        byte[] hashedPasswd = null; 

        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(password.getBytes());
            hashedPasswd = md.digest();
        }
        catch (NoSuchAlgorithmException ex) {}
    
        return HexString.encode(hashedPasswd);  
    } 
    
    public static boolean passwordMatch(String userPassword, String sentPassword) {
        String encryptedPwd = encrpytPasswd(sentPassword);
        return encryptedPwd.equals(userPassword);
    }
    
    static public String generatePass() {
        char[] buf = new char[DEFAULT_PASSWORD_LENGTH];
        Random rd = new Random();

        for (int i = 0; i < DEFAULT_PASSWORD_LENGTH; i++) { 
            int c = rd.nextInt(CA.length);
            buf[i] = CA[c];
        }
        return new String(buf);
    }
}