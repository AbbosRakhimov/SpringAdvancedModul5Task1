package uz.pdp.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TaskStatusDto {

	
	@NotNull(message = "TaskId is mandatory")
	private String taskName;
	
	@NotNull(message = "Taskstatus is mandatory")
	private boolean progressOrDone;
}
