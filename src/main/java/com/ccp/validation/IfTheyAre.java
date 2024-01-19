package com.ccp.validation;

import com.ccp.decorators.CcpJsonRepresentation;

public class IfTheyAre {
	public final CcpJsonRepresentation content;
	public final String[] fields;
	public IfTheyAre(CcpJsonRepresentation content, String[] fields) {
		this.content = content;
		this.fields = fields;
	}
	public boolean textsSoEachOneIsContainedAtTheList(String ...args) {
		return false;
	}
	public RangeSize textsSoEachOneHasTheSizeThatIs() {
		return new RangeSize(this.content, this.fields);
	}
	public RangeSize numbersSoEachOneIs() {
		return new RangeSize(this.content, this.fields);
	}
	public boolean numbersSoEachOneIsContainedAtTheList(int...args) {
		// TODO Auto-generated method stub
		return false;
	}

}
