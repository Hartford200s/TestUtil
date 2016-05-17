package main;

import com.test.Test;

import Common.ScanAnnoUtil;

public class runScanMarkAnno {
	
	public static void main(String args[]) throws Exception {
		ScanAnnoUtil.getInstance().doScan();
		
		Test.test();
	}
	
	
}
