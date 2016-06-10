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
	
	public NewDayBCast(Agent a) {
		super(a);
		myAgent = a;
		this.results = null;
	}
	
	public NewDayBCast(Agent a, DFAgentDescription[] results) {
		super(a);
		myAgent = a;
		this.results = results;
	}

	@Override
	public void action() {
		ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
		try{
			DFAgentDescription[] amsd = DFService.search(myAgent, new DFAgentDescription());
			for (DFAgentDescription amsAgentDescription : amsd) {
				//if(!(myAgent.getAID().equals(amsAgentDescription.getName())))
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
			else{
				((Global)myAgent).incrementTurn();
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
