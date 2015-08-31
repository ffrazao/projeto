package br.gov.df.emater.aterwebsrv.ferramenta;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.DigestUtils;

public final class Criptografia {

	public static final synchronized String md5(String text) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.update(text.getBytes(), 0, text.length());
		String result = new BigInteger(1, messageDigest.digest()).toString(16);
		if (result.length() < 32) {
			result = "0" + result;
		}
		return result;
	}

	public static final synchronized String md5File(byte[] bytes) throws IOException {
		return DigestUtils.md5DigestAsHex(bytes);
	}
	
	public static void main(String[] args) {
		// 20141103_070304.jpg
//		Pattern p = Pattern.compile("/^\\d{8}\\_\\d{6}\\.jpg$/");
		Pattern p = Pattern.compile("\\d{8}\\_\\d{6}");
		String arqs = Arrays.toString(new File("I:\\fotos\\2015\\camera").list());
		Matcher m = p.matcher(arqs);
		
		List<String> result = new ArrayList<String>();
		while (m.find()) {
			result.add(m.group());
		}
		System.out.println(result);
	}
	

}
