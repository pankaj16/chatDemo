package com.pankaj.chatapp.entities;

import java.io.Serializable;

public class Images implements Serializable{
	private static final long serialVersionUID = -3377592083233940970L;
	String name;
	String publicId;
	String url;
	String created_at;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPublicId() {
		return publicId;
	}
	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	
}
