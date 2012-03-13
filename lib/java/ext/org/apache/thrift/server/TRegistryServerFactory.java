package org.apache.thrift.server;

import java.util.List;

import org.apache.thrift.TProcessor;
import org.apache.thrift.registry.InvalidInputException;
import org.apache.thrift.registry.TRegistry;
import org.apache.thrift.registry.TRegistryBase;
import org.apache.thrift.registry.TRegistryHelper;
import org.apache.thrift.registry.TRegistryService;
import org.apache.thrift.registry.URIContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory class for generating registry service(processor)
 * for basic/customized implementation.
 * 
 * @author punit
 *
 */
 final class TRegistryServerFactory{
	
	/**
	 * Logger
	 */
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
	
	
	/**
	 * implementation of registryHelper
	 */
	private TRegistryHelper helperImpl =  null;
	
	/**
	 * Registry service(processor) instance
	 */
	private TRegistryService<TRegistry> registryService = null;
	
	
	
	
	private TRegistryServerFactory(TRegistry registryImpl, TRegistryHelper helperImpl) {
		super();
		this.helperImpl = helperImpl;
		this.registryService = new TRegistryService<TRegistry>(registryImpl);
	}


	/**
	 * Method for generating factory instance for specific implementation of registry and registryHelper interface
	 * @param registryImpl
	 * @return TRegistryFactory
	 */
	public static TRegistryServerFactory createFactory(TRegistry registryImpl, TRegistryHelper helperImpl){
		TRegistryServerFactory factory = new TRegistryServerFactory(registryImpl,helperImpl);
		return factory; 
	}
	
	
	/**
	 * Method for generating factory instance for default implementation of registry interface
	 * @param registryImpl
	 * @return TRegistryFactory
	 */
	public static TRegistryServerFactory createFactory(){
		TRegistryBase baseImpl = new TRegistryBase();
		return createFactory(baseImpl,baseImpl); 
	}
	
	
	/**
	 * Method for getting service(processor) for underlying registry implementation 
	 * @return TProcessor
	 */
	public TProcessor getService(){
		
		return registryService;
	}
	
	/**
	 * Method for getting helper instance for underlying TRegistryHelper implementation 
	 * @return TProcessor
	 */
	public TRegistryHelper getHelper(){
		
		return helperImpl;
	}
	
	
    /**
     * Method for getting service(processor) after registering the contexts with underlying registry instance. 
     * @param contexts
     * @return TProcessor
     */
	public TProcessor getService(List<URIContext> contexts){
		
		for(URIContext context : contexts){
			try {
				helperImpl.bind(context);
			} catch (InvalidInputException e) {
				LOGGER.error("ERROR WHILE INITIALISING SERVER. Not able to register context."+e);
			}
		}
		return registryService;
	}
	
}

