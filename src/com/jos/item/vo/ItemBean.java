package com.jos.item.vo;

import com.jos.item.model.Item;
import com.jos.item.model.ItemComment;
import com.jos.item.model.ItemDetail;
import com.jos.item.model.ItemGallery;
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
	private int useAmount;
	
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
