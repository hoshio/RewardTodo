package models;

import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.*;


import play.db.ebean.*;
import play.data.validation.*;

import com.avaje.ebean.annotation.*;
import play.data.validation.Constraints.*;

@Entity
public class Users extends Model {

	@Id
	public Long id;	

	@Required
	public String username;
	
	public String sum_point;
	
	@CreatedTimestamp
	public Date postdate;

	
	public static Finder<Long, Users> find = new Finder<Long, Users>(Long.class, Users.class);

}