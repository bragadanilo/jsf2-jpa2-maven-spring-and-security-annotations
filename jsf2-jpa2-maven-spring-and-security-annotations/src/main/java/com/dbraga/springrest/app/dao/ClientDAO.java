package com.dbraga.springrest.app.dao;

import java.util.List;

import com.dbraga.springrest.app.domain.Client;

public interface ClientDAO extends GenericDAO<Client, Integer> {
	public List<Client> getAll();
}
