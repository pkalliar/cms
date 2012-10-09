package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;


@Entity
@Table(name="account")
public class User extends Model{
	
	    public String email;
	    
	    @Constraints.Required
	    public String name;
	    
		@Id
		@Constraints.Required
		@Formats.NonEmpty
	    public String username;
	    
	    @Constraints.Required
	    public String password;
	    
	    // -- Queries
	    
	    public static Model.Finder<String,User> find = new Model.Finder(String.class, User.class);
	    
	    /**
	     * Retrieve all users.
	     */
	    public static List<User> findAll() {
	        return find.all();
	    }
	    
	    public static List<String> list() {
	        List<String> all = new ArrayList<String>();
	        for(User user : User.findAll()){
	        	all.add(user.name);
	        }
	        
	        return all;
	    }

	    /**
	     * Retrieve a User from email.
	     */
	    public static User findByEmail(String email) {
	        return find.where().eq("email", email).findUnique();
	    }
	    
	    public static User findByName(String name) {
	        return find.where().eq("name", name).findUnique();
	    }
	    
	    /**
	     * Authenticate a User.
	     */
	    public static User authenticate(String username, String password) {
//	    	User pkalliar = find.where()
//		            .eq("username", "pkalliar")
//		            .eq("password", "pkal12")
//		            .findUnique();
//	    	
//	    	System.out.println("Searched for " + username + " " + password + ", found " +
//	    			pkalliar.username + " " + pkalliar.password);
	    	
	        return find.where()
	            .eq("username", username)
	            .eq("password", password)
	            .findUnique();
	    }
	    
	    
	    // --
	    
	    public String toString() {
	        return "User(" + username + ")";
	    }
	    
	    public static Map<String,String> options() {
	        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
	        for(User c: User.find.orderBy("username").findList()) {
	            options.put(c.username, c.name);
	        }
	        return options;
	    }


}
