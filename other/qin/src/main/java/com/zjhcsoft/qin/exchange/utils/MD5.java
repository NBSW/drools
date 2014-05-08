package com.zjhcsoft.qin.exchange.utils;

/***********************************************************************************************************************
 * MD5 算法的Java Bean Last Modified:10,Mar,2001
 **********************************************************************************************************************/

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/***********************************************************************************************************************
 * md5 类实现了RSA Data Security, Inc.在提交给IETF 的RFC1321中的MD5 message-digest 算法。
 **********************************************************************************************************************/

public class MD5
{
	private static Logger log = LoggerFactory.getLogger(MD5.class);
	
	public static final String KEY = ""; //去除

	public final static String str2MD5(String _str)
	{
		char hexDigits[] =
		{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try
		{
			byte[] strTemp = _str.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++)
			{
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e)
		{
			return null;
		}
	}
	
	public static void main(String args[]){
		//85254b94a002e085eaabbe748f221b43
		log.debug(str2MD5("longTest"));
	}
}
