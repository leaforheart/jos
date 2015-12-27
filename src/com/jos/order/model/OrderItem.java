package com.jos.order.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.inveno.base.BaseModel;
@Entity
@Table(name = "jos_order_item")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderItem extends BaseModel {
	private static final long serialVersionUID = -3888184453495546109L;
	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "orderItemGenerate", strategy = GenerationType.IDENTITY)
	@GenericGenerator(name = "orderItemGenerate", strategy = "native")
	private String id;
	
	@Column(name = "order_id")
	private String orderId;
	
	@Column(name = "item_id")
	private String itemId;
	
	@Column(name = "item_name")
	private String itemName;
	
	@Column(name = "item_smpic")
	private String itemSmpic;
	
	@Column(name = "item_properties")
	private String itemProperties;
	
	@Column(name = "item_unit_price")
	private String itemUnitPrice;
	
	@Column(name = "item_amount")
	private String itemAmount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemSmpic() {
		return itemSmpic;
	}

	public void setItemSmpic(String itemSmpic) {
		this.itemSmpic = itemSmpic;
	}

	public String getItemProperties() {
		return itemProperties;
	}

	public void setItemProperties(String itemProperties) {
		this.itemProperties = itemProperties;
	}

	public String getItemUnitPrice() {
		return itemUnitPrice;
	}

	public void setItemUnitPrice(String itemUnitPrice) {
		this.itemUnitPrice = itemUnitPrice;
	}

	public String getItemAmount() {
		return itemAmount;
	}

	public void setItemAmount(String itemAmount) {
		this.itemAmount = itemAmount;
	}
	
	
}
