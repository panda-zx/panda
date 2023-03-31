package cn.pandaframework.common.password;

import java.security.MessageDigest;
import java.util.Base64;


/**
 * MD5加解密
 * @author zhaoxin
 * @date   2016年10月14日 下午9:28:00
 */
public class MD5Utils {
	/**
	 * 	将字符串通过 MD5 加密；
	 * @param original	原文；
	 * @return	密文；
	 */
	public static String enMD5(String original){
		//密文
		String ciphertext = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			ciphertext = Base64.getEncoder().encodeToString(md5.digest(original.getBytes("utf-8")));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ciphertext;
	}
}