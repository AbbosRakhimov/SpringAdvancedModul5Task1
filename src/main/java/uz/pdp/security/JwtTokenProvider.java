package uz.pdp.security;

import java.util.Date;
import java.util.Set;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import uz.pdp.entity.Role;

@Component
public class JwtTokenProvider {

	private static final long expireDate=1000*2*60*24;
	private static final String secretKey="Maxfiysozkalit";
	
	public String genereToken(String username, Set<Role>roles) {
		Date date = new Date(System.currentTimeMillis()+expireDate);
		String token=Jwts.
				builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(date)
				.claim("roles", roles)
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();
		return token;
	}
	public String getUsernameFromToken(String token) {
		try {
			String email= Jwts.
					parser()
					.setSigningKey(secretKey)
					.parseClaimsJws(token)
					.getBody()
					.getSubject();
//					.get("roles", Role.class)
			return email;
		} catch (Exception e) {
			return null;
		}
				
	}
}
