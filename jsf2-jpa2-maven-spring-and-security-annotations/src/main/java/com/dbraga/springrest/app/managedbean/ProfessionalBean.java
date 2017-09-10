package com.dbraga.springrest.app.managedbean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedProperty;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dbraga.springrest.app.converter.EventColorConverter;
import com.dbraga.springrest.app.dao.ProfessionalDAO;
import com.dbraga.springrest.app.domain.Professional;
import com.dbraga.springrest.app.util.EventColor;
import com.dbraga.springrest.app.util.EventColor.EventColorService;
import com.dbraga.springrest.app.util.MsgType;
import com.dbraga.springrest.app.util.Util;


@Component
@Scope("view")
public class ProfessionalBean {
	
	@Resource
	private ProfessionalDAO professionalDao;
	
	private List<Professional> list;
	private Professional professional = new Professional();
	
	@ManagedProperty("#{param.modelId}")
	private Integer modelId;
	
	private EventColor eventColor = new EventColor();   
    private List<EventColor> eventColorList;
    private EventColorConverter eventColorConverter;
    
	
	@PostConstruct
	private void init() {
		updateList();
		
		eventColorList = EventColorService.getEventColorList();
		eventColorConverter = new EventColorConverter(eventColorList);
	}
	
	public String save(){
		try {
			professional.setEventColor(eventColor.getName());
			if(professional.getId() == null)
				professionalDao.save(professional);
			else
				professionalDao.update(professional);
			professional = new Professional();
			updateList();
			Util.addFacesMessage("Profissional salvo com sucesso", MsgType.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "professional-list?faces-redirect=true";
	}
	
	public String edit(Integer modelId){
		if(modelId != null)
			professional = professionalDao.searchById(modelId);
		return "professional?faces-redirect=true&includeViewParams=true&modelId="+modelId;
	}
	
	public void handleDelete(Professional Professional){
		this.professional = Professional;
	}

	public void delete(){
		
		try {
			professionalDao.delete(this.professional);
			updateList();
			Util.addFacesMessage("Professional removido com sucesso", MsgType.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void loadById() {
		if(modelId > 0){
			professional = professionalDao.searchById(modelId);
			eventColor = EventColorService.getEventColorByName(professional.getEventColor());
		}
		else
			professional = new Professional();
	}
	
	public List<Professional> getList() {
		return list;
	}

	public void updateList() {
		list = professionalDao.all();
	}
	
	public String cancel(){
		return "professional-list?faces-redirect=true";
	}
	
	
	public void setList(List<Professional> list) {
		this.list = list;
	}
	public Professional getProfessional() {
		return professional;
	}
	public void setProfessional(Professional Professional) {
		this.professional = Professional;
	}

	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	public EventColor getEventColor() {
		return eventColor;
	}

	public void setEventColor(EventColor eventColor) {
		this.eventColor = eventColor;
	}

	public List<EventColor> getEventColorList() {
		return eventColorList;
	}

	public void setEventColorList(List<EventColor> eventColorList) {
		this.eventColorList = eventColorList;
	}

	public EventColorConverter getEventColorConverter() {
		return eventColorConverter;
	}

	public void setEventColorConverter(EventColorConverter eventColorConverter) {
		this.eventColorConverter = eventColorConverter;
	}

	
}


