package edu.npu.courseapp.dao;

import java.util.List;

import edu.npu.courseapp.domain.Student;

public interface StudentDao {
	public Student findStudentFromId(long id);
	public List<Student> findStudents();
	public void insertStudent(Student stud);
}
