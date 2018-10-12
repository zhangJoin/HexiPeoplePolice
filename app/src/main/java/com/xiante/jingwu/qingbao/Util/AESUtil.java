package com.xiante.jingwu.qingbao.Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by zhong on 2018/7/25.
 */

public class AESUtil {

    private static final String defaultCharset = "utf-8";
    private static final String KEY_AES = "AES";
    private static final char [] legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/" .toCharArray();
    /**
     * 加密
     *
     * @param data 需要加密的内容
     * @param key 加密密码
     * @return
     */
    public static String encrypt(String data, String key) {
        return doAES(data, key, Cipher.ENCRYPT_MODE);
    }

    /**
     * 解密
     *
     * @param data 待解密内容
     * @param key 解密密钥
     * @return
     */
    public static String decrypt(String data, String key) {
        return doAES(data, key, Cipher.DECRYPT_MODE);
    }

    /**
     * 加解密
     *
     * @param data 待处理数据
     * @param mode 加解密mode
     * @return
     */
    private static String doAES(String data, String key, int mode) {
        try {
            if (IsNullOrEmpty.isEmpty(data) || IsNullOrEmpty.isEmpty(key)) {
                return null;
            }
            //判断是加密还是解密
            boolean encrypt = mode == Cipher.ENCRYPT_MODE;
            byte[] content;
            //true 加密内容 false 解密内容
            if (encrypt) {
                content = data.getBytes(defaultCharset);
            } else {
                content = parseHexStr2Byte(data);
            }
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator kgen = KeyGenerator.getInstance(KEY_AES);
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            kgen.init(128, new SecureRandom(key.getBytes()));
            //3.产生原始对称密钥
            SecretKey secretKey = kgen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte[] enCodeFormat = secretKey.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, KEY_AES);
            //6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance(KEY_AES);// 创建密码器
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(mode, keySpec);// 初始化
            byte[] result = cipher.doFinal(content);
            if (encrypt) {
                  return parseByte2HexStr(result);
            } else {
                return new String(result, defaultCharset);
            }
        } catch (Exception e) {

        }
        return null;
    }
    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toLowerCase());
        }

        return sb.toString();
    }
    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }



    /**
     * AES加密字符串
     *
     * @param content
     *            需要被加密的字符串
     * @param password
     *            加密需要的密码
     * @return 密文
     */
    public static String encrypt2(String content, String password) {
        try {
            // 生成key
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128,new SecureRandom(password.getBytes()));
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keybytes = secretKey.getEncoded();

            // key的转换
            Key key = new SecretKeySpec(keybytes, "AES");
            // 加密
            // AES/工作模式/填充方式
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(content.getBytes());
            result = cipher.doFinal(result);

            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodebyte=cipher.doFinal(result);
            String destr=new String(decodebyte);
            return parseByte2HexStr(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 解密AES加密过的字符串
     *
     * @param content
     *            AES加密过过的内容
     * @param password
     *            加密时的密码
     * @return 明文
     */
    public static String decrypt2(String content, String password) {
        try {
            // 生成key
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128,new SecureRandom(password.getBytes()));
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keybytes = secretKey.getEncoded();
            // key的转换
            Key key = new SecretKeySpec(keybytes, "AES");
            // AES/工作模式/填充方式
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            // 解密
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] psd=parseHexStr2Byte(content);
           byte[] result = cipher.doFinal(psd);
            System.out.println("jdk aes decrypt : " + new String(result));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    public static String encode3( byte [] data) {
        int start = 0 ;
        int len = data.length;
        StringBuffer buf = new StringBuffer(data.length * 3 / 2 );

        int end = len - 3 ;
        int i = start;
        int n = 0 ;

        while (i <= end) {
            int d = (((( int ) data[i]) & 0x0ff ) << 16 ) | (((( int ) data[i + 1 ]) & 0x0ff ) << 8 ) | ((( int ) data[i + 2 ]) & 0x0ff );

            buf.append(legalChars[(d >> 18 ) & 63 ]);
            buf.append(legalChars[(d >> 12 ) & 63 ]);
            buf.append(legalChars[(d >> 6 ) & 63 ]);
            buf.append(legalChars[d & 63 ]);

            i += 3 ;

            if (n++ >= 14 ) {
                n = 0 ;
                buf.append( " " );
            }
        }

        if (i == start + len - 2 ) {
            int d = (((( int ) data[i]) & 0x0ff ) << 16 ) | (((( int ) data[i + 1 ]) & 255 ) << 8 );

            buf.append(legalChars[(d >> 18 ) & 63 ]);
            buf.append(legalChars[(d >> 12 ) & 63 ]);
            buf.append(legalChars[(d >> 6 ) & 63 ]);
            buf.append( "=" );
        } else if (i == start + len - 1 ) {
            int d = ((( int ) data[i]) & 0x0ff ) << 16 ;

            buf.append(legalChars[(d >> 18 ) & 63 ]);
            buf.append(legalChars[(d >> 12 ) & 63 ]);
            buf.append( "==" );
        }

        return buf.toString();
    }

    private static int decode( char c) {
        if (c >= 'A' && c <= 'Z' )
            return (( int ) c) - 65 ;
        else if (c >= 'a' && c <= 'z' )
            return (( int ) c) - 97 + 26 ;
        else if (c >= '0' && c <= '9' )
            return (( int ) c) - 48 + 26 + 26 ;
        else
            switch (c) {
                case '+' :
                    return 62 ;
                case '/' :
                    return 63 ;
                case '=' :
                    return 0 ;
                default :
                    throw new RuntimeException( "unexpected code: " + c);
            }
    }

    /**
     * Decodes the given Base64 encoded String to a new byte array. The byte array holding the decoded data is returned.
     */

    public static byte [] decode3(String s) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            decode(s, bos);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        byte [] decodedBytes = bos.toByteArray();
        try {
            bos.close();
            bos = null ;
        } catch (IOException ex) {
            System.err.println( "Error while decoding BASE64: " + ex.toString());
        }
        return decodedBytes;
    }

    private static void decode(String s, OutputStream os) throws IOException {
        int i = 0;

        int len = s.length();

        while (true) {
            while (i < len && s.charAt(i) <= ' ')
                i++;

            if (i == len)
                break;

            int tri = (decode(s.charAt(i)) << 18) + (decode(s.charAt(i + 1)) << 12) + (decode(s.charAt(i + 2)) << 6) + (decode(s.charAt(i + 3)));

            os.write((tri >> 16) & 255);
            if (s.charAt(i + 2) == '=')
                break;
            os.write((tri >> 8) & 255);
            if (s.charAt(i + 3) == '=')
                break;
            os.write(tri & 255);

            i += 4;
        }


    }

    /**对字符串进行MD5编码*/
    public static String encodeByMD5(String originString){
        if (originString!=null) {
            try {
                //创建具有指定算法名称的信息摘要
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                //使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
                byte[] results = md5.digest(originString.getBytes());
                //将得到的字节数组变成字符串返回
                String result = parseByte2HexStr(results);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static String jdkAESencry(String word) {

        try {

            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128,new SecureRandom("esint".getBytes()));
            // 生成key
           // KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keybytes = secretKey.getEncoded();

            // key的转换
            Key key = new SecretKeySpec(keybytes, "AES");

            // 加密
            // AES/工作模式/填充方式
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(word.getBytes());
           String w=parseByte2HexStr(result);

//           cipher.init(Cipher.DECRYPT_MODE,key);
//           byte[] debyte=cipher.doFinal(parseHexStr2Byte(w));
//           String emp=new String(debyte);
           return w;
        } catch (Exception e) {
            e.printStackTrace();
        }
         return null;
    }

    public static void jdkAESdecry(String word) {

        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128,new SecureRandom("esint".getBytes()));
            // 生成key
//            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//            keyGenerator.init(128);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keybytes = secretKey.getEncoded();

            // key的转换
            Key key = new SecretKeySpec(keybytes, "AES");

            // 加密
            // AES/工作模式/填充方式
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
           byte[] result = cipher.doFinal(parseHexStr2Byte(word));
            System.out.println("jdk aes decrypt : " + new String(result));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    }
