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
public class Protocol extends Model{
	
	  @Id
	  public Long id;
	  
	  @Constraints.Required
	  public int alfasigma;
	  
	  @Constraints.Required
	  public String name;
	  
	  @ManyToOne
	  public User creator;
	 
	  @ManyToOne
	  public Folder folder;
	  
	  @Formats.DateTime(pattern="dd-MM-yyyy")
	  public Date createdon;
	  
	  public String getCreatedAsString(){
		  DateFormatter df = new DateFormatter("dd-MM-yyyy");
		  return df.print(createdon, Locale.ENGLISH);
	  }
	  
	 
	  public static Model.Finder<Long,Protocol> find = new Model.Finder(Long.class, Protocol.class);
	  
	  public static List<Protocol> all() {
		  return find.where().orderBy("alfasigma asc").findList();
		}
	  
	  public static int findNextAS(){
		  List<Protocol> protocols = find.where()
				    .orderBy("alfasigma desc").findList();
		  
		  if(!protocols.isEmpty()){
			  Protocol maxasProt = protocols.get(0);
			  return maxasProt.alfasigma + 1;
		  }
		  else return 1;

	  }
	  
	  public static Protocol findByAS(int as){
		  return find.where().eq("alfasigma", as).findUnique();
	  }

		public static void create(Protocol proto) {

			
			proto.save();
		}
	
		public static void delete(Long id) {
		  find.ref(id).delete();
		}
	  
	  
	  

}
