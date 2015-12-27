package com.jos.order.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.inveno.base.BaseModel;
import com.jos.address.model.Address;
@Entity
@Table(name = "jos_order")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Order extends BaseModel {
	private static final long serialVersionUID = -5942576164868799469L;
	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "orderGenerate", strategy = GenerationType.IDENTITY)
	@GenericGenerator(name = "orderGenerate", strategy = "native")
	private String id;
	
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "address_id")
	private String addressId;
	
	@Column(name = "express_supplier")
	private String expressSupplier;
	
	@Column(name = "express_code")
	private String expressCode;
	
	@Column(name = "express_cost")
	private String expressCost;
	
	@Column(name = "pay_supplier")
	private String paySupplier;
	
	@Column(name = "pay_code")
	private String payCode;
	
	@Column(name = "pay_cost")
	private String payCost;
	
	@Column(name = "order_code")
	private String orderCode;
	
	@Column(name = "order_cost")
	private String orderCost;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "create_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	
	@Column(name = "last_update_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdateTime;
	
	@Transient
	private List<OrderItem> orderItemList;
	
	@Transient
	private Address address;

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getExpressSupplier() {
		return expressSupplier;
	}

	public void setExpressSupplier(String expressSupplier) {
		this.expressSupplier = expressSupplier;
	}

	public String getExpressCode() {
		return expressCode;
	}

	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}

	public String getExpressCost() {
		return expressCost;
	}

	public void setExpressCost(String expressCost) {
		this.expressCost = expressCost;
	}

	public String getPaySupplier() {
		return paySupplier;
	}

	public void setPaySupplier(String paySupplier) {
		this.paySupplier = paySupplier;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public String getPayCost() {
		return payCost;
	}

	public void setPayCost(String payCost) {
		this.payCost = payCost;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrderCost() {
		return orderCost;
	}

	public void setOrderCost(String orderCost) {
		this.orderCost = orderCost;
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
