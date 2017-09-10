package com.dbraga.springrest.app.converter;

import java.io.Serializable;
import java.util.List;

import javax.faces.convert.FacesConverter;

import com.dbraga.springrest.app.util.EventColor;


@FacesConverter(value = "eventColorConverter")
public class EventColorConverter extends ConverterTemplate<EventColor> implements Serializable{
	private static final long serialVersionUID = -765417588078973825L;

	public EventColorConverter(List<EventColor> list) {
		super(list);
	}

	@Override
	protected String getName(Object value) {
		if(value != null && value instanceof EventColor){
			EventColor customer = (EventColor) value;			
			return customer.toString();
		}
		return null;
	}
}