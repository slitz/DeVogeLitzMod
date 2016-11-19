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

import java.util.HashMap;

import GenCol.*;
import SimpArcMod.proc;
import model.modeling.*;
import model.simulation.*;
import util.rand;
import view.modeling.ViewableAtomic;
import view.simView.*;

public class generatorCoordinator extends ViewableAtomic{  
	
	protected double int_arr_time;
	protected int num_genrs, num_results;
	protected entity job, newConnectionsEnt, configurationEnt, networkLatencyEnt;	
	                                
	public generatorCoordinator() {
		this("generatorCoordinator", 30);
	}
	
	public generatorCoordinator(String name, double Int_arr_time) { 
		super(name);
		num_genrs = 0;
		num_results = 0;
		addInport("in");
	//	addInport("setup");
		addInport("x");
		addOutport("out");
		addOutport("y");
		int_arr_time = Int_arr_time ;
	}
	    
	public void initialize() {
		holdIn("active", int_arr_time);
		job = null;
	    super.initialize();
	 }
	
	public void  deltext(double e, message x) { 
		Continue(e);
		if (phaseIs("passive")) {
			 for (int i=0; i< x.size();i++) {
				 if (messageOnPort(x,"setup",i)) {
				     add_genrs(new clientConnectionsGenerator("clientConnectionsGenerator", 1, null));
				     add_genrs(new systemConfigurationGenerator("systemConfigurationGenerator", 1));
				     add_genrs(new networkLatencyGenerator("networkLatencyGenerator", 1));
				 }
			 }
		}
		
		if (phaseIs("active")) {
			for (int i=0; i< x.size();i++) {
				if (messageOnPort(x,"in",i)) {
					job = x.getValOnPort("in",i);
					num_results = num_genrs;
					holdIn("send_y", 0);
				} else if (messageOnPort(x,"x",i)) {
					entity ent = x.getValOnPort("x", i);
					Pair pr = (Pair)ent;
					entity en = (entity)pr.getKey();
					if(en.toString().contains("connections")) {
						newConnectionsEnt = (entity)pr.getValue();
					} else if (en.toString().contains("configuration")) {
						configurationEnt = (entity)pr.getValue();
					} else if (en.toString().contains("network")) {
						networkLatencyEnt = (entity)pr.getValue();
					}
					if (newConnectionsEnt != null && configurationEnt != null && networkLatencyEnt != null) {
						holdIn("send_out", 0);
					}					
				}
			}
		}
		
		else if (phaseIs("busy")) {
			newConnectionsEnt = null;
			configurationEnt = null;
			networkLatencyEnt = null;
			for (int i=0; i< x.size();i++) {
				if (messageOnPort(x,"x",i)) {
					// num_results--;
					entity ent = x.getValOnPort("x", i);
					Pair pr = (Pair)ent;
					entity en = (entity)pr.getKey();
					if(en.toString().contains("connections")) {
						newConnectionsEnt = (entity)pr.getValue();
					} else if (en.toString().contains("configuration")) {
						configurationEnt = (entity)pr.getValue();
					} else if (en.toString().contains("network")) {
						networkLatencyEnt = (entity)pr.getValue();
					}
					
					//if (num_results == 0) {
						holdIn("send_out", 0);
					//}
			    }
			}
		}
	}
	
	public void  deltint( ) { 
		if (phaseIs("active")) {
			holdIn("active", int_arr_time);
		} else if (phaseIs("send_y")) {
			passivateIn("busy");
		} else if (phaseIs("send_out")) {
			holdIn("send_y", 0);
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
			m.add(makeContent("out", new Pair(new Pair(new entity(newConnectionsEnt.toString()), new entity(configurationEnt.toString())), new entity(networkLatencyEnt.toString()))));
		} else if (phaseIs("send_y")) {
		   m.add(makeContent("y", job));
		}
		return m;
	}
	
	protected void add_genrs (devs g){
		num_genrs++;
		num_results++;
	}

	public void showState() {
		super.showState();	  
	}
	
	public String getTooltipText() {
		return super.getTooltipText();
	}
}
