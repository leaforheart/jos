package com.jos.item.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.struts2.ServletActionContext;

import com.inveno.util.JsonUtil;
import com.inveno.util.StringUtil;
import com.jos.common.baseclass.AbstractBaseService;
import com.jos.common.util.Constants;
import com.jos.dictionary.model.Dictionary;
import com.jos.image.FileByteBuffUtil;
import com.jos.image.Image;
import com.jos.image.ImageUtil;
import com.jos.item.dao.ItemDao;
import com.jos.item.model.ImageTmp;
import com.jos.item.model.Item;
import com.jos.item.model.ItemDetail;
import com.jos.item.model.ItemGallery;
import com.jos.item.model.ItemGalleryTmp;
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
			item.setStatus(Constants.ITEM_ONLINE);
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
	public HashMap<String, Object> getOnlineItemList(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			List<String> parameter = new ArrayList<String>();
			parameter.add(Constants.ITEM_ONLINE);
			List<Item> itemList = itemDao.findByHql("from Item where status=?", parameter);
			for(Item i:itemList) {
				String itemId = i.getId();
				List<Object> parameters =new ArrayList<Object>();
				parameters.add(itemId);
				parameters.add(1);
				List<ItemGallery> list = itemDao.findByHql("from ItemGallery where itemId=? and isDefault=?", parameters);
				if(list.size()==0) {
					map.put(Constants.RETURN_CODE, "-1");//该商品信息不完整，缺少图片信息
					return map;
				}
				String imageId = list.get(0).getImageId();
				Image image = itemDao.findById(imageId,Image.class);
				i.setDefaultImageUrl(image.getUrl());
			}
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
			List<Object> list = new ArrayList<Object>();
			list.addAll(itemList);
			map.put(Constants.RETURN_DATA, JsonUtil.getJsonStrFromList(list));
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
			List<Item> itemList = itemDao.findByHql("from Item", null);
			for(Item i:itemList) {
				String itemId = i.getId();
				List<Object> parameters =new ArrayList<Object>();
				parameters.add(itemId);
				parameters.add(1);
				List<ItemGallery> list = itemDao.findByHql("from ItemGallery where itemId=? and isDefault=?", parameters);
				if(list.size()==0) {
					map.put(Constants.RETURN_CODE, "-1");//该商品信息不完整，缺少图片信息
					return map;
				}
				String imageId = list.get(0).getImageId();
				Image image = itemDao.findById(imageId,Image.class);
				i.setDefaultImageUrl(image.getUrl());
			}
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
			List<Object> list = new ArrayList<Object>();
			list.addAll(itemList);
			map.put(Constants.RETURN_DATA, JsonUtil.getJsonStrFromList(list));
		} catch (Exception e) {
			e.printStackTrace();
			map.clear();
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
	public HashMap<String, Object> uploadDetailImage(ItemBean itemBean){
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			File file = itemBean.getFile();
			String fileName = itemBean.getFileName();
			String itemId = itemBean.getItem().getId();
			String dest = ServletActionContext.getServletContext().getContextPath()+Constants.IMAGEROOT+File.separator+Constants.DETAIL_IMAGE+File.separator+itemId+File.separator+Constants.IMAGE_TMP+File.separator+fileName;
			String url = Constants.IMAGE_URL+Constants.DETAIL_IMAGE+File.separator+itemId+File.separator+Constants.IMAGE_TMP+fileName;
			ImageUtil.saveImage(file, dest, null, 0);
			map.put(Constants.RETURN_DATA, url);
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
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
			
			String type = itemDetail.getType();
			if(Constants.DETAIL_TYPE_PIC.equals(type)) {
				String path = itemDetail.getPath();
				String content = itemDetail.getContent();
				if(StringUtil.isEmpty(path)) {
					map.put(Constants.RETURN_CODE, "-3");//图片路径不存在
					return map;
				}
				String pathPrefix = path.substring(0,path.lastIndexOf(File.separator)-1);
				String pathName = path.substring(path.lastIndexOf(File.separator)+1);
				String urlPrefix = content.substring(0,content.lastIndexOf(File.separator)-1);
				String urlName = content.substring(content.lastIndexOf(File.separator)+1);
				ImageUtil.copyImage(path, 
						pathPrefix.substring(0,pathPrefix.lastIndexOf(File.separator))+pathName,
						urlPrefix.substring(0,urlPrefix.lastIndexOf(File.separator))+urlName, 
						0);
				itemDetail.setContent(urlPrefix.substring(0,urlPrefix.lastIndexOf(File.separator))+urlName);
				ImageUtil.deleteImage(path);
			}
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
			delItemDetail(itemBean);
			addItemDetail(itemBean);
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
			String itemId = itemDetail.getItemId();
			String sequence = itemDetail.getSequence();
			List<String> parameters = new ArrayList<String>();
			parameters.add(itemId);
			parameters.add(sequence);
			List<ItemDetail> list = itemDao.findByHql("from ItemDetail where itemId=? and sequence=?", parameters);
			if(list.size()==0) {
				map.put(Constants.RETURN_CODE, "-1");
				return map;
			}
			String content = list.get(0).getContent();
			String type = list.get(0).getType();
			if(Constants.DETAIL_TYPE_PIC.equals(type)) {
				ImageUtil.deleteImage(ServletActionContext.getServletContext().getContextPath()+content.substring(Constants.IMAGE_URL.length()+1));
			}
			itemDao.delete(list.get(0));
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			map.clear();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		return map;
	}
	
	@Override
	public HashMap<String, Object> getItemDetail(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			ItemDetail itemDetail = itemBean.getItemDetail();
			String itemId = itemDetail.getItemId();
			String sequence = itemDetail.getSequence();
			List<String> parameters = new ArrayList<String>();
			parameters.add(itemId);
			parameters.add(sequence);
			List<ItemDetail> list = itemDao.findByHql("from ItemDetail where itemId=? and sequence=?", parameters);
			if(list.size()==0) {
				map.put(Constants.RETURN_CODE, "-1");
				return map;
			}
			map.put(Constants.RETURN_DATA, JsonUtil.getJsonStrFromObj(list.get(0)));
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
	public HashMap<String, Object> addImage(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			ImageTmp imageTmp = saveImageTmp(itemBean);
			ItemGalleryTmp igt = itemBean.getItemGalleryTmp();
			igt.setCreateTime(new Date());
			igt.setLastUpdateTime(new Date());
			igt.setImageId(imageTmp.getId());
			itemDao.save(igt);
			map.put(Constants.RETURN_DATA, JsonUtil.getJsonStrFromObj(imageTmp));
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
			return map;
		}
		map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		return map;
	}
	
	private ImageTmp saveImageTmp(ItemBean itemBean) throws Exception {
		File file = itemBean.getFile();
		String fileName = itemBean.getFileName();
		fileName = FileByteBuffUtil.getUuidName(fileName);
		ItemGalleryTmp igt = itemBean.getItemGalleryTmp();
		String itemId = igt.getItemId();
		String itemProperties = igt.getItemProperties(); 
		String dest = ServletActionContext.getServletContext().getContextPath()+Constants.IMAGEROOT+File.separator+itemId+File.separator+itemProperties+File.separator+Constants.IMAGE_TMP+File.separator+fileName;
		String url = Constants.IMAGE_URL+itemId+File.separator+itemProperties+File.separator+Constants.IMAGE_TMP+fileName;
		Image image = ImageUtil.saveImage(file, dest, url, Constants.IMAGE_TYPE_TMP);
		ImageTmp imageTmp = getImageTmp(image);
		itemDao.save(imageTmp);
		return imageTmp;
	}
	
	private ImageTmp getImageTmp(Image image) {
		ImageTmp imageTmp = new ImageTmp();
		imageTmp.setAltitude(image.getAltitude());
		imageTmp.setCompressionAlgorithm(image.getCompressionAlgorithm());
		imageTmp.setFormat(image.getFormat());
		imageTmp.setHeight(image.getHeight());
		imageTmp.setLastUpdateTime(image.getLastUpdateTime());
		imageTmp.setLatitude(image.getLatitude());
		imageTmp.setLongitude(image.getLongitude());
		imageTmp.setMimeType(image.getMimeType());
		imageTmp.setName(image.getName());
		imageTmp.setOriginalDateTime(image.getOriginalDateTime());
		imageTmp.setPath(image.getPath());
		imageTmp.setSize(image.getSize());
		imageTmp.setType(image.getType());
		imageTmp.setUrl(image.getUrl());
		imageTmp.setWidth(image.getWidth());
		return imageTmp;
	}
	

	@Override
	public HashMap<String, Object> delImage(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			ImageTmp imageTmp = itemBean.getImageTmp();
			String id = imageTmp.getId();
			ImageTmp it = itemDao.findById(id,ImageTmp.class);
			String path = it.getPath();
			ImageUtil.deleteImage(path);
			itemDao.delete(it);
			List<String> parameter = new ArrayList<String>();
			parameter.add(id);
			itemDao.excuteHql("delete from ItemGalleryTmp where imageId=?", parameter);
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
			return map;
		}
		map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		return map;
	}
	/**
	 * 根据某图片，找对对应类型的图片
	 */
	@Override
	public HashMap<String, Object> getImage(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			Image image = itemBean.getImage();
			String id = image.getId();
			int type = image.getType();
			Image it = itemDao.findById(id,Image.class);
			String originalId = it.getOriginalId();
			List<String> parameters = new ArrayList<String>();
			parameters.add(originalId);
			parameters.add(String.valueOf(type));
			List<Image> list = itemDao.findByHql("from Image where originalId=? and type=?", parameters);
			map.put(Constants.RETURN_DATA, JsonUtil.getJsonStrFromObj(list.get(0)));
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
			return map;
		}
		map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		return map;
	}
	
	@Override
	public HashMap<String, Object> delItemGallery(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			String itemId = itemBean.getItemGallery().getItemId();
			String properties = itemBean.getItemGallery().getItemProperties();
			Set<String> set = this.getPropertiesSet(properties);
			List<String> parameters = new ArrayList<String>();
			parameters.add(itemId);
			List<ItemGallery> list = itemDao.findByHql("from ItemGallery where itemId=?", parameters);
			for(ItemGallery ig:list) {
				if(set.containsAll(getPropertiesSet(ig.getItemProperties()))) {
					String imageId = ig.getImageId();
					itemDao.delete(ig);
					List<String> parameter = new ArrayList<String>();
					parameter.add(imageId);
					itemDao.excuteHql("delete from Image where id=?", parameter);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.clear();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		return map;
	}

	@Override
	public HashMap<String, Object> addItemGallery(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			String itemId = itemBean.getItemGalleryTmp().getItemId();
			String properties = itemBean.getItemGalleryTmp().getItemProperties();
			Set<String> set = this.getPropertiesSet(properties);
			List<String> parameters = new ArrayList<String>();
			parameters.add(itemId);
			List<ItemGalleryTmp> list = itemDao.findByHql("from ItemGalleryTmp where imageId=?", parameters);
			List<ItemGalleryTmp> listFit = new ArrayList<ItemGalleryTmp>();
			for(ItemGalleryTmp igt:list) {
				if(set.containsAll(getPropertiesSet(igt.getItemProperties()))) {
					listFit.add(igt);
					String imageId = igt.getImageId();
					ImageTmp it = itemDao.findById(imageId,ImageTmp.class);
					String src = it.getPath();
					String srcUrl = it.getUrl();
					String pathPrefix = src.substring(0,src.lastIndexOf(File.separator)-1);
					String pathName = src.substring(src.lastIndexOf(File.separator)+1);
					String urlPrefix = srcUrl.substring(0,srcUrl.lastIndexOf(File.separator)-1);
					String urlName = srcUrl.substring(srcUrl.lastIndexOf(File.separator)+1);
					//1.保存原图
					Image oriImage = ImageUtil.copyImage(src, 
							pathPrefix.substring(0,pathPrefix.lastIndexOf(File.separator))+Constants.IMAGE_ORI+File.separator+pathName,
							urlPrefix.substring(0,urlPrefix.lastIndexOf(File.separator))+Constants.IMAGE_ORI+File.separator+urlName, 
							Constants.IMAGE_TYPE_ORI);
					itemDao.save(oriImage);
					String oriImageId = oriImage.getId();
					oriImage.setOriginalId(oriImageId);
					itemDao.update(oriImage);
					ItemGallery ig = new ItemGallery();
					ig.setCreateTime(new Date());
					ig.setImageId(oriImageId);
					ig.setItemId(itemId);
					ig.setItemProperties(properties);
					ig.setLastUpdateTime(new Date());
					itemDao.save(ig);
					//2.剪辑大图
					Image bigImage = ImageUtil.sizeImage(srcUrl, 
							Constants.IMAGE_BIG_WIDTH, 
							Constants.IMAGE_BIG_HEIGHT,
							pathPrefix.substring(0,pathPrefix.lastIndexOf(File.separator))+Constants.IMAGE_BIG+File.separator+pathName, 
							urlPrefix.substring(0,urlPrefix.lastIndexOf(File.separator))+Constants.IMAGE_BIG+File.separator+urlName, 
							Constants.IMAGE_TYPE_BIG);
					itemDao.save(bigImage);
					String bigImageId = bigImage.getId();
					bigImage.setOriginalId(oriImageId);
					itemDao.update(bigImage);
					ItemGallery bigIg = new ItemGallery();
					bigIg.setCreateTime(new Date());
					bigIg.setImageId(bigImageId);
					bigIg.setItemId(itemId);
					bigIg.setItemProperties(properties);
					bigIg.setLastUpdateTime(new Date());
					if(isFirst(itemId)) {
						ig.setIsDefault(1);
					}else {
						ig.setIsDefault(0);
					}
					itemDao.save(bigIg);
					//3.生成缩略图
					Image smaImage = ImageUtil.sizeImage(srcUrl, 
							Constants.IMAGE_SMA_WIDTH, 
							Constants.IMAGE_SMA_HEIGHT,
							pathPrefix.substring(0,pathPrefix.lastIndexOf(File.separator))+Constants.IMAGE_SMA+File.separator+pathName, 
							urlPrefix.substring(0,urlPrefix.lastIndexOf(File.separator))+Constants.IMAGE_SMA+File.separator+urlName, 
							Constants.IMAGE_TYPE_SMA);
					itemDao.save(smaImage);
					String smaImageId = smaImage.getId();
					smaImage.setOriginalId(oriImageId);
					itemDao.update(smaImage);
					ItemGallery smaIg = new ItemGallery();
					smaIg.setCreateTime(new Date());
					smaIg.setImageId(smaImageId);
					smaIg.setItemId(itemId);
					smaIg.setItemProperties(properties);
					smaIg.setLastUpdateTime(new Date());
					itemDao.save(smaIg);
					//4.删除临时图
					ImageUtil.deleteImage(src);
					List<String> parameter = new ArrayList<String>();
					parameter.add(imageId);
					itemDao.excuteHql("delete from Image where imageId=?", parameter);
					itemDao.excuteHql("delete from ItemGallery where imageId=?", parameter);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			map.clear();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		return map;
	}
	
	private boolean isFirst(String itemId) {
		List<String> parameters = new ArrayList<String>();
		parameters.add(itemId);
		List<ItemGallery> list = itemDao.findByHql("from ItemGallery where itemId=?", parameters);
		if(list.size()==0) {
			return true;
		}
		return false;
	}
	
	@Override
	public HashMap<String, Object> setDefault(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			String id = itemBean.getItemGallery().getId();
			Image image = itemDao.findById(id, Image.class);
			String originalId = image.getOriginalId();
			List<Object> parameters = new ArrayList<Object>();
			parameters.add(originalId);
			parameters.add(Constants.IMAGE_TYPE_BIG);
			List<Image> list = itemDao.findByHql("from Image where originalId=? and type=?", parameters);
			String imageId = list.get(0).getId();
			List<Object> parameters1 = new ArrayList<Object>();
			parameters1.add(imageId);
			List<ItemGallery> list1 = itemDao.findByHql("from ItemGallery where imageId=?", parameters1);
			String itemId = list1.get(0).getItemId();
			List<Object> parameters2 = new ArrayList<Object>();
			parameters2.add(1);
			parameters2.add(imageId);
			itemDao.excuteSql("update jos_item_gallery set is_default=? where image_id=?", parameters2);
			List<Object> parameters3 = new ArrayList<Object>();
			parameters3.add(0);
			parameters3.add(imageId);
			parameters3.add(itemId);
			itemDao.excuteSql("update jos_item_gallery set is_default=? where image_id<>? and item_id=?", parameters3);
		} catch (Exception e) {
			e.printStackTrace();
			map.clear();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		return map;
	}
	
	@Override
	public HashMap<String, Object> delFormalImage(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			String imageId = itemBean.getImage().getId();
			Image image = itemDao.findById(imageId, Image.class);
			String originalId = image.getOriginalId();
			List<String> parameters = new ArrayList<String>();
			parameters.add(originalId);
			List<Image> list = itemDao.findByHql("from Image where originalId=?", parameters);
			for(Image i:list) {
				String path = i.getPath();
				String id = i.getId();
				ImageUtil.deleteImage(path);
				List<String> parameter = new ArrayList<String>();
				parameter.add(id);
				itemDao.excuteHql("delete from ItemGallery where imageId=?", parameter);
				itemDao.delete(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.clear();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		return map;
	}

	@Override
	public HashMap<String, Object> getSmallItemGallery(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			ItemGallery ig = itemBean.getItemGallery();
			String itemId = ig.getItemId();
			String pro = ig.getItemProperties();
			Set<String> set = this.getPropertiesSet(pro);
			List<String> parameters = new ArrayList<String>();
			parameters.add(itemId);
			List<ItemGallery> list = itemDao.findByHql("from ItemGallery where itemId=?", parameters);
			List<ItemGallery> fitList = new ArrayList<ItemGallery>();
			List<Object> imageList = new ArrayList<Object>();
			for(ItemGallery itemGallery:list) {
				if(set.containsAll(getPropertiesSet(itemGallery.getItemProperties()))) {
					fitList.add(itemGallery);
					String imageId = itemGallery.getImageId();
					Image image = itemDao.findById(imageId,Image.class);
					if(Constants.IMAGE_TYPE_SMA==image.getType()) {
						imageList.add(image);
					}
				}
			}
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
			map.put(Constants.RETURN_DATA, JsonUtil.getJsonStrFromList(imageList));
		} catch (Exception e) {
			e.printStackTrace();
			map.clear();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		return map;
	}

	@Override
	public HashMap<String, Object> getInitSmallItemGallery(ItemBean itemBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			ItemGallery ig = itemBean.getItemGallery();
			String itemId = ig.getItemId();
			String pro = getInitProperties(itemId);
			Set<String> set = this.getPropertiesSet(pro);
			List<String> parameters = new ArrayList<String>();
			parameters.add(itemId);
			List<ItemGallery> list = itemDao.findByHql("from ItemGallery where itemId=?", parameters);
			List<ItemGallery> fitList = new ArrayList<ItemGallery>();
			List<Object> imageList = new ArrayList<Object>();
			for(ItemGallery itemGallery:list) {
				if(set.containsAll(getPropertiesSet(itemGallery.getItemProperties()))) {
					fitList.add(itemGallery);
					String imageId = itemGallery.getImageId();
					Image image = itemDao.findById(imageId,Image.class);
					if(Constants.IMAGE_TYPE_SMA==image.getType()) {
						imageList.add(image);
					}
				}
			}
			map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
			map.put(Constants.RETURN_DATA, JsonUtil.getJsonStrFromList(imageList));
		} catch (Exception e) {
			e.printStackTrace();
			map.clear();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
		}
		return map;
	}
	
	private String getInitProperties(String itemId) {
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(itemId);
		parameters.add(1);
		List<ItemGallery> list = itemDao.findByHql("from ItemGallery where itemId=? and isDefault=?", parameters);
		String properties = list.get(0).getItemProperties();
		return properties;
	}
	
	private void deleteTmpImage() {
		File file = new File(ServletActionContext.getServletContext().getContextPath()+Constants.IMAGEROOT);
		circle(file);
	}
	
	private void circle(File file) {
		File[] files = file.listFiles();
		for(File f:files) {
			if(f.isDirectory()&&Constants.IMAGE_TMP.equals(f.getName())) {
				ImageUtil.deleteImage(f.getPath());
			}else if(f.isDirectory()) {
				circle(f);
			}
		}
	}
	
	private static final long PERIOD_DAY = 24 * 60 * 60 * 1000; 
	
	@Override
	public void timeInit() {
		Timer timer = new Timer();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 2);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		Date date = calendar.getTime();
		timer.schedule(new TimerTask() {  
		    @Override  
		    public void run() {  
		        deleteTmpImage();
		    }  
		}, date,PERIOD_DAY);  
	}

}
