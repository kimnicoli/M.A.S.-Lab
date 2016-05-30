package mas_lab;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.AMSService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.lang.acl.ACLMessage;

public class NewDayBCast extends OneShotBehaviour {

	public NewDayBCast() {
		// TODO Auto-generated constructor stub
	}

	public NewDayBCast(Agent a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
		try{
			AMSAgentDescription[] amsd = AMSService.search(myAgent, new AMSAgentDescription());
			for (AMSAgentDescription amsAgentDescription : amsd) {
				if(!myAgent.getAID().equals(amsAgentDescription.getName()))
					msg.addReceiver(amsAgentDescription.getName());
			}
			
			((Global)myAgent).incrementTurn();
			msg.setContent("Start: turn " + ((Global)myAgent).getTurn());
			myAgent.send(msg);
		} catch(FIPAException e) {
			e.printStackTrace();
		}
	}

}
