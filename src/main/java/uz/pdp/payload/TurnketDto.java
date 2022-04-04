package uz.pdp.payload;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Check;

import lombok.Data;

@Data
public class TurnketDto {
	
	@NotNull(message = "COmpany is mandatory")
	private Integer companyId;
	
	@NotNull(message = "exitOrEntry is mandatory")
	//@Check(constraints = "exitOrEntry==1 or exitOrEntry==0")
//	@Column(columnDefinition = "bigint check(exitOrEntry==1) or bigint check(exitOrEntry==0)")
	private Integer exitOrEntry; //if 0 than entry gets a value if 1 than gets exit a value in TurnketDto
}
