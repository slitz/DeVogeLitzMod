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

import GenCol.Pair;
import GenCol.entity;
import model.modeling.*;
import view.modeling.ViewableAtomic;

public class systemConfigurationGenerator extends ViewableAtomic{  
	
	protected double int_arr_time;
	protected String configuration;
	protected int system;
                                    
	public systemConfigurationGenerator() {
		this("systemConfigurationGenerator", 30);
	}
	
	public systemConfigurationGenerator(String name, double Int_arr_time) { 
	   super(name);
	   addInport("in");
	   addOutport("out");
	   int_arr_time = Int_arr_time ;	
	   configuration = "basic";
	   system = 1;
	}
	    
	public void initialize() {
		holdIn("active", int_arr_time);
		super.initialize();
	 }
	
	public void  deltext(double e, message x) { 
		Continue(e);
		if(phaseIs("passive")) {
			for (int i=0; i< x.getLength();i++) {
				if (messageOnPort(x,"in",i)) {
					system = (int) (Math.random()*3) + 1;
					holdIn("active", int_arr_time);								
				}
			}
		}
	}
	
	public void  deltint( ) { 
		if(phaseIs("active")){			   
			holdIn("active", int_arr_time);
		} else { 
			passivate();
		}	
	}
	
	public void deltcon(double e, message x) {
		deltint();
		deltext(0,x);
	}
	
	public message out( ) {
		message m = new message();
		if (phaseIs("active")) {
			if (system = 1){
				configuration = "basic";
			}
			else if (system = 2){
				configuration = "advance";
			}
			else {
				configuration = "multi-server";
			}
			m.add(makeContent("out", new Pair(new entity("configuration"), new entity(configuration))));
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
