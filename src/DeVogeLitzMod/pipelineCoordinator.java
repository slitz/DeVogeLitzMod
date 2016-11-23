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
import SimpArcMod.proc;
import model.modeling.*;
import model.simulation.*;
import util.rand;
import view.modeling.ViewableAtomic;
import view.simView.*;

public class pipelineCoordinator extends ViewableAtomic{  
	
	protected int num_procs, num_results;
	protected entity job, resource_capacity_ent, connection_cost_ent, max_connections_ent;
	                                
	public pipelineCoordinator() {
		this("pipelineCoordinator");
	}
	
	public pipelineCoordinator(String name) { 
		super(name);
		num_procs = 0;
		num_results = 0;
		addInport("in");
		addInport("w");
		addInport("x");
		addOutport("out");
		addOutport("y");
		addOutport("z");
	}
	    
	public void initialize() {
		phase = "passive";
		sigma = INFINITY;
		job = null;
	    super.initialize();
	 }
	
	public void  deltext(double e, message x) { 
		Continue(e);
		if (phaseIs("passive")) {
			 for (int i=0; i< x.size();i++) {
				 if (messageOnPort(x,"setup",i)) {
				     add_procs(new processorDivideAndConquer("processorDivideAndConquer", 1));
				     add_procs(new configurationProcessor("maxConnectionsProcessor", 1));
				 }
			 }
		}
		
		if (phaseIs("passive")) {
			for (int i=0; i< x.size();i++) {
				if (messageOnPort(x,"in",i)) {
					job = x.getValOnPort("in",i);
					num_results = num_procs;
					holdIn("send_y", 0);
				}
			}
		}
		
		else if (phaseIs("busy")) {
			resource_capacity_ent = null;
			connection_cost_ent = null;
			max_connections_ent = null;
			for (int i=0; i< x.size();i++) {
				if (messageOnPort(x, "x", i)) {
					num_results--;
					entity ent = x.getValOnPort("x", i);
					Pair pr = (Pair)ent;
					resource_capacity_ent = (entity)pr.getKey();
					connection_cost_ent = (entity)pr.getValue();
					holdIn("send_z", 0);
			    } else if (messageOnPort(x, "w", i)) {
					num_results--;
			    	job = x.getValOnPort("w", i);
			    	max_connections_ent = job;
					holdIn("send_out", 0);
			    } else if (messageOnPort(x, "in", i)) {
			    	job = x.getValOnPort("in", i);
					num_results = num_procs;
					holdIn("send_y", 0);
			    }
			}
		}
	}
	
	public void  deltint( ) { 
		if (phaseIs("send_y") || phaseIs("send_z")) {
			passivateIn("busy");			
		}
		else passivate();
	}
	
	public void deltcon(double e, message x) {
		deltint();
		deltext(0,x);
	}
	
	public message out( ) {
		message m = new message();
		if (phaseIs("send_out")) {
			m.add(makeContent("out", max_connections_ent));
		}
		else if (phaseIs("send_z")) {
			m.add(makeContent("z", new Pair(resource_capacity_ent, connection_cost_ent)));
		} else if (phaseIs("send_y")) {
			Pair pr = (Pair)job;
			entity jobKey = (entity)pr.getKey();
			entity jobValue = (entity)pr.getValue();
			m.add(makeContent("y", new Pair(jobKey, jobValue)));
		}
		return m;
	}
	
	protected void add_procs (devs  p){
		num_procs++;
		num_results++;
	}	

	public void showState() {
		super.showState();	  
	}
	
	public String getTooltipText() {
		return super.getTooltipText();
	}
}
