package src.people;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class Eat extends  OneShotBehaviour {
	
	AID place;

	public Eat() {
		place = null;
	}
	
	public Eat(AID place) {
		this.place = place;
	}

	@Override
	public void action() {
		if(place == null) {
			System.err.println("Error, no place to go!");
			myAgent.addBehaviour(new Evaluate(myAgent, -1.0, null));
			
		} else {
			ACLMessage msg = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
			msg.addReceiver(place);
			msg.setContent("Reserve me a table, please!");
			myAgent.send(msg);
			
			System.out.println("I, " + myAgent.getLocalName() 
			+ ", will eat at " + place.getLocalName() + ", of which I think " 
			+ ((Person)myAgent).restMap.get(place));
		}

	}
}
