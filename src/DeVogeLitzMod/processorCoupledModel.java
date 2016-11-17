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
import SimpArcMod.divideCoord;
import SimpArcMod.proc;
import model.modeling.*;
import model.simulation.*;

import view.modeling.ViewableAtomic;
import view.modeling.ViewableComponent;
import view.modeling.ViewableDigraph;
import view.simView.*;

public class processorCoupledModel extends ViewableDigraph{

	public processorCoupledModel(){
	    super("processorCoupledModel");
	    make(6000,2);
	    addTestInput("in", new Pair(new entity("1000"), new entity("basic")));
	    addTestInput("in", new Pair(new entity("1000"), new entity("advanced")));
	    addTestInput("in", new Pair(new entity("1000"), new entity("multicore")));
	}
	
	public processorCoupledModel(String name, double proc_time){
		 super(name);
		 make(proc_time, 2);
	}
	
	private void make(double proc_time, int size){
		addInport("in");
		addOutport("out");

		processorCoordinator procCoord  = new processorCoordinator("procCoord");
		add(procCoord);

		addCoupling(this, "in", procCoord, "in");
		addCoupling(procCoord,"out",this,"out");

	    connectionsProcessor  connectProc = new connectionsProcessor("connectProc", proc_time/size);
	    configurationProcessor  configProc = new configurationProcessor("configProc", proc_time/size);	    

	    add(connectProc);
	    add(configProc);

	    procCoord.add_procs(connectProc);
	    procCoord.add_procs(configProc);

	    addCoupling(procCoord, "y", connectProc,"in");
	    addCoupling(connectProc,"out",procCoord,"x");
	    addCoupling(procCoord, "y", configProc,"in");
	    addCoupling(configProc,"out",procCoord,"x");

	    initialize();
	}
	
    public void layoutForSimView() {
        preferredSize = new Dimension(500, 180);
        ((ViewableComponent)withName("procCoord")).setPreferredLocation(new Point(110, 35));
        ((ViewableComponent)withName("connectProc")).setPreferredLocation(new Point(20, 100));
        ((ViewableComponent)withName("configProc")).setPreferredLocation(new Point(232, 100));
    }
}

