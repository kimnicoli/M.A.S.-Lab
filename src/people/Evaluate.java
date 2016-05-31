package src.people;

import java.io.IOException;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class Evaluate extends OneShotBehaviour {

	AID place;
	double quality;
	PersonReceiver receiver;
	
	static AID global;
	
	public Evaluate (AID place) {
		this.place = place;
		System.out.println("qualcosa");
		
		if (global == null){
			global = getGlobal();
		}
	}
	
	public Evaluate (Agent a, double quality, AID place, PersonReceiver receiver) {
		myAgent = a;
		this.quality = quality;
		this.place = place;
		this.receiver = receiver;
		
		if (global == null){
			global = getGlobal();
		}
	}
	
	AID getGlobal(){
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Global");
		dfd.addServices(sd);
		try {
			AID gAID = DFService.search(myAgent, dfd)[0].getName();
			//System.out.println(gAID.getName());
			return gAID;
		} catch (FIPAException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public Evaluate(Agent a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		double think = ((Person)myAgent).restMap.get(place);
		double dThink = ((Person)myAgent).boldness * (quality - think);
		((Person)myAgent).restMap.put(place, think + dThink);
		
		System.out.println("Now I, " + myAgent.getLocalName() 
							+ ", think of " + place.getLocalName()
							+ " this: " +(think + dThink));
		
		System.out.println(((Person)myAgent).restMap);
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(global);
		try {
			msg.setContentObject(((Person)myAgent).restMap);
			//System.out.println("sent map");
		} catch(IOException e) {
			e.printStackTrace();
		}
		myAgent.send(msg);
	}
}
