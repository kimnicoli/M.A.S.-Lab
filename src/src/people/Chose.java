package src.people;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import java.util.HashMap;
import src.mas_lab.Main;

public class Chose extends OneShotBehaviour {

	PersonReceiver receiver;
	TreeMap<AID, Double> free;
	
	public Chose(PersonReceiver receiver, TreeMap<AID, Double> free) {
		this.free = free;
		this.receiver = receiver;
	}

	@Override
	public void action() {
		if(!free.isEmpty()){
			SortedSet<Map.Entry<AID, Double>> sortedset = new TreeSet<Map.Entry<AID, Double>>(
					new Comparator<Map.Entry<AID, Double>>() {
						@Override
						public int compare(Map.Entry<AID, Double> e1, Map.Entry<AID, Double> e2) {
							if (e1.getKey().equals(e2.getKey()))
								return 0;
							else {
								int diff = -e1.getValue().compareTo(e2.getValue());
								if (diff == 0.0)
									return e1.getKey().compareTo(e2.getKey());
								else
									return diff;
							}
						}
					});
			//double random = Math.random();
			//if(random > Main.ProbChooseBFF + Main.ProbChoseRandom){
				sortedset.addAll(free.entrySet());
				receiver.setCurrentTarget(sortedset.first().getKey());
				myAgent.addBehaviour(new Eat(sortedset.first().getKey()));
				System.out.println(myAgent.getLocalName() + ": Eating Now");
				//System.err.println("!!Normal choice!!");
			/*} else if(random < Main.ProbChooseBFF){
				sortedset.addAll(((Person)myAgent).worldTrust.entrySet());
				AID bff = null;
				while(!((Person)myAgent).friends.contains(bff)){
					bff = sortedset.first().getKey();
					sortedset.remove(sortedset.first());
				}
				sortedset.clear();
				
				if(!(bff != null)){
					sortedset.clear();
					sortedset.addAll(free.entrySet());
					receiver.setCurrentTarget(sortedset.first().getKey());
					myAgent.addBehaviour(new Eat(sortedset.first().getKey()));
					System.err.println("!!Normal choice!!");
					return;
				}
				
				HashMap<AID, Double> map = new HashMap<AID, Double>();
				
				for(AID rest : ((Person)myAgent).opinions.keySet()){
					if(((Person)myAgent).opinions.get(rest).containsKey(bff))
						map.put(rest, ((Person)myAgent).opinions.get(rest).get(bff));
				}
				sortedset.addAll(map.entrySet());
				AID target = null;
				if(sortedset.size() != 0)
					target = sortedset.first().getKey();
				if(target != null){
					receiver.setCurrentTarget(target);
					myAgent.addBehaviour(new Eat(target));
					
					System.err.println("!!BFF Choose!!");
				} else {
					sortedset.clear();
					sortedset.addAll(free.entrySet());
					receiver.setCurrentTarget(sortedset.first().getKey());
					myAgent.addBehaviour(new Eat(sortedset.first().getKey()));
					System.err.println("!!Normal choice!!");
					return;
				}
			} else {
				int rndIndex = (int)(Math.random()*free.size());
				if(rndIndex >= free.size())
					rndIndex = free.size() - 1;
				else if (rndIndex < 0)
					rndIndex = 0;
				AID chosenOne = free.keySet().toArray(new AID[0])[rndIndex];
				receiver.setCurrentTarget(chosenOne);
				myAgent.addBehaviour(new Eat(chosenOne));
				System.err.println("!!Random choice!!");
			}*/
			
		} else {
			myAgent.addBehaviour(new Eat());
		}

	}

}
