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

public class processorPipeline extends ViewableDigraph{

	public processorPipeline(){
	    super("processorPipeline");
	    make(6000,2); 
	}
	
	public processorPipeline(String name, double proc_time){
		 super(name);
		 make(proc_time, 2);
	}
	
	private void make(double proc_time, int size){
		addInport("in");
		addOutport("out");

		pipelineCoordinator pipeCoord = new pipelineCoordinator("pipeCoord");
		processorDivideAndConquer procDivAndCon  = new processorDivideAndConquer("procDivAndCon", 1);
		maxConnectionsProcessor maxConProc = new maxConnectionsProcessor("maxConProc", 1);
		
		add(pipeCoord);
		add(procDivAndCon);
		add(maxConProc);
		
		pipeCoord.add_procs(procDivAndCon);
		pipeCoord.add_procs(maxConProc);

		addCoupling(this, "in", pipeCoord, "in");
		addCoupling(pipeCoord, "out", this, "out");

	    addCoupling(pipeCoord, "y", procDivAndCon,"in");
	    addCoupling(procDivAndCon,"out",pipeCoord,"x");
	    addCoupling(pipeCoord, "z", maxConProc,"in");
	    addCoupling(maxConProc,"out",pipeCoord,"w");

	    initialize();
	}
	
    public void layoutForSimView() {
        preferredSize = new Dimension(670, 300);
        ((ViewableComponent)withName("pipeCoord")).setPreferredLocation(new Point(205, 30));
        ((ViewableComponent)withName("procDivAndCon")).setPreferredLocation(new Point(10, 100));
        ((ViewableComponent)withName("maxConProc")).setPreferredLocation(new Point(420, 100));
    }
}

