package org.tse.fise3.java_jpa.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tse.fise3.java_jpa.model.Course;
import org.tse.fise3.java_jpa.model.CulturalOption;
import org.tse.fise3.java_jpa.model.Student;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class StudentIntegrationTest {

	private EntityManagerFactory emf;
	private EntityManager em;

	@BeforeEach
	public void setup() {
		this.emf = Persistence.createEntityManagerFactory("jpa-h2");
		this.em = this.emf.createEntityManager();
		
		this.createDefaultCulturalOptions();
		
		this.createDefaultCourses();
	}

	@AfterEach
	public void destroy() {
		
		this.deleteDefaultCulturalOptions();
		
		this.deleteDefaultCourses();
		
		if (this.em != null) {
			this.em.close();
		}
		if (this.emf != null) {
			this.emf.close();
		}
	}
	
	private void deleteDefaultCulturalOptions() {
		List<CulturalOption> culturalOptions = this.findAllCulturalOptions();
		for (CulturalOption culturalOption : culturalOptions) {
			this.deleteCulturalOption(culturalOption);
		}
	}

	private void createDefaultCulturalOptions() {
		
		CulturalOption option1 = new CulturalOption();
		option1.setName("Painting");
		this.persistCulturalOption(option1);
		
		CulturalOption option2 = new CulturalOption();
		option2.setName("Theater");
		this.persistCulturalOption(option2);
		
		CulturalOption option3 = new CulturalOption();
		option3.setName("Choral");
		this.persistCulturalOption(option3);		
	}
	
	private List<CulturalOption> findAllCulturalOptions() {
		String selectQuery = "SELECT culturalOption FROM CulturalOption culturalOption";
		TypedQuery<CulturalOption> selectFromCulturalOptionTypedQuery = em.createQuery(selectQuery, CulturalOption.class);
		List<CulturalOption> culturalOptions = selectFromCulturalOptionTypedQuery.getResultList();
		return culturalOptions;
	}
	
	private void deleteCulturalOption(CulturalOption culturalOption) {
		this.em.getTransaction().begin();
		this.em.remove(culturalOption);
		this.em.getTransaction().commit();
	}

	private void persistCulturalOption(CulturalOption culturalOption) {
		this.em.getTransaction().begin();
		this.em.persist(culturalOption);
		this.em.getTransaction().commit();
	}
	
	private CulturalOption findCulturalOptionById(long id) {
		
		return this.em.find(CulturalOption.class, id);
	}
	
	private void createDefaultCourses() {
		
		Course course1 = new Course();
		course1.setName("Computer Science");
		this.persistCourse(course1);
		
		Course course2 = new Course();
		course2.setName("Network");
		this.persistCourse(course2);
		
		Course course3 = new Course();
		course3.setName("Data Science");
		this.persistCourse(course3);		
	}
	
	private void persistCourse(Course course) {
		this.em.getTransaction().begin();
		this.em.persist(course);
		this.em.getTransaction().commit();
	}
	
	private Course findCourseById(long id) {
		
		return this.em.find(Course.class, id);
	}
	
	private void deleteCourse(Course course) {
		this.em.getTransaction().begin();
		this.em.remove(course);
		this.em.getTransaction().commit();
	}
	
	private List<Course> findAllCourses() {
		String selectQuery = "SELECT course FROM Course course";
		TypedQuery<Course> selectFromCourseTypedQuery = em.createQuery(selectQuery, Course.class);
		List<Course> courses = selectFromCourseTypedQuery.getResultList();
		return courses;
	}
	
	private void deleteDefaultCourses() {
		List<Course> courses = this.findAllCourses();
		for (Course course : courses) {
			this.deleteCourse(course);
		}
	}
	
	@Test
	public void persistStudentThenRetrieveTheDetails() {
		
		CulturalOption option1 = this.findCulturalOptionById(1L);
		Course course1 = this.findCourseById(1L);
		Course course2 = this.findCourseById(2L);
		
		Student studentToCreate = new Student();
		studentToCreate.setName("Rémy Girodon");
		
		option1.addStudent(studentToCreate);
		
		course1.addStudent(studentToCreate);
		course2.addStudent(studentToCreate);
		
		this.persistStudent(studentToCreate);
		
		List<Student> students = this.findAllStudents();
		assertEquals(1, students.size());
		
		Student studentFound = students.get(0);
		assertEquals(1L, studentFound.getId());
		assertEquals("Rémy Girodon", studentFound.getName());
		assertEquals("Painting", studentFound.getCulturalOption().getName());
		assertEquals(2, studentFound.getCourses().size());
		
		this.deleteStudent(studentFound);
		
		students = this.findAllStudents();
		assertEquals(0, students.size());
	}

	private void deleteStudent(Student student) {
		this.em.getTransaction().begin();
		this.em.remove(student);
		this.em.getTransaction().commit();
	}

	private void persistStudent(Student student) {
		this.em.getTransaction().begin();
		this.em.persist(student);
		this.em.getTransaction().commit();
	}
	
	private List<Student> findAllStudents() {
		String selectQuery = "SELECT student FROM Student student";
		TypedQuery<Student> selectFromStudentTypedQuery = em.createQuery(selectQuery, Student.class);
		List<Student> students = selectFromStudentTypedQuery.getResultList();
		return students;
	}
}
