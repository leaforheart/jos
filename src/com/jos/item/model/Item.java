package com.jos.item.model;

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
@Table(name = "jos_item")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Item extends BaseModel {
	private static final long serialVersionUID = 7401417604615898347L;
	
	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "itemGenerate", strategy = GenerationType.IDENTITY)
	@GenericGenerator(name = "itemGenerate", strategy = "native")
	private String id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "des")
	private String des;
	
	@Column(name = "express_des")
	private String expressDes;
	
	@Column(name = "service_des")
	private String serviceDes;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "create_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	
	@Column(name = "last_update_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdateTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getExpressDes() {
		return expressDes;
	}
	public void setExpressDes(String expressDes) {
		this.expressDes = expressDes;
	}
	public String getServiceDes() {
		return serviceDes;
	}
	public void setServiceDes(String serviceDes) {
		this.serviceDes = serviceDes;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
}
