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
			String valueCode = dictionary.getValueCode();
			String typeCode = dictionary.getTypeCode();
			List<String> parameters = new ArrayList<String>();
			parameters.add(typeCode);
			parameters.add(valueCode);
			List<Dictionary> list = dictionaryDao.findByHql("from Dictionary where typeCode=? and valueCode=?", parameters);
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
			String typeCode = dictionary.getTypeCode();
			List<String> parameters = new ArrayList<String>();
			parameters.add(typeCode);
			List<Dictionary> list = dictionaryDao.findByHql("from Dictionary where typeCode=? and valueCode is null", parameters);
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
			String typeCode = dictionaryBean.getDictionary().getTypeCode();
			String valueCode = dictionaryBean.getDictionary().getValueCode();
			List<String> parameter = new ArrayList<String>();
			parameter.add(typeCode);
			parameter.add(valueCode);
			dictionaryDao.excuteHql("delete from Dictionary where typeCode=? and valueCode=?", parameter);
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
			String typeCode = dictionaryBean.getDictionary().getTypeCode();
			List<String> parameter = new ArrayList<String>();
			parameter.add(typeCode);
			dictionaryDao.excuteHql("delete from Dictionary where typeCode=?", parameter);
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
			String valueCode = dictionary.getValueCode();
			String typeCode = dictionary.getTypeCode();
			List<String> parameters = new ArrayList<String>();
			parameters.add(typeCode);
			parameters.add(valueCode);
			List<Dictionary> list = dictionaryDao.findByHql("from Dictionary where typeCode=? and valueCode=?", parameters);
			if(list.size()==0) {
				map.put(Constants.RETURN_CODE, "-1");//不存在
				return map;
			}
			list.get(0).setValueName(dictionary.getValueName());
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
			String typeCode = dictionary.getTypeCode();
			List<String> parameters = new ArrayList<String>();
			parameters.add(typeCode);
			List<Dictionary> list = dictionaryDao.findByHql("from Dictionary where typeCode=? and valueCode is null", parameters);
			if(list.size()==0) {
				map.put(Constants.RETURN_CODE, "-1");//不存在
				return map;
			}
			List<Dictionary> list1 = dictionaryDao.findByHql("from Dictionary where typeCode=?", parameters);
			for(Dictionary dic:list1) {
				dic.setTypeName(dictionary.getTypeName());
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
			parameters.add(dictionaryBean.getDictionary().getTypeCode());
			List<Object> list = dictionaryDao.findByHql("from Dictionary where typeCode=? and parentId is not null", parameters);
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
			List<Object> list = dictionaryDao.findByHql("from Dictionary where parentId is null", null);
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
			if(dictionaryBean.getDictionary()!=null) {
				if(StringUtil.isNotEmpty(dictionaryBean.getDictionary().getTypeName())) {
					hql += "and typeName like ?";
					parameters.add("%"+dictionaryBean.getDictionary().getTypeName()+"%");
				}
				if(StringUtil.isNotEmpty(dictionaryBean.getDictionary().getTypeCode())) {
					hql += "and typeCode = ?";
					parameters.add(dictionaryBean.getDictionary().getTypeCode());			
				}
				if(StringUtil.isNotEmpty(dictionaryBean.getDictionary().getValueName())) {
					hql += "and valueName like ?";
					parameters.add("%"+dictionaryBean.getDictionary().getValueName()+"%");
				}
				if(StringUtil.isNotEmpty(dictionaryBean.getDictionary().getValueCode())) {
					hql += "and valueCode = ?";
					parameters.add(dictionaryBean.getDictionary().getValueCode());
				}
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
