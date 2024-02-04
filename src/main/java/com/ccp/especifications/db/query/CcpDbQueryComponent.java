package com.ccp.especifications.db.query;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.json.CcpJsonHandler;

abstract class CcpDbQueryComponent {

	public CcpJsonRepresentation values = CcpConstants.EMPTY_JSON;
	protected CcpDbQueryComponent parent;
	protected String name;
	
	CcpDbQueryComponent(CcpDbQueryComponent parent, String name) {
		this.parent = parent;
		this.name = name;
	}


	 protected abstract <T extends CcpDbQueryComponent> T getInstanceCopy() ;
	
	 Object getValue() {
		return this.values.content;
	}
	 
	 @SuppressWarnings("unchecked")
	<T extends CcpDbQueryComponent> T addChild(CcpDbQueryComponent child) {

		 CcpDbQueryComponent instanceCopy = this.copy();
		 
		 Object value = child.getValue();
		 
		 instanceCopy.values = instanceCopy.values.put(child.name, value);
		 
		 return (T)instanceCopy;
	 }

	 @SuppressWarnings("unchecked")
	 protected <T extends CcpDbQueryComponent> T copy() {
		CcpDbQueryComponent instanceCopy = this.getInstanceCopy();
		 
		 instanceCopy.name = this.name;

		 if(this.parent != null) {
			 instanceCopy.parent = this.parent.copy();
		 }
		 
		 instanceCopy.values = this.values.copy();
		
		 return (T)instanceCopy;
	}
	 
	 
	public final String toString() {
		 Object value = this.getValue();

		 CcpJsonHandler json = CcpDependencyInjection.getDependency(CcpJsonHandler.class);

		 String _json = json.toJson(value);
		return _json;
	}
	 
	public boolean hasChildreen() {
		return this.values.content.isEmpty() == false;
	} 
	
	public <T extends CcpDbQueryComponent> T putProperty(String propertyName, Object propertyValue){
		T clone = this.copy();
		clone.values = clone.values.put(propertyName, propertyValue);
		return clone;

	}
}
