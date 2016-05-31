package src.people;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

public class Search extends OneShotBehaviour {
	
	PersonReceiver receiver;
	
	public Search() {}
	
	public Search(PersonReceiver receiver) {
		this.receiver = receiver;
	}
	
	public Search(Agent a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		
		if (!((Person)myAgent).restMap.isEmpty()) {
			SortedSet<Map.Entry<AID, Double>> sortedset = new TreeSet<Map.Entry<AID, Double>>(
		            new Comparator<Map.Entry<AID, Double>>() {
		                @Override
		                public int compare(Map.Entry<AID, Double> e1,
		                        Map.Entry<AID, Double> e2) {
		                    return e1.getValue().compareTo(e2.getValue());
		                }
		            });
			sortedset.addAll(((Person)myAgent).restMap.entrySet());
			
			Vector<AID> receivers = new Vector<AID>();
			//Set set = ((Person)myAgent).restMap.entrySet();
			Iterator i = sortedset.iterator();
			Map.Entry current;
			
			ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
			ACLMessage received;
			
			//int receivers = 0;
			
			while(i.hasNext()){
				current = (Map.Entry)i.next();
				if((Double)(current.getValue()) > ((Person)myAgent).maxValue)
					break;
				//receivers++;
				request.clearAllReceiver();
				request.addReceiver(((AID)current.getKey()));
				request.setContent("isFree?");
				myAgent.send(request);
				
				receivers.addElement(((AID)current.getKey()));
				
				//System.out.println("Request sent");
			}
			
			receiver.setReceivers(receivers);
			if(receivers.size() == 0)
				myAgent.addBehaviour(new Evaluate(myAgent, null));
			
			//System.out.println("Sent " + receivers.size() + " messages");
			//myAgent.addBehaviour(new Receive(receivers));
		}
	}

	/*@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}*/

}
