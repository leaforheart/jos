package com.jos.authenticate.vo;

import com.jos.authenticate.model.User;

public class AuthenticateBean {
	private User user;
	private String phoneCode;
	private String newCredential;
	private String phoneCodeUse;

	public String getPhoneCodeUse() {
		return phoneCodeUse;
	}

	public void setPhoneCodeUse(String phoneCodeUse) {
		this.phoneCodeUse = phoneCodeUse;
	}

	public String getNewCredential() {
		return newCredential;
	}

	public void setNewCredential(String newCredential) {
		this.newCredential = newCredential;
	}

	public String getPhoneCode() {
		return phoneCode;
	}

	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
