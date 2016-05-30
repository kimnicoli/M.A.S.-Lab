package mas_lab;

import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import people.Chose;
import people.Evaluate;
import people.Person;
import people.Search;

public class GlobalReceiver extends CyclicBehaviour {
	
	Vector<AID> allRestaurants;
	Vector<AID> allPeople;
	Vector<AID> currentRestaurant;
	Vector<AID> currentPeople;
	int PeopleReceived;
	int RestaurantReceived;

	public GlobalReceiver() {
		allRestaurants = new Vector<AID>();
		allPeople = new Vector<AID>();
		getAll();
		reset();
	}

	public GlobalReceiver(Agent a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if (msg == null) {
			//System.out.println("Receive loop");
			block();
		} else {
			switch (msg.getPerformative()) {
				case (ACLMessage.INFORM):{
					//System.out.println("Received inform from " + msg.getSender());
					//System.out.println(currentTarget);
					
					if(currentPeople.contains(msg.getSender())){
						//System.out.println("Starting valutation");
						currentPeople.remove(msg.getSender());
						PeopleReceived ++;
						
						if(PeopleReceived == allPeople.size()){
							myAgent.addBehaviour(new Log(true));
							reset();
						}
						else{
							myAgent.addBehaviour(new Log(false));
						}
						
						
					}
					break;
				}
				
				default:
					block();
			}
		}
	}
	
	public void reset(){
		currentRestaurant=allRestaurants;
		currentPeople=allPeople;
		PeopleReceived=0;
		RestaurantReceived=0;
		myAgent.addBehaviour(new NewDayBCast());
	}
	
}
