package controllers;

import java.util.List;

import common.ComSession;
import common.Constants;
import controllers.TwitterService;
import models.Rewards;
import models.Todos;
import play.Play;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import twitter4j.ResponseList;
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
import play.mvc.Security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Application extends Controller {
		
	public static class TweetForm {
		public String message;
	}
	
	@Security.Authenticated(Secured.class)
	public static Result index() throws TwitterException {
		
		//sessionからrequestTokenを取得
		ComSession session = Secured.getSession(Constants.SESSION_KEY);
		String str = TwitterService.getTimeLine(session);
		Secured.updateSession(Constants.SESSION_KEY, session);
		
//	    //ユーザ情報取得
//	    String str = "名前：" + user.getName()
//	    		+ "表示名：" + user.getScreenName()
//	    		+ "フォロー数：" + user.getFriendsCount()
//	    		+ "フォロワー数：" + user.getFollowersCount();
		
//		return ok(index.render(str.toString()));
		return ok(index.render(str, new Form(TweetForm.class)));
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

	@Security.Authenticated(Secured.class)
	public static Result addTodo(){
		Form<Todos> f = new Form(Todos.class);
		return ok(addTodo.render("aaaaa", f));
	}
	
	public static Result addReward(){
		Form<Rewards> f = new Form(Rewards.class);
		return ok(addReward.render("NEW REWARD", f));
	}
	
	@Security.Authenticated(Secured.class)
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
	
}
