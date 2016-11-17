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

public class connectionsProcessor extends ViewableAtomic{  
	
	protected double processing_time;
	protected entity new_connections;
	protected int total_connections;
                                    
	public connectionsProcessor() {
		this("connectionsProcessor", 1);
	}
	
	public connectionsProcessor(String name, double Processing_time) { 
		super(name);
		processing_time = Processing_time;
		addInport("in");		
		addOutport("out");		
		
		addTestInput("in", new entity("1000"));
		addTestInput("in", new entity("-1000"));
		addTestInput("in", new entity("5000"));
		addTestInput("in", new entity("-5000"));
	}
	    
	public void initialize() {
		phase = "passive";
		sigma = INFINITY;
		total_connections = 0;
	    super.initialize();
	 }
	
	public void  deltext(double e, message x) { 
		Continue(e);
		if (phaseIs("passive")) {
			for (int i = 0; i < x.getLength(); i++) {
				if (messageOnPort(x, "in", i)) {
					entity ent = x.getValOnPort("in", i);
					Pair pr = (Pair)ent;
					entity en = (entity)pr.getKey();
					new_connections = en;
					total_connections = total_connections + Integer.parseInt(new_connections.toString());
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
			m.add(makeContent("out", new Pair(new entity("total connections"), new entity(Integer.toString(total_connections)))));
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