package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.collabnet.ccf.ccfmaster.server.core.Persister;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMapping;

public class MockFieldMappingPersisterFactory implements FieldMappingPersisterFactory {
	public boolean calledSave = false;
	
	class MockFieldMappingPersister implements Persister<FieldMapping> {

		@Override
		public void save(FieldMapping cfg) {
			calledSave = true;
			log.debug("called save({})", cfg);
		}

		@Override
		public void delete(FieldMapping cfg) {
			throw new UnsupportedOperationException();
		}
	}

	private static final Logger log = LoggerFactory.getLogger(MockFieldMappingPersisterFactory.class);
	
	@Override
	public MockFieldMappingPersister get(ConversionResult conversionResult) {
		return new MockFieldMappingPersister();
	}

}
