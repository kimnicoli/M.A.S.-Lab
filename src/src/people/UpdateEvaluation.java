package src.people;

import java.util.Hashtable;
import java.util.Map;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

public class UpdateEvaluation extends OneShotBehaviour {

	Hashtable<AID, Double> map;
	AID sender;
	
	public UpdateEvaluation(Agent a, Hashtable<AID, Double> map, AID sender) {
		myAgent = a;
		this.map = map;
		this.sender = sender;
	}

	public UpdateEvaluation(Agent a) {
		super(a);
		
	}

	@Override
	public void action() {
		for(AID address : map.keySet()){
			System.out.println("Received opinion from" + sender.getLocalName());
			
			//Aggiungo l'opinione di sender alla mia lista di opinioni per 
			//		poter valutare la sua affidabilità in futuro
			((Person)myAgent).opinions.get(address).put(sender, map.get(address));
			
			double think = ((Person)myAgent).restMap.get(address);
			double ratio = ((Person)myAgent).worldTrust.get(sender);
			double meanThink = (map.get(address)*ratio + think)/(1 + ratio);
			((Person)myAgent).restMap.put(address, meanThink);
			
			System.out.println("Now I, " + myAgent.getLocalName() 
					+ ", think of " + address.getLocalName()
					+ " this: " + meanThink + "\n I thought " + think
					+ " It is " + sender.getLocalName() + " fault!");
		}

	}

}
