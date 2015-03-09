package controllers;

import models.Todos;
import play.Play;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import views.html.addTodo;
import play.cache.Cache;
import play.mvc.Security;
import play.cache.Cache;
import play.mvc.Http.Context;

import views.html.index;
import views.html.login;
import views.html.edit;
import views.html.index;
import views.html.setid;


public class TwitterService extends Controller {
	
	final static String TWITTER_CONSUMER_KEY = Play.application().configuration().getString("twitter.consumer.key");
	final static String TWITTER_CONSUMER_SECRET = Play.application().configuration().getString("twitter.consumer.secret");
    final static String callbackURL = "http://localhost:9000/callback";

    public static class SampleForm {
		public String message;
	}
	
	public static Result login() {
		return ok(login.render("Let's login!!"));
	}
	
	public static Result oauth() {
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
		RequestToken requestToken;
		try {
			//requestToken = twitter.getOAuthRequestToken(callbackURL);
			requestToken = twitter.getOAuthRequestToken();
			Secured.createSession(requestToken);
			String strUrl = requestToken.getAuthorizationURL();
			return redirect(strUrl);
			//return redirect(requestToken.getAuthenticationURL());
		} catch (TwitterException e) {
			// can not login
			e.printStackTrace();
			return ok(login.render("Cannot login!!"));
		}
	}
	
	public static Result callback() {
		
		RequestToken sessionRequestToken = Secured.getSession();
		String token = sessionRequestToken.getToken();
		String tokenSecret = sessionRequestToken.getTokenSecret();
		String oauth_verifier = request().getQueryString("oauth_verifier");
		if (token != null && tokenSecret != null) {
			try {
				RequestToken requestToken = new RequestToken(token, tokenSecret);
				Twitter twitter = new TwitterFactory().getInstance();
				twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
				twitter.getOAuthAccessToken(requestToken, oauth_verifier);
				AccessToken accessToken = twitter.getOAuthAccessToken();
				Secured.createAccessTokenSession(accessToken);
				// twitter.updateStatus("テストかしら");
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return redirect("/");
	}

	public static Result index() {
		AccessToken _accessToken = Secured.getAccessTokenSession();
		String token = _accessToken.getToken();
		String tokenSecret = _accessToken.getTokenSecret();
		if (token != null && tokenSecret != null) {
//			try {
				Twitter twitter = new TwitterFactory().getInstance();
				AccessToken accessToken = new AccessToken(token, tokenSecret);
				twitter.setOAuthAccessToken(accessToken);
//				List<Status> statuses;
//				statuses = twitter.getHomeTimeline();
//				Status status = statuses.get(0);
//				render(statuses);
//			} catch (TwitterException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		return ok(index.render("Let's login!!"));
	}
//
//	public String index() throws TwitterException {
//	    
//	    // アクセストークンの設定
//	    AccessToken token = new AccessToken(TWITTER_ACCESS_TOKEN, TWITTER_ACCESS_TOKEN_SECRET);
//	     
//	    Twitter twitter = new TwitterFactory().getInstance();
//	    twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
//	    twitter.setOAuthAccessToken(token);
//	    User user = twitter.verifyCredentials();
//
//	    //ユーザ情報取得
//	    String str = "名前：" + user.getName()
//	    		+ "表示名：" + user.getScreenName()
//	    		+ "フォロー数：" + user.getFriendsCount()
//	    		+ "フォロワー数：" + user.getFollowersCount();
////	    List<Todos> datas = Todos.find.orderBy("postdate desc").findList();
////	    List<Rewards> dataReward = Rewards.find.orderBy("postdate desc").findList();
////		return ok(index.render(str, new Form(SampleForm.class), datas, dataReward));
//	    return str;
//	}
//	
//	public static Result login(){
//			    
//		// Titterオブジェクトの生成
//        Twitter twitter = new TwitterFactory().getInstance();
//        twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
// 
//        try{
//            // リクエストトークンの生成
//            RequestToken reqToken = twitter.getOAuthRequestToken();
// 
//            // RequestTokenとTwitterオブジェクトをセッションに保存
////            HttpSession session = req.getSession();
////            session.setAttribute("RequestToken", reqToken);
////            session.setAttribute("Twitter", twitter);
// 
//            // 認証画面にリダイレクトするためのURLを生成
//            String strUrl = reqToken.getAuthorizationURL();
//            return redirect(strUrl);
//        }catch (TwitterException e){
//        	Form<Todos> f = new Form(Todos.class).bindFromRequest();
//        	return badRequest(addTodo.render("ERROR", f));
//        }
//	}

}