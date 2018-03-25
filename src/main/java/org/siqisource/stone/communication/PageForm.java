package org.siqisource.stone.communication;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.siqisource.stone.mybatis.condition.SqlKey;
import org.siqisource.stone.mybatis.condition.expression.Order;

public class PageForm {

	private int page;

	private int rows;

	private String[] sort;

	private String[] order;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String[] getSort() {
		return sort;
	}

	public void setSort(String[] sort) {
		this.sort = sort;
	}

	public String[] getOrder() {
		return order;
	}

	public void setOrder(String[] order) {
		this.order = order;
	}

	public RowBounds getRowBounds() {
		int offset = 0;
		offset = (page - 1) * rows;
		int limit = rows;
		RowBounds rowBounds = new RowBounds(offset, limit);
		return rowBounds;
	}

	public boolean hasOrder() {
		return sort != null;
	}

	public List<Order> getOrderList() {
		List<Order> orderList = new ArrayList<Order>();
		for (int i = 0; i < sort.length; i++) {
			String currentSort = sort[i];
			String currentOrder = order[i];
			Order order = new Order();
			order.setField(currentSort);
			if ("asc".equals(currentOrder)) {
				order.setDirection(SqlKey.ASC);
			} else {
				order.setDirection(SqlKey.DESC);
			}
			orderList.add(order);
		}
		return orderList;

	}

}
