package com.jos.qcloud;

import com.qcloud.PicAnalyze;
import com.qcloud.PicCloud;
import com.qcloud.PicInfo;
import com.qcloud.UploadResult;

public class PictureCloud {
	public static final int APP_ID_V2 = 10014293;
	public static final String SECRET_ID_V2 = "AKIDQeHIvR4sqhhAO8ykxVjdF9Cz3l0moBk8";
	public static final String SECRET_KEY_V2 = "We61ML8CzzUbhDnytuhNdGP5QHlmVhIQ";
    public static final String BUCKET = "webase";
    
//    public static final int APP_ID_V2 = 10000001;
//	public static final String SECRET_ID_V2 = "AKIDNZwDVhbRtdGkMZQfWgl2Gnn1dhXs95C0";
//	public static final String SECRET_KEY_V2 = "ZDdyyRLCLv1TkeYOl5OCMLbyH4sJ40wp";
//    public static final String BUCKET = "testb";        
    
	public static void main(String[] args) {
		PicCloud pc = new PicCloud(APP_ID_V2, SECRET_ID_V2, SECRET_KEY_V2, BUCKET);
		//UploadResult ur = pc.upload("C:\\Users\\Administrator\\Desktop\\QQ图片20151119191814.jpg");
		//ur.print();
		//PicInfo pi = pc.stat("30e3203d-8c0a-4d9c-8da0-3491fed7b7c1");
		//pi.print();
		int i = pc.delete("50f02784-3b9f-409d-abdd-389ff34f3be0");
		System.out.println(i);
	}
}
