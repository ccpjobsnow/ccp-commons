package com.ccp.especifications.db.utils;

import com.ccp.decorators.CcpJsonRepresentation;

public enum CcpEntityCrudOperationType 
//implements CcpHandleWithSearchResultsInTheEntity<List<CcpBulkItem>>
{
	save {
		public CcpJsonRepresentation execute(CcpEntity entity, CcpJsonRepresentation json) {
			entity.createOrUpdate(json);
			return json;
		}

		
	},
	delete {

		public CcpJsonRepresentation execute(CcpEntity entity, CcpJsonRepresentation json) {
			entity.delete(json);
			return json;
		}

		
	};

	public abstract CcpJsonRepresentation execute(CcpEntity entity, CcpJsonRepresentation json);
}
