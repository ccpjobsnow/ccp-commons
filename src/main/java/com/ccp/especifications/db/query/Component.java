package com.ccp.especifications.db.query;

import com.ccp.decorators.CcpMapDecorator;
import com.google.gson.GsonBuilder;

abstract class Component {

	public CcpMapDecorator values = new CcpMapDecorator();
	protected Component parent;
	protected String name;
	
	Component(Component parent, String name) {
		this.parent = parent;
		this.name = name;
	}


	 protected abstract <T extends Component> T getInstanceCopy() ;
	
	 Object getValue() {
		return this.values.content;
	}
	 
	 @SuppressWarnings("unchecked")
	<T extends Component> T addChild(Component child) {

		 Component instanceCopy = this.copy();
		 
		 Object value = child.getValue();
		 
		 instanceCopy.values = instanceCopy.values.put(child.name, value);
		 
		 return (T)instanceCopy;
	 }

	 @SuppressWarnings("unchecked")
	 protected <T extends Component> T copy() {
		Component instanceCopy = this.getInstanceCopy();
		 
		 instanceCopy.name = this.name;

		 if(this.parent != null) {
			 instanceCopy.parent = this.parent.copy();
		 }
		 
		 instanceCopy.values = this.values.copy();
		
		 return (T)instanceCopy;
	}
	 
	 @Override
	public final String toString() {
		 Object value = this.getValue();
		String json = new GsonBuilder().setPrettyPrinting().create().toJson(value);
		return json;
	}
	 
	public boolean hasChildreen() {
		return this.values.content.isEmpty() == false;
	} 
	
	public <T extends Component> T putProperty(String propertyName, Object propertyValue){
		T clone = this.copy();
		clone.values = clone.values.put(propertyName, propertyValue);
		return clone;

	}
}
