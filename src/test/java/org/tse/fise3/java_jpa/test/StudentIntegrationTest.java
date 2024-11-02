package org.tse.fise3.java_jpa.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
	}
	
	@AfterEach
	public void destroy() {
		if (this.em != null) {
			this.em.close();
		}
		if (this.emf != null) {
			this.emf.close();
		}
	}
	
	@Test
	public void persistStudentThenRetrieveTheDetails() {
		Student student = new Student();
		student.setName("Rémy Girodon");
		this.persistStudent(student);
		
		List<Student> students = this.findAllStudents();
		assertEquals(1, students.size());
		Student john = students.get(0);
		assertEquals(1L, john.getId().longValue());
		assertEquals("Rémy Girodon", john.getName());
		
		this.deleteStudent(student);
		
		students = this.findAllStudents();
		assertEquals(0, students.size());
	}
	
	private void deleteStudent(Student student) {
		this.em.getTransaction().begin();
		this.em.remove(student);;
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
