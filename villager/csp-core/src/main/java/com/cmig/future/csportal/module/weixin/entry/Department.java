package com.cmig.future.csportal.module.weixin.entry;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 16:28 2017/12/5.
 * @Modified by zhangtao on 16:28 2017/12/5.
 */
public class Department {


	/**
	 * name : 广州研发中心
	 * parentid : 1
	 * order : 1
	 * id : 2
	 */

	private String name;
	private Integer parentid;
	private Integer order;
	private Integer id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParentid() {
		return parentid;
	}

	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
