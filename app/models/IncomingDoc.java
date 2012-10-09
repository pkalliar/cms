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
public class IncomingDoc extends Model{
	
	  @Id
	  public Long id;
	  
	  public String protocolnumber;
	  
	  @Constraints.Required
	  public String name;
	  
	  @Constraints.Required
	  public String sender;
	 
	  @ManyToOne
	  public Folder folder;
	  
	  @ManyToOne
	  public TransmissionMedium medium;
	  
	  @Constraints.Required
	  public String location;
	  
	  @Formats.DateTime(pattern="dd-MM-yyyy")
	  public Date receivedon;
	  
	  public String getReceivedAsString(){
		  DateFormatter df = new DateFormatter("dd-MM-yyyy");
		  return df.print(receivedon, Locale.ENGLISH);
	  }
	  
	 
	  public static Model.Finder<Long,IncomingDoc> find = new Model.Finder(Long.class, IncomingDoc.class);
	  
	  public static List<IncomingDoc> all() {
		  return find.where().orderBy("protocolnumber asc").findList();
		}
	  
	  public static IncomingDoc findByAS(int as){
		  return find.where().eq("protocolnumber", as).findUnique();
	  }

		public static void createOrUpdate(IncomingDoc proto) {
			if(proto.id != null)
				proto.update();
			else			
				proto.save();
		}
	
		public static void delete(Long id) {
		  find.ref(id).delete();
		}
		
		public static IncomingDoc findById(Long id) {
			return find.ref(id);
		}
	  
	  
	  

}
