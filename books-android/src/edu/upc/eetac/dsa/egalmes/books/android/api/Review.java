package edu.upc.eetac.dsa.egalmes.books.android.api;

import java.util.HashMap;
import java.util.Map;

public class Review {
	private Map<String, Link> links = new HashMap<>();
	private String idreview;
	private String username;
	private String dateupdate;
	private String text;
	private String idbook;
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDateupdate() {
		return dateupdate;
	}
	public void setDateupdate(String dateupdate) {
		this.dateupdate = dateupdate;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getIdreview() {
		return idreview;
	}
	public void setIdreview(String idreview) {
		this.idreview = idreview;
	}
	public String getIdbook() {
		return idbook;
	}
	public void setIdbook(String idbook) {
		this.idbook = idbook;
	}
	public Map<String, Link> getLinks() {
		// TODO Auto-generated method stub
		return links;
	}
	
	
	

}
