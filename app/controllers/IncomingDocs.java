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
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
 
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
 
import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;
 
@Security.Authenticated(Secured.class)
public class IncomingDocs extends Controller {
	
	
	  public static Result index() {
		    //return ok("Hello world");
		    return ok(views.html.listIncoming.render("Λίστα εισερχόμενης αλληλογραφίας.", User.find.byId(request().username())));
		  }
 
 
	  public static Result list() {
		    /**
		     * Get needed params
		     */
		    Map<String, String[]> params = request().queryString();
		 
		    Integer iTotalRecords = IncomingDoc.find.findRowCount();
		    String filter = params.get("sSearch")[0];
		    Integer pageSize = Integer.valueOf(params.get("iDisplayLength")[0]);
		    Integer page = Integer.valueOf(params.get("iDisplayStart")[0]) / pageSize;
		 
		    /**
		     * Get sorting order and column
		     */
		    String sortBy = "name";
		    String order = params.get("sSortDir_0")[0];
		 
		    switch(Integer.valueOf(params.get("iSortCol_0")[0])) {
		      case 0 : sortBy = "protocolnumber"; break;
		      case 1 : sortBy = "folder"; break;
		      case 2 : sortBy = "receivedon"; break;
		      case 3 : sortBy = "sender"; break;
		      case 4 : sortBy = "name"; break;
		      case 5 : sortBy = "medium"; break;
		      case 6 : sortBy = "location"; break;
		    }
		 
		    /**
		     * Get page to show from database
		     * It is important to set setFetchAhead to false, since it doesn't benefit a stateless application at all.
		     */
		    Page<IncomingDoc> contactsPage = IncomingDoc.find.where(
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
		 
		    for(IncomingDoc c : contactsPage.getList()) {
		      ObjectNode row = Json.newObject();
		      row.put("0", "<a href='/incoming/" + c.id + "/edit' target='_blank'>" + c.protocolnumber + "</a>");
		      if(c.folder.name.equals("TO DO"))
		    	  row.put("1", c.folder.code);
		      else
		    	  row.put("1", c.folder.code + "-" + c.folder.name);
		      row.put("2", c.getReceivedAsString());
		      row.put("3", c.sender);
		      row.put("4", c.name);
		      if(c.medium != null)
		    	  row.put("5", c.medium.name);
		      else
		    	  row.put("5", "");
		      row.put("6", c.location);

		      an.add(row);	    }
		    
		    //return ok(datatable.render(result.asText()));
		 
		    return ok(result);
		 }
	  
	  public static Result protocols() {
	        Form<IncomingDoc> incomForm = form(IncomingDoc.class);
            return ok(
            	views.html.newIncoming.render(incomForm, User.find.byId(request().username()))
            );  

		}
	  
	  public static Result newProtocol() {
		  
		  Form<IncomingDoc> filledForm = form(IncomingDoc.class).bindFromRequest();
		  
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
			      views.html.newIncoming.render(filledForm, User.find.byId(request().username()))
			    );

		  } else {
			  IncomingDoc filled = filledForm.get();
			  System.out.println("Got id: " + filled.id);
			  
			  IncomingDoc.createOrUpdate(filled);
		  
			  //Protocol.create(filledForm.get());
		    return redirect(routes.IncomingDocs.index());  
		  }
		}
	  
	  public static Result editProtocol(Long id) {
	        Form<IncomingDoc> incomForm = form(IncomingDoc.class).fill(
	        		IncomingDoc.find.byId(id)
	            );
            return ok(
            	views.html.editIncoming.render(incomForm, User.find.byId(request().username()), id)
            );           
	  }
	  
	  public static Result updateProtocol(Long id) {
		  
		  Form<IncomingDoc> filledForm = form(IncomingDoc.class).bindFromRequest();
		  String username = request().username();
		  
		  if(filledForm.hasErrors()) {
			    return badRequest(
			      views.html.newIncoming.render(filledForm, User.find.byId(username))
			    );

		  } 
			  
		  IncomingDoc filled = filledForm.get();
		  
		  filled.update(id);
		  Logger.info("User " + username + " updating incoming document " + filled.protocolnumber);
		  
			  //Protocol.create(filledForm.get());
		    
		  return redirect(routes.IncomingDocs.index());  

	  }
	  
	  public static Result deleteProtocol(Long id) {
		  IncomingDoc.delete(id);
		  return redirect(routes.IncomingDocs.index());
	  }
	  
	  public static Result saveToExcel(){
			try {
				BatchImporter.saveToExcel();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  return redirect(routes.IncomingDocs.index());
	  }
	  
}