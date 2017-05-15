package cn.flying.rest.onlinefile.utils;

import java.nio.charset.Charset;

public class MD5Util {
	/***
	 * MD5加码 生成32位md5码
	 */
	public static String string2MD5(String inStr) {
//		MessageDigest md5 = null;
//		try {
//			md5 = MessageDigest.getInstance("MD5");
//		} catch (Exception e) {
//			System.out.println(e.toString());
//			e.printStackTrace();
//			return "";
//		}
//		char[] charArray = inStr.toCharArray();
//		byte[] byteArray = new byte[charArray.length];
//
//		for (int i = 0; i < charArray.length; i++)
//			byteArray[i] = (byte) charArray[i];
//		byte[] md5Bytes = md5.digest(byteArray);
//		StringBuffer hexValue = new StringBuffer();
//		for (int i = 0; i < md5Bytes.length; i++) {
//			int val = ((int) md5Bytes[i]) & 0xff;
//			if (val < 16)
//				hexValue.append("0");
//			hexValue.append(Integer.toHexString(val));
//		}
//		return hexValue.toString();
		
		return  encode(inStr);

	}

	/**
	 * 加密解密算法 执行一次加密，两次解密
	 */
	public static String convertMD5(String inStr) {

		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;

	}
	
	public static String md52String(String inStr){
		return  decode(inStr);
	}

	// 测试主函数
	public static void main(String args[]) {
		String s = new String("123");
		System.out.println("原始：" + s);
		System.out.println("MD5后：" + string2MD5(s));
		System.out.println("加密的：" + convertMD5(s));
		System.out.println("解密的：" + convertMD5(convertMD5("880852fdbe71cfe8f723f28fd64e5e7a")));
	}
	
	
	private static final String key0 = "FECOI()*&<MNCXZPKL";  
    private static final Charset charset = Charset.forName("UTF-8");  
    private static byte[] keyBytes = key0.getBytes(charset);  
      
    public static String encode(String enc){  
        byte[] b = enc.getBytes(charset);  
        for(int i=0,size=b.length;i<size;i++){  
            for(byte keyBytes0:keyBytes){  
                b[i] = (byte) (b[i]^keyBytes0);  
            }  
        }  
        return new String(b);  
    }  
      
    public static String decode(String dec){  
        byte[] e = dec.getBytes(charset);  
        byte[] dee = e;  
        for(int i=0,size=e.length;i<size;i++){  
            for(byte keyBytes0:keyBytes){  
                e[i] = (byte) (dee[i]^keyBytes0);  
            }  
        }  
        return new String(e);  
    }  
  
//    public static void main(String[] args) {  
//        String s="you are right";  
//        String enc = encode(s);  
//        String dec = decode(enc);  
//        System.out.println(enc);  
//        System.out.println(dec);  
//    }  

}
