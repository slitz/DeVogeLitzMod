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

public class processorDivideAndConquer extends ViewableDigraph{

	public processorDivideAndConquer(){
	    super("processorDivideAndConquer");
	    make(6000,2);
	    addTestInput("in", new Pair(new Pair(new entity("1000"), new entity("basic")), new entity("none")));	    
	}
	
	public processorDivideAndConquer(String name, double proc_time){
		 super(name);
		 make(proc_time, 2);
	}
	
	private void make(double proc_time, int size){
		addInport("in");
		addOutport("out");

		divideAndConquerCoordinator divAndConCoord  = new divideAndConquerCoordinator("divAndConCoord");
		configurationProcessor configProc  = new configurationProcessor("configProc", 1);
		connectionCostProcessor conCostProc  = new connectionCostProcessor("conCostProc", 1);
		
		add(divAndConCoord);
		add(configProc);
		add(conCostProc);

		divAndConCoord.add_procs(configProc);
		divAndConCoord.add_procs(conCostProc);
		
		addCoupling(this, "in", divAndConCoord, "in");
		addCoupling(divAndConCoord,"out",this,"out");   

	    addCoupling(divAndConCoord, "y", configProc,"in");
	    addCoupling(configProc,"out",divAndConCoord,"x");
	    addCoupling(divAndConCoord, "y", conCostProc,"in");
	    addCoupling(conCostProc,"out",divAndConCoord,"x");

	    initialize();
	}
	
    public void layoutForSimView() {
        preferredSize = new Dimension(400, 180);
        ((ViewableComponent)withName("divAndConCoord")).setPreferredLocation(new Point(70, 35));
        ((ViewableComponent)withName("configProc")).setPreferredLocation(new Point(-12, 100));
        ((ViewableComponent)withName("conCostProc")).setPreferredLocation(new Point(152, 100));
    }
}

