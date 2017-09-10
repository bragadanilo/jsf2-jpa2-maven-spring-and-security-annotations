package com.dbraga.springrest.app.converter;

import java.io.Serializable;
import java.util.List;

import javax.faces.convert.FacesConverter;

import com.dbraga.springrest.app.converter.ConverterTemplate;
import com.dbraga.springrest.app.domain.Client;


@FacesConverter(value = "clientConverter")
public class ClientConverter extends ConverterTemplate<Client> implements Serializable{
	private static final long serialVersionUID = -765417588078973825L;

	public ClientConverter(List<Client> list) {
		super(list);
	}

	@Override
	protected String getName(Object value) {
		if(value != null && value instanceof Client){
			Client customer = (Client) value;			
			return customer.toString();
		}
		return null;
	}
}