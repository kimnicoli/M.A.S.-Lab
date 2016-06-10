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
	
	static AID global;
	
	public PersonReceiver(Agent a) {
		super(a);
		free = new TreeMap<AID, Double>();
		receivers = new Vector<AID>();
		if (global == null){
			global = getGlobal();
		}
	}

	/*public PersonReceiver(Agent a) {
		super(a);
		// TODO Auto-generated constructor stub
	}*/
	
	void setupFriends(DFAgentDescription[] presults) {
		//System.out.println(myAgent.getLocalName() + " is chosing friends");
		
		/*DFAgentDescription pdfd = new DFAgentDescription();
		ServiceDescription psd = new ServiceDescription();
		psd.setType("Person");
		pdfd.addServices(psd);
		
		
		DFAgentDescription[] presults = new DFAgentDescription[0];	
		try {
			presults = DFService.search(myAgent, pdfd);	
		} catch (FIPAException e) {
			e.printStackTrace();
		}*/
		
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
		
		//System.out.println(((Person)myAgent).friends.toString());
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
						/*if(Math.random() < Main.RandomGoToFirst & arrived == 1){
							receivers.clear();
							setCurrentTarget(msg.getSender());
							myAgent.addBehaviour(new Eat(currentTarget));
							System.err.println("!!Too lazy to wait!!");
						}*/
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
					
					//System.out.println("Received inform");
					if(msg.getOntology().equals("Reviews")){
						//System.out.println("received opinion");
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
					//System.out.println("Received failure");
					if(msg.getSender().equals(currentTarget)){
						myAgent.addBehaviour(new Search(this));
						Reset();
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
					else if(msg.getOntology().equals("New Turn"))
						myAgent.addBehaviour(new Search(this));
					/*if(Integer.decode(msg.getContent()) == 1 & !((Person)myAgent).fromFile){
						setupFriends();
					}
					//System.out.println(myAgent.getLocalName() + "\n" +((Person)myAgent).Encode());
					ACLMessage JSONmsg = new ACLMessage(ACLMessage.CONFIRM);
					JSONmsg.addReceiver(global);
					JSONmsg.setContent(((Person)myAgent).Encode().toJSONString());
					myAgent.send(JSONmsg);
					myAgent.addBehaviour(new Search(this));*/
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
		//System.out.println("Current target: " + this.currentTarget.getLocalName());
		//ACLMessage msg = new ACLMessage(ACLMessage.);
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
			//System.out.println(gAID.getName());
			return gAID;
		} catch (FIPAException e){
			e.printStackTrace();
			return null;
		}
	}
}
