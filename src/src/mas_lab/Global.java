package src.mas_lab;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Global extends Agent {
	
	int turn;
	int nPeople;
	int nRestaurants;
	
	protected void setup() {
		Object[] args = getArguments();
		/*
		 * Formattazione:
		 * args[0] : int nRestaurant
		 * args[1] : int nPeople
		 */
		nRestaurants = (Integer)args[0];
		nPeople = (Integer)args[1];
		
		DFAgentDescription mydfd = new DFAgentDescription();
		ServiceDescription mysd = new ServiceDescription();
		mysd.setType("Global");
		mysd.setName(this.getName());
		mydfd.addServices(mysd);
		try {
			DFService.register(this, mydfd);
		} catch (FIPAException e){
			e.printStackTrace();
		}
		
		InitRestaurants();
		
		DFAgentDescription[] results = new DFAgentDescription[0];
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Restaurant");
		dfd.addServices(sd);
		
		//Launcher.instance().InitRestaurants();
		
		while(results.length < nRestaurants) {		
			try {
				results = DFService.search(this, dfd);
				
			} catch (FIPAException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("outta here, with " + nRestaurants);
		
		try{
			InitPeople();
		} catch(NullPointerException e) {
			e.printStackTrace();
		}
		
		turn = 0;
		
		DFAgentDescription pdfd = new DFAgentDescription();
		ServiceDescription psd = new ServiceDescription();
		psd.setType("Person");
		pdfd.addServices(psd);
		
		
		DFAgentDescription[] presults = new DFAgentDescription[0];
		
		while(presults.length < nPeople) {		
			try {
				presults = DFService.search(this, pdfd);
				
			} catch (FIPAException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("outta here, with " + results.length);
		
		
		addBehaviour(new GlobalReceiver(this, presults, results));
	}
	
	public void InitRestaurants () {
		AgentController ac;
		ContainerController cc = this.getContainerController();
		for(int i = 0; i < nRestaurants; i++) {
			try {
				ac = cc.createNewAgent("Restaurant " + i, "src.restaurants.Restaurant", null);
				ac.start();
			} catch(StaleProxyException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void InitPeople () {		
		AgentController ac;
		ContainerController cc = this.getContainerController();
		for(int i = 0; i < nPeople; i++) {
			try {
				ac = cc.createNewAgent("Person " + i, "src.people.Person", null);
				ac.start();
			} catch(StaleProxyException e) {
				e.printStackTrace();
			}
		}
	}

	public int getTurn() {
		return turn;
	}
	
	public void incrementTurn() {
		turn++;
	}
}
