package com.jos.dictionary.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.inveno.base.BaseModel;

@Entity
@Table(name = "jos_dictionary")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Dictionary extends BaseModel {
	private static final long serialVersionUID = 2630538478318275512L;
	
	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "dictionaryGenerate", strategy = GenerationType.IDENTITY)
	@GenericGenerator(name = "dictionaryGenerate", strategy = "native")
	private String id;
	
	@Column(name = "parent_id")
	private String parentId;
	
	@Column(name = "type_code")
	private String typeCode;
	
	@Column(name = "type_name")
	private String typeName;
	
	@Column(name = "value_code")
	private String valueCode;
	
	@Column(name = "value_name")
	private String valueName;

	@Column(name = "create_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date CreateTime;
	
	@Column(name = "last_update_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdateTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parent_id) {
		this.parentId = parent_id;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String type_code) {
		this.typeCode = type_code;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String type_name) {
		this.typeName = type_name;
	}

	public String getValueCode() {
		return valueCode;
	}

	public void setValueCode(String value_code) {
		this.valueCode = value_code;
	}

	public String getValueName() {
		return valueName;
	}

	public void setValueName(String value_name) {
		this.valueName = value_name;
	}

	public Date getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Date createTime) {
		CreateTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
	
}
