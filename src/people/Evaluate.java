package people;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class Evaluate extends Behaviour {

	AID place;
	
	public Evaluate (AID place) {
		this.place = place;
	}
	
	public Evaluate(Agent a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if (msg == null) {
			myAgent.addBehaviour(this);
			block();
		} else {
			if(msg.getSender() == place){
				if(msg.getPerformative() == ACLMessage.FAILURE){
					myAgent.addBehaviour(new Search());
					block();
				} else if(msg.getPerformative() == ACLMessage.INFORM){
					double quality = Double.parseDouble(msg.getContent());
					double think = ((Person)myAgent).restMap.get(place);
					double dThink = ((Person)myAgent).boldness * (quality - think);
					((Person)myAgent).restMap.put(place, think + dThink);
					
					System.out.println("Now I, " + myAgent.getLocalName() 
										+ ", think of " + place.getLocalName()
										+ " this: " +(think + dThink));
					
					myAgent.addBehaviour(new Search());
					block();
				}
			}
		}
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
