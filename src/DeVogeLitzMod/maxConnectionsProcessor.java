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

public class maxConnectionsProcessor extends ViewableAtomic{  
	
	protected double processing_time;
	protected entity resource_capacity, connection_cost;
                                    
	public maxConnectionsProcessor() {
		this("maxConnectionsProcessor", 0);
	}
	
	public maxConnectionsProcessor(String name, double Processing_time) { 
		super(name);
		processing_time = Processing_time;
		addInport("in");		
		addOutport("out");			
	}
	    
	public void initialize() {
		phase = "passive";
		sigma = INFINITY;
		resource_capacity = new entity("");
		connection_cost = new entity("");
	    super.initialize();
	 }
	
	public void  deltext(double e, message x) { 
		Continue(e);
		if (phaseIs("passive")) {
			for (int i = 0; i < x.getLength(); i++) {
				if (messageOnPort(x, "in", i)) {
					entity ent = x.getValOnPort("in", i);
					// the entity passed from the coordinator is a pair of entities
					Pair pr = (Pair)ent;
					resource_capacity = (entity)pr.getKey();
					connection_cost = (entity)pr.getValue();
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
			m.add(makeContent("out", new entity("" + Math.round(compute_max_connections()))));
		}
		return m;
	}
	
	private double compute_max_connections() {
		double max_connections = Double.parseDouble(resource_capacity.toString()) / Double.parseDouble(connection_cost.toString());
		
		return max_connections;
	}
	
	public void showState() {
		super.showState();	  
	}
	
	public String getTooltipText() {
		return super.getTooltipText();
	}
}