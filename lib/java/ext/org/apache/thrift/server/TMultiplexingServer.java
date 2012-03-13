package org.apache.thrift.server;

import java.util.List;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TMultiplexProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.registry.URIContext;
import org.apache.thrift.server.TLookupMultiplexer.MultiplexerArgs;
import org.apache.thrift.transport.TServerTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This abstract class facilitates user to quickly create and configure lookup multiplexing server
 * that can use varied server transport and protocol.
 * 
 * @author punit
 * @param <TServerTransport>
 * @param <TProtocolFactory>
 */
public abstract class TMultiplexingServer<T extends TServerTransport,F extends TProtocolFactory > {
	
	/**
	 * Logger
	 */
	protected static final Logger LOGGER = LoggerFactory.getLogger(TMultiplexingServer.class);
	
	/**
	 * worker instance
	 */
	protected TServer server;
	
	/**
	 * Constructor using arguments
	 * @param serverTransport
	 * @param protocolFactory
	 * @param factory : registry factory instance that server will use internally
	 * @param serverName
	 */
	public TMultiplexingServer (T serverTransport, F protFactory, TRegistryServerFactory factory){
		
		// getting protocol factory
		TProtocolFactory tProtFactory = getProtocolFactory(protFactory);
		
		//getting processor
		TProcessor processor = getProcessor(configureMultiplexer(),factory);
		
		//getting server
		server = getServer(serverTransport,tProtFactory, processor);
		
		
	}
	
	
	/**
	 * Constructor using arguments. This server will use the default implementation of registry.
	 * @param serverTransport
	 * @param protocolFactory
	 * @param serverName
	 */
	public TMultiplexingServer (T serverTransport, F protFactory){
		this(serverTransport,protFactory,null);
	}
	
	/**
	 * This method should provide the list of multiplexer arguments that
	 * can be used to configure multiplexer instance.  
	 * @return List of MultiplexerArgs
	 */
	protected abstract List<MultiplexerArgs<URIContext,TProcessor>> configureMultiplexer();
	
	
	/**
	 * Method that should return the desired server instance for the provided arguments.
	 * @param serverTransport
	 * @param protFactory
	 * @param processor
	 * @return TServer
	 */
	protected abstract TServer getServer( TServerTransport serverTransport,TProtocolFactory protFactory,TProcessor processor);
	
	
	/**
	 * Method for getting lookup multiplexer instance based on the configurations
	 * @param args
	 * @return TProcessor
	 */
	private static TProcessor getProcessor(List<MultiplexerArgs<URIContext,TProcessor>> args, TRegistryServerFactory factory){
		return new TLookupMultiplexer(args, factory);
	}
	
	
	/**
	 * Method for getting multiplex protocol instance for the underlying server protocol
	 * @param baseFactory - underlying server protocol factory
	 * @return TProtocolFactory
	 */
	private TProtocolFactory getProtocolFactory(F baseFactory) {
		return new TMultiplexProtocol.Factory(baseFactory);
		
	}
	
	
	/**
	 * Method to start the server
	 */
	public void start(){
		server.serve();
	}
	
	
	/**
	 * Method to stop the server
	 */
	public void stop(){
		server.stop();
	}
		

}
