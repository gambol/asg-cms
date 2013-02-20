package cn.bieshao.common;
import java.math.BigInteger;

public class Cryptor {
	private static final int RADIX = 16;
	private static final String SEED = "0933910847463829232312";

	 public static String encrypted500 = "32a09dd7b255ba6a88";
     public static String encryptedM500 = "32a09dd7b278ba6a88";
	
	public static final String encrypt(String password) {
		if (password == null)
			return "";
		if (password.length() == 0)
			return "";

		BigInteger bi_passwd = new BigInteger(password.getBytes());

		BigInteger bi_r0 = new BigInteger(SEED);
		BigInteger bi_r1 = bi_r0.xor(bi_passwd);

		return bi_r1.toString(RADIX);
	}

	public static final String decrypt(String encrypted) {
		if (encrypted == null)
			return "";
		if (encrypted.length() == 0)
			return "";

		BigInteger bi_confuse = new BigInteger(SEED);

		try {
			BigInteger bi_r1 = new BigInteger(encrypted, RADIX);
			BigInteger bi_r0 = bi_r1.xor(bi_confuse);

			return new String(bi_r0.toByteArray());
		} catch (Exception e) {
			return "";
		}
	}
	
	public static final int decryptAsInt(String encrypted) {
		String d = decrypt(encrypted);
		int re = 0;
		if (d != null) {
			try {
				re = Integer.parseInt(d);
			} catch (Exception e) {
				
			}
		}
		
		return re;
	}

	public static void main(String args[]){
		String ori = "-500";
		String a = encrypt(ori);
		System.out.println(a);
		int b = decryptAsInt("32a09dd7b278ba6a88");
		System.out.println(b);
	}
}
