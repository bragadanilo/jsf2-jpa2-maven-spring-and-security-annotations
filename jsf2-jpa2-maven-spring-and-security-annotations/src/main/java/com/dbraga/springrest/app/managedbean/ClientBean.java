package com.dbraga.springrest.app.managedbean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedProperty;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dbraga.springrest.app.dao.ClientDAO;
import com.dbraga.springrest.app.domain.Client;
import com.dbraga.springrest.app.util.MsgType;
import com.dbraga.springrest.app.util.Util;

@Component
@Scope("view")
public class ClientBean {
	
	@Resource
	private ClientDAO clientDao;
	
	private List<Client> list;
	private Client client = new Client();
	
	@ManagedProperty("#{param.modelId}")
	private Integer modelId;
	
	@PostConstruct
	private void init() {
		updateList();
	}
	
	public String save(){
		try {
				
			if(client.getId() == null)
				clientDao.save(client);
			else
				clientDao.update(client);
			client = new Client();
			updateList();
			Util.addFacesMessage("Cliente salvo com sucesso", MsgType.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "client-list?faces-redirect=true";
	}
	
	public String edit(Integer modelId){
		if(modelId != null)
			client = clientDao.searchById(modelId);
		return "client?faces-redirect=true&includeViewParams=true&modelId="+modelId;
	}
	
	public void handleDelete(Client client){
		this.client = client;
	}

	public void delete(){
		
		try {
			clientDao.delete(this.client);
			updateList();
			Util.addFacesMessage("Cliente removido com sucesso", MsgType.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void loadById() {
		if(modelId > 0)
			client = clientDao.searchById(modelId);
		else
			client = new Client();
	}
	
	public List<Client> getList() {
		return list;
	}

	public void updateList() {
		list = clientDao.all();
	}
	
	public String cancel(){
		return "client-list?faces-redirect=true";
	}
	
	
	public void setList(List<Client> list) {
		this.list = list;
	}
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}

	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}


}
