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

public class configurationProcessor extends ViewableAtomic{  
	
	protected double processing_time;
	protected entity configuration;
	protected entity network_latency;
	protected static final double MAX_CPU_UTILIZATION = 0.75;  // CPU percentage threshold
	protected static final double CONNECTION_COST = 0.245; // CPU cost per connection
                                    
	public configurationProcessor() {
		this("configurationProcessor", 1);
	}
	
	public configurationProcessor(String name, double Processing_time) { 
		super(name);
		processing_time = Processing_time;
		addInport("in");		
		addOutport("out");	
		
		addTestInput("in", new entity("basic"));
		addTestInput("in", new entity("advanced"));
		addTestInput("in", new entity("multicore"));
		
		addTestInput("in", new entity("none"));
		addTestInput("in", new entity("medium"));
		addTestInput("in", new entity("high"));
	}
	    
	public void initialize() {
		phase = "passive";
		sigma = INFINITY;
		configuration = new entity("");
		network_latency = new entity("");
	    super.initialize();
	 }
	
	public void  deltext(double e, message x) { 
		Continue(e);
		if (phaseIs("passive")) {
			for (int i = 0; i < x.getLength(); i++) {
				if (messageOnPort(x, "in", i)) {
					entity ent = x.getValOnPort("in", i);
					Pair pr = (Pair)ent;
					entity en = (entity)pr.getValue();
					configuration = en;
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
			m.add(makeContent("out", new Pair(new entity("max connections"), new entity("" + Math.round(compute_max_connections())))));
		}
		return m;
	}
	
	 public double compute_max_connections(){
		 double max_connections = 0;
		 int cpu_count = 0;
		 double cpu_speed = 0;
		 int network_latency_factor = 1;
		 
		 switch (configuration.toString()) {
		 	case "basic": 
		 		cpu_count = 1;
		 		cpu_speed = 1300;
		 		break;
		 	case "advanced":
		 		cpu_count = 1;
		 		cpu_speed = 2000;
		 		break;
		 	case "multicore":
		 		cpu_count = 2;
		 		cpu_speed = 2000;
		 		break;
		 	default:
		 		break;
		 }
		 
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
		 
		 max_connections = (cpu_count * cpu_speed * MAX_CPU_UTILIZATION) / (CONNECTION_COST * network_latency_factor);
		 
		 return max_connections;
	 }
	
	public void showState() {
		super.showState();	  
	}
	
	public String getTooltipText() {
		return super.getTooltipText();
	}
}