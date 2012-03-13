package org.apache.thrift.registry;

public class NotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2655236568557403419L;
		
	public String what; // required
	public String why; // required
  
	public String getWhat() {
		return what;
	}
	public void setWhat(String what) {
		this.what = what;
	}
	public String getWhy() {
		return why;
	}
	public void setWhy(String why) {
		this.why = why;
	}

	
  
	public NotFoundException(String what, String why) {
		super();
		this.what = what;
		this.why = why;
	}
	@Override
	public String toString() {
		return ""+what+" : "+why;
	}
	

}
