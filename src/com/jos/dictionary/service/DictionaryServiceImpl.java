package com.jos.dictionary.service;

import java.util.HashMap;

import com.jos.common.baseclass.AbstractBaseService;
import com.jos.dictionary.dao.DictionaryDao;
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

		return map;
	}

	@Override
	public HashMap<String, Object> addType(DictionaryBean dictionaryBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();

		return map;
	}

	@Override
	public HashMap<String, Object> delValue(DictionaryBean dictionaryBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();

		return map;
	}

	@Override
	public HashMap<String, Object> delType(DictionaryBean dictionaryBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();

		return map;
	}

	@Override
	public HashMap<String, Object> updValue(DictionaryBean dictionaryBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();

		return map;
	}

	@Override
	public HashMap<String, Object> updType(DictionaryBean dictionaryBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();

		return map;
	}

	@Override
	public HashMap<String, Object> queValue(DictionaryBean dictionaryBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();

		return map;
	}

	@Override
	public HashMap<String, Object> queType(DictionaryBean dictionaryBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();

		return map;
	}

	@Override
	public HashMap<String, Object> queList(DictionaryBean dictionaryBean) {
		HashMap<String,Object> map = new HashMap<String,Object>();

		return map;
	}
	
}
