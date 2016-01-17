package com.jos.item.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
@Entity
@Table(name = "jos_image")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ImageTmp {
	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "ImageGenerate", strategy = GenerationType.IDENTITY)
	@GenericGenerator(name = "ImageGenerate", strategy = "native")
	private String id;
	//原图id
	@Column(name = "original_id")
	private String originalId;
	
	@Column(name = "name")
	private String name;
	//-1临时图  0原图 1处理后的大图 2缩略图
	
	@Column(name = "type")
	private int type;
	
	@Column(name = "mime_type")
	private String mimeType;
	
	@Column(name = "format")
	private String format;
	
	@Column(name = "compression_algorithm")
	private String compressionAlgorithm;
	
	@Column(name = "path")
	private String path;
	
	@Column(name = "url")
	private String url;
	
	@Column(name = "size")
	private long size;
	
	@Column(name = "width")
	private int width;
	
	@Column(name = "height")
	private int height;
	
	@Column(name = "altitude")
	private String altitude ;//海拔
	
	@Column(name = "latitude")
	private double latitude;//纬度
	
	@Column(name = "longitude")
	private double longitude ;//经度
	
	@Column(name = "original_date_time")
	private String originalDateTime;
	
	@Column(name = "last_update_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdateTime;
	
	public String getOriginalDateTime() {
		return originalDateTime;
	}
	public void setOriginalDateTime(String originalDateTime) {
		this.originalDateTime = originalDateTime;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getCompressionAlgorithm() {
		return compressionAlgorithm;
	}
	public void setCompressionAlgorithm(String compressionAlgorithm) {
		this.compressionAlgorithm = compressionAlgorithm;
	}
	public String getAltitude() {
		return altitude;
	}
	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	@Override
	public String toString() {
		return "图片名称："+name+";图片类型："+type+";磁盘路径："+path+";下载链接："+url+"\n"+
				";图片大小："+size+";图片宽度："+width+";图片高度："+height+";海拔："+this.altitude+"\n"+
				";纬度："+this.latitude+";经度："+this.longitude+";更新时间："+this.lastUpdateTime+"\n"+
				";图片拍摄时间："+originalDateTime+";MIME："+mimeType+";文件格式："+format+";压缩算法："+compressionAlgorithm;
	}
	
}
