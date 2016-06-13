package src.people;

import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
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
	Vector<AID> fullOnes;
	
	public Search() {}
	
	/*public Search(PersonReceiver receiver) {
		this.receiver = receiver;
		this.fullOnes = new Vector<AID>();
	}*/
	
	public Search(PersonReceiver receiver, Vector<AID> fullOnes) {
		this.receiver = receiver;
		this.fullOnes = fullOnes;
	}

	@Override
	public void action() {
		
		if (!((Person)myAgent).restMap.isEmpty()) {
			SortedSet<Map.Entry<AID, Double>> sortedset = new TreeSet<Map.Entry<AID, Double>>(
		            new Comparator<Map.Entry<AID, Double>>() {
		                @Override
		                public int compare(Map.Entry<AID, Double> e1, Map.Entry<AID, Double> e2) {
		                	if(e1.getKey().equals(e2.getKey()))
		                		return 0;
		                	else{
		                		int diff = -e1.getValue().compareTo(e2.getValue());
		                		if(diff == 0.0)
		                			return e1.getKey().compareTo(e2.getKey());
		                		else 
		                			return diff;
		                	}
		                }
		            });
			sortedset.addAll(((Person)myAgent).restMap.entrySet());
			
			
			Vector<AID> receivers = new Vector<AID>();
			
			ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
			request.clearAllReceiver();
			
			while(sortedset.size() != 0) {
				Map.Entry<AID, Double> current = sortedset.first();
				sortedset.remove(current);
				if(!fullOnes.contains(current.getKey())){
					if((Double)(current.getValue()) > ((Person)myAgent).maxValue)
						break;
					request.addReceiver(current.getKey());
					request.setContent("isFree?");
					myAgent.send(request);
					
					receivers.addElement(current.getKey());
				} else
					System.out.println("Excluded " + current.getKey().getLocalName());
			}
			
			
			receiver.setReceivers(receivers);
			if(receivers.size() == 0){
				System.err.println(myAgent.getLocalName() + ": No good found");
				myAgent.addBehaviour(new Evaluate(myAgent, null));
			}
		}
	}
}
