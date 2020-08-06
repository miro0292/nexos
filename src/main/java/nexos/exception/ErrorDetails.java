package nexos.exception;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "LogBD")
public class ErrorDetails implements Serializable{

	@Transient
    public static final String SEQUENCE_NAME = "users_sequence";
	private static final long serialVersionUID = -4293488436461079597L;
	private Date timestamp;
	private String message;
	private String details;

	public ErrorDetails(Date timestamp, String message, String details) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}
	public ErrorDetails() {
		// TODO Auto-generated constructor stub
	}
	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public Date getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}
}
