package src.restaurants;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class RestaurantSender extends OneShotBehaviour {

	AID receiver;
	int performative;
	boolean ranking;
	
	public RestaurantSender(int performative, AID reciever) {
		this.receiver = reciever;
		this.performative = performative;
		this.ranking = false;
	}
	
	public RestaurantSender(int performative, AID receiver, boolean ranking) {
		this.receiver = receiver;
		this.performative = performative;
		this.ranking = ranking;
	}

	@Override
	public void action() {
		ACLMessage msg = new ACLMessage(performative);
		msg.addReceiver(receiver);
		
		if(ranking){
			msg.setContent(Double.toString(((Restaurant)myAgent).quality));
			msg.setOntology("Ranking");
		}
		
		myAgent.send(msg);
		
		myAgent.addBehaviour(new RestaurantReceiver());
	}
}
