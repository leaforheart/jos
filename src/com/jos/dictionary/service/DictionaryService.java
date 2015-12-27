package com.jos.dictionary.service;

import java.util.HashMap;

import com.jos.common.baseclass.IBaseService;
import com.jos.dictionary.vo.DictionaryBean;

public interface DictionaryService extends IBaseService {
	HashMap<String,Object> addValue(DictionaryBean dictionaryBean);
	HashMap<String,Object> addType(DictionaryBean dictionaryBean);
	HashMap<String,Object> delValue(DictionaryBean dictionaryBean);
	HashMap<String,Object> delType(DictionaryBean dictionaryBean);
	HashMap<String,Object> updValue(DictionaryBean dictionaryBean);
	HashMap<String,Object> updType(DictionaryBean dictionaryBean);
	HashMap<String,Object> queValue(DictionaryBean dictionaryBean);
	HashMap<String,Object> queType(DictionaryBean dictionaryBean);
	HashMap<String,Object> queList(DictionaryBean dictionaryBean);
}
