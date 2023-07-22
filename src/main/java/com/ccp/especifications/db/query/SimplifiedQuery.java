package com.ccp.especifications.db.query;
   
import java.util.Map;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.db.utils.CcpDbTableField;

public final class SimplifiedQuery  extends BooleanOperator {

	SimplifiedQuery(Component parent) {  
		super(parent, "query");
	}

	@Override
	@SuppressWarnings("unchecked")
	public SimplifiedQuery prefix(CcpDbTableField field, Object value) {
		return super.prefix(field, value);
	}

	public ElasticQuery endSimplifiedQueryAndBackToRequest() {
		return this.parent.addChild(this);
	}
	@SuppressWarnings("unchecked")
	protected <T extends Component> T getInstanceCopy() {
		return (T)new SimplifiedQuery(this.parent);
	}
	
	Object getValue() {
		return this.values.content;
	}
	 @SuppressWarnings("unchecked")
	 protected SimplifiedQuery copy() {
		SimplifiedQuery instanceCopy = this.getInstanceCopy();
		 
		 instanceCopy.name = this.name;

		 instanceCopy.parent = this.parent.copy();
		 
		 instanceCopy.values = this.values.copy();
		
		 return instanceCopy;
	}
	
	 @SuppressWarnings("unchecked")
	SimplifiedQuery addChild(Component child) {

		 SimplifiedQuery instanceCopy = this.copy();
		 
		 Object value = child.getValue();
		 
		 instanceCopy.values = instanceCopy.values.put(child.name, value);
		 
		 return instanceCopy;
	 }
		@Override
		@SuppressWarnings("unchecked")
		public SimplifiedQuery matchPhrase(CcpDbTableField field, Object value) {
			return super.matchPhrase(field, value);
		}

		@Override
		@SuppressWarnings("unchecked")
		public SimplifiedQuery term(CcpDbTableField field, Object value) {
			return super.term(field, value);
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public SimplifiedQuery match(CcpDbTableField field, Object value) {
			return super.match(field, value);
		}
		@SuppressWarnings("unchecked")
		public SimplifiedQuery exists(String field) {
			return super.exists(field);
		}
		
		@SuppressWarnings("unchecked")
		protected SimplifiedQuery addCondition(String field, Object value, String key) {
			Map<String, Object> map = new CcpMapDecorator().put(field, value).getContent();
			Map<String, Object> outerMap = new CcpMapDecorator().put(key, map).getContent();
			
			SimplifiedQuery clone = this.copy();
			clone.values = new CcpMapDecorator(outerMap);
			return clone;
		}
		
		@Override
		public boolean hasChildreen() {
			return this.values.content.isEmpty() == false;
		}


}
