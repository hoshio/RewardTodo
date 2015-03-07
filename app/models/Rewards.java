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
public class Rewards extends Model {

	@Id
	public Long id;	

	@Required
	public String reward_name;
	
	public String reward_detail;
	
	public String username;

	public String point;

	public String status;
	
	@CreatedTimestamp
	public Date postdate;

	
	public static Finder<Long, Rewards> find = new Finder<Long, Rewards>(Long.class, Rewards.class);

}