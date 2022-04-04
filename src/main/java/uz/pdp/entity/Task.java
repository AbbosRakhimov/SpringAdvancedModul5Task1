package uz.pdp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.entity.enums.TaskStatus;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, unique = true)
	private String taskName;
	
	@Column
	private String taskKommentar;
	
	@Column(nullable = false)
	private String dataForFinishWork;
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Worker worker;
	
	@Column(nullable = false)
	private TaskStatus taskstatus;
	
	@Email
	@Column(nullable = false)
	private String EmailFromDirektorOrHRManager;

	public Task(String taskName, String taskKommentar, String dataForFinishWork, Worker worker, TaskStatus taskstatus,
			@Email String emailFromDirektorOrHRManager) {
		super();
		this.taskName = taskName;
		this.taskKommentar = taskKommentar;
		this.dataForFinishWork = dataForFinishWork;
		this.worker = worker;
		this.taskstatus = taskstatus;
		EmailFromDirektorOrHRManager = emailFromDirektorOrHRManager;
	}
	
	
	
}
