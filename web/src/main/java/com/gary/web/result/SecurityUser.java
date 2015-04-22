package com.gary.web.result;

import java.util.Collection;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.security.core.GrantedAuthority;

import com.gary.web.util.DateJsonFull;

public class SecurityUser {
	/**用户*/
	private String account;
	/**账号未过期*/
	private boolean accountNonExpired;
	/**账号是否未锁定*/
	private boolean accountNonLocked;
	/**权限*/
	private Collection<? extends GrantedAuthority> authorities;
	/**凭证是否未过期*/
	private boolean credentialsNonExpired;
	/**是否可用*/
	private boolean enabled;
	/**最后一次请求时间*/
	private Date lastRequest;
	private String sessionId;
	/**session过期*/
	private boolean sessionExpired;
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}
	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}
	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}
	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	@JsonSerialize(using = DateJsonFull.class)
	public Date getLastRequest() {
		return lastRequest;
	}
	public void setLastRequest(Date lastRequest) {
		this.lastRequest = lastRequest;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public boolean isSessionExpired() {
		return sessionExpired;
	}
	public void setSessionExpired(boolean sessionExpired) {
		this.sessionExpired = sessionExpired;
	}
}
