package org.apache.thrift.registry;

import org.apache.thrift.protocol.TProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory class for generating registry client
 * that can be used for querying lookup registry.
 *   
 * @author punit
 *
 */
public final class TRegistryClientFactory {
	
	/**
	 * Logger
	 */
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
	
	
	/**
	 * private constructor
	 */
	private TRegistryClientFactory() {}
	
	
	/**
	 * Method for generating registry client for same input and output protocol
	 * @param protocol
	 * @return TRegistry
	 */
	public static TRegistry getClient(TProtocol prot) {
		return new TRegistryClient(prot);
	}

	
	/**
	 *  Method for generating registry client for different input and output protocol
	 * @param inputProtocol
	 * @param outputProtocol
	 * @return TRegistry
	 */
	public static TRegistry getClient(TProtocol iprot,	TProtocol oprot) {
		return new TRegistryClient(iprot, oprot);
	}
	
	
}
