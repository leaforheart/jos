package com.qcloud.cosapi;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import com.qcloud.cosapi.api.*;
import com.qcloud.cosapi.api.CosCloud.FolderPattern;
import com.qcloud.cosapi.common.Sign;
import com.sun.org.apache.bcel.internal.classfile.Field;

public class Demo {
	//通过控制台获取AppId,SecretId,SecretKey
	public static final int APP_ID = 1000000;
	public static final String SECRET_ID = "SECRET_ID";
	public static final String SECRET_KEY = "SECRET_KEY";

	public static void main(String[] args) {
		//分片上传大文件时，应把CosCloud构造方法第4个超时时间参数设置得长些，默认为60秒
		CosCloud cos = new CosCloud(10014293, "AKID5epQ6esXvWwawfiiW4sWDsSMuqWVlwtj", "cvn6eyddWtX3pN7TE4JXxYn9S2fDV1UO");
		try{			
			String result = "";
			String bucketName = "jos";
            long start = System.currentTimeMillis();
            //result = cos.getFolderList(bucketName, "/", 20, "", 0, CosCloud.FolderPattern.Both);
            result = cos.createFolder(bucketName, "/jos/20151211/");
            //result = cos.uploadFile(bucketName, "/jos/20151211/love.jpg", "C:\\Users\\Administrator\\Desktop\\QQ图片20151119191814.jpg");
            //result = cos.updateFile(bucketName, "/sdk/xx.txt", "test file");
            //result = cos.getFileStat(bucketName, "/jos/20151211/love.jpg");
            //result = cos.updateFolder(bucketName, "/sdk/", "test folder");
            //result = cos.getFolderStat(bucketName, "/jos/");
            //result = cos.getFolderList(bucketName, "/jos/20151211/",5,"",0,FolderPattern.File);
            //result = cos.deleteFile(bucketName, "/jos/20151211/wo.rar");
            //result = cos.deleteFolder(bucketName, "/jos/20151211");
//            FileInputStream方式上传
//            cos.deleteFile(bucketName, "/stream1.txt");
//            File file = new File("c:\\script.txt");
//            FileInputStream fileStream = new FileInputStream(file);
//            result = cos.uploadFile(bucketName, "/stream1.txt", fileStream);
//            ByteArrayInputStream方式上传
//            cos.deleteFile(bucketName, "/shitou.txt");
//            ByteArrayInputStream inputStream = new ByteArrayInputStream("woshiyikexiaoxiaodeshitou".getBytes());
//            result = cos.uploadFile(bucketName, "/shitou.txt", inputStream);
//            cos.deleteFile(bucketName, "/CentOS-6.5-i386-bin-DVD1.iso");
//            result = cos.sliceUploadFile(bucketName, "/CentOS-6.5-i386-bin-DVD1.iso", "E:\\QQDownload\\CentOS-6.5-i386-bin-DVD1.iso", 3 * 1024 * 1024);
            long end = System.currentTimeMillis();
            System.out.println(result);
            System.out.println("总用时：" + (end - start) + "毫秒");
			System.out.println("The End!");
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}
