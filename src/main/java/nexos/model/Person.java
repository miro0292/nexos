package nexos.model;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document (collection = "Person")
public class Person {

	@Transient
    public static final String SEQUENCE_NAME = "users_sequence";
	
	@Id
	private long id;
	
	private String docIdentidad;
	private String nombre;
	private String apellido;
	private String telefono;
	private String tipo;
	
	public Person() {
		// TODO Auto-generated constructor stub
	}

	public Person(String docIdentidad, String nombre, String apellido, String telefono, String tipo) {
		super();
		this.docIdentidad = docIdentidad;
		this.nombre = nombre;
		this.apellido = apellido;
		this.telefono = telefono;
		this.tipo = tipo;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDocIdentidad() {
		return docIdentidad;
	}

	public void setDocIdentidad(String docIdentidad) {
		this.docIdentidad = docIdentidad;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", docIdentidad=" + docIdentidad + ", nombre=" + nombre + ", apellido=" + apellido
				+ ", telefono=" + telefono + ", tipo=" + tipo + "]";
	} 
	
	
}
