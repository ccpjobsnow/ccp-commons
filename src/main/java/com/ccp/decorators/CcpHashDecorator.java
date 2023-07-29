package com.ccp.decorators;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

public class CcpHashDecorator {
	public final String content;

	protected CcpHashDecorator(String content) {
		this.content = content;
	}

	public String toString() {
		return this.content;
	}

	public String toThisHour() {
		Calendar cal = new CcpTimeDecorator().getBrazilianCalendar();

		int hora = cal.get(Calendar.HOUR_OF_DAY);
		int dia = cal.get(Calendar.DAY_OF_MONTH);
		int minuto = cal.get(Calendar.MINUTE);
		int mes = cal.get(Calendar.MONTH);
		int ano = cal.get(Calendar.YEAR);

		if (minuto >= 30) {
			hora++;
			hora %= 24;
		}	
		String format = String.format("%s-%s-%s-%s-%s", ano, mes, dia, hora, this.content);
		String hash = new CcpHashDecorator(format).asString("SHA1");
		return hash;
	}
	
	public String toThisDay() {

		Calendar cal = new CcpTimeDecorator().getBrazilianCalendar();

		int dia = cal.get(Calendar.DAY_OF_MONTH);
		int mes = cal.get(Calendar.MONTH);
		int ano = cal.get(Calendar.YEAR);
		
		
		String format = ano + " " + mes + " " + dia + " " + this.content;
		String hash = new CcpHashDecorator(format).asString("SHA1");
		return hash;
		
	}

	
	public String asString(String algorithm) {
		BigInteger bi = this.asBigInteger(algorithm);

		String strHash = bi.toString(16).toLowerCase();

		return strHash;
	}
	public BigInteger asBigInteger(String algorithm) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		byte[] bytes = this.content.getBytes(StandardCharsets.UTF_8);
		byte[] hash = digest.digest(bytes);
		BigInteger bi = new BigInteger(hash);
		return bi;
	}


}
