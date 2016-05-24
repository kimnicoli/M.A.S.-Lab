package restaurants;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.introspection.AddedBehaviour;
import jade.lang.acl.ACLMessage;

public class RestaurantReceiver extends Behaviour {

	public RestaurantReceiver() {}

	public RestaurantReceiver(Agent a) {
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
			switch (msg.getPerformative()){
				case ACLMessage.REQUEST:{
					if(((Restaurant)myAgent).isFree()){
						myAgent.addBehaviour(new RestaurantSender(ACLMessage.PROPOSE, msg.getSender()));
						block();
					} else {
						myAgent.addBehaviour(new RestaurantSender(ACLMessage.REFUSE, msg.getSender()));
						block();
					}
				}
				case ACLMessage.ACCEPT_PROPOSAL:{
					if(((Restaurant)myAgent).isFree()){
						myAgent.addBehaviour(new RestaurantSender(ACLMessage.INFORM, msg.getSender(), true));
						block();
					} else {
						myAgent.addBehaviour(new RestaurantSender(ACLMessage.FAILURE, msg.getSender()));
						block();
					}
				}
				default: {
					myAgent.addBehaviour(this);
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
