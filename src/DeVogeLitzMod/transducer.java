/*      Copyright 2016 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA 2.7
 *  Date       : 11-12-2016
 *  Authors	   : Scott DeVoge and Scott Litz
 */

package DeVogeLitzMod;

import GenCol.Pair;
import GenCol.entity;
import model.modeling.*;
import view.modeling.ViewableAtomic;

public class transducer extends ViewableAtomic{  
	
	protected entity new_connections, max_connections;
	protected double observation_time, total_connections; 
	public int count;
                                    
	public transducer() {
		this("systemMonitor", 10);
	}
	
	public transducer(String name, double Observation_time) { 
		super(name);
		total_connections = 0;
		max_connections = null;
		count = 0;
		observation_time = Observation_time;
		addInport("arriv");
		addInport("solved");
		addOutport("out");
	}
	    
	public void initialize() {
		phase = "active";
		sigma = observation_time;
		super.initialize();
	 }
	
	public void  deltext(double e, message x) { 
		Continue(e);		  
		for(int i=0; i < x.size(); i++) {
			if(messageOnPort(x, "arriv", i)) {
		       entity en = x.getValOnPort("arriv", i);
		       Pair pr1 = (Pair)en;
		       // the value on the arriv port arrives as a pair with new_connections as the key
		       Pair pr2 = (Pair)pr1.getKey();
		       new_connections = (entity)pr2.getKey();
		       total_connections += Double.parseDouble(new_connections.toString());
		    } else if(messageOnPort(x, "solved", i)) {
		       count++;
		       entity ent = x.getValOnPort("solved", i);
		       max_connections = ent;
		       if(total_connections > Double.parseDouble(max_connections.toString())) {
		    	   holdIn("finishing", 0);
		       }
		    }
		}
		show_state();
	}
	
	public void  deltint( ) {
		if(phaseIs("active")) {
			holdIn("active", observation_time);
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
		if (phaseIs("finishing")) {
			m.add(makeContent("out", new entity(Integer.toString(count))));
		}
		return m;
	}
	
	public void show_state() {
		System.out.println("New connections: "  +  new_connections);
		System.out.println("Total connections: "  +  total_connections); 
		System.out.println("Max connections: "  +  max_connections); 
	}
	
	public String getTooltipText() {
		return super.getTooltipText();
	}
}	