package controllers;

import play.*;
import play.libs.Json;
import play.mvc.*;
import play.data.*;
import play.data.format.Formats;
import play.data.format.Formats.DateFormatter;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;
 
import models.*;
import views.html.*;
 
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
 
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
 
import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;
 
@Security.Authenticated(Secured.class)
public class DataTable extends Controller {
	
//    public static class ProtocolF {
//        
//    	@Constraints.Required
//        public String name;
//    	
//    	@Constraints.Required
//        public String creator;
//    	
//    	@Constraints.Required
//        public String folder;
//    	
//	  	@Formats.DateTime(pattern="dd-MM-yyyy")
//	  	public Date createdon;
//        
//        public String validate() {
//            return null;
//        }
//        
//    }
	
//	static Form<Protocol> protoForm = form(Protocol.class);
	
	  public static Result index() {
		    //return ok("Hello world");
		    return ok(datatable.render("Λίστα εξερχόμενων Πρεσβείας Τρίπολης.", User.find.byId(request().username())));
		  }
 
 
	  public static Result list() {
		    /**
		     * Get needed params
		     */
		    Map<String, String[]> params = request().queryString();
		 
		    Integer iTotalRecords = Protocol.find.findRowCount();
		    String filter = params.get("sSearch")[0];
		    Integer pageSize = Integer.valueOf(params.get("iDisplayLength")[0]);
		    Integer page = Integer.valueOf(params.get("iDisplayStart")[0]) / pageSize;
		 
		    /**
		     * Get sorting order and column
		     */
		    String sortBy = "name";
		    String order = params.get("sSortDir_0")[0];
		 
		    switch(Integer.valueOf(params.get("iSortCol_0")[0])) {
		      case 0 : sortBy = "alfasigma"; break;
		      case 1 : sortBy = "folder"; break;
		      case 2 : sortBy = "createdon"; break;
		      case 3 : sortBy = "creator"; break;
		      case 4 : sortBy = "name"; break;
		    }
		 
		    /**
		     * Get page to show from database
		     * It is important to set setFetchAhead to false, since it doesn't benefit a stateless application at all.
		     */
		    Page<Protocol> contactsPage = Protocol.find.where(
		      Expr.or(
		        Expr.ilike("name", "%"+filter+"%"),
		        Expr.or(
		          Expr.ilike("name", "%"+filter+"%"),
		          Expr.ilike("name", "%"+filter+"%")
		        )
		      )
		    )
		    .orderBy(sortBy + " " + order + ", id " + order)
		    .findPagingList(pageSize).setFetchAhead(false)
		    .getPage(page);
		 
		    Integer iTotalDisplayRecords = contactsPage.getTotalRowCount();
		 
		    /**
		     * Construct the JSON to return
		     */
		    ObjectNode result = Json.newObject();
		 
		    result.put("sEcho", Integer.valueOf(params.get("sEcho")[0]));
		    result.put("iTotalRecords", iTotalRecords);
		    result.put("iTotalDisplayRecords", iTotalDisplayRecords);
		 
		    ArrayNode an = result.putArray("aaData");
		 
		    for(Protocol c : contactsPage.getList()) {
		      ObjectNode row = Json.newObject();
		      row.put("0", "<a href='/protocols/" + c.id + "/edit'>" + c.alfasigma + "</a>");
		      if(c.folder.name.equals("TO DO"))
		    	  row.put("1", c.folder.code);
		      else
		    	  row.put("1", c.folder.code + "-" + c.folder.name);
		      row.put("2", c.getCreatedAsString());
		      row.put("3", c.creator.name);
		      row.put("4", c.name);
		      row.put("5", c.id);
		      an.add(row);
		    }
		    
		    //return ok(datatable.render(result.asText()));
		 
		    return ok(result);
		 }
	  
	  public static Result protocols() {
		  
		  User currentUser = User.find.byId(request().username());
		  
//	      Form<Protocol> protoForm = form(Protocol.class);
//	      
//	      Protocol newProtocol = new Protocol();
//	      
//		  newProtocol.creator = currentUser;
//		  
//		  int maxas = Protocol.findNextAS();
//		  
//		  System.out.println("Found max as: " + maxas);
//		  
//		  newProtocol.alfasigma = maxas;
//		  
//		  newProtocol.createdon = new Date();
//	      
//	      protoForm.fill(newProtocol);
	      
	      Form<Protocol> protoForm = form(Protocol.class);
	      
            return ok(
            	views.html.protocol.render(protoForm, currentUser)
            );  
            
		}
	  
	  public static Result newProtocol() {
		  Form<Protocol> filledForm = form(Protocol.class).bindFromRequest();
		  
//		  Map<String,String> anyData = new HashMap();
//		  anyData.put("createdon", new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
//		  anyData.put("alfasigma", Protocol.findNextAS() + "");
//
//		  filledForm.bind(anyData);
		  
		  if(filledForm.hasErrors()) {
			  
//			  boolean isFatalError = true;
//			  Map<String, List<ValidationError>>  errorMap = filledForm.errors();
//			  for (String error : errorMap.keySet()){
//				  List<ValidationError> errorsList = errorMap.get(error);
//				  for(ValidationError valerror : errorsList){
//					  System.out.println("Found error: " + error + " and " + valerror.message());
//				  }			  
//			  }

			    return badRequest(
			      views.html.protocol.render(filledForm, User.find.byId(request().username()))
			    );

		  } else {
			  Protocol filled = filledForm.get();
			  
			  if(filled.createdon == null)
				  filled.createdon = new Date();
			  
			  filled.alfasigma = Protocol.findNextAS();
			  
			  Protocol.create(filled);
			  
			  
			  
			  //Protocol.create(filledForm.get());
		    return redirect(routes.DataTable.index());  
		  }
		}
	  
	  public static Result deleteProtocol(Long id) {
		  Protocol.delete(id);
		  return redirect(routes.DataTable.index());
		}
	  
	  public static Result editProtocol(Long id) {
	        Form<Protocol> incomForm = form(Protocol.class).fill(
	        		Protocol.find.byId(id)
	            );
          return ok(
          	views.html.editProtocol.render(incomForm, User.find.byId(request().username()), id)
          );           
	  }
	  
	  public static Result updateProtocol(Long id) {
		  
		  Form<Protocol> filledForm = form(Protocol.class).bindFromRequest();
		  String username = request().username();
		  
		  if(filledForm.hasErrors()) {
			    return badRequest(
			      views.html.protocol.render(filledForm, User.find.byId(username))
			    );

		  } 
			  
		  Protocol filled = filledForm.get();
		  
		  filled.update(id);
		  Logger.info("User " + username + " updating protocol document " + filled.alfasigma);
		  
			  //Protocol.create(filledForm.get());
		    
		  return redirect(routes.DataTable.index());  

	  }
	  
	  public static Result saveToExcel(){
			try {
				BatchImporter.saveToExcel();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  return redirect(routes.DataTable.index());
	  }
	  
}