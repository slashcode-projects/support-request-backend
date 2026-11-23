package com.slashcode.supportticket.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security.jwt")
public class JwtProperties {

	private String secret;
	private String issuer;
	private long accessTokenTtlSeconds;
	
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	public long getAccessTokenTtlSeconds() {
		return accessTokenTtlSeconds;
	}
	public void setAccessTokenTtlSeconds(long accessTokenTtlSeconds) {
		this.accessTokenTtlSeconds = accessTokenTtlSeconds;
	}
	
}
