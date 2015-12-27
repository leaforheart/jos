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
@Table(name = "jos_item_gallery")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ItemGallery extends BaseModel {
	private static final long serialVersionUID = 285403697463292037L;
	
	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "itemGalleryGenerate", strategy = GenerationType.IDENTITY)
	@GenericGenerator(name = "itemGalleryGenerate", strategy = "native")
	private String id;
	
	@Column(name = "item_id")
	private String itemId;
	
	@Column(name = "item_properties")
	private String itemProperties;
	
	@Column(name = "sequence")
	private String sequence;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "path")
	private String path;
	
	@Column(name = "is_default")
	private String isDefault;
	
	@Column(name = "create_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	
	@Column(name = "last_update_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdateTime;
}
