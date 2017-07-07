package edu.npu.courseapp.resthandlers;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import edu.npu.courseapp.domain.Student;
import edu.npu.courseapp.domain.StudentList;
import edu.npu.courseapp.exceptions.UnknownResourceException;
import edu.npu.courseapp.services.StudentService;

@Path("/")
public class StudentRestHandler {
	@Autowired
	private StudentService studentService;
	private Logger logger = Logger.getLogger(StudentRestHandler.class);
	
	/* Look up a Student from the student id.   
	 * Matching Url:
	 * http://localhost:8080/courseapp/webservices/student/{id}   Note -- replace {id} with a number (the id of the student)
	 * See web.xml file for Jersey configuration
	 */
	@GET
	@Path("/student/{id}")
	@Produces("application/xml, application/json")
	public Student getStudent(@PathParam("id") int id) {
		Student student = null;
		
		try {
			student = studentService.getStudentWithId(id);
		} catch (Exception ex) {
			throw new WebApplicationException(ex.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
		
		if (student == null) {
			logger.debug("Failed Request to lookup student with id  : " + id);
			throw new UnknownResourceException("Student id: " + id + " is invalid");
		}
		
		return student;
	}
	
	/* Return a list of all the students
	 * Matching Url:
	 * http://localhost:8080/courseapp/webservices/student
	 * See web.xml file for Jersey configuration
	 */
	@GET
	@Path("/student")
	@Produces("application/xml")
	public StudentList getStudentList() {
		List<Student> studList;
		StudentList listOfStudents = new StudentList();
		
		studList = studentService.getStudentList();
		listOfStudents.setStudentList(studList);
		return listOfStudents;
	}
	
	
	/* Create a new Student
	 * URL:  http://localhost:8080/courseapp/webservices/student
	 * To test, you can POST the data from file student.xml or student.json (see resources folder):
	 * After doing the post, use a get command to retrieve the student (and verify that the post was successful).
	 */
	@POST
	@Path("/student")
	@Produces("application/json, application/xml")
	@Consumes("application/json, application/xml")
	public Response addStudent(Student newStudent) {
		ResponseBuilder respBuilder;
		
		try { 
			studentService.addNewStudent(newStudent);
		} catch (Exception ex) {
			throw new WebApplicationException(ex.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
		
		logger.debug("Successfully created a new Student: " + newStudent);
		respBuilder = Response.status(Status.CREATED);
		respBuilder.entity(newStudent);
		return respBuilder.build();
	}
	
}
