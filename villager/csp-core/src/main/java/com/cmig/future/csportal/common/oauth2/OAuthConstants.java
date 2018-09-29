package com.cmig.future.csportal.common.oauth2;

public final class OAuthConstants {

	//Authorization request params
    public static final String OAUTH_RESPONSE_TYPE = "response_type";
    public static final String OAUTH_APP_ID = "appid";
    public static final String OAUTH_APP_SECRET = "secret";
    public static final String OAUTH_REDIRECT_URI = "redirect_uri";
    public static final String OAUTH_USERNAME = "username";
    public static final String OAUTH_PASSWORD = "password";
    public static final String OAUTH_ASSERTION_TYPE = "assertion_type";
    public static final String OAUTH_ASSERTION = "assertion";
    public static final String OAUTH_SCOPE = "scope";
    public static final String OAUTH_STATE = "state";
    public static final String OAUTH_GRANT_TYPE = "grant_type";
    public static final String OAUTH_TOKEN_TYPE = "token_type";

    //Authorization response params
    public static final String OAUTH_CODE = "code";
    public static final String OAUTH_ACCESS_TOKEN = "access_token";
    public static final String OAUTH_EXPIRES_IN = "expires_in";
    public static final String OAUTH_REFRESH_TOKEN = "refresh_token";
    
    public static final String INVALID_REQUEST = "invalid_request";
    
    //oauth2授权类型
    public static final String AUTHORIZATION_CODE = "authorization_code";
    
    //redis oauth token key prefix
    public static final String CODE = "open_code:";
	public static final String ACCESS = "open_access:";
	public static final String AUTH_TO_ACCESS = "open_auth_to_access:";
	public static final String AUTH = "open_auth:";
	public static final String REFRESH_AUTH = "open_refresh_auth:";
	public static final String ACCESS_TO_REFRESH = "open_access_to_refresh:";
	public static final String REFRESH = "open_refresh:";
	public static final String REFRESH_TO_ACCESS = "open_refresh_to_access:";
}
