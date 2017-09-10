package com.dbraga.springrest.app.converter;

import java.io.Serializable;
import java.util.List;

import javax.faces.convert.FacesConverter;

import com.dbraga.springrest.app.domain.Professional;


@FacesConverter(value = "professionalConverter")
public class ProfessionalConverter extends ConverterTemplate<Professional> implements Serializable{
	private static final long serialVersionUID = -765417588078973825L;

	public ProfessionalConverter(List<Professional> list) {
		super(list);
	}

	@Override
	protected String getName(Object value) {
		if(value != null && value instanceof Professional){
			Professional customer = (Professional) value;			
			return customer.toString();
		}
		return null;
	}
}