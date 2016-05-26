package people;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class Evaluate extends OneShotBehaviour {

	AID place;
	double quality;
	PersonReceiver receiver;
	
	public Evaluate (AID place) {
		this.place = place;
		System.out.println("qualcosa");
	}
	
	public Evaluate (double quality, AID place, PersonReceiver receiver) {
		this.quality = quality;
		this.place = place;
		this.receiver = receiver;
		System.out.println("qualcosa");
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
		
		//myAgent.addBehaviour(new Search(receiver));
		/*ACLMessage msg = myAgent.receive();
		if (msg == null) {
			myAgent.addBehaviour(this);
			block();
			//System.out.println("Eval loop");
		} else {
			if(msg.getSender() == place){
				if(msg.getPerformative() == ACLMessage.FAILURE){
					myAgent.addBehaviour(new Search());
					System.out.println("Received failure");
					block();
				} else if(msg.getPerformative() == ACLMessage.INFORM){
					System.out.println("Received inform");
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
			} else {
				myAgent.addBehaviour(this);
				block();
			}
		}*/
		
	}

	/*@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}*/

}
