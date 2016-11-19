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

import java.util.Random;

import GenCol.*;
import model.modeling.*;
import view.modeling.ViewableAtomic;

public class clientConnectionsGenerator extends ViewableAtomic{  
	
	protected double int_arr_time;
	protected int new_connections;
                                    
	public clientConnectionsGenerator() {
		this("clientConnectionsGenerator", 1, null);
	}
	
	public clientConnectionsGenerator(String name, double Int_arr_time, int... Connections) { 
	   super(name);
	   addInport("in");
	   addOutport("out");
	   int_arr_time = Int_arr_time ;
	   if(Connections != null){
		   new_connections = Connections[0];
	   } else {
		   new_connections = 1000;
	   }
	   
	   addTestInput("in", new entity("start"));
	   addTestInput("in", new entity("stop"));
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
			m.add(makeContent("out", new Pair(new entity("new connections"), new entity(Integer.toString(new_connections)))));
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
