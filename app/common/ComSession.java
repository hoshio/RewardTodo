package common;

import twitter4j.Twitter;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class ComSession {
	
	//twitter token
	private Twitter twitter = null;
	public void setTwitter(Twitter twitter){
		this.twitter = twitter;
	}
	public Twitter getTwitter(){
		return this.twitter;
	}
	
	//requestToken;
	private RequestToken requestToken = null;
	public void setRequestToken(RequestToken requestToken){
		this.requestToken = requestToken;
	}
	public RequestToken getRequestToken(){
		return this.requestToken;
	}
	
	//AccessToken
	private AccessToken accessToken = null;
	public void setAccessToken(AccessToken accessToken){
		this.accessToken = accessToken;
	}
	public AccessToken getAccessToken(){
		return this.accessToken;
	}
	
	//User
	private User user = null;
	public void setUser(User user){
		this.user = user;
	}
	public User getUser(){
		return this.user;
	}
	
}
