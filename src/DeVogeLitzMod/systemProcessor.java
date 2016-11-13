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

public class systemProcessor extends ViewableAtomic{  
	
	protected entity job;
	protected double processing_time;
                                    
	public systemProcessor() {
		this("systemProcessor", 1);
	}
	
	public systemProcessor(String name, double Processing_time) { 
		super(name);
		processing_time = Processing_time;
		addInport("in");		
		addOutport("out");		
	}
	    
	public void initialize() {
		phase = "passive";
		sigma = INFINITY;
		job = new entity("job");
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
		return super.getTooltipText() + "\n"+"job: " + job.getName();
	}
}