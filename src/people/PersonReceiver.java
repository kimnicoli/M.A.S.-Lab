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
	
	void setupFriends() {
		DFAgentDescription pdfd = new DFAgentDescription();
		ServiceDescription psd = new ServiceDescription();
		psd.setType("Person");
		pdfd.addServices(psd);
		
		
		DFAgentDescription[] presults = new DFAgentDescription[0];	
		try {
			presults = DFService.search(myAgent, pdfd);	
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		for(DFAgentDescription dfd : presults)
			((Person)myAgent).worldThrust.put(dfd.getName(), Math.random());
		
		while(((Person)myAgent).friends.size() < Main.MaxFriends){
			int i = (int)(Math.random() * presults.length);
			if(!((Person)myAgent).friends.contains(presults[i].getName()) 
					&& !presults[i].getName().equals(myAgent.getAID()))
				((Person)myAgent).worldThrust.put(presults[i].getName(), Math.random());
		}
		
		//System.out.println(((Person)myAgent).friends.size());
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
					
					if(msg.getOntology().equals("Reviews")){
						Hashtable<AID, Double> map = new Hashtable<AID, Double>();
						try {
							map = (Hashtable<AID, Double>)msg.getContentObject();
						} catch(UnreadableException e) {
							e.printStackTrace();
						}
						myAgent.addBehaviour(new UpdateEvaluation(map, msg.getSender()));						
					}
					break;
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
					if(Integer.decode(msg.getContent()) == 1)
						setupFriends();
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
