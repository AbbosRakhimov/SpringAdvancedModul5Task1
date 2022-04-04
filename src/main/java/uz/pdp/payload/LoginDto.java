package uz.pdp.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class LoginDto {

	@NotNull(message = "Email is mandatory")
	@Email
	private String username;
	
	@NotNull(message = "Password is madatory")
	private String password;
}
