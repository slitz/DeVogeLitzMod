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

public class generatorCoupledModel extends ViewableDigraph{

	public generatorCoupledModel(){
	    super("generatorCoupledModel");
	    make(6000, 3);
	    addTestInput("in", new entity("start"));
	}
	
	public generatorCoupledModel(String name, double proc_time){
		 super(name);
		 make(proc_time, 3);
	}
	
	private void make(double proc_time, int size){
		addInport("start");
		addInport("stop");
		addInport("in");
		addOutport("out");

		generatorCoordinator gCoord  = new generatorCoordinator("gCoord");
		add(gCoord);

		addCoupling(this, "in", gCoord, "in");
		addCoupling(gCoord,"out", this,"out");

	    clientConnectionsGenerator  ccg = new clientConnectionsGenerator("ccg", proc_time/size, null);
	    systemConfigurationGenerator  scg = new systemConfigurationGenerator("scg", proc_time/size);
	    networkLatencyGenerator  nlg = new networkLatencyGenerator("nlg", proc_time/size);

	    add(ccg);
	    add(scg);
	    add(nlg);

	    gCoord.add_genrs(ccg);
	    gCoord.add_genrs(scg);
	    gCoord.add_genrs(nlg);

	    addCoupling(gCoord, "y", ccg,"in");
	    addCoupling(ccg,"out",gCoord,"x");
	    addCoupling(gCoord, "y", scg,"in");
	    addCoupling(scg,"out",gCoord,"x");
	    addCoupling(gCoord, "y", nlg,"in");
	    addCoupling(nlg,"out",gCoord,"x");

	    initialize();
	}
	
    
    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView() {
        preferredSize = new Dimension(600, 180);
        ((ViewableComponent)withName("gCoord")).setPreferredLocation(new Point(158, 25));
        ((ViewableComponent)withName("ccg")).setPreferredLocation(new Point(-10, 110));
        ((ViewableComponent)withName("scg")).setPreferredLocation(new Point(165, 110));
        ((ViewableComponent)withName("nlg")).setPreferredLocation(new Point(340, 110));
    }
}
