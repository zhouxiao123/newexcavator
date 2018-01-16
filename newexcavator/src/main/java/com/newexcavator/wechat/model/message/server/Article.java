/**
 * 
 */
package com.newexcavator.wechat.model.message.server;

/**
 * @author Administrator
 * 上传到微信服务器
 */
public class Article {
	// 图文消息名称
	private String thumb_media_id;
	// 图文作者
	private String author;
	// 标题
	private String title;
	// 点击图文消息跳转链接
	private String content_source_url;
	// 图文内容
	private String content;
	// 描述
	private String digest;

	public String getThumb_media_id() {
		return thumb_media_id;
	}

	public void setThumb_media_id(String thumb_media_id) {
		this.thumb_media_id = thumb_media_id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent_source_url() {
		return content_source_url;
	}

	public void setContent_source_url(String content_source_url) {
		this.content_source_url = content_source_url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

}
