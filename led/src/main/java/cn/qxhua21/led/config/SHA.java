package cn.qxhua21.led.config;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class SHA {
    private static MessageDigest md5 = null;
    static {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
        }
    }

    public static String md5(String str) {
        byte[] bs = md5.digest(str.getBytes());
        StringBuilder sb = new StringBuilder(40);
        for(byte x:bs) {
            if((x & 0xff)>>4 == 0) {
                sb.append("0").append(Integer.toHexString(x & 0xff));
            } else {
                sb.append(Integer.toHexString(x & 0xff));
            }
        }
        return sb.toString();
    }

    public static String sha224(String input){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-224");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String base64(String input){
        Base64.Encoder encoder= Base64.getEncoder();
        byte[] data=encoder.encode(input.getBytes());
        return new String(data);
    }

    public static Double getDouble2(Double a){
        BigDecimal bg = new BigDecimal(a).setScale(2, RoundingMode.DOWN);
        return bg.doubleValue();
    }

    public static String getDataMD5(){
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
        Date date=new Date();
        return md5(df.format(date));
    }
    public static String getTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }
}
