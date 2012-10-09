package models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints;
import play.db.ebean.Model;



@Entity
public class TransmissionMedium extends Model{
	
	  @Id
	  public Long id;
	  
	  @Constraints.Required
	  public String name;	
	  
	  // -- Queries
    
	  public static Model.Finder<String,TransmissionMedium> find = new Model.Finder(String.class, TransmissionMedium.class);
    
	    /**
	     * Retrieve all folders.
	     */
	  public static List<TransmissionMedium> findAll() {
		  return find.all();
	  }
	  
	    public static List<String> list() {
	        List<String> all = new ArrayList<String>();
	        for(TransmissionMedium medium : TransmissionMedium.findAll()){
	        	all.add(medium.name);
	        }
	        
	        return all;
	    }
	    
	    public static Map<String,String> options() {
	        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
	        for(TransmissionMedium c: TransmissionMedium.find.orderBy("name").findList()) {
	            options.put(c.id.toString(), c.name);
	        }
	        return options;
	    }
	    
	    
	    public static TransmissionMedium findByName(String name) {
	        return find.where().eq("name", name).findUnique();
	    }
	  
}
