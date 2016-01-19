package com.jos.item.vo;

import java.io.File;

import com.jos.image.Image;
import com.jos.item.model.ImageTmp;
import com.jos.item.model.Item;
import com.jos.item.model.ItemComment;
import com.jos.item.model.ItemDetail;
import com.jos.item.model.ItemGallery;
import com.jos.item.model.ItemGalleryTmp;
import com.jos.item.model.ItemPrice;
import com.jos.item.model.ItemProperties;
import com.jos.item.model.ItemStock;

public class ItemBean {
	
	private Item item;
	private ItemComment itemComment;
	private ItemDetail itemDetail;
	private ItemGallery itemGallery;
	private ItemPrice itemPrice;
	private ItemProperties itemProperties;
	private ItemStock itemStock;
	private ItemGalleryTmp itemGalleryTmp;
	private Image image;
	private ImageTmp imageTmp;
	private int useAmount;
	private File file;
	private String fileName;
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public ItemGalleryTmp getItemGalleryTmp() {
		return itemGalleryTmp;
	}
	public void setItemGalleryTmp(ItemGalleryTmp itemGalleryTmp) {
		this.itemGalleryTmp = itemGalleryTmp;
	}
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public ImageTmp getImageTmp() {
		return imageTmp;
	}
	public void setImageTmp(ImageTmp imageTmp) {
		this.imageTmp = imageTmp;
	}
	public int getUseAmount() {
		return useAmount;
	}
	public void setUseAmount(int useAmount) {
		this.useAmount = useAmount;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public ItemComment getItemComment() {
		return itemComment;
	}
	public void setItemComment(ItemComment itemComment) {
		this.itemComment = itemComment;
	}
	public ItemDetail getItemDetail() {
		return itemDetail;
	}
	public void setItemDetail(ItemDetail itemDetail) {
		this.itemDetail = itemDetail;
	}
	public ItemGallery getItemGallery() {
		return itemGallery;
	}
	public void setItemGallery(ItemGallery itemGallery) {
		this.itemGallery = itemGallery;
	}
	public ItemPrice getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(ItemPrice itemPrice) {
		this.itemPrice = itemPrice;
	}
	public ItemProperties getItemProperties() {
		return itemProperties;
	}
	public void setItemProperties(ItemProperties itemProperties) {
		this.itemProperties = itemProperties;
	}
	public ItemStock getItemStock() {
		return itemStock;
	}
	public void setItemStock(ItemStock itemStock) {
		this.itemStock = itemStock;
	}
	
}
