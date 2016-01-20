package com.jos.authenticate.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.inveno.base.BaseModel;
@Entity
@Table(name = "ems_mt_task")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EmsMtTask extends BaseModel {
	private static final long serialVersionUID = 2052948531240875605L;

	@Id
	@Column(name = "TASK_ID")
	private String taskId;
	
	@Column(name = "MSG_CONTENT")
	private String msgContent;
	
	@Column(name = "TO_MOBILE")
	private String toMobile;
	
	@Column(name = "CHANNEL_ID")
	private String channelId;
	
	@Column(name = "EXT")
	private String ext;
	
	@Column(name = "CREATE_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	
	@Column(name = "SEND_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date sendTime;
	
	@Column(name = "PRIORITY")
	private int priority;
	
	@Column(name = "RESEND")
	private int resend;
	
	@Column(name = "RESEND_TIMES")
	private int resendTimes;
	
	@Column(name = "ALLOW_ROUTE")
	private int allwoRoute;

	

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getToMobile() {
		return toMobile;
	}

	public void setToMobile(String toMobile) {
		this.toMobile = toMobile;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getResend() {
		return resend;
	}

	public void setResend(int resend) {
		this.resend = resend;
	}

	public int getResendTimes() {
		return resendTimes;
	}

	public void setResendTimes(int resendTimes) {
		this.resendTimes = resendTimes;
	}

	public int getAllwoRoute() {
		return allwoRoute;
	}

	public void setAllwoRoute(int allwoRoute) {
		this.allwoRoute = allwoRoute;
	}
	
	
}
