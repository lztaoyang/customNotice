package com.lazhu.generate.util;

import java.text.DecimalFormat;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(Integer.MAX_VALUE);
		System.out.println(Long.MAX_VALUE);
		System.out.println(new DecimalFormat("#,##0.00").format(Float.MAX_VALUE).length());
		System.out.println(new DecimalFormat("#,##0.00").format(Double.MAX_VALUE).length());
	}
}
