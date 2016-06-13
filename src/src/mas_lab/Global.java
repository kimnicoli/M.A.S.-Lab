package src.mas_lab;

import org.json.simple.JSONObject;

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
	
	//Oggetti JSON per il salvataggio del settaggio della simulazione corrente
	JSONObject myPeople;
	JSONObject myRestaurants;
	
	protected void setup() {
		Object[] args = getArguments();
		/*
		 * Formattazione:
		 * args[0] : int nRestaurant
		 * args[1] : int nPeople
		 * args[2] : JSONObject myRestaurants
		 * args[3] : JSONObject myPeople
		 */
		//inizializzo le variabili
		nRestaurants = (Integer)args[0];
		nPeople = (Integer)args[1];
		myRestaurants = (JSONObject)args[2];
		myPeople = (JSONObject)args[3];
		System.out.println(myPeople);
		System.out.println(myRestaurants);
		turn = 0;
		
		//Registro il Global al DF
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
		
		//Aspetto finché tutti i ristoranti sono attivi		
		while(results.length < nRestaurants) {		
			try {
				results = DFService.search(this, dfd);
				
			} catch (FIPAException e) {
				e.printStackTrace();
			}
		}
		

		InitPeople(results);
		
		DFAgentDescription pdfd = new DFAgentDescription();
		ServiceDescription psd = new ServiceDescription();
		psd.setType("Person");
		pdfd.addServices(psd);
		
		//Aspetto finché tutte le persone non sono attive
		DFAgentDescription[] presults = new DFAgentDescription[0];
		while(presults.length < nPeople) {		
			try {
				presults = DFService.search(this, pdfd);
				
			} catch (FIPAException e) {
				e.printStackTrace();
			}
		}
		
		//Turno di inizializzazione
		addBehaviour(new NewDayBCast(this, presults));
		//Inizio la simulazione
		addBehaviour(new GlobalReceiver(this, presults, results));
	}
	
	public void InitRestaurants () {
		AgentController ac;
		ContainerController cc = this.getContainerController();
		for(int i = 0; i < nRestaurants; i++) {
			try {
				Object[] args = new Object[1];
				String key = "Restaurant " + i;
				if(myRestaurants != null){
					args[0] = myRestaurants.get(key);
				} else
					args[0] = null;
				ac = cc.createNewAgent(key, "src.restaurants.Restaurant", args);
				ac.start();
			} catch(StaleProxyException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void InitPeople (DFAgentDescription[] results) {		
		AgentController ac;
		ContainerController cc = this.getContainerController();
		for(int i = 0; i < nPeople; i++) {
			try {
				Object[] args = new Object[2];
				String key = "Person " + i;
				if(myPeople != null){
					args[0] = myPeople.get(key);
				} else
					args[0] = null;
				args[1] = results;
				ac = cc.createNewAgent(key, "src.people.Person", args);
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
