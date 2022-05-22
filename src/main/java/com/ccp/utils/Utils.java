package com.ccp.utils;

public class Utils {
	public static void sleep(int i) {
		
		if(i <= 0) {
			return;
		}
		
		try {
			Thread.sleep(i);
		} catch (Exception e) {
		}

	}

}
