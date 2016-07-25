package com.jiangge.vo;

import java.util.List;

public class PageBean {
	
	private int pageIndex;     //当前页
	private int pageSize;      //每页条数
	private int totalCount;    //总条数
	private List<?> list;      //数据List
	
	public PageBean(int pageIndex,int pageSize,int totalCount,List<?> list){
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.totalCount = totalCount;
		this.list = list;
	}
	
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public List<?> getList() {
		return list;
	}
	public void setList(List<?> list) {
		this.list = list;
	}
	
    
	
}
