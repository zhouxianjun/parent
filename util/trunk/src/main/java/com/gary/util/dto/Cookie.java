package com.gary.util.dto;

public class Cookie {
	private String host;
	private String path;
	private String name;
	private String value;
	public Cookie() {
		// TODO Auto-generated constructor stub
	}
	public Cookie(String name, String value) {
		this.name = name;
		this.value = value;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(name).append("=").append(value).append(";domain=").append(host).append(";Path=").append(path);
		return sb.toString();
	}
	public void setCookie(Cookie cookie){
		this.host = cookie.getHost();
		this.name = cookie.getName();
		this.path = cookie.getPath();
		this.value = cookie.getValue();
	}
}
