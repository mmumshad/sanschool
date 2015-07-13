package controllers;

import play.*;
import play.libs.Json;
import play.mvc.*;
import play.db.jpa.*;
import views.html.*;
import models.Challenge;
import models.Person;
import play.data.Form;

import java.util.Arrays;
import java.util.List;

//import org.eclipse.jetty.util.ajax.JSON;









import org.javatuples.Triplet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static play.libs.Json.*;

public class Application extends Controller {

    public Result index() {
        return ok(index.render());
    }

    @Transactional
    public Result addPerson() {
        Person person = Form.form(Person.class).bindFromRequest().get();
        JPA.em().persist(person);
        return redirect(routes.Application.index());
    }

    @Transactional(readOnly = true)
    public Result getPersons() {
        List<Person> persons = (List<Person>) JPA.em().createQuery("select p from Person p").getResultList();
        return ok(toJson(persons));
    }
    
    @Transactional(readOnly = true)
    public Result getViprCourse() {
    	return ok(vipr.render("ViPR Controller Course"));
    }
    
    @Transactional(readOnly = true)
    public Result getViprChallenge() {
    	return ok(challenge_vipr_1.render("ViPR Controller Challenge"));
    	//return ok(result);
    }
    
    @Transactional(readOnly = true)
    public Result getChallengeQuestion() throws Exception{
    	String qs = request().getQueryString("questionNumber");
    	
    	int questionNumber = 0;
    	if(qs != null) {
    		questionNumber = Integer.valueOf(qs);
    	}
    	questionNumber+=1;
    	System.out.println("Question Number =" + questionNumber);
    	ObjectNode result = Json.newObject();
    	
    	Challenge c = new Challenge();
    	
    	Triplet<String,String[],String> questionTriplet = c.getQuestion("ViPR","1",String.valueOf(questionNumber));
    	
    	String question_text = questionTriplet.getValue0();
    	String[] options_array = questionTriplet.getValue1();
    	String answer = questionTriplet.getValue2();
    	String question_type = "text";
    	
    	System.out.println("Options Array =" + options_array);
    	List<String> options = null;
    	
    	if(options_array != null){
    		question_type = "radio";
    		options = Arrays.asList(options_array);
    	}
    	
    	System.out.println("Options =" + options);
    	//String[] options = options_array;
    	String question_html_page = question.render(String.valueOf(questionNumber),question_text,options,question_type).body();
    	System.out.println("Rendered");
    	result.put("question", question_html_page);
    	result.put("answer", answer);
    	result.put("questionNumber", questionNumber);
    	result.put("question_type", question_type);
    	return ok(result);
    }
    
    
    
}
