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
	protected static final double MAX_CPU_UTILIZATION = 0.75;  // CPU percentage threshold	
                                    
	public configurationProcessor() {
		this("configurationProcessor", 0);
	}
	
	public configurationProcessor(String name, double Processing_time) { 
		super(name);
		processing_time = Processing_time;
		addInport("in");		
		addOutport("out");	
	}
	    
	public void initialize() {
		phase = "passive";
		sigma = INFINITY;
		configuration = new entity("");
	    super.initialize();
	 }
	
	public void  deltext(double e, message x) { 
		Continue(e);
		if (phaseIs("passive")) {
			for (int i = 0; i < x.getLength(); i++) {
				if (messageOnPort(x, "in", i)) {
					entity ent = x.getValOnPort("in", i);
					// the entity passed from the generator is a pair that contains a pair and an entity
					Pair pr1 = (Pair)ent;
					Pair pr2 = (Pair)pr1.getKey();					
					configuration = (entity)pr2.getValue();
					holdIn("busy", processing_time);
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
			m.add(makeContent("out", new Pair(new entity("resource capacity"), new entity("" + Math.round(compute_total_resource_capacity())))));
		}
		return m;
	}
	
	 public double compute_total_resource_capacity(){
		 double resource_capacity = 0;
		 int cpu_count = 0;
		 double cpu_speed = 0;
		 
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
		 
		 resource_capacity = cpu_count * cpu_speed * MAX_CPU_UTILIZATION;
		 
		 return resource_capacity;
	 }
	
	public void showState() {
		super.showState();	  
	}
	
	public String getTooltipText() {
		return super.getTooltipText();
	}
}
