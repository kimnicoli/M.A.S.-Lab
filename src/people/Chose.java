package src.people;

import java.util.TreeMap;

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
			receiver.setCurrentTarget(free.firstEntry().getKey());
			myAgent.addBehaviour(new Eat(free.firstEntry().getKey()));
			//System.out.println("Eating there");
		} else {
			//TODO
			myAgent.addBehaviour(new Eat());
			//System.out.println("Eating nowhere");
		}

	}

}
