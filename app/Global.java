import play.*;
import play.libs.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import com.avaje.ebean.*;

import controllers.BatchImporter;
import models.*;
import java.util.concurrent.*;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
 
public class Global extends GlobalSettings {
	

 
  @Override
  public void onStart(Application app) {
	  
	  Logger.info("Starting..");
 
    /**
     * Here we load the initial data into the database
     */
	  	  
    if(Ebean.find(Contact.class).findRowCount() == 0) {
      Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("initial-data.yml");
      
      // Insert users first
      if(User.findAll().size() == 0)
    	  Ebean.save(all.get("users"));
      
      if(TransmissionMedium.findAll().size() == 0)
    	  Ebean.save(all.get("TransmissionMedia"));
      
      Map<String,List<Object>> foldersFile = (Map<String,List<Object>>)Yaml.load("folders.yml");
      
      if(FolderCategory.findAll().size() == 0)
    	  Ebean.save(foldersFile.get("folderCategories"));
      
      if(Folder.findAll().size() == 0)
    	  Ebean.save(foldersFile.get("folders"));
      

      
      
//      if(Protocol.all().size() == 0)
//    	  Ebean.save(all.get("protocols"));
    }
//	try {
//	
//		BatchImporter.synchronizeFromExcel();
//	} catch (InvalidFormatException | IOException e) {
//		e.printStackTrace();
//	}
  }
  
  @Override
  public void onStop(Application app){

	  
  }
}