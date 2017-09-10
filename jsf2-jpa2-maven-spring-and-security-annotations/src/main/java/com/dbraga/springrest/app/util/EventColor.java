package com.dbraga.springrest.app.util;

import java.util.ArrayList;
import java.util.List;

public class EventColor {
	private int id;    
    private String displayName; 
    private String name;
     
    public EventColor() {}
 
    public EventColor(int id, String displayName, String name) {
        this.id = id;
        this.displayName = displayName;
        this.name = name;
    }
 
    public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
 
    public String getDisplayName() {
        return displayName;
    }
 
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
     
    @Override
    public String toString() {
        return name;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventColor other = (EventColor) obj;
		if (id != other.id)
			return false;
		return true;
	}


public static class EventColorService{
	
	private static List<EventColor> eventColorList = new ArrayList<EventColor>();
	
	static{
		eventColorList.add(new EventColor(0, "Vermelho", "eventRed"));
		eventColorList.add(new EventColor(1, "Verm. claro", "eventSoftRed"));
		eventColorList.add(new EventColor(2, "Azul", "eventBlue"));
		eventColorList.add(new EventColor(3, "Azul claro", "eventSoftBlue"));
		eventColorList.add(new EventColor(4, "Verde", "eventGreen"));
		eventColorList.add(new EventColor(5, "Verde claro", "eventSoftGreen"));
	}
	
	
	public static List<EventColor> getEventColorList(){
		return eventColorList;
	}
	
	public static EventColor getEventColorByName(String name){
		for (EventColor eventColor : eventColorList) {
			if(eventColor.getName().equals(name))
				return eventColor;
		}
		return null;
	}
}

}