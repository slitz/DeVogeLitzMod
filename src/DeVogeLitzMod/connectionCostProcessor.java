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

import GenCol.*;
import model.modeling.*;
import model.simulation.*;
import util.rand;
import view.modeling.ViewableAtomic;
import view.simView.*;

public class connectionCostProcessor extends ViewableAtomic{  
	
	protected double processing_time;
	protected entity network_latency;
	protected static final double CONNECTION_COST = 0.245; // CPU cost per connection
                                    
	public connectionCostProcessor() {
		this("connectionCostProcessor", 1);
	}
	
	public connectionCostProcessor(String name, double Processing_time) { 
		super(name);
		processing_time = Processing_time;
		addInport("in");		
		addOutport("out");			
	}
	    
	public void initialize() {
		phase = "passive";
		sigma = INFINITY;
		network_latency = new entity("");
	    super.initialize();
	 }
	
	public void  deltext(double e, message x) { 
		Continue(e);
		if (phaseIs("passive")) {
			for (int i = 0; i < x.getLength(); i++) {
				if (messageOnPort(x, "in", i)) {
					entity ent = x.getValOnPort("in", i);
					// the entity passed from the generator is a pair that contains a pair and an entity
					Pair pr = (Pair)ent;
					network_latency = (entity)pr.getValue();
					holdIn("busy", 0);
				}
			}
		}
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
		if (phaseIs("busy")) {
			m.add(makeContent("out", new Pair(new entity("connection cost"), new entity("" + compute_total_connection_cost()))));
		}
		return m;
	}
	
	private double compute_total_connection_cost() {
		double total_connection_cost = 0;
		int network_latency_factor = 1;
		
		switch (network_latency.toString()) {
		 case "none":
			 network_latency_factor = 1;
			 break;
		 case "medium":
			 network_latency_factor = 2;
			 break;
		 case "high":
			 network_latency_factor = 3;
			 break;
		 default:
			 break;
		 }
		
		total_connection_cost = CONNECTION_COST * network_latency_factor;
				
		return total_connection_cost;
	}
	
	public void showState() {
		super.showState();	  
	}
	
	public String getTooltipText() {
		return super.getTooltipText();
	}
}