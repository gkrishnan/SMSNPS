package com.gkrishnan.smsnps;


import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable public class VoteInfo {   

	@PrimaryKey @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY) 
	private Key id; 

	@Persistent
	private String mobile;

	@Persistent
	private Date date;

	@Persistent
	private String response;     

	public Key getKey() { 
		return this.id;    
	} 

	public VoteInfo(String mobile,String rank) {    
		this.setMobile(mobile);
		this.setRank(rank);   
		this.setDate(new Date());
	}

	public void setRank(String response) {
		this.response = response;
	}

	public String getRank() {
		return this.response;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return this.date;
	}	
}


