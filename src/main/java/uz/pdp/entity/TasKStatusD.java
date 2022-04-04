package uz.pdp.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
public class TasKStatusD {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private TaskStatus taskStatus;
	
	public String getTaskStatusName() {
		return taskStatus.name();
	}
}
