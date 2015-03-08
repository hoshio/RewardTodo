package controllers;

import java.util.List;

import models.Rewards;
import models.Todos;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import views.html.addReward;
import views.html.addTodo;
import views.html.edit;
import views.html.index;
import views.html.setid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Application extends Controller {
	
	public static class SampleForm {
		public String message;
	}
	
//	public static Result index() {
	public static Result index() throws TwitterException {
		List<Todos> datas = Todos.find.orderBy("postdate desc").findList();
		List<Rewards> dataReward = Rewards.find.orderBy("postdate desc").findList();
		TwitterService tweet = new TwitterService();
		return ok(index.render(tweet.index(), new Form(SampleForm.class), datas, dataReward));
//		return ok(index.render("sample", new Form(SampleForm.class), datas, dataReward));
	}
	
	public static Result ajax(){
		String input = request().body().asFormUrlEncoded().get("input")[0];
		Todos data = new Todos();
		data.todo_name = input;
		data.save();
		List<Todos> datas = Todos.find.orderBy("postdate desc").findList();
		ObjectMapper objectMapper = new ObjectMapper();
		String json = null;
		try {
			json = objectMapper.writeValueAsString(datas);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return ok(json);
	}

		
//	public static Result send(){
//		Form<SampleForm> f = form(SampleForm.class).bindFromRequest();
//		List<Todos> datas = Todos.find.all();
//		if(!f.hasErrors()){
//			SampleForm data = f.get();
//			String msg = "you typed: " + data.message;
//			return ok(index.render(msg, f, datas));
//		}else{
//			return badRequest(index.render("ERROR", form(SampleForm.class), datas));
//		}
//	}
	
	public static Result addTodo(){
		Form<Todos> f = new Form(Todos.class);
		return ok(addTodo.render("NEW TODO", f));
	}
	
	public static Result addReward(){
		Form<Rewards> f = new Form(Rewards.class);
		return ok(addReward.render("NEW REWARD", f));
	}
	
	public static Result createTodo(){
		Form<Todos> f = new Form(Todos.class).bindFromRequest();
		if(!f.hasErrors()){
			Todos data = f.get();
			data.save();
			return redirect("/");
		}else{
			return badRequest(addTodo.render("ERROR", f));
		}
	}

	public static Result createReward(){
		Form<Rewards> f = new Form(Rewards.class).bindFromRequest();
		if(!f.hasErrors()){
			Rewards data = f.get();
			data.save();
			return redirect("/");
		}else{
			return badRequest(addReward.render("ERROR", f));
		}
	}

	
	public static Result set(){
		Form<Todos> f = new Form(Todos.class);
		return ok(setid.render("IDを入力", f));
	}
	
	public static Result edit(){
		Form<Todos> f = new Form(Todos.class).bindFromRequest();
		if(!f.hasErrors()){
			Todos obj = f.get();
			Long id = obj.id;
			obj = Todos.find.byId(id);
			if(obj != null){
				f = new Form(Todos.class).fill(obj);
				return ok(edit.render("ID=" + id + "の投稿を編集", f));
			}else{
				return ok(setid.render("ERROR:IDの投稿が見つかりません", f));
			}
		}else{
			return ok(setid.render("ERROR:投稿に問題があります", f));
		}
	}
	
	public static Result update(){
		Form<Todos> f = new Form(Todos.class).bindFromRequest();
		if(!f.hasErrors()){
			Todos data = f.get();
			data.update();
			return ok(edit.render("update完了", f));
		}else{
			return ok(edit.render("ERRO:再度入力ください", f));
		}
	}
	
	public String twitter_info() throws TwitterException {
//	    final String TWITTER_CONSUMER_KEY    = "取得したコードを入力";
//	    final String TWITTER_CONSUMER_SECRET = "取得したコードを入力";
//	    final String TWITTER_ACCESS_TOKEN        = "取得したコードを入力";
//	    final String TWITTER_ACCESS_TOKEN_SECRET = "取得したコードを入力";

	    final String TWITTER_CONSUMER_KEY    = "s9CYuFNQKENgm7NPV2qNGgfRF";
	    final String TWITTER_CONSUMER_SECRET = "vR0IfAa1vuXKq38tOtH2RDeFWinqN9osLENhrdtSfIAXkrAwhD";
	    final String TWITTER_ACCESS_TOKEN        = "168507212-U1lBhfiX4qTplJ0adaCJ88ilvrAjVwLXhcDBDewh";
	    final String TWITTER_ACCESS_TOKEN_SECRET = "XfSwNC0ccVogIMeSvm73OethMoIp8wugrNwKc0OeDRBRP";

	    
	    // アクセストークンの設定
	    AccessToken token = new AccessToken(TWITTER_ACCESS_TOKEN, TWITTER_ACCESS_TOKEN_SECRET);
	     
	    Twitter twitter = new TwitterFactory().getInstance();
	    twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
	    twitter.setOAuthAccessToken(token);
	    User user = twitter.verifyCredentials();

	    // 表示してみる。
	    //ユーザ情報取得
	    System.out.println("名前　　　　：" + user.getName());
	    System.out.println("表示名　　　：" + user.getScreenName());
	    System.err.println("フォロー数　：" + user.getFriendsCount());
	    System.out.println("フォロワー数：" + user.getFollowersCount());
//		//つぶやきの実行
//		Status status = twitter.updateStatus("test for twitter4J");
//	    ResponseList<Status> list_status = twitter.getHomeTimeline();
//	    System.out.println("自分の名前：" + user.getScreenName());
//	    System.out.println("概要　　　：" + user.getDescription());
//	    for (Status status : list_status) {
//	        System.out.println("ツイート：" + status.getText());
//	    }
	    String str = "名前：" + user.getName()
	    		+ "表示名：" + user.getScreenName()
	    		+ "フォロー数：" + user.getFriendsCount()
	    		+ "フォロワー数：" + user.getFollowersCount();
//	    List<Todos> datas = Todos.find.orderBy("postdate desc").findList();
//	    List<Rewards> dataReward = Rewards.find.orderBy("postdate desc").findList();
//		return ok(index.render(str, new Form(SampleForm.class), datas, dataReward));
	    return str;
	}


}
