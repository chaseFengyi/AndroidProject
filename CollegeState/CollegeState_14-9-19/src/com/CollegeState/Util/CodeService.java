package com.CollegeState.Util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class CodeService {
	private static KeyGenerator keygen;
	private static SecretKey secretKey;
	private static Cipher cipher;
	private static CodeService security = null;

	private CodeService() {
	}

	public static CodeService getInstance() throws Exception {
		if (security == null) {
			security = new CodeService();
			keygen = KeyGenerator.getInstance("AES");
			secretKey = keygen.generateKey();
			cipher = Cipher.getInstance("AES");
		}
		return security;
	}

	// ����
	public String JiaMi(String str) throws Exception {
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);

		byte[] src = str.getBytes();
		byte[] enc = cipher.doFinal(src);
		return parseByte2HexStr(enc);
	}

	// ����
	public String JieMi(String str) throws Exception {
		cipher.init(Cipher.DECRYPT_MODE, secretKey);

		byte[] enc = parseHexStr2Byte(str);
		byte[] dec = cipher.doFinal(enc);
		return new String(dec);
	}

	
	 // ��16����ת��Ϊ������
	
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	//��������ת����16����
	 
	
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

}