package src.mas_lab;

public class Main {
	
	/**
	 * Limite superiore della scala di valutazione
	 */
	public static double EvaluateRange = 5.0;
	/**
	 * Valore minimo per la soglia di preferenza degli agenti Person0
	 */
	public static double MinimumEvaluation = 3.0;
	/**
	 * Capacità massima per gli agenti Restaurant
	 */
	public static int MaxCapacity = 10;
	/**
	 * Valore minimo per la "cocciutaggine" degli agenti Person
	 */
	public static double MinBoldness = 0.0;
	/**
	 * Valore massimo per la "cocciutaggine" degli agenti Person
	 */
	public static double MaxBoldness = 1.0;
	/**
	 * Numero di turni a cui terminare la simulazione
	 */
	public static int MaxTurns = 1;
	
	/**
	 * Larghezza della fluttuazione random nella valutazione razionale
	 */
	public static double ProbEvaluation = 1.0;
	/**
	 * Probabilità di una valutazione irrazionale
	 */
	public static double ProbFuzzyEvaluation = 0.1;
	
	/**
	 * Probabilità di scegliere in base alla miglior opinione del miglior amico
	 */
	public static double ProbChooseBFF = 	0.1;
	/**
	 * Probabilità di scegliere il primo ristorante che risponde "libero"
	 */
	public static double ProbChoseRandom = 0.1;
	
	/**
	 * Numero massimo di amici per cerchia di amicizia
	 */
	public static int MaxFriends = 0;

	public static void main(String[] args) {
		Launcher launcher = new Launcher();
		launcher.InitJade(1, 10, null, null);
	}

}
