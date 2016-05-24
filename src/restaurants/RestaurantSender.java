package restaurants;

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

	public RestaurantSender(Agent a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		ACLMessage msg = new ACLMessage(performative);
		msg.addReceiver(receiver);
		
		if(ranking)
			msg.setContent(Double.toString(((Restaurant)myAgent).quality));
		
		myAgent.send(msg);
		System.out.println("Sent restaurant message");
		
		myAgent.addBehaviour(new RestaurantReceiver());
	}

	/*@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}*/

}
