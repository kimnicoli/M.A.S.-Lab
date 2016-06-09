package src.mas_lab;

public class Main {
	
	public static double EvaluateRange = 5.0;
	public static double MinimumEvaluation = 2.0;
	public static int MaxCapacity = 20;
	public static double MinBoldness = 0.0;
	public static double MaxBoldness = 1.0;
	public static int MaxTurns = 100;
	
	public static double ProbEvaluation = 1.0;
	public static double ProbFuzzyEvaluation = 0.1;
	
	public static double ProbChooseBFF = 	0.1;
	public static double ProbChoseRandom = 0.1;
	
			
	public static int MaxFriends = 2;

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		Launcher launcher = new Launcher();
		launcher.InitJade(4, 10, null, null);
	}

}
