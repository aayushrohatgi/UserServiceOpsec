package com.opsec.userservice.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * The User Details Entity
 *
 * @author Aayush Rohatgi
 */
@Document(collection = "users")
@Data
public class UserDetails {
	@Id
	private String id;
	private String firstName;
	private String lastName;
	private String title;
	private Date dob;
}
