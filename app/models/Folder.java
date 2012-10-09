package models;

import java.util.*;

import javax.persistence.*;
 
import play.api.libs.Crypto;
import play.db.ebean.*;
import play.data.format.*;
import play.data.format.Formats.DateFormatter;
import play.data.validation.*;
 
import play.Logger;
 

@Entity
public class Folder extends Model{
	
	  @Id
	  public Long id;
	  
	  @Constraints.Required
	  public String name;	
	  
	  @Constraints.Required
	  public String code;
	  
	  public FolderCategory category;
	  
	    // -- Queries
	    
	    public static Model.Finder<String,Folder> find = new Model.Finder(String.class, Folder.class);
	    
	    /**
	     * Retrieve all folders.
	     */
	    public static List<Folder> findAll() {
	        return find.where().orderBy("code asc").findList();
	    }
	    
	  
	    public static List<String> list() {
	        List<String> all = new ArrayList<String>();
	        for(Folder folder : Folder.findAll()){
	        	all.add(folder.code + "-" + folder.name);
	        }
	        
	        return all;
	    }
	    
	    public static Map<String,String> options() {
	        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
	        for(Folder c: Folder.find.orderBy("code").findList()) {
	            options.put(c.id.toString(), c.code);
	        }
	        return options;
	    }
	    
	    public static Folder findByName(String name) {
	        return find.where().eq("name", name).findUnique();
	    }
	    
	    public static Folder findByCode(String code) {
	    	//System.out.println("findByCode: Looking for code: ==" + code + "==");
	        return find.where().eq("code", code).findUnique();
	    }
	    
		public static void create(Folder folder) {

			folder.save();
		}
	
}