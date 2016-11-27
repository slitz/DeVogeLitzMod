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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

import GenCol.Pair;
import GenCol.entity;
import model.modeling.*;
import view.modeling.ViewableAtomic;

public class transducer extends ViewableAtomic{  
	
	protected entity new_connections, max_connections;
	protected double observation_time, total_connections; 
	public int count;
	public String[] resource_utilizaton_by_hour;
                                    
	public transducer() {
		this("transducer", 12);
	}
	
	public transducer(String name, double Observation_time) { 
		super(name);
		observation_time = Observation_time;
		resource_utilizaton_by_hour = new String[(int)Observation_time];
		addInport("arriv");
		addInport("solved");
		addOutport("out");
	}
	    
	public void initialize() {
		phase = "passive";
		sigma = INFINITY;
		total_connections = 0;
		max_connections = null;
		count = 0;
		resource_utilizaton_by_hour = new String[(int)observation_time];;
		super.initialize();
	 }
	
	public void  deltext(double e, message x) { 
		Continue(e);
		if(phaseIs("passive") && count == 0){
			holdIn("active", observation_time);
		}
		if(phaseIs("active")){
			for(int i=0; i < x.size(); i++) {
				if(messageOnPort(x, "arriv", i)) {
				   count++;
			       entity en = x.getValOnPort("arriv", i);
			       Pair pr1 = (Pair)en;
			       // the value on the arriv port arrives as a pair with new_connections as the key
			       Pair pr2 = (Pair)pr1.getKey();
			       new_connections = (entity)pr2.getKey();
			       total_connections += Double.parseDouble(new_connections.toString());
			    } else if(messageOnPort(x, "solved", i)) {		       
			       entity ent = x.getValOnPort("solved", i);
			       max_connections = ent;	       
			       double resource_utilization = total_connections / Double.parseDouble(max_connections.toString());
			       resource_utilizaton_by_hour[count - 1] = convert_double_to_string_percentage(resource_utilization);
			    }
			}
		}
		show_state();
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
		m.add(makeContent("out", new entity(Arrays.toString(resource_utilizaton_by_hour))));
		return m;
	}
	
	private String convert_double_to_string_percentage(double number) {
		String stringPercentage;
		NumberFormat defaultFormat = NumberFormat.getPercentInstance();
		defaultFormat.setMinimumFractionDigits(2);
		stringPercentage = defaultFormat.format(number).toString(); 
		return stringPercentage;
	}
	
	public void show_state() {
		System.out.println("New connections: "  +  new_connections);
		System.out.println("Total connections: "  +  Math.round(total_connections)); 
		System.out.println("Max connections: "  +  max_connections); 
		System.out.println("Count: "  +  Integer.toString(count));
		System.out.println("Resource utilization by hour: " + Arrays.toString(resource_utilizaton_by_hour));
	}
	
	public String getTooltipText() {
		return super.getTooltipText();
	}
}	