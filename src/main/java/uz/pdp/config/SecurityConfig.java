package uz.pdp.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import uz.pdp.security.JwtFiler;
import uz.pdp.service.WorkerService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	WorkerService workerService;
	
	@Autowired
	JwtFiler jwtFiler;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(workerService).passwordEncoder(passwordEncoder());
	}
	
	
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManagerBean();
	}



	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.
				csrf().disable()
				.httpBasic().disable()
				.authorizeRequests()
				.antMatchers("/company","/addDirektor","/setPassword", 
						"/login", "/api/auth/login").permitAll()
				.anyRequest().authenticated();
		http.addFilterBefore(jwtFiler, UsernamePasswordAuthenticationFilter.class);
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailsender = new JavaMailSenderImpl();
		mailsender.setHost("smtp.gmail.com");
		mailsender.setPort(465);
		mailsender.setUsername("");
		mailsender.setPassword("");
		Properties properties=mailsender.getJavaMailProperties();
		properties.put("mail.transport.protocol","smtp");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.ssl.enable", "true");
//		System.setProperty("java.net.preferIPv4Stack" , "true");
//		properties.put("mail.smtp.starttls.required", "false");
		properties.put("mail.debug", "true");
		return  mailsender;
	}	 
}
