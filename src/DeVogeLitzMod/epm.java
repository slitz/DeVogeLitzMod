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

public class epm extends ViewableDigraph{

	public epm(){
	    super("epm");
	
	    ViewableDigraph ef = new experimentalFrame("experimentalFrame", 1, 12);	    
	    ViewableDigraph p = new processorCoupledModel("processorCoupledModel", 1);
	
	    add(ef);
	    add(p);	    
	
	    addInport("in");
	    addInport("x");
	    addOutport("out");
	    addOutport("result");
	
	    addCoupling(this, "in", ef, "in");
	    addCoupling(this, "x", ef, "x");
	    
	    addCoupling(ef, "out", p, "in");	    
	    addCoupling(p, "out", ef, "in");
	    
	    addCoupling(ef, "result", this, "result");	    
	    
	    addTestInput("x", new Pair(new Pair(new entity("10"), new entity("basic")), new entity("none")));
	    addTestInput("x", new Pair(new Pair(new entity("100"), new entity("advanced")), new entity("none")));
	    addTestInput("x", new Pair(new Pair(new entity("1000"), new entity("multicore")), new entity("none")));
	    
	    addTestInput("x", new Pair(new Pair(new entity("1000"), new entity("basic")), new entity("none")));
	    addTestInput("x", new Pair(new Pair(new entity("1000"), new entity("advanced")), new entity("none")));
	    addTestInput("x", new Pair(new Pair(new entity("1000"), new entity("multicore")), new entity("none")));
	    
	    addTestInput("x", new Pair(new Pair(new entity("1000"), new entity("basic")), new entity("none")));
	    addTestInput("x", new Pair(new Pair(new entity("1000"), new entity("advanced")), new entity("medium")));
	    addTestInput("x", new Pair(new Pair(new entity("1000"), new entity("multicore")), new entity("high")));
	}
    
    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
	public void layoutForSimView()
    {
        preferredSize = new Dimension(640, 400);
        ((ViewableComponent)withName("experimentalFrame")).setPreferredLocation(new Point(32, 38));
        ((ViewableComponent)withName("processorCoupledModel")).setPreferredLocation(new Point(31, 180));
    }
}

