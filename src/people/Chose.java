package src.people;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

public class Chose extends OneShotBehaviour {

	PersonReceiver receiver;
	TreeMap<AID, Double> free;
	
	public Chose(PersonReceiver receiver, TreeMap<AID, Double> free) {
		this.free = free;
		this.receiver = receiver;
	}

	public Chose(Agent a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		if(!free.isEmpty()){
			SortedSet<Map.Entry<AID, Double>> sortedset = new TreeSet<Map.Entry<AID, Double>>(
		            new Comparator<Map.Entry<AID, Double>>() {
		                @Override
		                public int compare(Map.Entry<AID, Double> e1,
		                        Map.Entry<AID, Double> e2) {
		                    return e1.getValue().compareTo(e2.getValue());
		                }
		            });
			sortedset.addAll(free.entrySet());
			
			//receiver.setCurrentTarget(free.firstEntry().getKey());
			//myAgent.addBehaviour(new Eat(free.firstEntry().getKey()));
			receiver.setCurrentTarget(sortedset.first().getKey());
			myAgent.addBehaviour(new Eat(sortedset.first().getKey()));
			//System.out.println("Eating there");
		} else {
			//TODO
			myAgent.addBehaviour(new Eat());
			//System.out.println("Eating nowhere");
		}

	}

}
