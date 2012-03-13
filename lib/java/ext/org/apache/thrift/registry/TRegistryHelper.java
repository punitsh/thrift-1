package org.apache.thrift.registry;

/**
 * Helper interface that will facilitate server instance to perform
 * bind, unbinding and rebinding of context.
 * 
 * @author punit
 *
 */
public interface TRegistryHelper {
	
	/**
	 * Method for binding context to underlying registry
	 * @param context
	 * @return true if binding a context for the first time else false
	 * @throws InvalidInputException
	 */
	public boolean bind(URIContext context) throws InvalidInputException;

	/**
	 * Method for updating/changing the existing service information against a context within the underlying registry
	 * @param context
	 * @return true if existing context is updated successfully else false
	 * @throws NotFoundException when context is not available with the registry
	 * @throws InvalidInputException
	 */
	public boolean rebind(URIContext context) throws NotFoundException,InvalidInputException;

	/**
	 * Method for removing a context from underlying registry
	 * @param context
	 * @return true when context is removed successfully else false
	 * @throws NotFoundException when context is not available with the registry
	 */
	public boolean unbind(String context) throws NotFoundException;
	
	/**
	 * Method for removing a context from underlying registry
	 * @param context
	 * @return true when context is removed successfully else false
	 * @throws NotFoundException when context is not available with the registry
	 */
	public boolean unbind(URIContext context) throws NotFoundException;
	
	
	
	/**
	 * Method for removing all contexts from underlying registry
	 * @param context
	 * @return true when all contexts are removed successfully else false
	 * 
	 */
	public boolean unbindAll();
	

}

