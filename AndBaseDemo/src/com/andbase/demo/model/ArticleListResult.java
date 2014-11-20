package com.andbase.demo.model;

import java.util.List;

import com.ab.model.AbResult;

/**
 * 
 *
 */
public class ArticleListResult extends AbResult {

	private List<Article> items;

	public List<Article> getItems() {
		return items;
	}

	public void setItems(List<Article> items) {
		this.items = items;
	}
	
	

}
