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

public class systemMonitor extends ViewableAtomic{  
	
	protected entity total_connections, max_connections;
	protected double observation_time;
                                    
	public systemMonitor() {
		this("systemMonitor", 10);
	}
	
	public systemMonitor(String name, double Observation_time) { 
		super(name);
		total_connections = null;
		max_connections = null;
		observation_time = Observation_time;
		addInport("in");
		addOutport("out");
		addOutport("capacity");
		addOutport("utilization");
	}
	    
	public void initialize() {
		phase = "active";
		sigma = observation_time;
		super.initialize();
	 }
	
	public void  deltext(double e, message x) { 
		Continue(e);		  
		System.out.println("sm deltext triggered");
		for(int i=0; i < x.size(); i++) {
			if(messageOnPort(x,"in", i)) {
		       entity en = x.getValOnPort("in", i);
		       Pair pr = (Pair)en;
		       // the value on the in port arrives as a pair with total_connections as the key and max_connections as the value
		       total_connections = (entity)pr.getKey();
		       max_connections = (entity)pr.getValue();
		       holdIn("active", 0);
		    }
		}
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
		if (phaseIs("active")) {
			content  con1 = makeContent("capacity", new entity("" + isOverCapacity()));
			content  con2 = makeContent("utilization", new entity("" + computeUtilization()));
			m.add(con1);
			m.add(con2);
		}
		return m;
	}
	
	private boolean isOverCapacity() {
		boolean isOverCapacity = false;
		if (Double.parseDouble(total_connections.toString()) > Double.parseDouble(max_connections.toString())) {
			isOverCapacity = true;
		}
		return isOverCapacity;
	}
	
	private double computeUtilization() {
		double utilizationPercentage = Double.parseDouble(total_connections.toString()) / Double.parseDouble(max_connections.toString());
		return utilizationPercentage;
	}
	
	public void showState() {
		super.showState();	  
	}
	
	public String getTooltipText() {
		return super.getTooltipText();
	}
}	