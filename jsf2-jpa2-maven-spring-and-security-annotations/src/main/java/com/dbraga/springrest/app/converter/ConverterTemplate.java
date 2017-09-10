package com.dbraga.springrest.app.converter;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public abstract class ConverterTemplate<T> implements Converter, Serializable {
	private static final long serialVersionUID = 4784866636691223848L;
	private List<T> list;
	
	public ConverterTemplate(List<T> list) {
		this.list = list;
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if(value!=null){
			Iterator<T> iterator = list.iterator();
			while(iterator.hasNext()){
				T objValue = iterator.next();
				String name = getName(objValue);
				if(value.equals(name)){
					return objValue;
				}
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String ret = "";
		if (value != null) {
			ret = getName(value);
		}
		return ret;
	}
	
	public List<T> getList() {
		return list;
	}
	
	abstract protected String getName(Object value);
}