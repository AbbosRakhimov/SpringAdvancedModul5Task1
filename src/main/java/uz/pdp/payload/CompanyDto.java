package uz.pdp.payload;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Data;
@Data
public class CompanyDto {
	
	@NotNull(message ="CompanyName ist mandatory")
	private String companyName;
	
	private Date exit;
	
	private Date entry;
	
	private boolean turniket;
}
