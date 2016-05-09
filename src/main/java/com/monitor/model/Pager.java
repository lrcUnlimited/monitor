package com.monitor.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页工具类
 * 
 * @author li
 * 
 */
public class Pager {

	public static final int MAX_PAGE_SIZE = 500;// 每页最大记录数限制
	private int pageNum = 1;// 当前页码
	private int pageSize = 20;// 每页记录数
	private int totalCount = 0;// 总记录数
	private int totalPage = 0;// 总页数
	private Object item;
	private List<?> items = new ArrayList();// 数据List

	public Pager() {

	}

	/**
	 * 构造函数
	 * 
	 * @param totalPage
	 *            总页数
	 * @param totalCount
	 *            总数据条数
	 * @param items
	 *            当前页数据列表
	 */
	public Pager(int pageNum, int pageSize) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	/**
	 * 构造函数
	 * 
	 * @param totalPage
	 *            总页数
	 * @param totalCount
	 *            总数据条数
	 * @param items
	 *            当前页数据列表
	 */
	public Pager(int totalPage, int totalCount, List<Object> items) {
		this.totalPage = totalPage;
		this.totalCount = totalCount;
		this.items = items;
	}

	/**
	 * 构造函数
	 * 
	 * @param totalPage
	 *            总页数
	 * @param totalCount
	 *            总数据条数
	 * @param items
	 *            当前页数据列表
	 * @param pageNum
	 *            当前页号
	 * @param totalSize
	 *            每页数据条数
	 */
	public Pager(int totalPage, int totalCount, List<Object> items,
			int pageNum, int pageSize) {
		this(totalPage, totalCount, items);
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	/**
	 * 数据总条数
	 * 
	 * @return
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * 数据总条数
	 * 
	 * @param totalCount
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 总页数
	 * 
	 * @return
	 */
	public int getTotalPage() {
		totalPage = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			totalPage++;
		}
		return totalPage;
	}

	/**
	 * 总页数
	 * 
	 * @param totalPage
	 */
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	/**
	 * 当前页的数据列表
	 * 
	 * @return
	 */
	public List<?> getItems() {
		return items;
	}

	/**
	 * 当前页的数据列表
	 * 
	 * @param items
	 */
	public void setItems(List<?> items) {
		this.items = items;
	}

	/**
	 * 每页数据条数
	 * 
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 每页数据条数
	 * 
	 * @return
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * 当前页号
	 * 
	 * @return
	 */
	public int getPageNum() {
		if (pageNum < 1) {
			return 1;
		}
		return pageNum;
	}

	/**
	 * 当前页号
	 * 
	 * @param pageNum
	 */
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	@Override
	public String toString() {
		return "Pager [pageNum=" + pageNum + ", pageSize=" + pageSize
				+ ", totalCount=" + totalCount + ", totalPage=" + totalPage
				+ ", items=" + items + "]";
	}

	public Object getItem() {
		return item;
	}

	public void setItem(Object item) {
		this.item = item;
	}

}
