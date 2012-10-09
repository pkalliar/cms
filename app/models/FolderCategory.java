package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints;
import play.db.ebean.Model;



@Entity
public class FolderCategory extends Model{
	
	  @Id
	  public Long id;
	  
	  @Constraints.Required
	  public String name;	
	  
	  // -- Queries
    
	  public static Model.Finder<String,FolderCategory> find = new Model.Finder(String.class, FolderCategory.class);
    
	    /**
	     * Retrieve all folders.
	     */
	  public static List<FolderCategory> findAll() {
		  return find.all();
	  }
	  
}
