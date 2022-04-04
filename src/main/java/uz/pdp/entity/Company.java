package uz.pdp.entity;

import lombok.Data;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Company {

	@Id
	@GeneratedValue
	private Integer id;
	
	@Column(nullable = false, unique = true)
	private String companyName;
	
	@OneToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Turniket turniket;

	public Company(String companyName, Turniket turniket) {
		super();
		this.companyName = companyName;
		this.turniket = turniket;
	}
	
	
}
