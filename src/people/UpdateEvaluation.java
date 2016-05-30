package src.people;

import java.util.Hashtable;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

public class UpdateEvaluation extends OneShotBehaviour {

	Hashtable<AID, Double> map;
	AID sender;
	
	public UpdateEvaluation(Hashtable<AID, Double> map, AID sender) {
		this.map = map;
		this.sender = sender;
	}

	public UpdateEvaluation(Agent a) {
		super(a);
		
	}

	@Override
	public void action() {
		for(AID address : map.keySet()){
			double think = ((Person)myAgent).restMap.get(address);
			double ratio = ((Person)myAgent).worldThrust.get(sender);
			double meanThink = (map.get(address)*ratio + think)/(1 + ratio);
			((Person)myAgent).restMap.put(address, meanThink);
		}

	}

}
