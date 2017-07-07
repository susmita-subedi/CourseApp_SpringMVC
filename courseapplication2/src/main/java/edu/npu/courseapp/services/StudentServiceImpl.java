package edu.npu.courseapp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.npu.courseapp.dao.StudentDao;
import edu.npu.courseapp.domain.Student;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {
	@Autowired
	@Qualifier("StudentDaoJdbcImpl")
	private StudentDao studentDao;
	
	public void addNewStudent(Student stud) {
		studentDao.insertStudent(stud);
	}
	
	public void addTwoStudents(Student stud1, Student stud2) {
		studentDao.insertStudent(stud1);
		studentDao.insertStudent(stud2);
	}

	@Override
	public Student getStudentWithId(int studId) {
		return studentDao.findStudentFromId(studId);
		
	}

	@Override
	public List<Student> getStudentList() {
		return studentDao.findStudents();
	}

}
