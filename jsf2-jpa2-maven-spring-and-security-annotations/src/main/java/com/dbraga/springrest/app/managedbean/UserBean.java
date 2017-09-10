package com.dbraga.springrest.app.managedbean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedProperty;

import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.dbraga.springrest.app.dao.UserDAO;
import com.dbraga.springrest.app.domain.User;
import com.dbraga.springrest.app.domain.UserRole;
import com.dbraga.springrest.app.util.MsgType;
import com.dbraga.springrest.app.util.Util;


@Component
@Scope("view")
public class UserBean {
	
	@Resource
	private UserDAO userDAO;
	
	private List<User> list;
	private User user = new User();
	private String roleName;
	
	@ManagedProperty("#{param.modelId}")
	private Integer modelId;
	
	
	@PostConstruct
	private void init() {
		updateList();
	}
	
	public String save(){
		try {
			user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
			user.getUserRole().clear();
			user.getUserRole().add(new UserRole(user, roleName));
			
			if(user.getId() == null)
				userDAO.save(user);
			else
				userDAO.update(user);
			user = new User();
			updateList();
			Util.addFacesMessage("User salvo com sucesso", MsgType.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "user-list?faces-redirect=true";
	}
	
	public String edit(Integer modelId){
//		if(modelId != null)
//			user = userDAO.getUserWithRoles(modelId);
		return "user?faces-redirect=true&includeViewParams=true&modelId="+modelId;
	}
	
	public void handleDelete(User user){
		this.user = user;
	}

	public void delete(){
		
		try {
			userDAO.delete(this.user);
			updateList();
			Util.addFacesMessage("User removido com sucesso", MsgType.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void loadById() {
		if(modelId > 0){
			user = userDAO.getUserWithRoles(modelId);
			if(!user.getUserRole().isEmpty())				
				roleName = user.getUserRole().iterator().next().getRole();
		}
		else
			user = new User();
	}
	
	public List<User> getList() {
		return list;
	}

	public void updateList() {
		list = userDAO.all();
	}
	
	public String cancel(){
		return "user-list?faces-redirect=true";
	}
	
	
	public void setList(List<User> list) {
		this.list = list;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}


}
