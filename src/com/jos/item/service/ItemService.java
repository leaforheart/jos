package com.jos.item.service;

import java.util.HashMap;

import com.jos.common.baseclass.IBaseService;
import com.jos.item.vo.ItemBean;

public interface ItemService extends IBaseService {
	HashMap<String,Object> addItem(ItemBean itemBean);
	HashMap<String,Object> updItem(ItemBean itemBean);
	HashMap<String,Object> itemOnline(ItemBean itemBean);
	HashMap<String,Object> itemOffline(ItemBean itemBean);
	HashMap<String,Object> getItem(ItemBean itemBean);
	HashMap<String,Object> getItemList(ItemBean itemBean);
	
	HashMap<String,Object> addItemProperties(ItemBean itemBean);
	HashMap<String,Object> delItemProperties(ItemBean itemBean);
	HashMap<String,Object> queItemProperties(ItemBean itemBean);
	
	HashMap<String,Object> addItemDetail(ItemBean itemBean);
	HashMap<String,Object> updItemDetail(ItemBean itemBean);
	HashMap<String,Object> delItemDetail(ItemBean itemBean);
	HashMap<String,Object> getItemDetail(ItemBean itemBean);
	HashMap<String,Object> queItemDetail(ItemBean itemBean);
	HashMap<String,Object> uploadDetailImage(ItemBean itemBean);
	
	HashMap<String,Object> addItemStock(ItemBean itemBean);
	HashMap<String,Object> delItemStock(ItemBean itemBean);
	HashMap<String,Object> updItemStock(ItemBean itemBean);
	HashMap<String,Object> queItemStock(ItemBean itemBean);
	HashMap<String,Object> getItemStock(ItemBean itemBean);
	
	HashMap<String,Object> addItemPrice(ItemBean itemBean);
	HashMap<String,Object> delItemPrice(ItemBean itemBean);
	HashMap<String,Object> updItemPrice(ItemBean itemBean);
	HashMap<String,Object> queItemPrice(ItemBean itemBean);
	HashMap<String,Object> getItemPrice(ItemBean itemBean);
	
	HashMap<String,Object> addImage(ItemBean itemBean);
	HashMap<String,Object> delImage(ItemBean itemBean);
	HashMap<String,Object> getImage(ItemBean itemBean);
	HashMap<String, Object> setDefault(ItemBean itemBean);
	HashMap<String,Object> delFormalImage(ItemBean itemBean);
	HashMap<String,Object> addItemGallery(ItemBean itemBean);
	HashMap<String,Object> delItemGallery(ItemBean itemBean);
	HashMap<String,Object> getSmallItemGallery(ItemBean itemBean);
	HashMap<String,Object> getInitSmallItemGallery(ItemBean itemBean);
	
	void timeInit();
}
