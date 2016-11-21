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
                                    
	public generator() {
		this("generator", 30);
	}
	
	public generator(String name, double Int_arr_time) { 
	   super(name);
	   addInport("in");
	   addOutport("out");
	   addInport("stop");
	   addInport("start");
	   int_arr_time = Int_arr_time ;
	}
	    
	public void initialize() {
		holdIn("active", int_arr_time);
		super.initialize();
	 }
	
	public void  deltext(double e, message x) { 
		Continue(e);
		if(phaseIs("active")) {
			for (int i=0; i< x.getLength();i++) {
				if (messageOnPort(x,"in",i)) {
					entity ent = x.getValOnPort("in", i);
					Pair pr1 = (Pair)ent;
					Pair pr2 = (Pair)pr1.getKey();
					new_connections = (entity)pr2.getKey();
					configuration = (entity)pr2.getValue();
					network_latency = (entity)pr1.getValue();
					holdIn("active", int_arr_time);						
				} else if (messageOnPort(x, "stop", i)) {
					phase = "finishing";
				}
			}
		}
	}
	
	public void  deltint( ) { 
		if(phaseIs("active")){			   
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
