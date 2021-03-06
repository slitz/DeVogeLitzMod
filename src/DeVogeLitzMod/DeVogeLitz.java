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

import java.awt.*;

import GenCol.*;


import model.modeling.*;
import model.simulation.*;

import view.modeling.ViewableAtomic;
import view.modeling.ViewableComponent;
import view.modeling.ViewableDigraph;
import view.simView.*;

public class DeVogeLitz extends ViewableDigraph{

	public DeVogeLitz(){
	    super("DeVogeLitz");
	
	    ViewableDigraph ef = new experimentalFrame("experimentalFrame", 1, 12);	    
	    ViewableDigraph p = new processorCoupledModel("processorCoupledModel", 1);
	
	    add(ef);
	    add(p);	    
	
	    addInport("Connections");
	    addInport("Configuration");
	    addInport("Latency");
	    addOutport("Result");
	    addCoupling(this, "Connections", ef, "Connections");
	    addCoupling(this, "Configuration", ef, "Configuration");
	    addCoupling(this, "Latency", ef, "Latency");
	    addCoupling(ef, "result", this, "Result");	
		
	    addCoupling(ef, "out", p, "in");	    
	    addCoupling(p, "out", ef, "in");	    
	    
	  
	    addTestInput("Connections", new entity("100"));
	    addTestInput("Connections", new entity("500"));
	    addTestInput("Connections", new entity("1000"));	
	    
	    addTestInput("Configuration", new entity("basic"));
	    addTestInput("Configuration", new entity("advanced"));
	    addTestInput("Configuration", new entity("multicore"));
	    
	    addTestInput("Latency", new entity("none"));
	    addTestInput("Latency", new entity("medium"));
	    addTestInput("Latency", new entity("high"));
	}
    
    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
	public void layoutForSimView()
    {
        preferredSize = new Dimension(870, 530);
        ((ViewableComponent)withName("experimentalFrame")).setPreferredLocation(new Point(10, 38));
        ((ViewableComponent)withName("processorCoupledModel")).setPreferredLocation(new Point(10, 180));
    }
}

