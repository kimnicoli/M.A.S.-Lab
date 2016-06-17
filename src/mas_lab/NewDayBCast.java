package src.mas_lab;

import java.io.IOException;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.AMSService;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;

public class NewDayBCast extends OneShotBehaviour {

	DFAgentDescription[] results;
	boolean restReset;
	
	/**
	 * Costruttore standard. Usato per la partenza delle persone
	 * @param a agente responsabile dell'esecuzione (Global)
	 */
	public NewDayBCast(Agent a) {
		super(a);
		myAgent = a;
		this.results = null;
		this.restReset = false;
	}
	
	/**
	 * Costruttore per il reset dei ristoranti
	 * @param a agente responsabile dell'esecuzione (Global)
	 * @param restReset flag per eseguire il reset dei ristoranti. <b>Deve essere vero</b>
	 */
	public NewDayBCast(Agent a, boolean restReset) {
		super(a);
		myAgent = a;
		this.results = null;
		this.restReset = restReset;
	}
	
	/**
	 * Costruttore per il turno di inizializzazione
	 * @param a agente responsabile dell'esecuzione (Global)
	 * @param results lista di tutte le persone attive
	 */
	public NewDayBCast(Agent a, DFAgentDescription[] results) {
		super(a);
		myAgent = a;
		this.results = results;
		this.restReset = false;
	}

	@Override
	public void action() {
		ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
		try{
			DFAgentDescription[] amsd = DFService.search(myAgent, new DFAgentDescription());
			for (DFAgentDescription amsAgentDescription : amsd) {
				msg.addReceiver(amsAgentDescription.getName());
			}
			
			System.out.println(amsd.length);
			
			if(results != null)
				try {
					msg.setOntology("Init Phase");
					msg.setContentObject(results);
					System.out.println("-------------------Init Phase-------------------");
				} catch (IOException e) {
					e.printStackTrace();
				}
			else if (restReset) {
				((Global)myAgent).incrementTurn();
				msg.setOntology("Restaurant Reset");
				System.out.println("-------------------Restaurant Reset-------------------");
			} else {
				msg.setOntology("New Turn");
				msg.setContent(Integer.toString(((Global)myAgent).getTurn()));
				System.out.println("-------------------New Turn-------------------");
			}
			myAgent.send(msg);
		} catch(FIPAException e) {
			e.printStackTrace();
		}
	}

}
