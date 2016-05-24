package people;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class Eat extends Behaviour {
	
	AID place;

	public Eat() {
		place = null;
	}
	
	public Eat(AID place) {
		this.place = place;
	}

	public Eat(Agent a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		if(place == null) {
			System.out.println("Error, no place to go!");
			myAgent.doDelete();
			
		} else {
			ACLMessage msg = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
			msg.addReceiver(place);
			msg.setContent("Reserve me a table, please!");
			myAgent.send(msg);
			
			//Logger.log(me_at_place)
			
			System.out.println("I, " + myAgent.getLocalName() 
			+ ", will eat at " + place.getLocalName() + ", of which I think " 
			+ ((Person)myAgent).restMap.get(place));
			
			myAgent.addBehaviour(new Evaluate(place));
		}

	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
