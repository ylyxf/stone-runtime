package org.siqisource.stone.communication;

import java.util.List;

public class Page<M> {

	private int total;

	private List<M> rows;

	public Page(int total, List<M> rows) {
		this.total = total;
		this.rows = rows;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<M> getRows() {
		return rows;
	}

	public void setRows(List<M> rows) {
		this.rows = rows;
	}

}
