package edu.npu.courseapp.services;

import java.util.List;

import edu.npu.courseapp.domain.Student;

public interface StudentService {
	public Student getStudentWithId(int studId);
	public List<Student> getStudentList();
	public void addNewStudent(Student stud);
	public void addTwoStudents(Student stud1, Student stud2);
}
