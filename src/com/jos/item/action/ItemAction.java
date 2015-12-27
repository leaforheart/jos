package com.jos.item.action;

import com.inveno.util.JsonUtil;
import com.jos.common.baseclass.BaseAction;
import com.jos.item.service.ItemService;
import com.jos.item.vo.ItemBean;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("rawtypes")
public class ItemAction extends BaseAction implements ModelDriven  {

	private static final long serialVersionUID = 4978177278011045763L;
	private ItemService itemService;
	private ItemBean itemBean;
	

	public ItemService getItemService() {
		return itemService;
	}


	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}


	public ItemBean getItemBean() {
		return itemBean;
	}


	public void setItemBean(ItemBean itemBean) {
		this.itemBean = itemBean;
	}


	@Override
	public Object getModel() {
		if(itemBean==null) {
			itemBean = new ItemBean();
		}
		return itemBean;
	}
	
	public void addItem(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(itemService.addItem(itemBean)));
	}
	public void updItem(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(itemService.updItem(itemBean)));
	}
	public void itemOnline(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(itemService.itemOnline(itemBean)));
	}
	public void itemOffline(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(itemService.itemOffline(itemBean)));
	}
	
	public void addItemProperties(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(itemService.addItemProperties(itemBean)));
	}
	public void delItemProperties(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(itemService.delItemProperties(itemBean)));
	}
	
	public void addItemDetail(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(itemService.addItemDetail(itemBean)));
	}
	public void updItemDetail(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(itemService.updItemDetail(itemBean)));
	}
	public void delItemDetail(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(itemService.delItemDetail(itemBean)));
	}
	
	public void addItemStock(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(itemService.addItemStock(itemBean)));
	}
	public void delItemStock(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(itemService.delItemStock(itemBean)));
	}
	public void updItemStock(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(itemService.updItemStock(itemBean)));
	}
	
	public void addItemPrice(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(itemService.addItemPrice(itemBean)));
	}
	public void delItemPrice(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(itemService.delItemPrice(itemBean)));
	}
	public void updItemPrice(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(itemService.updItemPrice(itemBean)));
	}
	
	public void addItemGallery(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(itemService.addItemGallery(itemBean)));
	}
	public void delItemGallery(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(itemService.delItemGallery(itemBean)));
	}
	public void updItemGallery(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(itemService.updItemGallery(itemBean)));
	}
	
	public void getItem(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(itemService.getItem(itemBean)));
	}
	public void getItemList(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(itemService.getItemList(itemBean)));
	}
	public void getItemStock(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(itemService.getItemStock(itemBean)));
	}
	public void getItemPrice(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(itemService.getItemPrice(itemBean)));
	}
	public void getItemGallery(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(itemService.getItemGallery(itemBean)));
	}

}
