package com.cmig.future.csportal.module.weixin.entry;

import java.util.List;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 18:19 2017/11/28.
 * @Modified by zhangtao on 18:19 2017/11/28.
 */
public class Message {


	/**
	 * touser : UserID1|UserID2|UserID3
	 * toparty : PartyID1|PartyID2
	 * totag : TagID1 | TagID2
	 * msgtype : text
	 * agentid : 1
	 * text : {"content":"你的快递已到，请携带工卡前往邮件中心领取。\n出发前可查看<a href=\"http://work.weixin.qq.com\">邮件中心视频实况<\/a>，聪明避开排队。"}
	 * safe : 0
	 */

	private String touser;
	private String toparty;
	private String totag;
	private String msgtype;
	private int agentid;
	private TextBean text;
	private int safe;
	private TextCard textcard;
	private News news;
	private Mpnews mpnews;

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getToparty() {
		return toparty;
	}

	public void setToparty(String toparty) {
		this.toparty = toparty;
	}

	public String getTotag() {
		return totag;
	}

	public void setTotag(String totag) {
		this.totag = totag;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public int getAgentid() {
		return agentid;
	}

	public void setAgentid(int agentid) {
		this.agentid = agentid;
	}

	public TextBean getText() {
		return text;
	}

	public void setText(TextBean text) {
		this.text = text;
	}

	public int getSafe() {
		return safe;
	}

	public void setSafe(int safe) {
		this.safe = safe;
	}

	public TextCard getTextcard() {
		return textcard;
	}

	public void setTextcard(TextCard textcard) {
		this.textcard = textcard;
	}

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	public Mpnews getMpnews() {
		return mpnews;
	}

	public void setMpnews(Mpnews mpnews) {
		this.mpnews = mpnews;
	}

	public static class TextBean {
		/**
		 * content : 你的快递已到，请携带工卡前往邮件中心领取。
		 出发前可查看<a href="http://work.weixin.qq.com">邮件中心视频实况</a>，聪明避开排队。
		 */

		private String content;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}

	public static class TextCard {

		private String title;

		private String description;

		private String url;

		private String btntxt;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getBtntxt() {
			return btntxt;
		}

		public void setBtntxt(String btntxt) {
			this.btntxt = btntxt;
		}
	}

	public static class Articles {

		private String title;

		private String description;

		private String url;

		private String picurl;

		private String btntxt;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getPicurl() {
			return picurl;
		}

		public void setPicurl(String picurl) {
			this.picurl = picurl;
		}

		public String getBtntxt() {
			return btntxt;
		}

		public void setBtntxt(String btntxt) {
			this.btntxt = btntxt;
		}

		private String thumb_media_id;
		private String author;
		private String content_source_url;
		private String content;
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


	public static class News {
		private List<Articles> articles;

		public List<Articles> getArticles() {
			return articles;
		}

		public void setArticles(List<Articles> articles) {
			this.articles = articles;
		}
	}

	public static class Mpnews {
		private List<Articles> articles;

		public List<Articles> getArticles() {
			return articles;
		}

		public void setArticles(List<Articles> articles) {
			this.articles = articles;
		}
	}
}
