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

import java.util.Map;
import model.modeling.*;
import view.modeling.ViewableAtomic;

public class systemMonitor extends ViewableAtomic{  
	
	protected Map arrived, solved;
	protected double clock, total_ta, observation_time;
                                    
	public systemMonitor() {
		this("systemMonitor", 200);
	}
	
	public systemMonitor(String name, double Observation_time) { 
		super(name);
		observation_time = Observation_time;
		addInport("ariv");
		addInport("solved");	
		addOutport("out");
	}
	    
	public void initialize() {
		phase = "active";
		sigma = observation_time;
		clock = 0;
		total_ta = 0;
		super.initialize();
	 }
	
	public void  deltext(double e, message x) { 
		Continue(e);
	}
	
	public void  deltint( ) { 
	}
	
	public void deltcon(double e, message x) {
		deltint();
		deltext(0,x);
	}
	
	public message out( ) {
		message m = new message();
		return m;
	}
	
	public void showState() {
		super.showState();	  
	}
	
	public String getTooltipText() {
		return super.getTooltipText();
	}
}