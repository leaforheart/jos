package com.jos.image;

import java.io.File;
import java.util.Date;
import javax.imageio.ImageIO;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata.GPSInfo;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;

public class ImageUtil {
	
	public static Image saveImage(File file,String dest,String url,int type) throws Exception {
		checkImage(file);
		//把文件存到服务器本地目录
		FileByteBuffUtil.saveFile(file, dest);
		//设置图片基本属性
		Image image = genImage(dest,url,type);
		return image;
	}
	
	public static int deleteImage(String path) {
		return FileByteBuffUtil.deleteFile(path);
	}
	
	public static Image getImage(String dest,String url,int type) throws Exception {
		File file = new File(dest);
		checkImage(file);
		Image image = genImage(dest,url,type);
		return image;
	}
	
	public static Image copyImage(String src,String dest,String url,int type) throws Exception {
		File file = new File(src);
		checkImage(file);
		FileByteBuffUtil.saveFile(file, dest);
		Image image = genImage(dest,url,type);
		return image;
	}
	
	public static Image sizeImage(String src,int width,int height,String dest,String url,int type) throws Exception {
		File file = new File(src);
		checkImage(file);
		Thumbnails.of(src).size(width, height).toFile(dest);
		Image image = genImage(dest,url,type);
		return image;
	}
	
	public static Image watermarkImage(String src,String watermark,String dest,String url,int type) throws Exception {
		File file = new File(src);
		checkImage(file);
		ImageInfo imageInfo = Imaging.getImageInfo(file);
		Thumbnails.of(src).size(imageInfo.getWidth(), imageInfo.getHeight()).watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(watermark)), 0.8f).outputQuality(1f).toFile(dest);
		Image image = genImage(dest,url,type);
		return image;
	}
	
	public static Image sizeWatermarkImage(String src,int width,int height,String watermark,String dest,String url,int type) throws Exception {
		File file = new File(src);
		checkImage(file);
		Thumbnails.of(src).size(width, height).watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(watermark)), 0.8f).outputQuality(1f).toFile(dest);
		Image image = genImage(dest,url,type);
		return image;
	}
	
	private static Image genImage(String dest,String url,int type) throws Exception {
		//设置图片基本属性
		Image image = new Image();
		File localFile = new File(dest);
		image.setPath(dest);
		image.setUrl(url);
		image.setSize(localFile.length());
		image.setType(type);
		image.setName(dest.substring(dest.lastIndexOf(File.separator)+1));
		image.setLastUpdateTime(new Date(localFile.lastModified()));
		
		//设置图片参数属性
		ImageInfo imageInfo = Imaging.getImageInfo(localFile);
		image.setWidth(imageInfo.getWidth());
		image.setHeight(imageInfo.getHeight());
		image.setMimeType(imageInfo.getMimeType());
		image.setCompressionAlgorithm(imageInfo.getCompressionAlgorithm().toString());
		image.setFormat(imageInfo.getFormat().toString());
		
		//设置JPEG图片元数据属性
		if(ImageFormats.JPEG.getName().equals(imageInfo.getFormat().toString())) {
			JpegImageMetadata metadata = (JpegImageMetadata)Imaging.getMetadata(localFile);
			TiffImageMetadata exif = null;
			GPSInfo gps = null;
			if(metadata!=null) {
				exif = metadata.getExif();
				gps = exif.getGPS();
				image.setLatitude(gps.getLatitudeAsDegreesNorth());
				image.setLongitude(gps.getLongitudeAsDegreesEast());
				image.setAltitude(metadata.findEXIFValue(GpsTagConstants.GPS_TAG_GPS_ALTITUDE).getValue().toString());
				image.setOriginalDateTime(metadata.findEXIFValue(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL).getValue().toString());
			}
		}
		
		return image;
	}
	
	private static void checkImage(File file) throws Exception {
		try {
			Imaging.getImageInfo(file);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("文件被损坏或文件不是图片格式");
		}
		
		if(file.length()>1024*1024*10) {
			throw new Exception("图片大小不能超过10M");
		}
	}
	
}
