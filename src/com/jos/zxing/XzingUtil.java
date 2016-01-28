package com.jos.zxing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.struts2.ServletActionContext;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class XzingUtil {
	public static String getQRCode(String content,int size) {
		QRCodeWriter writer = new QRCodeWriter();
	    BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB); // create an empty image
	    int white = 255 << 16 | 255 << 8 | 255;
	    int black = 0;
	    try {
	        BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size);
	        for (int i = 0; i < size; i++) {
	            for (int j = 0; j < size; j++) {
	                image.setRGB(i, j, bitMatrix.get(i, j) ? black : white); // set pixel one by one
	            }
	        }
	        String path = "C:\\Users\\Administrator\\Desktop\\图片\\erweima.jpg";
	        ImageIO.write(image, "jpg", new File(path)); // save QR image to disk
	    } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
		return null;
	}
}
