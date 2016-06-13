package src.people;

import java.io.IOException;
import java.util.Hashtable;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import src.mas_lab.Main;

public class Evaluate extends OneShotBehaviour {

	AID place;
	double quality;
	
	static AID global;

	public Evaluate(Agent a, AID place) {
		myAgent = a;
		this.place = place;

		if (global == null) {
			global = getGlobal();
		}
	}

	public Evaluate(Agent a, double quality, AID place) {
		myAgent = a;
		this.quality = quality;
		this.place = place;

		if (global == null) {
			global = getGlobal();
		}
	}

	AID getGlobal() {
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Global");
		dfd.addServices(sd);
		try {
			AID gAID = DFService.search(myAgent, dfd)[0].getName();
			return gAID;
		} catch (FIPAException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void action() {
		if(place != null){
			double think = ((Person) myAgent).restMap.get(place);
			double dThink = ((Person) myAgent).boldness * (quality - think);
	
			think += dThink;
			if (think < 0)
				think = 0;
			else if (think > Main.EvaluateRange)
				think = Main.EvaluateRange;
	
			((Person) myAgent).restMap.put(place, think);
	
	
			// Mappa locale di tutte le persone che mi hanno parlato di place (bene
			// o male)
			Hashtable<AID, Double> map = ((Person) myAgent).opinions.get(place);
			for (AID person : map.keySet()) {
				double myTrust = ((Person) myAgent).worldTrust.get(person);
				double difference = Math.abs(think - map.get(person)) / Main.EvaluateRange;
				double newTrust = 1 - difference;
				myTrust = 0.5 * (myTrust + newTrust);
				((Person) myAgent).worldTrust.put(person, myTrust);
			}
	
			ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
			msg2.setOntology("Reviews");
			map = new Hashtable<AID, Double>();
			map.put(place, ((Person) myAgent).restMap.get(place));
			try {
				msg2.setContentObject(map);
			} catch (IOException e) {
				e.printStackTrace();
			}
	
			for (AID address : ((Person) myAgent).friends) {
				msg2.addReceiver(address);
			}
			myAgent.send(msg2);
		}
		
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(global);
		
		Object[] objsend = new Object[3];
		objsend[0] = ((Person) myAgent).worldTrust;
		objsend[1] = ((Person) myAgent).restMap;
		if(place != null)
			objsend[2] = place.getLocalName();
		else
			objsend[2] = "Nowhere -1";
		try {
			msg.setContentObject(objsend);
		} catch (IOException e) {
			e.printStackTrace();
		}
		myAgent.send(msg);
	}
}
