package com.newexcavator.wechat.model.message.server;

import java.util.List;

/**
 * 图文消息
 * 
 * @author Administrator
 *
 */
public class NewsMessage {
    // 多条图文消息信息，默认第一个item为大图  
	private List<Article> articles;

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}
}
