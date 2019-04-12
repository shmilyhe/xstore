package com.eshore.xmlstore;

public class TEstMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String dir="d:\\wer\\werw\\WEB-INF\\lib";
		int off=dir.indexOf("WEB-INF");
		if(off>-1){
			dir=dir.substring(0, off-1);
		}
		System.out.println(dir);
	}

}
