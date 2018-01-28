package org.siqisource.stone.result;

public class ActionResult {

	/** failed */
	public String status = "success";

	public String message = "";

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
