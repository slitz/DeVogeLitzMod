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

import model.modeling.*;
import view.modeling.ViewableAtomic;

public class clientConnectionsGenerator extends ViewableAtomic{  
	
	protected double int_arr_time;
                                    
	public clientConnectionsGenerator() {
		this("clientConnectionsGenerator", 30);
	}
	
	public clientConnectionsGenerator(String name, double Int_arr_time) { 
	   super(name);
	   addInport("in");
	   addOutport("out");
	   addInport("stop");
	   addInport("start");
	   int_arr_time = Int_arr_time ;		
	}
	    
	public void initialize() {
		holdIn("active", int_arr_time);
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