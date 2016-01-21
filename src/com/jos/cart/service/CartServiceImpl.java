package com.jos.cart.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.inveno.util.JsonUtil;
import com.inveno.util.StringUtil;
import com.jos.cart.dao.CartDao;
import com.jos.cart.model.Cart;
import com.jos.cart.vo.CartBean;
import com.jos.common.baseclass.AbstractBaseService;
import com.jos.common.util.Constants;
import com.jos.common.util.SysContext;
import com.jos.item.model.ItemPrice;

public class CartServiceImpl extends AbstractBaseService implements CartService {
	private CartDao cartDao;

	public CartDao getCartDao() {
		return cartDao;
	}

	public void setCartDao(CartDao cartDao) {
		this.cartDao = cartDao;
	}

	@Override
	public HashMap<String, Object> add(CartBean cartBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			String userId = SysContext.getUser().getId();
			Cart cart = cartBean.getCart();
			cart.setUserId(userId);
			cart.setCreateTime(new Date());
			cart.setLastUpdateTime(new Date());
			String itemId = cart.getItemId();
			String properties = cart.getItemProperties();
			Set<String> set = this.getPropertiesSet(properties);
			List<String> parameters = new ArrayList<String>();
			parameters.add(itemId);
			List<Cart> list = cartDao.findByHql("from Cart where itemId=?", parameters);
			Cart carted = null;
			for(Cart c:list) {
				Set<String> seted = this.getPropertiesSet(c.getItemProperties());
				if(seted.containsAll(set)) {
					carted = c;
				}
			}
			if(carted==null) {
				cartDao.save(cart);
			} else {
				String amounted = carted.getItemAmount();
				String amounting = cart.getItemAmount();
				carted.setItemAmount(String.valueOf(Integer.parseInt(amounted)+Integer.parseInt(amounting)));
				cartDao.update(carted);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
			return map;
		}
		map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		return map;
	}
	
	private Set<String> getPropertiesSet(String properties) {
		Set<String> set = new HashSet<String>();
		String[] pro = properties.split(";");
		for(int i=0;i<pro.length;i++) {
			set.add(pro[i]);
		}
		return set;
	}

	@Override
	public HashMap<String, Object> upd(CartBean cartBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			Cart cart = cartBean.getCart();
			String id = cart.getId();
			String amount = cart.getItemAmount();
			String properties = cart.getItemProperties();
			Cart cartDB = null;
			if(StringUtil.isNotEmpty(id)) {
				cartDB = cartDao.findById(id, Cart.class);
				if(StringUtil.isNotEmpty(amount)) {
					cartDB.setItemAmount(amount);
				}
				if(StringUtil.isNotEmpty(properties)) {
					cartDB.setItemProperties(properties);
				}
				cartDao.saveOrUpdate(cartDB);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
			return map;
		}
		map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		return map;
	}

	@Override
	public HashMap<String, Object> del(CartBean cartBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			Cart cart = cartBean.getCart();
			String id = cart.getId();
			String[] ids = new String[1];
			ids[0] = id;
			cartDao.delete(Cart.class, ids);
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
			return map;
		}
		map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		return map;
	}
	
	@Override
	public HashMap<String, Object> remove(String[] ids) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			cartDao.delete(Cart.class, ids);
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
			return map;
		}
		map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		return map;
	}

	@Override
	public HashMap<String, Object> que() {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			String userId = SysContext.getUser().getId();
			List<String> parameters = new ArrayList<String>();
			parameters.add(userId);
			List<Cart> list = cartDao.findByHql("from Cart where userId=?", parameters);
			double tPrice = 0;
			for(Cart cart:list) {
				String itemId = cart.getItemId();
				String properties = cart.getItemProperties();
				Set<String> propertiesSet = new HashSet<String>();
				String[] propertiesArr= properties.split(",");
				for(int i=0;i<propertiesArr.length;i++) {
					propertiesSet.add(propertiesArr[i]);
				}
				String amount = cart.getItemAmount();
				List<String> parameters1 = new ArrayList<String>();
				parameters1.add(itemId);
				List<ItemPrice> list1 = cartDao.findByHql("from ItemPrice where itemId=?", parameters1);
				for(ItemPrice ip:list1) {
					String pro = ip.getItemProperties();
					String[] proArr = pro.split(",");
					Set<String> proSet = new HashSet<String>();
					for(int i=0;i<proArr.length;i++) {
						proSet.add(proArr[i]);
					}
					if(propertiesSet.containsAll(proSet)) {
						cart.setPrice(Double.parseDouble(ip.getPrice()));
						cart.setDiscount(Double.parseDouble(ip.getDiscount()));
						if(StringUtil.isNotEmpty(ip.getDiscount())) {
							cart.setRealPrice(Double.parseDouble(ip.getPrice())*Double.parseDouble(ip.getDiscount()));
						}else {
							cart.setRealPrice(Double.parseDouble(ip.getPrice()));
						}
						tPrice += Integer.parseInt(amount) * cart.getRealPrice();
						break;
					}
				}
			}
			CartBean cartBean = new CartBean();
			cartBean.settPrice(tPrice);
			cartBean.setCartList(list);
			map.put(Constants.RETURN_DATA, JsonUtil.getJsonStrByConfigFromObj(cartBean));
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
			return map;
		}
		map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		return map;
	}

	@Override
	public HashMap<String, Object> clear() {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			String userId = SysContext.getUser().getId();
			List<String> parameter = new ArrayList<String>();
			parameter.add(userId);
			cartDao.excuteHql("delete from Cart where userId=?", parameter);
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
			return map;
		}
		map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		return map;
	}
	
}
