package com.jos.address.action;

import com.inveno.util.JsonUtil;
import com.jos.address.service.AddressService;
import com.jos.address.vo.AddressBean;
import com.jos.common.baseclass.BaseAction;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("rawtypes")
public class AddressAction extends BaseAction implements ModelDriven {

	private static final long serialVersionUID = 1L;
	private AddressBean addressBean;
	private AddressService addressService;

	public AddressBean getAddressBean() {
		return addressBean;
	}

	public void setAddressBean(AddressBean addressBean) {
		this.addressBean = addressBean;
	}

	public AddressService getAddressService() {
		return addressService;
	}

	public void setAddressService(AddressService addressService) {
		this.addressService = addressService;
	}

	@Override
	public Object getModel() {
		if(addressBean==null) {
			addressBean = new AddressBean();
		}
		return addressBean;
	}
	
	public void add(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(addressService.add(addressBean)));
	}
	
	public void del(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(addressService.del(addressBean)));
	}
	
	public void upd(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(addressService.upd(addressBean)));
	}
	
	public void que(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(addressService.que(addressBean)));
	}
	
	public void setDefault(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(addressService.setDefault(addressBean)));
	}

}
