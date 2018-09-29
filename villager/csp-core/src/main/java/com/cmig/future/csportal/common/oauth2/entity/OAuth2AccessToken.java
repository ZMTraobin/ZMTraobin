package com.cmig.future.csportal.common.oauth2.entity;


import com.cmig.future.csportal.common.oauth2.OAuthConstants;

import java.io.Serializable;
import java.util.Set;

public class OAuth2AccessToken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//访问令牌
	private String accessToken;

	//刷新令牌
	private String refreshToken;
	
	//访问令牌有效期
	private int expiresIn;

	//接口范围
	private Set<String> scope;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Set<String> getScope() {
		return scope;
	}

	public void setScope(Set<String> scope) {
		this.scope = scope;
	}
	
	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	@Override
	public String toString() {
		return "["+ OAuthConstants.OAUTH_ACCESS_TOKEN+":"+this.accessToken
				+ ","+OAuthConstants.OAUTH_REFRESH_TOKEN+":"+this.refreshToken+"]";
	}
}
