package uz.pdp.payload;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Data;
import uz.pdp.entity.Worker;

@Data
public class TaskDto {

	@NotNull(message = "TaskName is mandatory")
	private String taskName;
	
	@NotNull(message="Kommentar is mandatory")
	private String taskKommentar;
	
	@NotNull(message="Data is mandatory")
	private String dataForFinishWork;
	
	@NotNull(message = "Id for Worker is mandatory")
	private String workerEmail;

}
