package mas_lab;

import java.util.TreeMap;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Log extends OneShotBehaviour {
	static String stringa;

	TreeMap<AID, Double> restMap;
	DFAgentDescription[] listRe;
	boolean newFile;
	AID sender;

	public Log(DFAgentDescription[] listRe, AID sender, TreeMap<AID, Double> restMap, boolean newFile) {

		if (stringa == null)
			stringa = new String();

		this.listRe = listRe;
		this.newFile = newFile;
		this.sender = sender;
		this.restMap = restMap;

	}

	@Override
	public void action() {
		if (this.listRe != null || restMap != null) {
			int turn = 0;// = ((Global)myAgent).getTurn();
			stringa = stringa.concat(String.valueOf(turn));
			stringa = stringa.concat(this.sender.getName());

			for (int i = 0; i < listRe.length; i++) {
				stringa = stringa.concat(",");
				stringa = stringa.concat(String.valueOf(this.restMap.get(listRe[i].getName())));
			}
		}

		stringa = stringa.concat("\n");

		if (this.newFile) {
			// -----------------------WRITE LOG----------------------------
			// -----------------------WRITE LOG----------------------------

			String aggFileName = "agg-" + String.valueOf("LOG.txt");
			FileWriter fstream = null;
			try {
				fstream = new FileWriter(aggFileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
			BufferedWriter out = new BufferedWriter(fstream);
			try {
				out.write(stringa);
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				out.close();

			} catch (IOException ex) {
				Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
			}

			// -----------------------WRITE INFO----------------------------
			// -----------------------WRITE INFO----------------------------

			String aggFileNameInfo = "agg-" + String.valueOf("infoLOG.txt");
			String info = new String();
			for (int i = 0; i < listRe.length; i++) {

				info = info.concat(listRe[i].getName().getName());
				info = info.concat(",");

				// Iterator services = listRe[0].getAllServices();
				// ServiceDescription service = null;
				// double rank = 0.f;
				// while (services.hasNext())
				// {
				// service = (ServiceDescription)services.next();
				// Iterator properties = service.getAllProperties();
				// while (properties.hasNext())
				// {
				// service.get
				// }
				// }
			}

			try {
				fstream = new FileWriter(aggFileNameInfo);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				out.write(info);
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			stringa = null;
		}
	}
}
