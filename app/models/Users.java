package models;

import java.net.URL;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.*;

@Entity
public class Users extends Model {

	@Id
	public Long id;	
	
	public Long twitterId;
	public String userName;
	public URL profileImgUrl;
	public String accessToken;
	public String accessTokenSecret;
	
	public Users(String userName) {
		this.userName = userName;
	}

	public static Finder<Long, Users> find = new Finder<Long, Users>(Long.class, Users.class);

	public static Users findOrCreate(String userName) {
		Users account = Users.find.where().eq("userName", userName).findUnique();
		if(account == null)
			account = new Users(userName);
		return account;
	}	


}