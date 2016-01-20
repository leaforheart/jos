package com.jos.dictionary.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.inveno.util.JsonUtil;
import com.inveno.util.StringUtil;
import com.jos.common.baseclass.AbstractBaseService;
import com.jos.common.util.Constants;
import com.jos.dictionary.dao.DictionaryDao;
import com.jos.dictionary.model.Dictionary;
import com.jos.dictionary.vo.DictionaryBean;

public class DictionaryServiceImpl extends AbstractBaseService implements DictionaryService {
	
	private DictionaryDao dictionaryDao;
	
	

	public DictionaryDao getDictionaryDao() {
		return dictionaryDao;
	}

	public void setDictionaryDao(DictionaryDao dictionaryDao) {
		this.dictionaryDao = dictionaryDao;
	}

	@Override
	public HashMap<String, Object> addValue(DictionaryBean dictionaryBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			Dictionary dictionary = dictionaryBean.getDictionary();
			String valueCode = dictionary.getValue_code();
			String typeCode = dictionary.getType_code();
			List<String> parameters = new ArrayList<String>();
			parameters.add(typeCode);
			parameters.add(valueCode);
			List<Dictionary> list = dictionaryDao.findByHql("from Dictionary where type_code=? and value_code=?", parameters);
			if(list.size()>0) {
				map.put(Constants.RETURN_CODE, "-1");//已存在
				return map;
			}
			dictionary.setCreateTime(new Date());
			dictionary.setLastUpdateTime(new Date());
			dictionaryDao.save(dictionary);
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
			return map;
		}
		map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		return map;
	}

	@Override
	public HashMap<String, Object> addType(DictionaryBean dictionaryBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			Dictionary dictionary = dictionaryBean.getDictionary();
			String typeCode = dictionary.getType_code();
			List<String> parameters = new ArrayList<String>();
			parameters.add(typeCode);
			List<Dictionary> list = dictionaryDao.findByHql("from Dictionary where type_code=? and value_code is null", parameters);
			if(list.size()>0) {
				map.put(Constants.RETURN_CODE, "-1");//已存在
				return map;
			}
			dictionary.setCreateTime(new Date());
			dictionary.setLastUpdateTime(new Date());
			dictionaryDao.save(dictionary);
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
			return map;
		}
		map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		return map;
	}

	@Override
	public HashMap<String, Object> delValue(DictionaryBean dictionaryBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			String typeCode = dictionaryBean.getDictionary().getType_code();
			String valueCode = dictionaryBean.getDictionary().getValue_code();
			List<String> parameter = new ArrayList<String>();
			parameter.add(typeCode);
			parameter.add(valueCode);
			dictionaryDao.excuteHql("delete from Dictionary where type_code=? and value_code=?", parameter);
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
			return map;
		}
		map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		return map;
	}

	@Override
	public HashMap<String, Object> delType(DictionaryBean dictionaryBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			String typeCode = dictionaryBean.getDictionary().getType_code();
			List<String> parameter = new ArrayList<String>();
			parameter.add(typeCode);
			dictionaryDao.excuteHql("delete from Dictionary where type_code=? and value_code is null", parameter);
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
			return map;
		}
		map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		return map;
	}

	@Override
	public HashMap<String, Object> updValue(DictionaryBean dictionaryBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			Dictionary dictionary = dictionaryBean.getDictionary();
			String valueCode = dictionary.getValue_code();
			String typeCode = dictionary.getType_code();
			List<String> parameters = new ArrayList<String>();
			parameters.add(typeCode);
			parameters.add(valueCode);
			List<Dictionary> list = dictionaryDao.findByHql("from Dictionary where type_code=? and value_code=?", parameters);
			if(list.size()==0) {
				map.put(Constants.RETURN_CODE, "-1");//不存在
				return map;
			}
			list.get(0).setValue_name(dictionary.getValue_name());
			list.get(0).setLastUpdateTime(new Date());
			dictionaryDao.update(list.get(0));
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
			return map;
		}
		map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		return map;
	}

	@Override
	public HashMap<String, Object> updType(DictionaryBean dictionaryBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			Dictionary dictionary = dictionaryBean.getDictionary();
			String typeCode = dictionary.getType_code();
			List<String> parameters = new ArrayList<String>();
			parameters.add(typeCode);
			List<Dictionary> list = dictionaryDao.findByHql("from Dictionary where type_code=? and value_code is null", parameters);
			if(list.size()==0) {
				map.put(Constants.RETURN_CODE, "-1");//不存在
				return map;
			}
			List<Dictionary> list1 = dictionaryDao.findByHql("from Dictionary where type_code=?", parameters);
			for(Dictionary dic:list1) {
				dic.setType_name(dictionary.getType_name());
				dic.setLastUpdateTime(new Date());
				dictionaryDao.update(dic);
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
	public HashMap<String, Object> queValue(DictionaryBean dictionaryBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			List<String> parameters = new ArrayList<String>();
			parameters.add(dictionaryBean.getDictionary().getType_code());
			List<Object> list = dictionaryDao.findByHql("from Dictionary where type_code=? and parent_id is not null", parameters);
			map.put(Constants.RETURN_DATA,JsonUtil.getJsonStrFromList(list));
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
			return map;
		}
		map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		return map;
	}

	@Override
	public HashMap<String, Object> queType(DictionaryBean dictionaryBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			List<Object> list = dictionaryDao.findByHql("from Dictionary where parent_id is null", null);
			map.put(Constants.RETURN_DATA,JsonUtil.getJsonStrFromList(list));
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
			return map;
		}
		map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		return map;
	}

	@Override
	public HashMap<String, Object> queList(DictionaryBean dictionaryBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			String hql = "from Dictionary where 1=1";
			List<String> parameters = new ArrayList<String>();
			if(StringUtil.isNotEmpty(dictionaryBean.getDictionary().getType_name())) {
				hql += "and type_name like ?";
				parameters.add("%"+dictionaryBean.getDictionary().getType_name()+"%");
			}
			if(StringUtil.isNotEmpty(dictionaryBean.getDictionary().getType_code())) {
				hql += "and type_code = ?";
				parameters.add(dictionaryBean.getDictionary().getType_code());			
			}
			if(StringUtil.isNotEmpty(dictionaryBean.getDictionary().getValue_name())) {
				hql += "and value_name like ?";
				parameters.add("%"+dictionaryBean.getDictionary().getValue_name()+"%");
			}
			if(StringUtil.isNotEmpty(dictionaryBean.getDictionary().getValue_code())) {
				hql += "and value_code = ?";
				parameters.add(dictionaryBean.getDictionary().getValue_code());
			}
			List<Object> list = dictionaryDao.findByHql(hql, parameters);
			map.put(Constants.RETURN_DATA,JsonUtil.getJsonStrFromList(list));
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constants.RETURN_CODE, Constants.SEVER_ERROR);
			return map;
		}
		map.put(Constants.RETURN_CODE, Constants.SUCCESS_CODE);
		return map;
	}
	
}
