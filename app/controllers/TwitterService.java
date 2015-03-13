package controllers;

import common.Constants;
import common.ComSession;
import controllers.Application.TweetForm;

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
import twitter4j.ResponseList;
import views.html.index;
import views.html.login;


public class TwitterService extends Controller {
	
	final static String TWITTER_CONSUMER_KEY = Play.application().configuration().getString("twitter.consumer.key");
	final static String TWITTER_CONSUMER_SECRET = Play.application().configuration().getString("twitter.consumer.secret");
    final static String callbackURL = "http://localhost:9000/callback";

    public static class TweetForm {
		public String message;
	}
	
	public static Result login() {
		return ok(login.render("Let's login!!"));
	}
	
	public static Result oauth() {
		ComSession session = new ComSession();
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
		RequestToken requestToken;
		try {
			//local用
			//requestToken = twitter.getOAuthRequestToken(callbackURL);
			//本番用
			requestToken = twitter.getOAuthRequestToken();
			
			//Session生成
			session.setTwitter(twitter);
			session.setRequestToken(requestToken);
			Secured.createSession(Constants.SESSION_KEY, session);
			
			//認可ページへ遷移
			String strUrl = requestToken.getAuthorizationURL();
			return redirect(strUrl);
		} catch (TwitterException e) {
			// can not login
			e.printStackTrace();
			return ok(login.render("Cannot login!!"));
		}
	}
	
	
	public static Result callback() {
		//sessionからrequestTokenを取得
		ComSession session = Secured.getSession(Constants.SESSION_KEY);
		String token = session.getRequestToken().getToken();
		String tokenSecret = session.getRequestToken().getTokenSecret();		
		String oauth_verifier = request().getQueryString("oauth_verifier");
		
		
		String str = new String();
		
		try {
			//AccessTokenの取得
			RequestToken requestToken = new RequestToken(token, tokenSecret);
			Twitter twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
			twitter.getOAuthAccessToken(requestToken, oauth_verifier);
			AccessToken accessToken = twitter.getOAuthAccessToken();
			User user = twitter.verifyCredentials();

			//sessionの更新
			session.setTwitter(twitter);
			session.setRequestToken(requestToken);
			session.setAccessToken(accessToken);
			session.setUser(user);
			Secured.updateSession(Constants.SESSION_KEY, session);
			
			str = getTimeLine(session);
			
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//return ok(index.render(str.toString()));
		return ok(index.render(str, new Form(TweetForm.class)));
	}

	
	//タイムラインの取得
	public static String getTimeLine(ComSession session){
		
		//sessionからrequestTokenを取得
		session = Secured.getSession(Constants.SESSION_KEY);
		Twitter twitter = session.getTwitter();
		StringBuilder str = new StringBuilder();
		
		//タイムラインの取得
		ResponseList<twitter4j.Status> homeTl = null;
		try {
			homeTl = twitter.getHomeTimeline();
			for (twitter4j.Status status : homeTl) {
		        //つぶやきのユーザーIDの取得
		        str.append("ユーザーID：");
		        str.append(status.getUser().getScreenName());
		        str.append("<br>");
		        //つぶやきの取得
		        str.append("tweet：");
		        str.append(status.getText());
		        str.append("<br>");
			}
		} catch (TwitterException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
		return str.toString();
	}
	
}