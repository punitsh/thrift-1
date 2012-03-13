package org.apache.thrift.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.thrift.TMultiplexerConstants;
import org.apache.thrift.TProcessor;
import org.apache.thrift.registry.InvalidInputException;
import org.apache.thrift.registry.URIContext;

/**
 * Processor class that provides support for lookup registry along with multiplexing capability.
 * 
 * @author punit
 *
 */
public class TLookupMultiplexer extends TMultiplexer {

	TRegistryServerFactory factory; 
	
	/**
	 * Constructor that accepts a list of <MultiplexerArgs>
	 * to register provided services(processors) with 
	 * lookup registry and multiplexer
	 * 
	 * @param list
	 */
	public TLookupMultiplexer(List<MultiplexerArgs<URIContext,TProcessor>> list, TRegistryServerFactory factory) {
		
		//creating multiplexer using arguments
		super();
		
		//creating factory
		this.factory = (factory==null)?TRegistryServerFactory.createFactory():factory;
		
		//creating list of URIContext from argumentList and registering processors
		List<URIContext> urilist = new ArrayList<URIContext>();
		
		for(MultiplexerArgs<URIContext,TProcessor> arg : list){
			
			//adding context to list
			urilist.add(arg.getUricontext());
			
			//registering processors
			try {
				
				//registering lookup service with multiplexer
				super.registerProcessor(arg.getUricontext(), arg.getProcessor());
				
			} catch (InvalidInputException e) {
				LOGGER.error("LOOKUP SERVICE REGISTRATION FAILED. DETAILS : "+e.what+"||"+e.why);
			}
		}
		
		//configuring lookup service 
		TProcessor lookupProcessor = this.factory.getService(urilist);
		URIContext lookupContext = new URIContext(TMultiplexerConstants.LOOKUP_CONTEXT,"Lookup Service");
		
		try {
			
			//registering lookup service with multiplexer
			super.registerProcessor(lookupContext, lookupProcessor);
			
		} catch (InvalidInputException e) {
			LOGGER.error("LOOKUP SERVICE REGISTRATION FAILED. DETAILS : "+e.what+"||"+e.why);
		}
		
	}
	
	
	
	/**
	 * Method for registering a service(processor) against a context with
	 * lookup registry and multiplexer. 
	 */
	public void registerProcessor(MultiplexerArgs<URIContext,TProcessor> args) throws InvalidInputException {
		factory.getHelper().bind(args.getUricontext());	
		super.registerProcessor(args.getUricontext(),args.getProcessor());
		
	}
	
	
	 /**
     * Class that should be used for providing the required information
     * about the context and service(processor). so that appropriate 
     * registrations can be done.
     *   
     * @author punit
     *
     */
    public static class MultiplexerArgs<U extends URIContext ,V extends TProcessor> {
		/**
		 * Service instance
		 */
		TProcessor tProcessor;
		
		/**
		 * service context
		 */
		URIContext uricontext;
		
		public MultiplexerArgs(V processor, U context) {
			tProcessor = processor;
			uricontext = context;
		}

		public TProcessor getProcessor() {
			return tProcessor;
		}

		public URIContext getUricontext() {
			return uricontext;
		}
		
				
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("MultiplexerArgs(");
		    boolean first = true;

		    sb.append("URIContext:");
		    if (this.uricontext == null) {
		      sb.append("null");
		    } else {
		      sb.append(this.uricontext);
		    }
		    first = false;
		    if (!first) sb.append(", ");
		    sb.append("TProcessor:");
		    if (this.tProcessor == null) {
		      sb.append("null");
		    } else {
		      sb.append(this.tProcessor);
		    }
		    sb.append(")");
		    return sb.toString();
		}
		
	}
	
	

}
