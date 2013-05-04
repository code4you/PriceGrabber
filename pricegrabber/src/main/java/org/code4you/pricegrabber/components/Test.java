package org.code4you.pricegrabber.components;

import java.io.UnsupportedEncodingException;

import org.code4you.pricegrabber.parser.ContentParser;

public class Test {

	public static void main(String[] args) throws UnsupportedEncodingException {
		String s = " –";
		String s2 = "''";
		byte[] bytes = s.getBytes("UTF-8");
		byte[] b2 = s2.getBytes("UTF-8");
		for (byte b : bytes) {
			System.out.println(b);
			System.out.printf("%02X\n",b);  
		}
		System.out.println();
		for (byte b : b2) {
			System.out.println(b);
			System.out.printf("%02X\n",b);  
		}
		
		
	}


}
