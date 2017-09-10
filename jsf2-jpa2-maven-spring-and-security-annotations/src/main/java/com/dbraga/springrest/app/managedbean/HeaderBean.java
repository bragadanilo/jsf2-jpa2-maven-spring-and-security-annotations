package com.dbraga.springrest.app.managedbean;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
@Scope("session")
public class HeaderBean {
	
	org.springframework.security.core.userdetails.User principal;
	
	@PostConstruct
	public void init(){
		principal = (org.springframework.security.core.userdetails.User) 
			SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	public String getLoggedUser() {
		return principal.getUsername();
	}
	
	@SuppressWarnings("unchecked")
	public boolean hasRole(String role) {
		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) SecurityContextHolder.getContext()
				.getAuthentication().getAuthorities();
		boolean hasRole = false;
		for (GrantedAuthority authority : authorities) {
			hasRole = authority.getAuthority().equals(role);
			if (hasRole) {
				break;
			}
		}
		return hasRole;
	}

}
