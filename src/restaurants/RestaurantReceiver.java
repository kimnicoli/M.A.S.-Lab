package restaurants;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
 
public class RestaurantReceiver extends CyclicBehaviour {

	public RestaurantReceiver() {}

	public RestaurantReceiver(Agent a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if (msg == null) {
			//myAgent.addBehaviour(this);
			block();
		} else {
			switch (msg.getPerformative()){
				case ACLMessage.REQUEST:{
					if(((Restaurant)myAgent).isFree()){
						myAgent.addBehaviour(new RestaurantSender(ACLMessage.PROPOSE, msg.getSender()));
						//System.out.println("Sending restaurant proposal");
						block();
					} else {
						myAgent.addBehaviour(new RestaurantSender(ACLMessage.REFUSE, msg.getSender()));
						//System.out.println("Sending restaurant proposal");
						block();
					}
					break;
				}
				case ACLMessage.ACCEPT_PROPOSAL:{
					if(((Restaurant)myAgent).isFree()){
						myAgent.addBehaviour(new RestaurantSender(ACLMessage.INFORM, msg.getSender(), true));
						//System.out.println("Sending restaurant inform");
						((Restaurant)myAgent).fullness++;
						block();
					} else {
						myAgent.addBehaviour(new RestaurantSender(ACLMessage.FAILURE, msg.getSender()));
						//System.out.println("Sending restaurant failure");
						block();
					}
					break;
				}
				case ACLMessage.CONFIRM:{
					((Restaurant)myAgent).fullness = 0;
					break;
				}
				default: {
					//myAgent.addBehaviour(this);
					block();
				}
					
			}
		}

	}

	/*@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}*/

}
