package controllers;

import static play.data.Form.form;
import models.Users;
import play.cache.Cache;
import play.data.Form;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;
import common.Constants;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;


public class Secured extends Security.Authenticator{
	
	/*
	 * 現在ログインしているユーザーのユーザー名を取得
	 * @see play.mvc.Security.Authenticator#getUsername(play.mvc.Http.Context)
	 */
	public RequestToken getSession(Context ctx){
		RequestToken session = (RequestToken)Cache.get("login.key");
		if (session == null){
			return null;    // ログインセッションなし
		}

		// アクセスのたびにログイン情報登録をリフレッシュする
		updateSession(session);
        
		return session;
	}
	
	public static void updateSession(RequestToken session)
    {
        // アプリケーションキャッシュの有効期限を更新
        Cache.set("beforelogin.key", session);
    }

	public static void updateAccessTokenSession(AccessToken session)
    {
        // アプリケーションキャッシュの有効期限を更新
        Cache.set("login.key", session);
    }

	
    /**
    * セッション作成機能
    */
    public static void createSession(RequestToken session) {
        //フォームオブジェクト生成
		Object strCache = Cache.get("beforelogin.key");
		if( strCache == null ) {
			updateSession(session);
		} else {
		    strCache = strCache.toString();
		}
	}

    /**
    * セッション作成機能
    */
    public static void createAccessTokenSession(AccessToken accessTokenSession) {
        //フォームオブジェクト生成
		Object strCache = Cache.get("afterlogin.key");
		if( strCache == null ) {
			updateAccessTokenSession(accessTokenSession);
		} else {
		    strCache = strCache.toString();
		}
	}

    
    //セッションにユーザー情報を保存。
    public static void setSession(RequestToken session) {
    	Cache.set(Constants.Session.key, session);
    }
    
    //ユーザー情報の取得。同時にセッション更新
    public static RequestToken getSession() {
    	RequestToken session = (RequestToken)Cache.get(Constants.Session.key);
    	if (session == null) {
    		return null;
    	} else {
    		setSession(session);
    		return session;
    	}
    }
    
//    public static void removeUserInfo() {
//    	Users user = getUserInfo();
//    	if (user != null){
//    		Cache.remove(Constants.Session.key);
//    	}
//    }

    //セッションにユーザー情報を保存。
    public static void setAccessTokenSession(AccessToken session) {
    	Cache.set(Constants.Session.key, session);
    }
    
    //ユーザー情報の取得。同時にセッション更新
    public static AccessToken getAccessTokenSession() {
    	AccessToken session = (AccessToken)Cache.get(Constants.Session.key);
    	if (session == null) {
    		return null;
    	} else {
    		setAccessTokenSession(session);
    		return session;
    	}
    }

	
	/*
	 * getUsernameがnullを返すと、
	 * 認証機能はリクエストを中断し、
	 * その代わりにログイン画面にリダイレクトする
	 * @see play.mvc.Security.Authenticator#onUnauthorized(play.mvc.Http.Context)
	 */
//	@Override
//	public Result onUnauthorized(Context ctx){
//    	Form<Users> loginForm = form(Users.class).bindFromRequest();
//		return badRequest(views.html.login.render("session timeout", loginForm));
////		return redirect(controllers.routes.Authentication.login());
//	}

}