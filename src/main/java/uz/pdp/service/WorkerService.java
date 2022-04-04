package uz.pdp.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import uz.pdp.entity.Company;
import uz.pdp.entity.Role;
import uz.pdp.entity.Task;
import uz.pdp.entity.Turniket;
import uz.pdp.entity.Worker;
import uz.pdp.entity.enums.RoleName;
import uz.pdp.entity.enums.TaskStatus;
import uz.pdp.payload.ApiResponse;
import uz.pdp.payload.CompanyDto;
import uz.pdp.payload.LoginDto;
import uz.pdp.payload.TaskDto;
import uz.pdp.payload.TaskStatusDto;
import uz.pdp.payload.TurnketDto;
import uz.pdp.payload.WorkerDto;
import uz.pdp.repository.CompanyRepository;
import uz.pdp.repository.RoleRepository;
import uz.pdp.repository.TaskRepository;
import uz.pdp.repository.TurniketRepository;
import uz.pdp.repository.WorkerRepository;
import uz.pdp.security.JwtTokenProvider;

@Service
public class WorkerService implements UserDetailsService {

	@Autowired
	WorkerRepository wRepository;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	JavaMailSender javaMailSender;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtTokenProvider jprovider;
	
	@Autowired
	TurniketRepository turniketRepository;
	
	@Autowired
	TaskRepository taskRepository;
	
	public ApiResponse addDirektor( WorkerDto workerDto) {
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(wRepository.existsByEmailOrCompanyId(workerDto.getEmail(), workerDto.getCompanyId()))
			return new ApiResponse("Direktor exist alerady", false);
//		if(auth instanceof AnonymousAuthenticationToken) {
			Worker worker = new Worker();
			Optional<Company> cOptional=companyRepository.findById(workerDto.getCompanyId());
			if(!cOptional.isPresent())
				return new ApiResponse("Company not found",false);
			worker.setFullName(workerDto.getFullName());
			worker.setCompany(cOptional.get());
			worker.setEmail(workerDto.getEmail());
			worker.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_DIREKTOR)));
			
			
			worker.setEmailCode(UUID.randomUUID().toString());
			
			wRepository.save(worker);
			boolean succeOrNot=sendEmail(worker.getEmail());
			if(!succeOrNot)
				return new ApiResponse("email could not be sent",false);
			
			
			return new ApiResponse("successfully registered.To activate the account, confirm the email", true);
			
//		}
//		Worker worker1 = (Worker)auth.getPrincipal();
//		Set<Role> roles = worker1.getRoles();
//		List<Role> roles2 = new ArrayList<>(roles);
//		for (Role role : roles2) {
//			if(role.getAuthority().equals("DIREKTOR"))
//			return new ApiResponse("Direktor already exist",false);
//			break;
//		}
//		return null;
	}
	public ApiResponse addMeneger( WorkerDto workerDto) {
		if(wRepository.existsByEmail(workerDto.getEmail()))
			return new ApiResponse("MENEGER exist alerady", false);
			Worker worker = new Worker();
			Optional<Company> cOptional=companyRepository.findById(workerDto.getCompanyId());
			if(!cOptional.isPresent())
				return new ApiResponse("Company not found",false);
			worker.setFullName(workerDto.getFullName());
			worker.setCompany(cOptional.get());
			worker.setEmail(workerDto.getEmail());
			worker.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_HR_MANAGER)));
			worker.setEmailCode(UUID.randomUUID().toString());
			
			wRepository.save(worker);
			sendEmail(worker.getEmail());
			
			return new ApiResponse("successfully registered.To activate the account, confirm the email", true);
			
	}
	public ApiResponse addWorker( WorkerDto workerDto) {
		if(wRepository.existsByEmail(workerDto.getEmail()))
			return new ApiResponse("MENEGER exist alerady", false);
			Worker worker = new Worker();
			Optional<Company> cOptional=companyRepository.findById(workerDto.getCompanyId());
			if(!cOptional.isPresent())
				return new ApiResponse("Company not found",false);
			worker.setFullName(workerDto.getFullName());
			worker.setCompany(cOptional.get());
			worker.setEmail(workerDto.getEmail());
			worker.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_WORKER)));
			worker.setEmailCode(UUID.randomUUID().toString());
			
			wRepository.save(worker);
			sendEmail(worker.getEmail());
			
			return new ApiResponse("successfully registered.To activate the account, confirm the email", true);
			
	}
