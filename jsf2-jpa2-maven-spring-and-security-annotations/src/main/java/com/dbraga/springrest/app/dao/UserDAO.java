package com.dbraga.springrest.app.dao;

import com.dbraga.springrest.app.domain.User;

public interface UserDAO extends GenericDAO<User, Integer>{

	User findByUserName(String username);

	User getUserWithRoles(Integer modelId);

}