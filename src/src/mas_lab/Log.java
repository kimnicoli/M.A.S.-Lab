package src.mas_lab;

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
import jade.domain.FIPAAgentManagement.Property;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Log extends OneShotBehaviour {
	
	static String stringa;
        static String stringaTrust;
        static String stringaTarget;
        
        static int TURNO = -1;
        static double[][] matriceT;
        
        static Set<AID> listPe;

                
                
	TreeMap<AID, Double> restMap;
        Hashtable<AID, Double> trustMap;
	DFAgentDescription[] listRe;
	boolean newFile;
	AID sender;
        AID target;
        

        public Log(DFAgentDescription[] listRe, AID sender, boolean newFile, Object[] objArr)
        {
            if (stringa == null)
			stringa = new String();
            if (stringaTrust == null)
			stringaTrust = new String();
            if (stringaTrust == null)
			stringaTarget = new String();

            this.listRe = listRe;
            this.newFile = newFile;
            this.sender = sender;
            this.restMap = (TreeMap<AID, Double>)objArr[1];
            this.trustMap = (Hashtable<AID, Double>)objArr[0];
            //this.target = (AID)objArr[2];
            
            if (listPe == null)
                listPe = trustMap.keySet();
        }
        
        
        
	@Override
	public void action() {
                // -----------------------LOG TARGET----------------------------
                // -----------------------LOG TARGET----------------------------
                if(this.target != null)
                {
                        int turn = ((Global)myAgent).getTurn();
			stringaTarget = stringaTarget.concat(this.sender.getName() + ",");
			stringaTarget = stringaTarget.concat(String.valueOf(turn));
                        stringaTarget = stringaTarget.concat(target.getLocalName() + ",");
                        stringaTarget = stringaTarget.concat("\n");
                }
            
                
            
                // -----------------------LOG RESTMAP----------------------------
                // -----------------------LOG RESTMAP----------------------------
                if (this.listRe != null || restMap != null) {
			int turn = ((Global)myAgent).getTurn();
			stringa = stringa.concat(this.sender.getName() + ",");
			stringa = stringa.concat(String.valueOf(turn));

			for (int i = 0; i < listRe.length; i++) {
				stringa = stringa.concat(",");
				stringa = stringa.concat(String.valueOf(this.restMap.get(listRe[i].getName())));
			}
		}
                stringa = stringa.concat("\n");
                
                
                // -----------------------LOG TRUSTMAP----------------------------
                // -----------------------LOG TRUSTMAP----------------------------
                if (this.trustMap != null)
                {
                        int nPer = trustMap.keySet().size();
                        
                        
                        if (((Global)myAgent).getTurn() != TURNO) //se il turno e cambiato
                        {
                            
                            if (TURNO == -1) //se e il primo turno
                            {
                                
                             // metti la matrice = 0
                             for (int ii = 0; ii < nPer; ii++){
                                for (int i = 0; i < nPer; i++) {   
                                    matriceT = new double [nPer] [nPer];
                                    matriceT[ii][i] = 0;
                                    }
                                }
                            }
                            
                            else
                            {
                                // riempi la stringa e annulla la matrice
                                for (int ii = 0; ii < nPer; ii++)
                                {
                                    for (int i = 0; i < nPer; i++) 
                                    {   
                                        stringaTrust = stringaTrust.concat(String.valueOf(matriceT[ii][i]));
                                        stringaTrust = stringaTrust.concat(",");
                                        matriceT[ii][i] = 0;
                                    }
                                    stringaTrust = stringaTrust.concat("\n");
                                } 
                                
                                //na riga nulla
                                for (int i = 0; i < nPer; i++)
                                {
                                    stringaTrust = stringaTrust.concat(",");
                                }
                                stringaTrust = stringaTrust.concat("\n");
                            }
                        }
                        TURNO = ((Global)myAgent).getTurn();
                        
                         // riempi la matrice
                         for (int ii = 0; ii < nPer; ii++)
                            {
                                
                                //Object[] person = (AID[])listPe.toArray();
                                Iterator iter = listPe.iterator();
                                while (iter.hasNext()) 
                                {
                                    System.out.println(iter.next().getClass());
                                }
//                                if (person[ii].getName().equalsIgnoreCase(sender.getName()))  
//                                {
//                                    for (int i = 0; i < nPer; i++) 
//                                    {
//                                        matriceT[ii][i] = trustMap.get(person[i]);
//                                    }
//                                }
                            }     
                }

		
		
		if (this.newFile) 
                {
			
                        // -----------------------WRITE RESTMAP----------------------------
                        // -----------------------WRITE RESTMAP----------------------------                   
			String aggFileName = "agg-" + String.valueOf("LOG.csv");
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
			
//                        // -----------------------WRITE TARGET----------------------------
//                        // -----------------------WRITE TARGET----------------------------
//                        String aggFileNameTarget = "agg-" + String.valueOf("LOGTARGET.csv");
//			try {
//				fstream = new FileWriter(aggFileNameTarget);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			out = new BufferedWriter(fstream);
//			try {
//				out.write(stringaTarget);
//				out.flush();
//				out.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
                        
                        
                        // -----------------------WRITE TRUSTMAP----------------------------
			// -----------------------WRITE TRUSTMAP----------------------------
                        String aggFileNameT = "agg-" + String.valueOf("LOGTRUST.csv");
			try {
				fstream = new FileWriter(aggFileNameT);
			} catch (IOException e) {
				e.printStackTrace();
			}
			out = new BufferedWriter(fstream);
			try {
				out.write(stringaTrust);
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
                        
                        
                        
                        
			// -----------------------WRITE INFO RE----------------------------
			// -----------------------WRITE INFO RE----------------------------

			String aggFileNameInfo = "agg-" + String.valueOf("infoLOG.csv");
			String info = new String();
			for (int i = 0; i < listRe.length; i++) {

				info = info.concat(listRe[i].getName().getLocalName());
				info = info.concat(",");
				ServiceDescription sd = (ServiceDescription)listRe[i].getAllServices().next();
				Property props = (Property)sd.getAllProperties().next();
				
				info = info.concat(props.getValue() + "\n");

			}

			try {
				fstream = new FileWriter(aggFileNameInfo);
			} catch (IOException e) {
				e.printStackTrace();
			}
			out = new BufferedWriter(fstream);
			try {
				out.write(info);
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println(stringaTrust);
			System.exit(0);
		}
	}
}
