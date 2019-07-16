package com.taobao.arthas.manage.dao.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

public class BaseDo implements Serializable{
	private static final long serialVersionUID = -2162145358751660659L;
	
	@Column(nullable = false)
	private Date gmtCreate = new Date();
	@Column(nullable = false)
	private Date gmtModified = new Date();
	
	public Date getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public Date getGmtModified() {
		return gmtModified;
	}
	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}
}
