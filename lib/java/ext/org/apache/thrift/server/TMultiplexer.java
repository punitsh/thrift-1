package org.apache.thrift.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TMessage;
import org.apache.thrift.protocol.TMessageType;
import org.apache.thrift.protocol.TMultiplexProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.registry.InvalidInputException;
import org.apache.thrift.registry.URIContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This processor class provides support for hosting multiple services on a server. 
 * It acts as a server side request broker and is responsible for identifying the 
 * service that client has requested based on the service context propagated by client.
 * Any client that has to communicate with Tmultiplexer should wrap the underlying 
 * protocol by TmultiplexProtocol instance and provide the appropriate service context.
 * 
 * @author punit
 *
 */
 class TMultiplexer implements TProcessor {
		
		/**
		 * Logger
		 */
		protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
		
		
		/**
		 * Map storing processor instances against service context.
		 */
	    protected final Map<String,TProcessor> SERVICE_PROCESSOR_MAP = new ConcurrentHashMap<String,TProcessor>();
	
	    /**
	     * Method for registering a service(processor) against a context with multiplexer. 
	     * @param uricontext
	     * @param processor
	     * @throws InvalidInputException
	     */
	    public void registerProcessor(URIContext uricontext,TProcessor processor) throws InvalidInputException {
	        SERVICE_PROCESSOR_MAP.put(uricontext.getContext().trim(), processor);
	    }
	    
	    
	    
	    /**
	     * Method performs the desired request brocking task by reading the service context 
	     * from the underlying protocol and based on the mapping it directs the request to 
	     * the appropriate service instance.
	     */
	    public boolean process(TProtocol iprot, TProtocol oprot) throws TException {
	       
	        TMessage message = iprot.readMessageBegin();
	
	        if (message.type != TMessageType.CALL && message.type != TMessageType.ONEWAY) {
	              throw new TException("INCOMPATIBLE MESSAGE TYPE : "+ message.type);
	        }
	
	        // Extract the service name
	        int index = message.name.indexOf(TMultiplexProtocol.SEPARATOR);
	        if (index < 0) {
	            throw new TException("SERVICE CONTEXT NOT FOUND IN MESSAGE : "+message.name+". Use TMultiplexProtocol in your client.");
	        }
	
	        // Create a new TMessage, something that can be consumed by any TProtocol
	        String serviceName = message.name.substring(0, index);
	        TProcessor actualProcessor = SERVICE_PROCESSOR_MAP.get(serviceName);
	        if (actualProcessor == null) {
	            throw new TException("SERVICE NOT FOUNT: " + serviceName + ".  Use TMultiplexer to registerProcessor.");
	        }
	
	        // Create a new TMessage, removing the service name
	        TMessage standardMessage = new TMessage(
	                message.name.substring(serviceName.length()+TMultiplexProtocol.SEPARATOR.length()),
	                message.type,
	                message.seqid
	        );
	
	        // Dispatch processing to the stored processor
	        return actualProcessor.process(new TMultiplexProtocol(iprot, standardMessage), oprot);
	    }
	    
	    
	    
	   
	
	    
}	    
