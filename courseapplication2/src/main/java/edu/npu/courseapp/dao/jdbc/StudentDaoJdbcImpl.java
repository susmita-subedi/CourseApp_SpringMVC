package edu.npu.courseapp.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import edu.npu.courseapp.dao.StudentDao;
import edu.npu.courseapp.domain.Gender;
import edu.npu.courseapp.domain.Student;

@Repository("StudentDaoJdbcImpl")
public class StudentDaoJdbcImpl implements StudentDao {
	@Autowired
	private DataSource dataSource;
	private NamedParameterJdbcTemplate dbTemplate;
	private SimpleJdbcInsert jdbcInsert;
	private StudentRowMapper studentRowMapper;

	@PostConstruct
	public void setup() {
		dbTemplate = new NamedParameterJdbcTemplate(dataSource);
		jdbcInsert = new SimpleJdbcInsert(dataSource);
		jdbcInsert.withTableName("student");
		jdbcInsert.usingGeneratedKeyColumns("id");
		
		studentRowMapper = new StudentRowMapper();
	}
	
	@Override
	public Student findStudentFromId(long id) {
		String sql = "SELECT * FROM student WHERE id = :id";
		MapSqlParameterSource params = new MapSqlParameterSource("id", id);
		List<Student> matchingStudents = dbTemplate.query(sql, params, studentRowMapper);
		if (matchingStudents.size() != 1) {
			return null;  /* student with requested id was not found  */
		}

		return matchingStudents.get(0);
	}
	
	@Override
	public List<Student> findStudents() {
		String sql = "SELECT * FROM student";
		List<Student> studentList = dbTemplate.query(sql, studentRowMapper);
		return studentList;
	}
	
	/*   Student has an enumeration data member so we can't use BeanPropertySqlParameterSource.
	 *   Instead, we'll generate our own Map from the data members.
	 */
	public Map<String, Object> getStudentParams(Student stud) {
		Map<String, Object> studParams = new HashMap<String, Object>();
		Gender genderVal;
		int genderIntVal;
		
		studParams.put("lastName", stud.getLastName());
		studParams.put("firstName", stud.getFirstName());
		studParams.put("age", stud.getAge());
		
		genderVal = stud.getGender();
		genderIntVal = genderVal.ordinal();
		studParams.put("gender", genderIntVal);
		return studParams;
	}
	
	public void insertStudent(Student stud) {
		long id;
		Map<String, Object> studParams = getStudentParams(stud);
		Number newId = jdbcInsert.executeAndReturnKey(studParams);
		id = newId.longValue();
		stud.setId(id);
	}

}
