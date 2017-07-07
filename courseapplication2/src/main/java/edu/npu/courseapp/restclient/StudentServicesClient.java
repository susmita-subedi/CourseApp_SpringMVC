package edu.npu.courseapp.restclient;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import edu.npu.courseapp.domain.Gender;
import edu.npu.courseapp.domain.Student;


/* This is code that would appear on the Client side.   Remember that before you can run this, you must
 * have your Rest Server running!
 */
public class StudentServicesClient {
	static private Logger logger = Logger.getLogger(StudentServicesClient.class);
	private static String STUDENT_SERVICES_URL = "http://localhost:8080/courseapp/webservices/student/";
	private static String STUDENT_LOOKUP_URL = STUDENT_SERVICES_URL + "{id}";
	private static Client client=null;  
	
	public static void main(String args[]) {
		testLookupStudent();  /* requires a Student to already be in the database -- set the id for the student to lookup in this method  */
		testPost();
	}
	
	private static Client getClient() {
		if (client == null) {
			client = ClientBuilder.newClient();
		}
		
		return client;
	}
	
	public static Student testLookupStudent() {
		int idToLookup = 1;  /* Just some hardcoded test data -- a student with this id must be in the database */
		int responseCode;
		Student student=null;
		
		Client client = getClient();
		
		//Targeting the RESTful Webservice we want to invoke by capturing it in WebTarget instance.
		WebTarget target = client.target(STUDENT_LOOKUP_URL);
		target = target.resolveTemplate("id", idToLookup);
		
		// Now that we have the URI in the target, build the request i.e a GET request to the RESTful Webservice 
		Builder request = target.request(MediaType.APPLICATION_XML_TYPE);
		Response response = request.get();
		
		responseCode = response.getStatus();
		logger.info("The response code is: " + responseCode);
		if (responseCode == 200) {
			student = response.readEntity(Student.class);
			logger.info(student);
		}
		
		return student;
	}
	
	
	/* Using a POST Http Command, we'll add a completely new student */
	public static void testPost() {
		int responseCode;
		Student newStudent;
		Client client = getClient();
		
		newStudent = createNewStudent();
		
		WebTarget target = client.target(STUDENT_SERVICES_URL);
		
		Builder request = target.request();
		request.accept(MediaType.APPLICATION_XML_TYPE);
		Response response = request.post(Entity.xml(newStudent));
		
		responseCode = response.getStatus();
		logger.info("The response code from Post operation is: " + responseCode);
		
		if (responseCode == 200) {
			Student createdStudent = response.readEntity(Student.class);
			logger.debug(createdStudent);
		}
	}
	
	public static Student createNewStudent() {
		Student stud;
		
		stud = new Student();
		stud.setFirstName("Susan");
		stud.setLastName("Wilson");
		stud.setAge(25);
		stud.setGender(Gender.Female);
		return stud;
	}


}
