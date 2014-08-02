package de.oglimmer.lunchy.rest;

import org.dozer.DozerBeanMapper;

public enum BeanMappingProvider {
	INSTANCE;

	private DozerBeanMapper dbm = new DozerBeanMapper();

	public DozerBeanMapper getMapper() {
		return dbm;
	}

}
