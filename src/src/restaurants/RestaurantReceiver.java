package src.restaurants;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
 
public class RestaurantReceiver extends CyclicBehaviour {

	public RestaurantReceiver() {}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if (msg == null) {
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
					break;
				}
				case ACLMessage.ACCEPT_PROPOSAL:{
					if(((Restaurant)myAgent).isFree()){
						myAgent.addBehaviour(new RestaurantSender(ACLMessage.INFORM, msg.getSender(), true));
						((Restaurant)myAgent).fullness++;
						block();
					} else {
						myAgent.addBehaviour(new RestaurantSender(ACLMessage.FAILURE, msg.getSender()));
						block();
					}
					break;
				}
				case ACLMessage.CONFIRM:{
					if(msg.getOntology().equals("Restaurant Reset")){
						((Restaurant)myAgent).fullness = 0;
						myAgent.addBehaviour(new RestaurantSender(ACLMessage.PROPAGATE, msg.getSender()));
					}
					break;
				}
				default: {
					block();
				}
					
			}
		}

	}
}
