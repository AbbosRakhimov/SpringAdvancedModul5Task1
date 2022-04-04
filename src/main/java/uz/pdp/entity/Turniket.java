package uz.pdp.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Turniket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Date exit;
	
	private Date entry;
	
//	private boolean turniket; // if true means employer beginn to work enty=timestemp else employer finish with work exit=timestamp
	


	public Turniket(Date exit, Date entry) {
		super();
		this.exit = exit;
		this.entry = entry;
	}
	
	
}
