/*      Copyright 2016 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA 2.7
 *  Date       : 11-13-2016
 *  Authors	   : Scott DeVoge and Scott Litz
 */

package DeVogeLitzMod;

import GenCol.Pair;
import GenCol.entity;
import model.modeling.*;
import view.modeling.ViewableAtomic;

public class networkLatencyGenerator extends ViewableAtomic{  
	
	protected double int_arr_time;
	protected String network_latency;
	protected int latency; 
                                    
	public networkLatencyGenerator() {
		this("networkLatencyGenerator", 30);
	}
	
	public networkLatencyGenerator(String name, double Int_arr_time) { 
	   super(name);
	   addInport("in");
	   addOutport("out");
	   int_arr_time = Int_arr_time ;	
	   network_latency = "none";
	   latency = 1;	
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
					latency = (int) (Math.random()*3) + 1;
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
			if (latency = 1){
				network_latency = "none";
			}
			else if (latency = 2){
				network_latency = "medium";
			}
			else{
				network_latency ="high";
			}
			m.add(makeContent("out", new Pair(new entity("network latency"), new entity(network_latency))));
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
