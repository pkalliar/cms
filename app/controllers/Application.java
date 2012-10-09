package controllers;

import java.util.Map;

import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;

import models.Contact;
import models.Task;
import models.User;
import play.*;
import play.data.Form;
import play.libs.Json;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
	
	static Form<Task> taskForm = form(Task.class);
	
    // -- Authentication
    
    public static class Login {
        
        public String username;
        public String password;
        
        public String validate() {
            if(User.authenticate(username, password) == null) {
                return "Invalid user or password";
            }
            return null;
        }
        
    }

    /**
     * Login page.
     */
    public static Result login() {
        return ok(
            login.render(form(Login.class))
        );
    }
    
    /**
     * Handle login form submission.
     */
    public static Result authenticate() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        
    	System.out.println("Searching for " + loginForm.get().username + " " + loginForm.get().password);
        
        if(loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session("username", loginForm.get().username);
            return redirect(
                routes.DataTable.index()
            );
        }
    }

    /**
     * Logout and clean the session.
     */
    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(
            routes.Application.login()
        );
    }
  
  public static Result index() {
	return ok("Hello world");
    //return ok(index.render("Your new application is ready."));
  }
  
  public static Result tasks() {
	  return ok(
	    views.html.index.render(Task.all(), taskForm, User.find.byId(request().username()))
	  );
	}
  
  public static Result newTask() {
	  Form<Task> filledForm = taskForm.bindFromRequest();
	  if(filledForm.hasErrors()) {
	    return badRequest(
	      views.html.index.render(Task.all(), filledForm, User.find.byId(request().username()))
	    );
	  } else {
	    Task.create(filledForm.get());
	    return redirect(routes.Application.tasks());  
	  }
	}
  
  public static Result deleteTask(Long id) {
	  Task.delete(id);
	  return redirect(routes.Application.tasks());
	}
  

  
}