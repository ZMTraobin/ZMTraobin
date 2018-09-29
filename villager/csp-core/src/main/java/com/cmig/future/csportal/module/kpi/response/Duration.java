package com.cmig.future.csportal.module.kpi.response;

import java.util.List;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 10:42 2018/1/25.
 * @Modified by zhangtao on 10:42 2018/1/25.
 */
public class Duration {


	/**
	 * data : [{"key":"1-3秒","num":1,"percent":20},{"key":"3-10秒","num":2,"percent":40},{"key":"30-60秒","num":1,"percent":20},{"key":"1-3分钟","num":1,"percent":20}]
	 * average : 00:00:34
	 */

	private String average;
	private List<Duration.DataBean> data;

	public String getAverage() {
		return average;
	}

	public void setAverage(String average) {
		this.average = average;
	}

	public List<Duration.DataBean> getData() {
		return data;
	}

	public void setData(List<Duration.DataBean> data) {
		this.data = data;
	}


	public static class DataBean {
		/**
		 * key : 1-3秒
		 * num : 1
		 * percent : 20
		 */

		private String key;
		private Long num;
		private Double percent;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public Long getNum() {
			return num;
		}

		public void setNum(Long num) {
			this.num = num;
		}

		public Double getPercent() {
			return percent;
		}

		public void setPercent(Double percent) {
			this.percent = percent;
		}
	}
}
