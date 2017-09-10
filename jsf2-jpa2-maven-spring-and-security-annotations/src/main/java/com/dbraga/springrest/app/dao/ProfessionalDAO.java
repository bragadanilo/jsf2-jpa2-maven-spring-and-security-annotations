package com.dbraga.springrest.app.dao;

import java.util.List;

import com.dbraga.springrest.app.domain.Professional;

public interface ProfessionalDAO extends GenericDAO<Professional, Integer> {
	public List<Professional> getAll();
}
