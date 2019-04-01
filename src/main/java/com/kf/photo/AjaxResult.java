package com.kf.photo;


public class AjaxResult {
	
	private boolean success;
	
	
	private Object data;
	
	private String message;
	
	public static AjaxResult newInstance(){
		return new AjaxResult();
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
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
	
	public AjaxResult doSuccess(String msg){
		this.message = msg;
		this.success = Boolean.TRUE;
		return this;
	}
	
	public AjaxResult doSuccess(String msg, Object data){
		this.message = msg;
		this.data = data;
		this.success = Boolean.TRUE;
		return this;
	}
	
	public AjaxResult doFail(String msg){
		this.message = msg;
		this.success = Boolean.FALSE;
		return this;
	}

	public AjaxResult doFail(String msg,Object data){
		this.message = msg;
		this.data = data;
		this.success = Boolean.FALSE;
		return this;
	}
}
