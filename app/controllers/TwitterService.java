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


public class TwitterService extends Controller {
	
	final static String TWITTER_CONSUMER_KEY = Play.application().configuration().getString("twitter.consumer.key");
	final static String TWITTER_CONSUMER_SECRET = Play.application().configuration().getString("twitter.consumer.secret");
    final static String TWITTER_ACCESS_TOKEN = Play.application().configuration().getString("twitter.access.token");
    final static String TWITTER_ACCESS_TOKEN_SECRET = Play.application().configuration().getString("twitter.access.secret");

    
	public static class SampleForm {
		public String message;
	}

	public String index() throws TwitterException {
	    
	    // アクセストークンの設定
	    AccessToken token = new AccessToken(TWITTER_ACCESS_TOKEN, TWITTER_ACCESS_TOKEN_SECRET);
	     
	    Twitter twitter = new TwitterFactory().getInstance();
	    twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
	    twitter.setOAuthAccessToken(token);
	    User user = twitter.verifyCredentials();

	    //ユーザ情報取得
	    String str = "名前：" + user.getName()
	    		+ "表示名：" + user.getScreenName()
	    		+ "フォロー数：" + user.getFriendsCount()
	    		+ "フォロワー数：" + user.getFollowersCount();
//	    List<Todos> datas = Todos.find.orderBy("postdate desc").findList();
//	    List<Rewards> dataReward = Rewards.find.orderBy("postdate desc").findList();
//		return ok(index.render(str, new Form(SampleForm.class), datas, dataReward));
	    return str;
	}
	
	public static Result login(){
			    
		// Titterオブジェクトの生成
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
 
        try{
            // リクエストトークンの生成
            RequestToken reqToken = twitter.getOAuthRequestToken();
 
            // RequestTokenとTwitterオブジェクトをセッションに保存
//            HttpSession session = req.getSession();
//            session.setAttribute("RequestToken", reqToken);
//            session.setAttribute("Twitter", twitter);
 
            // 認証画面にリダイレクトするためのURLを生成
            String strUrl = reqToken.getAuthorizationURL();
            return redirect(strUrl);
        }catch (TwitterException e){
        	Form<Todos> f = new Form(Todos.class).bindFromRequest();
        	return badRequest(addTodo.render("ERROR", f));
        }
	}

}