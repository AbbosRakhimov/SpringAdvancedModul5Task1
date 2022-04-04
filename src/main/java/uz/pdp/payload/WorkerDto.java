package uz.pdp.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class WorkerDto {

	@Email
	@NotNull(message = "Email is mandatory")
	private String email;

	@NotNull(message = "Fillnamei is mandatory")
	private String fullName;
	
	@NotNull(message = "CompanyId is Mandatory")
	private Integer companyId;

	private Double salary;

	private String monthForSalary;


}
