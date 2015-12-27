package com.jos.dictionary.action;

import java.util.HashMap;

import com.inveno.util.JsonUtil;
import com.jos.common.baseclass.BaseAction;
import com.jos.dictionary.service.DictionaryService;
import com.jos.dictionary.vo.DictionaryBean;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("rawtypes")
public class DictionaryAction extends BaseAction implements ModelDriven {
	private static final long serialVersionUID = 5047564842394663031L;
	private DictionaryService dictionaryService;
	private DictionaryBean dictionaryBean;
	

	public DictionaryService getDictionaryService() {
		return dictionaryService;
	}


	public void setDictionaryService(DictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}


	public DictionaryBean getDictionaryBean() {
		return dictionaryBean;
	}


	public void setDictionaryBean(DictionaryBean dictionaryBean) {
		this.dictionaryBean = dictionaryBean;
	}


	@Override
	public Object getModel() {
		if(dictionaryBean==null) {
			dictionaryBean = new DictionaryBean();
		}
		return dictionaryBean;
	}
	
	public void addValue(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(dictionaryService.addValue(dictionaryBean)));
	}
	public void addType(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(dictionaryService.addType(dictionaryBean)));
	}
	public void delValue(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(dictionaryService.delValue(dictionaryBean)));
	}
	public void delType(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(dictionaryService.delType(dictionaryBean)));
	}
	public void updValue(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(dictionaryService.updValue(dictionaryBean)));
	}
	public void updType(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(dictionaryService.updType(dictionaryBean)));
	}
	public void queValue(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(dictionaryService.queValue(dictionaryBean)));
	}
	public void queType(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(dictionaryService.queType(dictionaryBean)));
	}
	public void queList(){
		this.writeToClient(JsonUtil.getJsonStrByConfigFromMap(dictionaryService.queList(dictionaryBean)));
	}

}
