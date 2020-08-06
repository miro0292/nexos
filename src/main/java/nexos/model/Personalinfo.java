package nexos.model;

import java.util.ArrayList;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document (collection = "Personalinfo")
public class Personalinfo {

	@Transient
    public static final String SEQUENCE_NAME = "users_sequence";
	
	@Id
	private long id;
	
	private Person personal;
	
	private ArrayList<Person> familiares = new ArrayList<>(); 
	
	public Personalinfo() {
		// TODO Auto-generated constructor stub
	}

	public Personalinfo(Person personal, ArrayList<Person> familiares) {
		super();
		this.personal = personal;
		this.familiares = familiares;
	}

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Person getPersonal() {
		return personal;
	}

	public void setPersonal(Person personal) {
		this.personal = personal;
	}

	public ArrayList<Person> getFamiliares() {
		return familiares;
	}

	public void setFamiliares(ArrayList<Person> familiares) {
		this.familiares = familiares;
	}

	@Override
	public String toString() {
		return "Personalinfo [id=" + id + ", personal=" + personal + ", familiares=" + familiares + "]";
	}
	
}
