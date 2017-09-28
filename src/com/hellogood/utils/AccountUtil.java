package com.hellogood.utils;

import org.apache.commons.lang.math.RandomUtils;

public class AccountUtil {

	public static String make() {
		long cur = System.currentTimeMillis();
		cur = cur * 10000 + RandomUtils.nextInt(10000);
		System.out.println(cur);
		return Long.toHexString(cur);
	}

	public static void main(String[] args) {
		for(int i=0;i<100;i++){
			System.out.println(AccountUtil.make());
		}
	}

}
