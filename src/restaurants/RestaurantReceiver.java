package src.restaurants;

import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
 
public class RestaurantReceiver extends CyclicBehaviour {

	Vector<AID> booked;
	
	public RestaurantReceiver() {
		booked = new Vector<AID>();
	}

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
					if(((Restaurant)myAgent).isFree() & !booked.contains(msg.getSender())){
						myAgent.addBehaviour(new RestaurantSender(ACLMessage.INFORM, msg.getSender(), true));
						((Restaurant)myAgent).fullness++;
						booked.add(msg.getSender());
						block();
					} else {
						if(booked.contains(msg.getSender()))
							System.err.println(myAgent.getLocalName() + ": Received double booking from " 
												+ msg.getSender().getLocalName());
						myAgent.addBehaviour(new RestaurantSender(ACLMessage.FAILURE, msg.getSender()));
						block();
					}
					break;
				}
				case ACLMessage.CONFIRM:{
					if(msg.getOntology().equals("Restaurant Reset")){
						((Restaurant)myAgent).fullness = 0;
						booked.clear();
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
