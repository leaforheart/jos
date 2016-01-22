package com.jos.address.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.inveno.util.StringUtil;
import com.jos.address.dao.AddressDao;
import com.jos.address.model.Address;
import com.jos.address.vo.AddressBean;
import com.jos.common.baseclass.AbstractBaseService;
import com.jos.common.util.Constants;
import com.jos.common.util.SysContext;

public class AddressServiceImpl extends AbstractBaseService implements AddressService {
	
	private AddressDao addressDao;

	public AddressDao getAddressDao() {
		return addressDao;
	}

	public void setAddressDao(AddressDao addressDao) {
		this.addressDao = addressDao;
	}

	@Override
	public HashMap<String, Object> add(AddressBean addressBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		Address address = addressBean.getAddress();
		String userId = SysContext.getUser().getId();
		if(StringUtil.isEmpty(userId)) {
			map.put(Constants.RETURN_CODE, "-1");
			return map;
		}
		List<String> parameters = new ArrayList<String>();
		parameters.add(userId);
		List<Address> list = addressDao.findByHql("from Address where userId=?", parameters);
		if(list==null||list.size()==0) {
			address.setIsDefault("0");
		}
		address.setUserId(userId);
		address.setCreateTime(new Date());
		address.setLastUpdateTime(new Date());
		addressDao.save(address);
		map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		return map;
	}

	@Override
	public HashMap<String, Object> del(AddressBean addressBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			String id = addressBean.getAddress().getId();
			Address address = addressDao.findById(id, Address.class);
			if(address==null) {
				map.put(Constants.RETURN_CODE, "-1");
				return map;
			}
			if("0".equals(address.getIsDefault())) {
				map.put(Constants.RETURN_CODE, "-2");
				return map;
			}
			addressDao.delete(address);
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			map.clear();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		
		return map;
	}

	@Override
	public HashMap<String, Object> upd(AddressBean addressBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			Address address = addressBean.getAddress();
			String id = address.getId();
			Address addressDB = addressDao.findById(id,Address.class);
			if(addressDB==null) {
				map.put(Constants.RETURN_CODE, "-1");
				return map;
			}
			addressDB.setName(address.getName());
			addressDB.setAddress(address.getAddress());
			addressDB.setTelphone(address.getTelphone());
			addressDB.setLastUpdateTime(new Date());
			addressDao.update(addressDB);
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			map.clear();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		
		return map;
	}

	@Override
	public HashMap<String, Object> que(AddressBean addressBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			String userId = SysContext.getUser().getId();
			if(StringUtil.isEmpty(userId)) {
				map.put(Constants.RETURN_CODE, "-1");
				return map;
			}
			List<String> parameters = new ArrayList<String>();
			parameters.add(userId);
			List<Address> list = addressDao.findByHql("from Address where userId=?", parameters);
			map.put("data", list);
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			map.clear();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		
		return map;
	}

	@Override
	public HashMap<String, Object> setDefault(AddressBean addressBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			String id = addressBean.getAddress().getId();
			Address addressDB = addressDao.findById(id,Address.class);
			String userId = SysContext.getUser().getId();
			if(StringUtil.isEmpty(userId)) {
				map.put(Constants.RETURN_CODE, "-1");
				return map;
			}
			if(addressDB==null) {
				map.put(Constants.RETURN_CODE, "-2");
				return map;
			}
			List<String> parameters = new ArrayList<String>();
			parameters.add(userId);
			parameters.add(id);
			List<Address> list = addressDao.findByHql("from Address where userId=? and id<>?", parameters);
			addressDB.setIsDefault("0");
			if(list!=null&&list.size()>0) {
				for(Address address:list) {
					address.setIsDefault("1");
				}
			}
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			map.clear();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		
		return map;
	}

}
