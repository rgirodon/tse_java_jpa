package org.tse.fise3.java_jpa.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class CulturalOption {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    private String name;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "culturalOption")
    private List<Student> students;
    
    public CulturalOption() {
    	
    	this.students = new ArrayList<>();
    }
    
    public void addStudent(Student student) {
    	
    	this.students.add(student);
    	
    	student.setCulturalOption(this);
    }
    
    public void removeStudent(Student student) {
    	
    	this.students.remove(student);
    	
    	student.setCulturalOption(null);
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}
}
