package controllers;

import play.cache.Cache;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;
import common.Constants;
import common.ComSession;

public class Secured extends Security.Authenticator{
	
	/*
	 * 現在ログインしているユーザーのユーザー名を取得
	 * @see play.mvc.Security.Authenticator#getUsername(play.mvc.Http.Context)
	 */
	@Override
	public String getUsername(Context ctx){
		ComSession session = (ComSession)Cache.get(Constants.SESSION_KEY);
		if (session == null){
			return null;    // ログインセッションなし
		}

		// アクセスのたびにログイン情報登録をリフレッシュする
		updateSession(Constants.SESSION_KEY,session);
		String username = session.toString();
        
		return username;
	}
	
	/*
	 * getUsernameがnullを返すと、
	 * 認証機能はリクエストを中断し、
	 * その代わりにログイン画面にリダイレクトする
	 * @see play.mvc.Security.Authenticator#onUnauthorized(play.mvc.Http.Context)
	 */
	@Override
	public Result onUnauthorized(Context ctx){
		return badRequest(views.html.login.render("session timeout"));
	}
	
	public static void updateSession(String key, ComSession session)
    {
        // アプリケーションキャッシュの有効期限を更新
        Cache.set(key, session, Constants.SEIION_TIME);
    }

    /**
    * セッション作成機能
    */
    public static void createSession(String key, ComSession session) {
        //フォームオブジェクト生成
		Object strCache = Cache.get(key);
		if( strCache == null ) {
			updateSession(key, session);
		} else {
		    strCache = strCache.toString();
		}
	}
    
    //ユーザー情報の取得
    public static ComSession getSession(String key) {
    	ComSession session = (ComSession)Cache.get(key);
    	if (session == null) {
    		return null;
    	} else {
    		return session;
    	}
    }

}