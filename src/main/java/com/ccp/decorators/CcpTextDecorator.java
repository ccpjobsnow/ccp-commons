package com.ccp.decorators;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class CcpTextDecorator {
	public final String content;

	protected CcpTextDecorator(String content) {
		this.content = content;
	}

	public String completeLeft(char complement, int length) {
		if((length - this.content.length() )<=0) {
			return this.content;
		}
		String x = "";
		for(int k = this.content.length(); k < length; k++) {
			x += complement;
		}
		return x + this.content;

	}
	public String stripAccents() {
		String s = Normalizer.normalize(this.content, Normalizer.Form.NFD);
		s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "").replaceAll("[^\\w\\s.,+-]", "");
		return s;
		
	}
	public String generateToken(long charactersSize) {

		Random random = new Random();
		char[] charArray = this.content.toCharArray();
		StringBuilder sb = new StringBuilder();

		for (int k = 0; k < charactersSize; k++) {

			int indiceAleatorio = random.nextInt(charArray.length);

			char caractereAleatorio = charArray[indiceAleatorio];
			sb.append(caractereAleatorio);
		}

		return sb.toString();
	}
	
	public InputStream getByteArrayInputStream() {
		byte[] byteArrayFromBase64String = this.getByteArrayFromBase64String();
		ByteArrayInputStream is = new ByteArrayInputStream(byteArrayFromBase64String);
		return is;
	}
	
	public byte[] getByteArrayFromBase64String() {
		String[] split = this.content.split(",");
		String str = split[0];

		if (split.length > 1) {
			str = split[1];
		}

		String base64 = str;

		Decoder decoder = Base64.getDecoder();

		byte[] byteArray = decoder.decode(base64);
		return byteArray;
	}
	
	public  ByteArrayInputStream getParameterAsByteArrayInputStream() {

		byte[] byteArray = this.getByteArrayFromBase64String();

		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);

		return byteArrayInputStream;

	}
	public String getMessage(CcpMapDecorator parameters) {

		Map<String, Object> content = parameters.getContent();
		Set<String> keySet = content.keySet();
		String message = new String(this.content);
		for (String key : keySet) {
			String value = parameters.getAsString(key);
			message = message.replace("{" + key + "}", value);
		}
		return message;
	}
	//StringDecorator removeStartingCharacters()
	public String removeStartingCharacters( char c) {
		
		if(this.content == null) {
			return this.content;
		}
		
		if(this.content.startsWith("" + c) == false) {
			return this.content;
		}
		
		String substring = this.content.substring(1);
		String removeStartingCharacters = new CcpTextDecorator(substring).removeStartingCharacters(c);
		return removeStartingCharacters;
	}

	//StringDecorator removeEndingCharacters()
	public String removeEndingCharacters(char c) {
		
		if(this.content == null) {
			return this.content;
		}
		
		if(this.content.endsWith("" + c) == false) {
			return this.content;
		}

		String substring = this.content.substring(0, this.content.length() - 1);
		String removed = new CcpTextDecorator(substring). removeEndingCharacters(c);
		return removed;
	}

	public boolean isLongNumber() {
		try {
			Long.valueOf(this.content);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isDoubleNumber() {
		try {
			Double.valueOf(this.content);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isValidSingleJson() {
		try {
			new CcpMapDecorator(this.content);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
