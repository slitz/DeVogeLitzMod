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
                                    
	public systemConfigurationGenerator() {
		this("systemConfigurationGenerator", 30);
	}
	
	public systemConfigurationGenerator(String name, double Int_arr_time) { 
	   super(name);
	   addInport("in");
	   addOutport("out");
	   int_arr_time = Int_arr_time ;	
	   configuration = "";
	}
	    
	public void initialize() {
		holdIn("passive", int_arr_time);
		super.initialize();
	 }
	
	public void  deltext(double e, message x) { 
		Continue(e);
		if(phaseIs("passive")) {
			for (int i=0; i< x.getLength();i++) {
				if (messageOnPort(x,"in",i)) {
					holdIn("busy", int_arr_time);			
					configuration = "basic"; // TODO: add randomizer or way to pass in configuration
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