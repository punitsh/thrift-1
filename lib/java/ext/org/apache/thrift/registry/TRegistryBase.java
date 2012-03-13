package org.apache.thrift.registry;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic implementation of registry that performs in memory management of service context.
 * @author punit
 *
 */
public class TRegistryBase implements TRegistry, TRegistryHelper {


	/**
	 * Logger
	 */
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
	
	/**
	 * registry map
	 */
	protected final Map<String, URIContext> registry = new ConcurrentHashMap<String, URIContext>();
	
	
	/**
	 * @see org.apache.thrift.registry.TRegistry#isExist(String)
	 */
	public boolean isExist(String context) throws TException {
		return registry.containsKey(context);
	}

	
	/**
	 * @see org.apache.thrift.registry.TRegistry#lookup(String)
	 */
	public URIContext lookup(String context) throws TException {
		return registry.get(context);
		
	}

	
	/**
	 * @see org.apache.thrift.registry.TRegistry#regexlookup(String)
	 */
	public Set<URIContext> regexlookup(String regex) throws TException {
		
		Set<URIContext> contexts = new HashSet<URIContext>(); 
		
		Set<String> keys = registry.keySet();
		Pattern pattern = Pattern.compile(regex);
		
		for(String key : keys){
			Matcher matcher = pattern.matcher(key);
			if (matcher.matches())   contexts.add(registry.get(key));
	    }
			
		return contexts;
	}

	
	/**
	 * @see org.apache.thrift.registry.TRegistry#lookupByName(String)
	 */
	public Set<URIContext> lookupByName(String servicename)	throws  TException {
		
		Set<URIContext> contexts = new HashSet<URIContext>(); 
		
		Collection<URIContext> values = registry.values();
		
		for(URIContext context : values){
			if(context.getServiceName().equals(servicename)){
				contexts.add(context);
			}
	    }
		
		return contexts;
	}

	
	/**
	 * @see org.apache.thrift.registry.TRegistry#regexlookupByName(String)
	 */
	public Set<URIContext> regexlookupByName(String regex) throws  TException {
		
		Set<URIContext> contexts = new HashSet<URIContext>(); 
		
		Collection<URIContext> values = registry.values();
		Pattern pattern = Pattern.compile(regex);
		
		for(URIContext context : values){
			
			Matcher matcher = pattern.matcher(context.getServiceName());
			if (matcher.matches())   contexts.add(context);
			
	    }
			
		return contexts;
	}
	

	/**
	 * @see org.apache.thrift.registry.TRegistry#listAll()
	 */
	public Set<URIContext> listAll() throws TException {
		return new HashSet<URIContext>(registry.values()); 
	}

	
	/**
	 * @see org.apache.thrift.registry.TRegistry.Helper#bind(URIContext)
	 */
	public boolean bind(URIContext uricontext) throws InvalidInputException {
		
		return bind(uricontext,false);
	}
	
	
	/**
	 * Method for binding and rebinding a context
	 * @param uricontext
	 * @param isRebind
	 * @return true/false
	 * @throws InvalidInputException
	 */
	private boolean bind(URIContext uricontext, boolean isRebind) throws InvalidInputException {
		
		if(uricontext==null
				|| uricontext.getContext()==null
				|| uricontext.getContext().trim().length()==0)
				throw new InvalidInputException(uricontext.toString(), "INVALID INPUT");
		
		if(!isRebind && !registry.containsKey(uricontext.getContext().trim())){
			registry.put(uricontext.getContext().trim(),uricontext);
			return true;
		} else {		
			return false;
		}	
	}

	
	/**
	 * @see org.apache.thrift.registry.TRegistry.Helper#rebind(URIContext)
	 */
	public boolean rebind(URIContext uricontext) throws NotFoundException, InvalidInputException {
		
		if(!registry.containsKey(uricontext.getContext().trim()))
				throw new NotFoundException(uricontext.toString(), "NOT EXISTING");
		
		return bind(uricontext, true);
		
	}

	/**
	 * @see org.apache.thrift.registry.TRegistry.Helper#unbind(URIContext)
	 */
	public boolean unbind(URIContext uricontext) throws NotFoundException {
		
		if(uricontext==null
				|| !registry.containsKey(uricontext.getContext().trim()))
				throw new NotFoundException(uricontext.toString(), "NOT EXISTING");
		
		return unbind(uricontext.getContext());
	}

	
	/**
	 * @see org.apache.thrift.registry.TRegistry.Helper#unbind(String)
	 */
	public boolean unbind(String context) throws NotFoundException {

		if(context==null
			|| context.trim().length()==0
			|| !registry.containsKey(context.trim()))
				throw new NotFoundException(context.trim(), "NOT EXISTING");
	
		registry.remove(context.trim());
		return true;
	}


	/**
	 * @see org.apache.thrift.registry.TRegistry.Helper#unbindAll()
	 */
	public boolean unbindAll() {
		
		try{
			
			registry.clear();
			return true;
			
		} catch (Exception e) {
			LOGGER.error("Not able to unbind all entries from registry.", e);
		}
		return false;
	}

}
