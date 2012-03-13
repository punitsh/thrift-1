package org.apache.thrift.registry;

import java.util.Set;

/**
 * Registry interface that provides basic lookup methods
 * to locate service context.
 * 
 * @author punit
 *
 */
public interface TRegistry {
	
		/**
	     * Method to check if the desired context is existing in the server registry or not.
	     * This method returns true if the context exist else return false.
	     * @param context
	     * @return boolean
	     * @throws org.apache.thrift.TException
	     */
	    public boolean isExist(String context) throws org.apache.thrift.TException;

	    /**
	     * Basic registry lookup method for getting desired service context instance from registry.
	     * null value will be returned if the desired context does not exist.
	     * @param context
	     * @return URIContext
	     * @throws NotFoundException when context is not registered with registry
	     * @throws org.apache.thrift.TException
	     */
	    public URIContext lookup(String context) throws org.apache.thrift.TException;

	    /**
	     * Registry lookup method for getting desired service context instance(s) from registry 
	     * after matching available contexts with the provided regular expression argument.
	     * Empty set will be returned if the desired context does not exist. 
	     * @param regex[regular expression]
	     * @return Set<URICOntext>
	     * @throws NotFoundException when no matching context is available with the registry
	     * @throws org.apache.thrift.TException
	     */
	    public Set<URIContext> regexlookup(String regex) throws org.apache.thrift.TException;

	    /**
	     * Basic registry lookup method for getting desired service context instance from registry based 
	     * on the serviceName provided as argument.Empty set will be returned if the desired context does not exist.
	     * @param servicename
	     * @return Set<URIContext>
	     * @throws NotFoundException when no service with the desired service name is available
	     * @throws org.apache.thrift.TException
	     */
	    public Set<URIContext> lookupByName(String servicename) throws org.apache.thrift.TException;

	    /**
	     * Registry lookup method for getting desired service context instance(s) from registry 
	     * after matching available service name with the provided regular expression argument.
	     * Empty set will be returned if the desired context does not exist. 
	     * @param regex
	     * @return
	     * @throws NotFoundException
	     * @throws org.apache.thrift.TException
	     */
	    public Set<URIContext> regexlookupByName(String regex) throws org.apache.thrift.TException;

	    /**
	     * Method for getting all registered context with the registry.
	     * Empty set will be returned if no context exist.
	     * @return
	     * @throws org.apache.thrift.TException
	     */
	    public Set<URIContext> listAll() throws org.apache.thrift.TException;
	    
	    
	    
	    
}
