package src.people;

import java.util.Hashtable;
import java.util.TreeMap;
import java.util.Vector;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class Receive extends Behaviour {

	int receivers;
	int arrived;
	TreeMap<AID, Double> free;
	
	public Receive(int receivers) {
		this.receivers = receivers;
		arrived = 0;
		free = new TreeMap<AID, Double>();
	}

	public Receive(Agent a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if (msg == null) {
			//System.out.println("Receive loop");
			myAgent.addBehaviour(this);
			block();
		} else {
			if(!free.containsKey(msg.getSender())){
				if (msg.getPerformative() == ACLMessage.PROPOSE){
					System.out.println("Received propose");
					arrived++;
					free.put(msg.getSender(), ((Person)myAgent).restMap.get(msg.getSender()));
				}
				else if (msg.getPerformative() == ACLMessage.REFUSE){
					arrived++;
					System.out.println("Received refuse");
				}
			}
			if(arrived == receivers) {
				if(!free.isEmpty()){
					myAgent.addBehaviour(new Eat(free.firstEntry().getKey()));
					System.out.println("Eating there");
					block();
				} else {
					myAgent.addBehaviour(new Eat());
					System.out.println("Eating nowhere");
					block();
				}
			} else {
				myAgent.addBehaviour(this);
				System.out.println("Receive loop 2");
				block();
			}
		}
		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
