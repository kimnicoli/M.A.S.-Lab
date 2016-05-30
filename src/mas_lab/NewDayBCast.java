package src.mas_lab;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.AMSService;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;

public class NewDayBCast extends OneShotBehaviour {

	public NewDayBCast() {
		// TODO Auto-generated constructor stub
	}

	public NewDayBCast(Agent a) {
		super(a);
		myAgent = a;
		// TODO Auto-generated constructor stub
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
			
			((Global)myAgent).incrementTurn();
			msg.setContent("Start: turn " + ((Global)myAgent).getTurn());
			myAgent.send(msg);
		} catch(FIPAException e) {
			e.printStackTrace();
		}
	}

}
