/*      Copyright 2016 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA 2.7
 *  Date       : 11-13-2016
 *  Authors	   : Scott DeVoge and Scott Litz
 */

package DeVogeLitzMod;

import GenCol.Pair;
import GenCol.entity;
import model.modeling.*;
import view.modeling.ViewableAtomic;

public class generator extends ViewableAtomic{  
	
	protected double int_arr_time;
	protected entity new_connections;
	protected entity configuration;
	protected entity network_latency;
	protected int count;
                                    
	public generator() {
		this("generator", 1);
	}
	
	public generator(String name, double Int_arr_time) { 
	   super(name);
	   addInport("Connections");
	   addInport("Configuration");
	   addInport("Latency");	
	   addOutport("out");
	   addInport("stop");
	  // addInport("start");
	   int_arr_time = Int_arr_time ;
	}
	    
	public void initialize() {
		holdIn("active", int_arr_time);
		count = 0;
		super.initialize();
	 }
	
	public void  deltext(double e, message x) { 
		Continue(e);
		if(phaseIs("active")) {
			for (int i=0; i< x.getLength();i++) {
			if (messageOnPort(x, "stop", i)) {
					phase = "finishing";
				return;
				}
			}
			
			for (int i=0; i< x.getLength();i++) {
				if (messageOnPort(x,"Connections",i)){
					new_connections = x.getValOnPort("Connections", i);
					} 
			}
			
			for (int i=0; i< x.getLength();i++) {
				if (messageOnPort(x,"Configuration",i)){
					configuration = x.getValOnPort("Configuration", i);
					
				} 
			}
			for (int i=0; i< x.getLength();i++) {
				if (messageOnPort(x,"Latency",i)){
					network_latency = x.getValOnPort("Latency", i);
				} 
			}
		}
	}
	
	public void  deltint( ) { 
		count ++;
		if(phaseIs("active")){	
			// change new connections to a negative number for 2nd half of run to mimic decreasing usage
			if (count >= 6){
				int negative_connections = Math.negateExact(Integer.parseInt(new_connections.toString()));
				new_connections = new entity(Integer.toString(negative_connections));
			}
			holdIn("active", int_arr_time);
		} else { 
			passivate();
		}	
	}
	
	public void deltcon(double e, message x) {
		deltint();
		deltext(0,x);
	}
	
	public message out( ) {
		message m = new message();
		if (phaseIs("active")) {
			m.add(makeContent("out", new Pair(new Pair(new_connections, configuration), network_latency)));
		}
		return m;
	}
	
	public void showState() {
		super.showState();	  
	}
	
	public String getTooltipText() {
		return super.getTooltipText();
	}
}