//		Worker worker = new Worker();
//		Optional<Company> cOptional=companyRepository.findById(workerDto.getCompanyId());
//		if(!cOptional.isPresent())
//			return new ApiResponse("Company not found",false);
//		worker.setFullName(workerDto.getFullName());
//		worker.setCompany(cOptional.get());
//		worker.setEmail(workerDto.getEmail());
//		worker.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.DIREKTOR)));
//		
//		
//		worker.setEmailCode(UUID.randomUUID().toString());
//		
//		wRepository.save(worker);
//		sendEmail(worker.getEmail());
//		
//		return new ApiResponse("successfully registered.To activate the account, confirm the email", true);
		
	public ApiResponse addCompany( CompanyDto companyDto) {
		if (companyRepository.existsByCompanyName(companyDto.getCompanyName()))
			return new ApiResponse("Company exist", false);

		Turniket turniket = new Turniket(null, null);
		Turniket turniket2 = turniketRepository.save(turniket);

		Company company = new Company(companyDto.getCompanyName(), turniket2);
		companyRepository.save(company);

		return new ApiResponse("Company successfuly added", true);
	}
	
	public ApiResponse getCompany(Integer id) {
		Optional<Company> cOptional = companyRepository.findById(id);
		return cOptional.isPresent()?new ApiResponse("exist",true,cOptional.get()):new ApiResponse("not found",false);
	}

	public ApiResponse register(LoginDto loginDto) {
		try {
			Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
			Worker worker = (Worker)authenticate.getPrincipal();
			String token = jprovider.genereToken(loginDto.getUsername(), worker.getRoles());
			return new ApiResponse("Token", true, token);
		} catch (Exception e) {
			return new ApiResponse("Password or Email is wrong", false);
		}	
	}
	public boolean sendEmail(String email) {
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setFrom("Ali@pdp.com");
			mailMessage.setTo(email);
			mailMessage.setSubject("Link to verify account");
			mailMessage.setText("<a href='http://localhost:8080/setPassword?email="+email+"'>SetPassword</a>");
			javaMailSender.send(mailMessage);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public Boolean sendEmailForTask(String email, String EmailAssidned, String tasName, String dataFroFinish) {
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setFrom("Ali@pdp.com");
			mailMessage.setTo(email);
			mailMessage.setSubject("message about the task assigned to them");
			mailMessage.setText("you have received a new Task with TaskName "+tasName+" from "
			+EmailAssidned+" must be completed by "+dataFroFinish);
			javaMailSender.send(mailMessage);
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public Boolean sendEmailForDirekOrMana(String email, String fullName, String taskname ) {
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setFrom("Ali@pdp.com");
			mailMessage.setTo(email);
			mailMessage.setSubject("message about the task assigned to them");
			mailMessage.setText("The Employer "+ fullName+" has set the Task "+taskname+" to done ");
			javaMailSender.send(mailMessage);
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
/**
 * 
 * @param password
 * @param passwordrepeat
 * @param email
 * @return
 */
	public ApiResponse getParam(String password, String passwordrepeat, String email) {
		if (!password.isEmpty() && !passwordrepeat.isEmpty() && !email.isEmpty()) {
			if(password.length()<5)
				return new ApiResponse("password must be longer/*/** than 5 characters", false);
			Optional<Worker> wOptional = wRepository.findByEmail(email);
			if (!wOptional.isPresent())
				return new ApiResponse("Worker not exist", false);
			if (password.equals(passwordrepeat) && wOptional.get().isSetPassword()) {
				Worker worker = wOptional.get();
				worker.setPassword(passwordEncoder.encode(password));
				worker.setEnabled(true);
				worker.setSetPassword(false);
				wRepository.save(worker);

				return new ApiResponse("Password successfuly saved", true);
			}
			if (!wOptional.get().isSetPassword())
				return new ApiResponse("Password already Set", false);
			return new ApiResponse("Password and Passwordrepaid do not match", false);
		}
		return new ApiResponse("the fields cannot be empty", false);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		return wRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException(username+"notfound"));
	}
	
	public ApiResponse addTask(TaskDto taskDto) {
		if (taskRepository.existsByTaskName(taskDto.getTaskName()))
			return new ApiResponse("Task is already exist", false);
		Optional<Worker> wOptional = wRepository.findByEmail(taskDto.getWorkerEmail());
		if (!wOptional.isPresent())
			return new ApiResponse("Worker not found", false);
		Worker worker = wOptional.get();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Worker taskAssigner = (Worker) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Set<Role> roles = worker.getRoles();
		List<Role> roles2 = new ArrayList<>(roles);
		for (Role role : roles2) {
			if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DIREKTOR"))
					&& role.getAuthority().equals("ROLE_DIREKTOR")) {
				return new ApiResponse("you are the director and cannot give yourself any tasks", false);
			}
			if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_HR_MANAGER"))
					&& role.getAuthority().equals("ROLE_HR_MANAGER") 
					|| auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_HR_MANAGER")) 
					&& role.getAuthority().equals("ROLE_DIREKTOR") ) {
				return new ApiResponse("you can't give yourself a task as HR manager or for Director", false);
			}
		}
		Task task= new Task(taskDto.getTaskName(), taskDto.getTaskKommentar(), 
				taskDto.getDataForFinishWork(), wOptional.get(),TaskStatus.START,taskAssigner.getEmail());
		taskRepository.save(task);
		sendEmailForTask(worker.getEmail(), taskAssigner.getEmail(), taskDto.getTaskName(), taskDto.getDataForFinishWork());
		return new ApiResponse("Task succesfuly assigned", true);
		
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		if(auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))	
	}

	public ApiResponse taskStatus(TaskStatusDto taskStatusDto) {

		Worker auth = (Worker) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Optional<Task> task = taskRepository.findTaskByWorkerIdAndTaskName(auth.getId(), taskStatusDto.getTaskName());
		Task task2 = task.get();
		if (!task.isPresent())
			return new ApiResponse("Task not found or is not your task", false);
		if (taskStatusDto.isProgressOrDone()) {
			task2.setTaskstatus(TaskStatus.DONE);
			taskRepository.save(task2);
			sendEmailForDirekOrMana(task2.getEmailFromDirektorOrHRManager(), auth.getFullName(), task2.getTaskName());
		}
		task2.setTaskstatus(TaskStatus.PROGRES);
		taskRepository.save(task2);
		return new ApiResponse("Task to Progresse changed", true);
	}
	public ApiResponse registerTurnket(TurnketDto turnketDto) {
		Optional<Company> cOptional = companyRepository.findById(turnketDto.getCompanyId());
		if(!cOptional.isPresent())
			return new ApiResponse("Comnpany not found", false);
		Turniket turniket = cOptional.get().getTurniket();
		if(turnketDto.getExitOrEntry()>1 || turnketDto.getExitOrEntry()<0 )
			return new ApiResponse("except 1 and 0 no numbers allowed", false);
		if(turnketDto.getExitOrEntry()==0) {
			turniket.setEntry(new Date());;
		}
		if(turnketDto.getExitOrEntry()==1) {
			turniket.setExit(new Date());
		}
		turniketRepository.save(turniket);
		
		return new ApiResponse("Turniket succesfuly saved", true);
	}
	public List<Worker> getWorkers(){
		return wRepository.findAll();
	}
	
	public List<Worker> getWorkersById(Integer id){
		return wRepository.getworker(id);
	}
}
