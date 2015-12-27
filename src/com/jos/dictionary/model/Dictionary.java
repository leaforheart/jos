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
	private String parent_id;
	
	@Column(name = "type_code")
	private String type_code;
	
	@Column(name = "type_name")
	private String type_name;
	
	@Column(name = "value_code")
	private String value_code;
	
	@Column(name = "value_name")
	private String value_name;

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

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getType_code() {
		return type_code;
	}

	public void setType_code(String type_code) {
		this.type_code = type_code;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getValue_code() {
		return value_code;
	}

	public void setValue_code(String value_code) {
		this.value_code = value_code;
	}

	public String getValue_name() {
		return value_name;
	}

	public void setValue_name(String value_name) {
		this.value_name = value_name;
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
