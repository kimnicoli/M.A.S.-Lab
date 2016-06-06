package src.people;

import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import java.util.SortedSet;
import src.mas_lab.Main;

public class Person extends Agent {

	TreeMap<AID, Double> restMap;
	double maxValue; //Consideriamo una scala di valutazione inversa. Perché la TreeMap ordina dal più basso al più alto
	double boldness;
	
	Hashtable<AID, Double> worldTrust;
	Vector<AID> friends;
	
	Hashtable<AID, Hashtable<AID, Double>> opinions;
	
	protected void setup() {
		
		restMap = new TreeMap();
		maxValue = Main.EvaluateRange;//Math.random() * Main.EvaluateRange;//
		boldness = Main.MinBoldness + Math.random() * (Main.MaxBoldness - Main.MinBoldness);
		worldTrust = new Hashtable<AID, Double>();
		friends = new Vector<AID>();
		opinions = new Hashtable<AID, Hashtable<AID,Double>>();
		
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
			opinions.put(result.getName(), new Hashtable<AID, Double>());
		}
		
		DFAgentDescription mydfd = new DFAgentDescription();
		ServiceDescription mysd = new ServiceDescription();
		mysd.setType("Person");
		mysd.setName(this.getName());
		mydfd.addServices(mysd);
		try {
			DFService.register(this, mydfd);	
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		PersonReceiver receiver = new PersonReceiver();
		addBehaviour(receiver);
		//addBehaviour(new Search(receiver));
		
	}
}
