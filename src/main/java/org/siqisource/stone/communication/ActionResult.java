package org.siqisource.stone.communication;

import java.util.ArrayList;
import java.util.List;

public class ActionResult {

	/** failed */
	public String status = "success";

	public String message = "";

	/**
	 * 当数据给到前端时，应该同时通知前端，对于这个数据，下一步 还可以做哪些操作。 对单条数据而言，可以是删除、修改
	 * 对列表数据而言，可以是新增，对列表中的每条数据而言，可以是删除、修改.... 这相当于“预判”，让UI层能更好地展示数据。
	 *
	 */
	private List<String> nextActions = new ArrayList<String>();

	public Object data = null;

	public ActionResult() {

	}

	public ActionResult(Boolean result) {
		if (!result) {
			this.status = "failed";
		}
	}

	public ActionResult(String message) {
		this.message = message;
	}

	public ActionResult(Boolean result, String message) {
		if (!result) {
			this.status = "failed";
		}
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void failed(String message) {
		this.status = "failed";
		this.message = message;
	}

	public void success(String message) {
		this.status = "success";
		this.message = message;
	}

}
