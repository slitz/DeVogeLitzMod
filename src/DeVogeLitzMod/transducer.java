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

import java.util.ArrayList;
import java.util.Arrays;

import GenCol.Pair;
import GenCol.entity;
import model.modeling.*;
import view.modeling.ViewableAtomic;

public class transducer extends ViewableAtomic{  
	
	protected entity new_connections, max_connections;
	protected double observation_time, total_connections; 
	public int count;
	public String[] hours_over_capacity;
                                    
	public transducer() {
		this("systemMonitor", 12);
	}
	
	public transducer(String name, double Observation_time) { 
		super(name);
		max_connections = null;
		hours_over_capacity = new String[(int)Observation_time];
		observation_time = Observation_time;
		addInport("arriv");
		addInport("solved");
		addOutport("out");
	}
	    
	public void initialize() {
		phase = "active";
		sigma = observation_time;
		total_connections = 0;
		count = 0;
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
		       entity ent = x.getValOnPort("solved", i);
		       max_connections = ent;
		       if(total_connections > Double.parseDouble(max_connections.toString())) {
		    	   hours_over_capacity[count] = Integer.toString(count);
		       }
		       count++;
		    }
		}
		show_state();
	}
	
	public void  deltint( ) {
		passivate();
	}
	
	public void deltcon(double e, message x) {
		deltint();
		deltext(0,x);
	}
	
	public message out( ) {
		message m = new message();
		m.add(makeContent("out", new entity(Arrays.toString(hours_over_capacity))));
		return m;
	}
	
	public void show_state() {
		System.out.println("New connections: "  +  new_connections);
		System.out.println("Total connections: "  +  Math.round(total_connections)); 
		System.out.println("Max connections: "  +  max_connections); 
		System.out.println("Count: "  +  Integer.toString(count));
	}
	
	public String getTooltipText() {
		return super.getTooltipText();
	}
}	