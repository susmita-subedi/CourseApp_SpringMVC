package edu.npu.courseapp.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.npu.courseapp.domain.Gender;
import edu.npu.courseapp.domain.Student;
import edu.npu.courseapp.services.StudentService;

/*
     To get to the home page, point your browser to:  http://localhost:8080/courseapp
     or get the new student data form presented directly by going to:  http://localhost:8080/courseapp/newStudentDataForm
*/

@Controller
public class StudentController {
	@Autowired
	StudentService studentService;
	
	private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
	
	// Present the student data form
	@RequestMapping(value = "/newStudentDataForm", method = RequestMethod.GET)
	public ModelAndView newStudentDataForm() {
		ModelAndView modelView;
		
 		modelView = new ModelAndView("studentDataForm");
 		modelView.addObject("student", new Student());
		return modelView;
	}
	
	// Process the data the user has entered in the student data form
	@RequestMapping(value = "/processNewStudentProfile", method = RequestMethod.POST)
	public ModelAndView processNewStudentForm(@ModelAttribute("student") @Valid Student student, BindingResult result, HttpSession session) 
	{
		ModelAndView modelView;
		
		if (result.hasErrors()) {
			/*  Re-present the form with error messages */
			modelView = new ModelAndView("studentDataForm");
			return modelView;
		}
		
		studentService.addNewStudent(student);
 		modelView = new ModelAndView("newStudentProfileSuccess");
 		session.setAttribute("student", student);
 		modelView.addObject("student", student);
		
		return modelView;
	}
	
	/* Not for production code, just a demo of transaction rollback */
	/* If transactions are working, you should not see goodStudent in the database -- a rollback should occur when adding badStudent  */
	@RequestMapping(value = "/testTransactions", method = RequestMethod.GET)
	public ModelAndView testTransactions() 
	{
		Student goodStudent, badStudent;
		ModelAndView modelView;
		
		goodStudent = new Student();
		goodStudent.setLastName("ValidStudent");
		goodStudent.setFirstName("Joe");
		goodStudent.setGender(Gender.Male);
		goodStudent.setAge(25);
		
		modelView = new ModelAndView("studentUpdateResult");
		
		try {
			/* This student object will cause an exception in the DAO */
			badStudent = new Student();
			logger.debug("Test Transaction use:  add two students to the database");
			studentService.addTwoStudents(goodStudent, badStudent);
			modelView.addObject("resultMsg", "The Update was performed.");
			logger.debug("Unexpected result.  The update succeeded?");   /* We shouldn't get here */
		} catch (Exception ex) {  /* Exception from null student name */
			logger.info("Exception occurred as expected in testTransactions: " + ex);
			modelView.addObject("resultMsg", "Unable to perform the Student Update!");
		}
		
		return modelView;
	}
	
}
