/**
* Development by Cybercom Software Service(Beijing)Co.,Ltd Chengdu Branch <br/>
* which under commissioned by Sichuang changhong electric cod.,ltd.
*
* Sichuang changhong electric cod.,ltd owns the intellectual property rights of <br/>
* software source code, any company or individual without the written permission<br/>
* can not modify, copy and distribute the software (including source code) or related derivatives.
*
* Copyrights:		Sichuang changhong electric cod.,ltd.
* Address:		No. 35, East Mianxing Road, High-tech Park, Mianyang, Sichuan, China
* ZIP:				621000
*
* Development:	Cybercom Software Service(Beijing)Co.,Ltd Chengdu Branch
* Address:		5F, A7, Tianfu Software Park, Tianfu Avenue, High-tech Zone, Sichuan, Chengdu, China
* ZIP:				610041
*
*/
package com.newexcavator.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 工具类: 提供密码加密解密算法等工具方法。
 * 
 * @author yihe02
 * 
 */
public class PasswordUtil {

	private static MessageDigest messageDigest;

	/**
	 * 将密码进行加密处理。处理后的结果不可逆。其中算法要进行两次加密和交叉换位算法。增加破解难度
	 * @param password
	 * @return
	 */
	public static String encode(String password) {
		if (messageDigest == null) {
			try {
				messageDigest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
		}
		messageDigest.reset();
		byte[] buf = messageDigest.digest(password.getBytes());
		//将加密后的结果进行交叉换位
		buf=exchangeByte(buf);
		//再次进行加密和交叉换位
		buf=messageDigest.digest(buf);
		buf=exchangeByte(buf);

		return toString(buf);
	}
	
	/**
	 * 算法与encode(String)相同。增加可以截断出指定长度的字符串。
	 * @param password
	 * @param length
	 * @return
	 */
	public static String encode(String password,Integer length){
		return encode(password).substring(0,length);
	}
	
	/**
	 * 将加密后的结果进行交叉换位，增加破解难度.<br/>
	 * 换位方式: 拆分成为2个登长数组，然后交叉顺序放入新数组中。
	 * @param buf
	 * @return
	 */
	private static byte[] exchangeByte(byte[] buf){
		byte[] rlt=new byte[buf.length];
		int size=buf.length/2;
		int position=0;
		for(int i=0;i<size;i++){
			rlt[position++]=buf[i];
			rlt[position++]=buf[i+size];
		}
		return rlt;
	}

	/**
	 * 将生成的加密串转换成为字符串输出
	 * @param buf
	 * @return
	 */
	public static String toString(byte[] buf) {
		StringBuilder sb = new StringBuilder();
		for (byte b : buf) {
			sb.append(Integer.toHexString(0xFF & b));
		}
		return sb.toString();
	}
	
	public static void main(String [] args) {
		System.out.println(PasswordUtil.encode("888888"));
	}
}
