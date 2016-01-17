package com.jos.item.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.inveno.util.JsonUtil;
import com.inveno.util.StringUtil;
import com.jos.common.baseclass.AbstractBaseService;
import com.jos.common.util.Constants;
import com.jos.dictionary.model.Dictionary;
import com.jos.item.dao.ItemDao;
import com.jos.item.model.Item;
import com.jos.item.model.ItemDetail;
import com.jos.item.model.ItemPrice;
import com.jos.item.model.ItemProperties;
import com.jos.item.model.ItemStock;
import com.jos.item.vo.ItemBean;

public class ItemServiceImpl extends AbstractBaseService implements ItemService {
	private ItemDao itemDao;

	public ItemDao getItemDao() {
		return itemDao;
	}

	public void setItemDao(ItemDao itemDao) {
		this.itemDao = itemDao;
	}

	@Override
	public HashMap<String, Object> addItem(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			Item item = itemBean.getItem();
			item.setCreateTime(new Date());
			item.setLastUpdateTime(new Date());
			item.setServiceDes(Constants.ITEM_ONLINE);
			itemDao.save(item);
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
			return map;
		}
		
		return map;
	}

	@Override
	public HashMap<String, Object> updItem(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			Item item = itemBean.getItem();
			String id = item.getId();
			Item itemDB = itemDao.findById(id, Item.class);
			if(StringUtil.isNotEmpty(item.getDes())) {
				itemDB.setDes(item.getDes());
			}
			if(StringUtil.isNotEmpty(item.getExpressDes())) {
				itemDB.setExpressDes(item.getExpressDes());
			}
			
			itemDB.setLastUpdateTime(new Date());
			
			if(StringUtil.isNotEmpty(item.getName())) {
				itemDB.setName(item.getName());
			}
			
			if(StringUtil.isNotEmpty(item.getServiceDes())) {
				itemDB.setServiceDes(item.getServiceDes());
			}
			
			if(Constants.ITEM_ONLINE.equals(item.getStatus())||Constants.ITEM_OFFLINE.equals(item.getStatus())) {
				itemDB.setStatus(item.getStatus());
			}
			
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		return map;
	}

	@Override
	public HashMap<String, Object> itemOnline(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			Item item = itemBean.getItem();
			String id = item.getId();
			Item itemDB = itemDao.findById(id, Item.class);
			itemDB.setStatus(Constants.ITEM_ONLINE);
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		return map;
	}

	@Override
	public HashMap<String, Object> itemOffline(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			Item item = itemBean.getItem();
			String id = item.getId();
			Item itemDB = itemDao.findById(id, Item.class);
			itemDB.setStatus(Constants.ITEM_OFFLINE);
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		return map;
	}

	@Override
	public HashMap<String, Object> addItemProperties(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			ItemProperties ip = itemBean.getItemProperties();
			ip.setCreateTime(new Date());
			ip.setLastUpdateTime(new Date());
			itemDao.save(ip);
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		
		return map;
	}

	@Override
	public HashMap<String, Object> delItemProperties(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			String id = itemBean.getItemProperties().getId();
			List<String> parameters = new ArrayList<String>();
			parameters.add(id);
			itemDao.excuteHql("delete from ItemProperties where id=?", parameters);
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		return map;
	}
	
	@Override
	public HashMap<String, Object> queItemProperties(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			String itemId = itemBean.getItemProperties().getItemId();
			List<String> parameters = new ArrayList<String>();
			parameters.add(itemId);
			List<Object> list = itemDao.findByHql("from ItemProperties where itemId=?", parameters);
			for(Object object:list) {
				ItemProperties ip = (ItemProperties)object;
				String dictionaryId = ip.getDictionaryId();
				Dictionary dictionary = itemDao.findById(dictionaryId, Dictionary.class);
				ip.setDictionary(dictionary);
			}
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
			map.put(Constants.RETURN_DATA, JsonUtil.getJsonStrFromList(list));
		} catch (Exception e) {
			e.printStackTrace();
			map.clear();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		return map;
	}

	@Override
	public HashMap<String, Object> addItemDetail(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			ItemDetail itemDetail = itemBean.getItemDetail();
			String sequence = itemDetail.getSequence();
			if(StringUtil.isEmpty(sequence)) {
				map.put(Constants.RETURN_CODE, "-1");//排序号不能为空
				return map;
			}
			String itemId = itemDetail.getItemId();
			List<String> parameters = new ArrayList<String>();
			parameters.add(itemId);
			parameters.add(sequence);
			List<ItemDetail> list = detailCheck(itemId,sequence);
			if(list!=null&&list.size()>0) {
				map.put(Constants.RETURN_CODE, "-2");//该商品的排序号已存在
				return map;
			}
			itemDetail.setCreateTime(new Date());
			itemDetail.setLastUpdateTime(new Date());
			itemDao.save(itemDetail);
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			map.clear();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		return map;
	}
	
	private List<ItemDetail> detailCheck(String itemId,String sequence) {
		List<String> parameters = new ArrayList<String>();
		parameters.add(itemId);
		parameters.add(sequence);
		List<ItemDetail> list = itemDao.findByHql("from ItemDetail where itemId=? and sequence=?", parameters);
		return list;
	}
	
	@Override
	public HashMap<String, Object> updItemDetail(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			ItemDetail itemDetail = itemBean.getItemDetail();
			String id = itemDetail.getId();
			String sequence = itemDetail.getSequence();
			String itemId = itemDetail.getItemId();
			List<ItemDetail> list = detailCheck(itemId,sequence);
			if(list!=null&&list.size()>0) {
				map.put(Constants.RETURN_CODE, "-1");//该商品的排序号已存在
				return map;
			}
			
			ItemDetail itemDetailDB = itemDao.findById(id, ItemDetail.class);
			if(itemDetailDB==null) {
				map.put(Constants.RETURN_CODE, "-2");//找不到该商品详细
				return map;
			}
			if(StringUtil.isNotEmpty(itemDetail.getContent())) {
				itemDetailDB.setContent(itemDetail.getContent());
			}
			if(StringUtil.isNotEmpty(sequence)) {
				itemDetailDB.setSequence(sequence);
			}
			
			if(StringUtil.isNotEmpty(itemDetail.getType())) {
				itemDetailDB.setType(itemDetail.getType());
			}
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			map.clear();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		return map;
	}

	@Override
	public HashMap<String, Object> delItemDetail(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			ItemDetail itemDetail = itemBean.getItemDetail();
			String id = itemDetail.getId();
			List<String> parameter = new ArrayList<String>();
			parameter.add(id);
			itemDao.excuteHql("delete from ItemDetail where id=?", parameter);
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			map.clear();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		return map;
	}
	
	@Override
	public HashMap<String, Object> queItemDetail(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			ItemDetail itemDetail = itemBean.getItemDetail();
			String itemId = itemDetail.getItemId();
			List<String> parameters = new ArrayList<String>();
			parameters.add(itemId);
			List<Object> list = itemDao.findByHql("from ItemDetail where itemId=?", parameters);
			map.put(Constants.RETURN_DATA, JsonUtil.getJsonStrFromList(list));
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			map.clear();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		return map;
	}

	@Override
	public HashMap<String, Object> addItemStock(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			ItemStock itemStock = itemBean.getItemStock();
			itemStock.setCreateTime(new Date());
			itemStock.setLastUpdateTime(new Date());
			itemDao.save(itemStock);
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			map.clear();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		return map;
	}

	@Override
	public HashMap<String, Object> delItemStock(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			ItemStock itemStock = itemBean.getItemStock();
			String id = itemStock.getId();
			List<String> parameter = new ArrayList<String>();
			parameter.add(id);
			itemDao.excuteHql("delete from ItemStock where id=?", parameter);
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			map.clear();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		return map;
	}

	@Override
	public HashMap<String, Object> updItemStock(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			ItemStock itemStock = itemBean.getItemStock();
			String itemId = itemStock.getItemId();
			List<String> parameters = new ArrayList<String>();
			parameters.add(itemId);
			List<ItemStock> list = itemDao.findByHql("from ItemStock where itemId=?", parameters);
			
			String properties = itemStock.getItemProperties();
			Set<String> set = getPropertiesSet(properties);
			
			for(ItemStock is:list) {
				String propertiesDB = is.getItemProperties();
				Set<String> setDB = getPropertiesSet(propertiesDB);
				if(set.containsAll(setDB)) {
					is.setLastUpdateTime(new Date());
					is.setStockAmount(itemStock.getStockAmount());
					itemDao.update(is);
					break;
				}
			}
			
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			map.clear();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
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
	public HashMap<String, Object> queItemStock(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			ItemStock itemStock = itemBean.getItemStock();
			String itemId = itemStock.getItemId();
			List<String> parameters = new ArrayList<String>();
			parameters.add(itemId);
			List<Object> list = itemDao.findByHql("from ItemStock where itemId=?", parameters);
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
			map.put(Constants.RETURN_DATA, JsonUtil.getJsonStrFromList(list));
		} catch (Exception e) {
			e.printStackTrace();
			map.clear();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		return map;
	}

	@Override
	public HashMap<String, Object> addItemPrice(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			ItemPrice itemPrice = itemBean.getItemPrice();
			itemPrice.setCreateTime(new Date());
			itemPrice.setLastUpdateTime(new Date());
			itemDao.save(itemPrice);
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			map.clear();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		return map;
	}

	@Override
	public HashMap<String, Object> delItemPrice(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			ItemPrice itemPrice = itemBean.getItemPrice();
			String id = itemPrice.getId();
			List<String> parameter = new ArrayList<String>();
			parameter.add(id);
			itemDao.excuteHql("delete from ItemPrice where id=?", parameter);
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			map.clear();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		return map;
	}

	@Override
	public HashMap<String, Object> updItemPrice(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			ItemPrice itemPrice = itemBean.getItemPrice();
			String itemId = itemPrice.getItemId();
			List<String> parameters = new ArrayList<String>();
			parameters.add(itemId);
			List<ItemPrice> list = itemDao.findByHql("from ItemPrice where itemId=?", parameters);
			
			String properties = itemPrice.getItemProperties();
			Set<String> set = getPropertiesSet(properties);
			
			for(ItemPrice ip:list) {
				String propertiesDB = ip.getItemProperties();
				Set<String> setDB = getPropertiesSet(propertiesDB);
				if(set.containsAll(setDB)) {
					ip.setLastUpdateTime(new Date());
					ip.setPrice(itemPrice.getPrice());
					ip.setDiscount(itemPrice.getDiscount());
					itemDao.update(ip);
					break;
				}
			}
			
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			map.clear();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		return map;
	}
	
	@Override
	public HashMap<String, Object> queItemPrice(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			ItemPrice itemPrice = itemBean.getItemPrice();
			String itemId = itemPrice.getItemId();
			List<String> parameters = new ArrayList<String>();
			parameters.add(itemId);
			List<Object> list = itemDao.findByHql("from ItemPrice where itemId=?", parameters);
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
			map.put(Constants.RETURN_DATA, JsonUtil.getJsonStrFromList(list));
		} catch (Exception e) {
			e.printStackTrace();
			map.clear();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		return map;
	}

	@Override
	public HashMap<String, Object> addItemGallery(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			
		} catch (Exception e) {
			
		}
		return map;
	}

	@Override
	public HashMap<String, Object> delItemGallery(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			
		} catch (Exception e) {
			
		}
		return map;
	}

	@Override
	public HashMap<String, Object> updItemGallery(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			
		} catch (Exception e) {
			
		}
		return map;
	}

	@Override
	public HashMap<String, Object> getItem(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			Item item = itemBean.getItem();
			String id = item.getId();
			Item itemDB = itemDao.findById(id, Item.class);
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
			map.put(Constants.RETURN_DATA, JsonUtil.getJsonStrByConfigFromObj(itemDB));
		} catch (Exception e) {
			e.printStackTrace();
			map.clear();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		return map;
	}

	@Override
	public HashMap<String, Object> getItemList(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			
		} catch (Exception e) {
			
		}
		return map;
	}

	@Override
	public HashMap<String, Object> getItemStock(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			ItemStock is = itemBean.getItemStock();
			String itemId = is.getItemId();
			String properties = is.getItemProperties();
			List<String> parameters = new ArrayList<String>();
			Set<String> set = this.getPropertiesSet(properties);
			parameters.add(itemId);
			List<ItemStock> list = itemDao.findByHql("from ItemStock where itemId=?", parameters);
			ItemStock itemStock = null;
			for(ItemStock isDB:list) {
				String propertiesDB = isDB.getItemProperties();
				Set<String> setDB = this.getPropertiesSet(propertiesDB);
				if(set.containsAll(setDB)) {
					itemStock = isDB;
					break;
				}
			}
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
			map.put(Constants.RETURN_DATA, JsonUtil.getJsonStrByConfigFromObj(itemStock));
		} catch (Exception e) {
			e.printStackTrace();
			map.clear();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		return map;
	}

	@Override
	public HashMap<String, Object> getItemPrice(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			ItemPrice ip = itemBean.getItemPrice();
			String itemId = ip.getItemId();
			String properties = ip.getItemProperties();
			List<String> parameters = new ArrayList<String>();
			Set<String> set = this.getPropertiesSet(properties);
			parameters.add(itemId);
			List<ItemPrice> list = itemDao.findByHql("from ItemPrice where itemId=?", parameters);
			ItemPrice itemPrice = null;
			for(ItemPrice ipDB:list) {
				String propertiesDB = ipDB.getItemProperties();
				Set<String> setDB = this.getPropertiesSet(propertiesDB);
				if(set.containsAll(setDB)) {
					itemPrice = ipDB;
					break;
				}
			}
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
			map.put(Constants.RETURN_DATA, JsonUtil.getJsonStrByConfigFromObj(itemPrice));
		} catch (Exception e) {
			e.printStackTrace();
			map.clear();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		return map;
	}

	@Override
	public HashMap<String, Object> getSmallItemGallery(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			
		} catch (Exception e) {
			
		}
		return map;
	}

	@Override
	public HashMap<String, Object> addImage(ItemBean itemBean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, Object> delImage(ItemBean itemBean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, Object> getImage(ItemBean itemBean) {
		// TODO Auto-generated method stub
		return null;
	}

}
