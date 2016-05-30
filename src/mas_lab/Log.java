package mas_lab;

import java.util.TreeMap;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log extends OneShotBehaviour {
	static String stringa;

	TreeMap<AID, Double> restMap;
	AID[] listRe;
	boolean newFile;
	AID sender;

	public Log(AID[] listRe, AID sender, TreeMap<AID, Double> restMap, boolean newFile) {

		if (stringa == null)
			stringa = new String();

		this.listRe = listRe;
		this.newFile = newFile;
		this.sender = sender;
		this.restMap = restMap;

	}

	@Override
	public void action() {
		if (this.listRe != null && restMap != null) {
			Map<AID, TreeMap<AID, Double>> output = new HashMap<AID, TreeMap<AID, Double>>();
			output.put(sender, restMap);
			int turn = 0;// = ((Global)myAgent).getTurn();

			for (Entry<AID, TreeMap<AID, Double>> entry : output.entrySet()) {
				stringa = stringa.concat(String.valueOf(turn));
				stringa = stringa.concat(entry.getKey().getName());
				for (int i = 0; i < listRe.length; i++) {
					stringa = stringa.concat(",");
					stringa = stringa.concat(String.valueOf(entry.getValue().get(listRe[i])));
				}
			}

			stringa = stringa.concat("\n");
		}

		if (newFile) {
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

			try {
				fstream = new FileWriter(aggFileNameInfo);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				out.write(stringa);
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
