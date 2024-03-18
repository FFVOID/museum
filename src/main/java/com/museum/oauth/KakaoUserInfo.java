package com.museum.oauth;

import java.util.Map;

public class KakaoUserInfo implements Oauth2UserInfo{
	
	private Map<String, Object> attributes;
    private Map<String, Object> attributesAccount;
    private Map<String, Object> attributesProfile;
    
    public KakaoUserInfo(Map<String, Object> attributes) {
        
        this.attributes = attributes;
        this.attributesAccount = (Map<String, Object>) attributes.get("kakao_account");
        this.attributesProfile = (Map<String, Object>) attributesAccount.get("profile");
    }
    
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "Kakao";
    }

    @Override
    public String getEmail() {
    	
    	String email = attributesAccount.get("email").toString();
    	
    	if(attributesAccount.get("email")==null) {
    		email = "email_disagree";
    	}
    	
        return email;
    }

    @Override
    public String getName() {
        return attributesProfile.get("nickname").toString();
    }
}
