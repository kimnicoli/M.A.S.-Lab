package src.people;

import java.util.Hashtable;
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
import src.mas_lab.Launcher;
import src.mas_lab.Main;

public class PersonReceiver extends CyclicBehaviour {

	AID currentTarget;
	int maxReceivers;
	int arrived;
	TreeMap<AID, Double> free;
	Vector<AID> receivers;
	
	public PersonReceiver() {
		free = new TreeMap<AID, Double>();
		receivers = new Vector<AID>();
	}

	public PersonReceiver(Agent a) {
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
				case (ACLMessage.REFUSE):{
					//System.out.println("Received refuse");
					if(receivers.contains(msg.getSender())){
						arrived++;
						receivers.remove(msg.getSender());
					}
					if(arrived == maxReceivers)
						myAgent.addBehaviour(new Chose(this, free));
					break;
				}
				case (ACLMessage.PROPOSE):{
					//System.out.println("Received propose");
					if(receivers.contains(msg.getSender())){
						arrived++;
						receivers.remove(msg.getSender());
						free.put(msg.getSender(), ((Person)myAgent).restMap.get(msg.getSender()));
					}
					if(arrived == maxReceivers)
						myAgent.addBehaviour(new Chose(this, free));
					break;
				}
				case (ACLMessage.INFORM):{
					//System.out.println("Received inform from " + msg.getSender());
					//System.out.println(currentTarget);
					
					if(msg.getSender().equals(currentTarget)){
						//System.out.println("Starting evaluation");
						myAgent.addBehaviour(new Evaluate(myAgent, Double.parseDouble(msg.getContent()),currentTarget, this));
						Reset();
					}
				}
				case (ACLMessage.FAILURE):{
					//System.out.println("Received failure");
					if(msg.getSender().equals(currentTarget)){
						myAgent.addBehaviour(new Search(this));
						Reset();
					}
					break;
				}
				case (ACLMessage.CONFIRM):{
					myAgent.addBehaviour(new Search(this));
					break;
				}
				default:
					block();
			}
		}
	}
	
	public void Reset() {
		currentTarget = null;
		maxReceivers  = 0;
		receivers.clear();
		arrived = 0;
		free.clear();
	}

	public void setCurrentTarget(AID currentTarget) {
		this.currentTarget = currentTarget;
		System.out.println("Current target: " + this.currentTarget.getLocalName());
	}

	public void setReceivers(Vector<AID> receivers) {
		this.receivers = receivers;
		maxReceivers = receivers.size();
		arrived = 0;
	}

}
