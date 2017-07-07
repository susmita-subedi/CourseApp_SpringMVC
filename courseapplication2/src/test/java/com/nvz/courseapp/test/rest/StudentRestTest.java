package com.nvz.courseapp.test.rest;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;

import edu.npu.courseapp.domain.Gender;
import edu.npu.courseapp.domain.Student;

/*  Test the REST part of our application -- MAKE YOU HAVE STARTED THE SERVER before running this test and used
 * addSampleData.sql (resources folder) to add a sample Student into the database.
 */  
public class StudentRestTest {
	private static String STUDENT_SERVICES_URL = "http://localhost:8080/courseapp/webservices/student/";
	private static String STUDENT_LOOKUP_URL = STUDENT_SERVICES_URL + "{id}";
	private Student testStudent;
	
	@Before
	public void init() {
		testStudent = new Student(); 
		testStudent.setLastName("Jefferson");
		testStudent.setFirstName("Bill");
		testStudent.setAge(23);
		testStudent.setGender(Gender.Male);
		testStudent.setId(1);
	}
	
	@Test
	public  void testLookupStudent() {
		int idToLookup = 1;  /* Just some hardcoded test data -- make sure this is already in the database! */
		int responseCode;
		Student student=null;
		
		Client client = ClientBuilder.newClient();
		
		//Targeting the RESTful Webservice we want to invoke by capturing it in WebTarget instance.
		WebTarget target = client.target(STUDENT_LOOKUP_URL);
		target = target.resolveTemplate("id", idToLookup);
		
		// Now that we have the URI in the target, build the request i.e a GET request to the RESTful Webservice 
		Builder request = target.request(MediaType.APPLICATION_XML_TYPE);
		Response response = request.get();
		
		responseCode = response.getStatus();
		assertEquals(responseCode, 200);
		
		student = response.readEntity(Student.class);
		assertEquals(student,testStudent);
	}
	
	@Test
	public void testPost() {
		int responseCode;
		Student newStudent = new Student();
		newStudent.setFirstName("Sue");
		newStudent.setLastName("Tao");
		newStudent.setAge(21);
		newStudent.setGender(Gender.Female);
		
		Client client = ClientBuilder.newClient();
		
		WebTarget target = client.target(STUDENT_SERVICES_URL);
		
		Builder request = target.request();
		request.accept(MediaType.APPLICATION_XML_TYPE);
		Response response = request.post(Entity.xml(newStudent));
		
		responseCode = response.getStatus();
		assertEquals(responseCode, 201);
		
		Student createdStudent = response.readEntity(Student.class);
		long createdId = createdStudent.getId();
		newStudent.setId(createdId);
		assertEquals(newStudent,createdStudent);
	}
	
}
