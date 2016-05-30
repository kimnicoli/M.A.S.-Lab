package mas_lab;

import java.util.TreeMap;
import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import people.Chose;
import people.Evaluate;
import people.Person;
import people.Search;

public class GlobalReceiver extends CyclicBehaviour {
	
	Vector<DFAgentDescription> allRestaurants;
	Vector<DFAgentDescription> allPeople;
	Vector<DFAgentDescription> currentRestaurant;
	Vector<DFAgentDescription> currentPeople;
	int PeopleReceived;
	int RestaurantReceived;

	public GlobalReceiver() {
		allRestaurants = new Vector<DFAgentDescription>();
		allPeople = new Vector<DFAgentDescription>();
		getAll();
		reset();
	}
	
	void getAll() {
		DFAgentDescription pdfd = new DFAgentDescription();
		ServiceDescription psd = new ServiceDescription();
		psd.setType("Person");
		pdfd.addServices(psd);
		DFAgentDescription[] results;
 		try {
			results = DFService.search(myAgent, pdfd);
			for(DFAgentDescription dfd : results)
				if(dfd != null)
					allPeople.add(dfd);
		} catch(FIPAException e) {
			e.printStackTrace();
		}
 		
 		DFAgentDescription rdfd = new DFAgentDescription();
		ServiceDescription rsd = new ServiceDescription();
		rsd.setType("Restaurant");
		rdfd.addServices(rsd);
 		try {
			results = DFService.search(myAgent, rdfd);
			for(DFAgentDescription dfd : results)
				if(dfd != null)
					allRestaurants.add(dfd);
		} catch(FIPAException e) {
			e.printStackTrace();
		}
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
							try{
								myAgent.addBehaviour(new Log((DFAgentDescription[])allRestaurants.toArray(),
										msg.getSender(), (TreeMap<AID, Double>)msg.getContentObject(), true));
							} catch(UnreadableException e) {
								e.printStackTrace();
							}
							reset();
						}
						else{
							try{
								myAgent.addBehaviour(new Log((DFAgentDescription[])allRestaurants.toArray(),
										msg.getSender(), (TreeMap<AID, Double>)msg.getContentObject(), false));
							} catch(UnreadableException e) {
								e.printStackTrace();
							}
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
