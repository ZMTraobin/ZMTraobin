package com.cmig.future.csportal.common.oauth2.service;

import com.cmig.future.csportal.common.oauth2.OAuthConstants;
import com.cmig.future.csportal.common.oauth2.entity.OAuth2AccessToken;
import com.cmig.future.csportal.common.utils.JedisUtils;
import com.cmig.future.csportal.common.utils.MD5;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenService {

	private int refreshTokenValiditySeconds = 60 * 60 * 24 *7; // default 7 days.

	private int accessTokenValiditySeconds = 60 * 60 * 2; // default 2 hours.

	/**
	 * 生成令牌
	 * @param uid
	 * @return
	 */
	public OAuth2AccessToken createAccessToken(String uid, String appId){

		OAuth2AccessToken oauthAccessToken = getAccessTokenByUser(appId,uid);
		if(oauthAccessToken!=null){
			return oauthAccessToken ;
		}
		
		//生成token
		String accessToken = getAccessToken(uid,appId);

		//存储用户id
		String authKey = OAuthConstants.AUTH + accessToken;
		JedisUtils.set(authKey, uid, accessTokenValiditySeconds);
		
		//存储token
		String authToAccessKey = OAuthConstants.AUTH_TO_ACCESS +appId+":" + uid;
		JedisUtils.set(authToAccessKey, accessToken, accessTokenValiditySeconds);
		
		//刷新access token
		String refreshToken = getRefreshToken(uid,appId);
		
		//关联access token
		String refreshToAccessKey = OAuthConstants.REFRESH_TO_ACCESS+refreshToken;
		JedisUtils.set(refreshToAccessKey, accessToken, refreshTokenValiditySeconds);
		
		//存储刷新token
		String accessToRefreshKey = OAuthConstants.ACCESS_TO_REFRESH + accessToken;
		JedisUtils.set(accessToRefreshKey, refreshToken, refreshTokenValiditySeconds);
		
		OAuth2AccessToken token = new OAuth2AccessToken();
		token.setAccessToken(accessToken);
		token.setExpiresIn(accessTokenValiditySeconds);
		token.setRefreshToken(refreshToken);
		return token;
	}
	
	/**
	 * 刷新token
	 * @param refreshToken
	 * @return
	 */
	public OAuth2AccessToken refreshAccessToken(String refreshToken){
		String refreshToAccessKey = OAuthConstants.REFRESH_TO_ACCESS + refreshToken;
		String cacheAccessToken = JedisUtils.get(refreshToAccessKey);
		if (cacheAccessToken == null) {
			return null;
		}
		JedisUtils.set(refreshToAccessKey, cacheAccessToken, refreshTokenValiditySeconds);
		OAuth2AccessToken token = new OAuth2AccessToken();
		token.setAccessToken(cacheAccessToken);
		token.setRefreshToken(refreshToken);
		return token;
	}
	
	/**
	 * 查询用户的token
	 * @param uid
	 * @return
	 */
	public OAuth2AccessToken getAccessTokenByUser(String appId,String uid){
		String accessTokenKey = OAuthConstants.AUTH_TO_ACCESS +appId+":" + uid;
		String accessToken = JedisUtils.get(accessTokenKey);
        int leftSec=JedisUtils.getLeftSeconds(accessTokenKey);
		String accessToRefreshKey = OAuthConstants.ACCESS_TO_REFRESH+accessToken;
		String refreshToken = JedisUtils.get(accessToRefreshKey);
		if(accessToken==null||refreshToken==null){
			return null;
		}
		OAuth2AccessToken token = new OAuth2AccessToken();
		token.setAccessToken(accessToken);
		token.setRefreshToken(refreshToken);
        token.setExpiresIn(leftSec);
		return token ;
	}
	
	/**
	 * 生成授权token
	 * @return
	 */
	private String getAccessToken(String uid,String appId) {
		return MD5.MD5Encode(uid+appId+UUID.randomUUID().toString()
				+System.currentTimeMillis());
	}

	/**
	 * 生成刷新token
	 * @return
	 */
	private String getRefreshToken(String uid,String appId) {
		return MD5.MD5Encode(uid+appId+UUID.randomUUID().toString()
				+System.currentTimeMillis());
	}
}
