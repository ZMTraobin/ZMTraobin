/**
 * .
 */
package com.cmig.future.csportal.module.base.entity;


/**
 * 分页类
 * @author ThinkGem
 * @version 2013-7-2
 * @param <T>
 */
public class AppPage<T> extends Page<T>{

	/**
	* serialVersionUID:TODO（用一句话描述这个变量表示什么）
	*/
	private static final long serialVersionUID = 1L;
	
	protected long count;// 总记录数，设置为“-1”表示不查询总数

	/**
	 * 设置数据总数
	 * @param count
	 */
	public void setCount(long count) {
		super.count = count;
//		if (pageSize >= count){
//			pageNo = 1;
//		}
	}
	 
	/**
	 * 获取 Hibernate FirstResult
	 */
	public int getFirstResult(){
		int firstResult = (getPageNo() - 1) * getPageSize();
//		if (firstResult >= getCount()) {
//			firstResult = 0;
//		}
		return firstResult;
	}
	/**
	 * 获取分页HTML代码
	 * @return
	 */
	public String getHtml(){
		return "";
	}
	
}
