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
	protected int connections;
	protected boolean random;
	protected Random rn = new Random();
	protected int max_connections;
	protected int min_connections;
                                    
	public clientConnectionsGenerator() {
		this("clientConnectionsGenerator", 1, null);
	}
	
	public clientConnectionsGenerator(String name, double Int_arr_time, int... Connections) { 
	   super(name);
	   addInport("in");
	   addOutport("out");
	   addInport("stop");
	   addInport("start");
	   int_arr_time = Int_arr_time ;
	   if(Connections != null){
		   connections = Connections[0];
	   } else { 		   
		   random = true;
	   }
	   
	   addTestInput("start", new entity("start"));
	   addTestInput("stop", new entity("stop"));
	}
	    
	public void initialize() {
		random = false;
		connections = 0;
		max_connections = 5000;
		min_connections = -5000;
		holdIn("active", int_arr_time);
		super.initialize();
	 }
	
	public void  deltext(double e, message x) { 
		Continue(e);
		if(phaseIs("passive")) {
			for (int i=0; i< x.getLength();i++) {
				if (messageOnPort(x,"start",i)) {
					holdIn("active",int_arr_time);					
				}
			}
		}
		if(phaseIs("active")) {
			for (int i=0; i< x.getLength();i++) {
				if (messageOnPort(x,"stop",i)) {
					phase = "finishing";
				}
				if (messageOnPort(x,"start",i)) {
					holdIn("active",int_arr_time);
					connections = rn.nextInt((max_connections - min_connections) + 1) + min_connections;
				}
			}
		}
	}
	
	public void  deltint( ) { 
		if(phaseIs("active")){
			holdIn("active", 1);
		}
		else passivate();			
	}
	
	public void deltcon(double e, message x) {
		deltint();
		deltext(0,x);
	}
	
	public message out( ) {
		System.out.println(connections);
		message m = new message();
		content con = makeContent("out",
	            new entity(Integer.toString(connections)));
		m.add(con);
		return m;
	}
	
	public void showState() {
		super.showState();	  
	}
	
	public String getTooltipText() {
		return super.getTooltipText();
	}
}