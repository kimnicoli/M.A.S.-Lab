package mas_lab;

import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Global extends Agent {
	
	protected void setup() {
		DFAgentDescription[] results = new DFAgentDescription[0];
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Restaurant");
		dfd.addServices(sd);
		
		//Launcher.instance().InitRestaurants();
		
		while(results.length < Launcher.instance().getnRestaurants()) {		
			try {
				results = DFService.search(this, dfd);
				
			} catch (FIPAException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("outta here, with " + Launcher.instance().getnRestaurants());
		
		try{
			InitPeople();
		} catch(NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	public void InitPeople () {		
		AgentController ac;
		ContainerController cc = this.getContainerController();
		for(int i = 0; i < Launcher.instance().getnPeople(); i++) {
			try {
				ac = cc.createNewAgent("Person " + i, "people.Person", null);
				ac.start();
			} catch(StaleProxyException e) {
				e.printStackTrace();
			}
		}
	}
}
