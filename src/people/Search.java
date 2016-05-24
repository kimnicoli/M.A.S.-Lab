package people;

import java.util.Set;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import java.util.Iterator;
import java.util.Map;

public class Search extends Behaviour {
	
	public Search() {}
	
	public Search(Agent a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		
		if (!((Person)myAgent).restMap.isEmpty()) {
			Set set = ((Person)myAgent).restMap.entrySet();
			Iterator i = set.iterator();
			Map.Entry current;
			
			ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
			ACLMessage received;
			
			int receivers = 0;
			
			while(i.hasNext()){
				current = (Map.Entry)i.next();
				if((Double)(current.getValue()) > ((Person)myAgent).maxValue)
					break;
				receivers++;
				request.clearAllReceiver();
				request.addReceiver(((AID)current.getKey()));
				request.setContent("isFree?");
				myAgent.send(request);
			}
			myAgent.addBehaviour(new Receive(receivers));
		}
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
