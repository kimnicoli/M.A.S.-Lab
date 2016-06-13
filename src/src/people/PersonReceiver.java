package src.people;

import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.Vector;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

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
	Vector<AID> fullRestaurants;
	
	static AID global;
	
	boolean choosing;
	
	
	public PersonReceiver(Agent a) {
		super(a);
		free = new TreeMap<AID, Double>();
		receivers = new Vector<AID>();
		fullRestaurants = new Vector<AID>();
		choosing = false;
		if (global == null){
			global = getGlobal();
		}
	}
	
	void setupFriends(DFAgentDescription[] presults) {
			
		Vector<AID> results = new Vector<AID>();
		
		for (int i = 0; i < presults.length; i++) {
			results.add(presults[i].getName());
		}
		
		for(DFAgentDescription dfd : presults)
			((Person)myAgent).worldTrust.put(dfd.getName(), Math.random());
		
		while(((Person)myAgent).friends.size() < Main.MaxFriends){
			int i = (int)(Math.random() * (results.size() - 1));
			if(!(results.get(i).equals(myAgent.getAID()))){
				((Person)myAgent).friends.add(results.get(i));
				results.remove(i);
			}	
			if(results.size() == 0)
				break;
		}
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if (msg == null) {
			block();
		} else {
			switch (msg.getPerformative()) {
				case (ACLMessage.REFUSE):{
					if(receivers.contains(msg.getSender())){
						arrived++;
						receivers.remove(msg.getSender());
					}
					if(arrived == maxReceivers & !choosing){
						myAgent.addBehaviour(new Chose(this, free));
						choosing = true;
					}
					break;
				}
				case (ACLMessage.PROPOSE):{
					if(receivers.contains(msg.getSender())){
						arrived++;
						receivers.remove(msg.getSender());
						free.put(msg.getSender(), ((Person)myAgent).restMap.get(msg.getSender()));
					}
					if(arrived == maxReceivers & !choosing){
						myAgent.addBehaviour(new Chose(this, free));
						choosing = true;
					}
					break;
				}
				case (ACLMessage.INFORM):{
					if(msg.getSender().equals(currentTarget)){
						myAgent.addBehaviour(new Evaluate(myAgent, Double.parseDouble(msg.getContent()),currentTarget));
						Reset();
						fullRestaurants.clear();
					}
					
					if(msg.getOntology().equals("Reviews")){
						Hashtable<AID, Double> map = new Hashtable<AID, Double>();
						try {
							map = (Hashtable<AID, Double>)msg.getContentObject();
						} catch(UnreadableException e) {
							e.printStackTrace();
						}
						myAgent.addBehaviour(new UpdateEvaluation(myAgent, map, msg.getSender()));						
					}
					break;
				}
				case (ACLMessage.FAILURE):{
					if(msg.getSender().equals(currentTarget)){
						System.err.println(myAgent.getLocalName() + ": Received Failure");
						fullRestaurants.add(currentTarget);
						Reset();
						myAgent.addBehaviour(new Search(this, fullRestaurants));
					}
					break;
				}
				case (ACLMessage.CONFIRM):{
					if(msg.getOntology().equals("Init Phase")){
						if(!((Person)myAgent).fromFile)
							try {
								setupFriends((DFAgentDescription[])msg.getContentObject());
							} catch (UnreadableException e1) {
								e1.printStackTrace();
							}
						ACLMessage JSONmsg = new ACLMessage(ACLMessage.CONFIRM);
						JSONmsg.addReceiver(global);
						try {
							JSONmsg.setContentObject(((Person)myAgent).Encode());
						} catch (IOException e) {
							e.printStackTrace();
						}
						myAgent.send(JSONmsg);
					}
					else if(msg.getOntology().equals("New Turn")){
						myAgent.addBehaviour(new Search(this, fullRestaurants));
					}
					break;
				}
				default:
					block();
			}
		}
	}
	
	public void Reset() {
		choosing = false;
		currentTarget = null;
		maxReceivers  = 0;
		receivers.clear();
		arrived = 0;
		free.clear();
	}

	public void setCurrentTarget(AID currentTarget) {
		this.currentTarget = currentTarget;
	}

	public void setReceivers(Vector<AID> receivers) {
		this.receivers = receivers;
		maxReceivers = receivers.size();
		arrived = 0;
	}

	AID getGlobal(){
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Global");
		dfd.addServices(sd);
		try {
			AID gAID = DFService.search(myAgent, dfd)[0].getName();
			return gAID;
		} catch (FIPAException e){
			e.printStackTrace();
			return null;
		}
	}
}
