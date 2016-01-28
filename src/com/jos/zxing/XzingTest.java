package com.jos.zxing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class XzingTest {
	public static void main(String[] args) {
		QRCodeWriter writer = new QRCodeWriter();
	    int width = 256, height = 256;
	    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); // create an empty image
	    int white = 255 << 16 | 255 << 8 | 255;
	    int black = 0;
	    try {
	    	String contents = "一个个长得那么丑，还要人发红包看，想要知道怎么玩二维码的，发个红包给肖哥哥就行啦！";
	    	contents=new String(contents.getBytes("UTF-8"),"ISO-8859-1");
	        BitMatrix bitMatrix = writer.encode(contents, BarcodeFormat.QR_CODE, width, height);
	        for (int i = 0; i < width; i++) {
	            for (int j = 0; j < height; j++) {
	                image.setRGB(i, j, bitMatrix.get(i, j) ? black : white); // set pixel one by one
	            }
	        }
	  
	        try {
	            ImageIO.write(image, "jpg", new File("C:\\Users\\Administrator\\Desktop\\图片\\erweima.jpg")); // save QR image to disk
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	  
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	}
}
