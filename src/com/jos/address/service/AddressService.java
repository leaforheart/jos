package com.jos.address.service;

import java.util.HashMap;

import com.jos.address.vo.AddressBean;
import com.jos.common.baseclass.IBaseService;

public interface AddressService extends IBaseService {
	HashMap<String,Object> add(AddressBean addressBean);
	HashMap<String,Object> del(AddressBean addressBean);
	HashMap<String,Object> upd(AddressBean addressBean);
	HashMap<String,Object> que(AddressBean addressBean);
	HashMap<String,Object> setDefault(AddressBean addressBean);
}
