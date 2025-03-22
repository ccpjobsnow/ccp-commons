package com.ccp.especifications.db.utils;

import com.ccp.decorators.CcpJsonRepresentation;

public enum CcpEntityCrudOperationType 
//implements CcpHandleWithSearchResultsInTheEntity<List<CcpBulkItem>>
{
	save {
		CcpJsonRepresentation execute(CcpEntity entity, CcpJsonRepresentation json) {
			CcpJsonRepresentation result = entity.createOrUpdate(json);
			return result;
		}

		
	},
	delete {

		CcpJsonRepresentation execute(CcpEntity entity, CcpJsonRepresentation json) {
			CcpJsonRepresentation result = entity.delete(json);
			return result;
		}

		
	};

	abstract CcpJsonRepresentation execute(CcpEntity entity, CcpJsonRepresentation json);
}
