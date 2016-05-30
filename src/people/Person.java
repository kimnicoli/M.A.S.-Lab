package people;

import java.util.TreeMap;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import mas_lab.Main;

public class Person extends Agent {

	TreeMap<AID, Double> restMap;
	double maxValue; //Consideriamo una scala di valutazione inversa. Perché la TreeMap ordina dal più basso al più alto
	double boldness;
	
	protected void setup() {
		
		restMap = new TreeMap();
		maxValue = Math.random() * Main.EvaluateRange;//Main.EvaluateRange;//
		boldness = Main.MinBoldness + Math.random() * (Main.MaxBoldness - Main.MinBoldness);
		
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
		System.out.println("Hi from " + getLocalName() + "! I prefer " + maxValue);
		
		for (DFAgentDescription result : results){
			restMap.put(result.getName(), Math.random() * Main.EvaluateRange);
		}
		
		PersonReceiver receiver = new PersonReceiver();
		addBehaviour(receiver);
		addBehaviour(new Search(receiver));
	}

}
