package people;

import java.sql.ResultSet;
import java.util.Hashtable;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.RequestManagementBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import mas_lab.Main;

public class Person extends Agent {

	Hashtable<AID, Double> restMap;
	double minValue;
	
	protected void setup() {
		
		restMap = new Hashtable();
		minValue = Math.random() * Main.EvaluateRange;
		
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Restaurant");
		dfd.addServices(sd);
		DFAgentDescription[] results = new DFAgentDescription[0];
		try {
			results = DFService.search(this, dfd);
			System.out.println(results.length + " results from " + getLocalName());
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		System.out.println("Hi from " + getLocalName() + "! I prefer " + minValue);
		
		for (DFAgentDescription result : results){
			restMap.put(result.getName(), Math.random() * Main.EvaluateRange);
		}
		
		//addBehaviour(new Search());
	}

}
