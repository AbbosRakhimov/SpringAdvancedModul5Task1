package uz.pdp.controller;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import uz.pdp.entity.Role;
import uz.pdp.entity.Worker;
import uz.pdp.payload.ApiResponse;
import uz.pdp.payload.CompanyDto;
import uz.pdp.payload.LoginDto;
import uz.pdp.payload.TaskDto;
import uz.pdp.payload.TaskStatusDto;
import uz.pdp.payload.TurnketDto;
import uz.pdp.payload.WorkerDto;
import uz.pdp.service.WorkerService;


@RequestMapping("/")
@RestController
public class AutController {
	
	@Autowired
	WorkerService wService;
	
	@PostMapping(value = "company")
	public HttpEntity<?> addCompany(@Valid @RequestBody CompanyDto companyDto){
		ApiResponse apiResponse = wService.addCompany(companyDto);
		return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
	}
//	@PreAuthorize("hasAnyRole('DIREKTOR')")
//	@GetMapping(value = "company/{id}")
//	public HttpEntity<?> getCompanyById(@PathVariable Integer id) {
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		Worker worker = (Worker)auth.getPrincipal();
//		Set<Role> roles = worker.getRoles();
//		List<Role> roles2 = new ArrayList<>(roles);
//		for (Role role : roles2) {
//			String d = role.getAuthority();
//			if(role.getAuthority().equals("DIREKTOR")) {
//				ApiResponse apiResponse = wService.getCompany(id);
//				return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
//			}
//		}
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//	}
	
	@PreAuthorize("hasAnyRole('DIREKTOR')")
	@GetMapping(value = "company/{id}")
	public HttpEntity<?> getCompanyById(@PathVariable Integer id) {	
		ApiResponse apiResponse = wService.getCompany(id);
		return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
	}
	@PreAuthorize("hasAuthority('ROLE_DIREKTOR')")
	@PostMapping(value = "addManeger")
	public HttpEntity<?> addMeneger(@Valid @RequestBody WorkerDto workerDto){
		ApiResponse apiResponse = wService.addMeneger(workerDto);
		return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
	}
	@PreAuthorize("hasAnyRole('HR_MANAGER')")
	@PostMapping(value = "addWorker")
	public HttpEntity<?> addWorker(@Valid @RequestBody WorkerDto workerDto){
		ApiResponse apiResponse = wService.addWorker(workerDto);
		return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
	}
	@Transactional
	@PostMapping(value = "addDirektor")
	public HttpEntity<?> addDirektor(@Valid @RequestBody WorkerDto workerDto){
		ApiResponse apiResponse = wService.addDirektor(workerDto);
		return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
	}
	@GetMapping(value = "setPassword")
	public ModelAndView getHtml(){
		ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("/SetPassword.html");
	    return modelAndView;
	}
	@PostMapping(value = "login")
	public HttpEntity<?> savedPassword(@RequestParam String password,@RequestParam String passwordrepeat, @RequestParam String email){
		ApiResponse apiResponse =wService.getParam(password, passwordrepeat, email);
		return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
	}
	@PostMapping(value = "api/auth/login")
	public HttpEntity<?> register(@RequestBody LoginDto loginDto){
		ApiResponse apiResponse = wService.register(loginDto);
		return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
	}
	@PreAuthorize("hasAnyRole('HR_MANAGER','DIREKTOR')")
	@PostMapping(value = "addTask")
	public HttpEntity<?> addTask(@Valid @RequestBody TaskDto taskDto){
		ApiResponse apiResponse = wService.addTask(taskDto);
		return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
	}
	@PreAuthorize("hasAnyAuthority('ROLE_WORKER','ROLE_HR_MANAGER')")
	@PostMapping(value = "taskStatus")
	public HttpEntity<?> taskStuatus(@Valid @RequestBody TaskStatusDto taskDto){
		ApiResponse apiResponse = wService.taskStatus(taskDto);
		return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
	}
	@PreAuthorize("hasAnyRole('HR_MANAGER','DIREKTOR','WORKER')")
	@PostMapping(value = "turniketRegister")
	public HttpEntity<?> registerTurnkit(@Valid @RequestBody TurnketDto turnketDto){
		ApiResponse apiResponse = wService.registerTurnket(turnketDto);
		return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
	}
	@PreAuthorize("hasAnyRole('HR_MANAGER','DIREKTOR')")
	@GetMapping(value = "worker")
	public HttpEntity<?> getWorkers(){
		List<Worker> worker=wService.getWorkers();
		return ResponseEntity.status(worker.isEmpty()?HttpStatus.NO_CONTENT:HttpStatus.OK).body(worker);
	}
	@PreAuthorize("hasAnyRole('HR_MANAGER','DIREKTOR')")
	@GetMapping(value = "worker/{id}")
	public HttpEntity<?> getWorkersId(@PathVariable Integer id){
		List<Worker> worker=wService.getWorkersById(id);
		return ResponseEntity.status(worker.isEmpty()?HttpStatus.NO_CONTENT:HttpStatus.OK).body(worker);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(
	  MethodArgumentNotValidException ex) {
	    Map<String, String> errors = new HashMap<>();
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
	        String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        errors.put(fieldName, errorMessage);
	    });
	    return errors;
	}
	
}