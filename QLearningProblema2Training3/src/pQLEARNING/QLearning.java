 package pQLEARNING;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class QLearning {

	// amount of possible states
	private int states;
	// amount of possible actions
	private int actions;
	// q-values
	private double[][] tablaQ;
	// exploration policy
	private Politica pol;

	// discount factor
	private double gamma = 0.2; // Como de importante son las acciones futuras
	// learning rate
	private double alpha = 0.4;

	public QLearning(int states, int actions, boolean exploro) {
		this.states = states;
		this.actions = actions;
		// Creo la tabla Q
		//System.out.println("La tablaQ tiene "+states+" estados y "+actions+" acciones");
		tablaQ = new double[states][];
		
		this.tablaQ = leoTabla();
		
		
		// La inicializo random
		Random r = new Random();
		ArrayList<Double> fila = new ArrayList<Double>();
		for (int i = 0; i < states; i++) {
			fila.clear();
			for (int j = 0; j < actions; j++) {
				fila.add(tablaQ[i][j]);
			}
		}
		// Inicializo la politica con la tabla Q, los estados y las acciones
		this.pol = new Politica(this.states, this.actions, this.tablaQ, exploro);

	}

	/**
	 * Amount of possible states.
	 * 
	 * @return States
	 */
	public int getStates() {
		return states;
	}

	/**
	 * Amount of possible actions.
	 * 
	 * @return Actions
	 */
	public int getActions() {
		return actions;
	}

	/**
	 * Exploration policy.
	 * 
	 * @return Exploration Policy
	 */
	public Politica getExplorationPolicy() {
		return pol;
	}

	/**
	 * Policy, which is used to select actions.
	 * 
	 * @param explorationPolicy Exploration Policy
	 */
	public void setExplorationPolicy(Politica explorationPolicy) {
		this.pol = explorationPolicy;
	}

	/**
	 * Get Learning Rate
	 * 
	 * @return Learning Rate
	 */
	public double getLearningRate() {
		return alpha;
	}

	/**
	 * Learning rate, [0, 1]. The value determines the amount of updates Q-function
	 * receives during learning. The greater the value, the more updates the
	 * function receives. The lower the value, the less updates it receives.
	 * 
	 * @param learningRate
	 */
	public void setLearningRate(double learningRate) {
		this.alpha = Math.max(0.0, Math.min(1.0, learningRate));
	}

	/**
	 * Get Discount factor for the expected summary reward.
	 * 
	 * @return Discount Factor
	 */
	public double getDiscountFactor() {
		return gamma;
	}

	/**
	 * Discount factor for the expected summary reward. The value serves as
	 * multiplier for the expected reward. So if the value is set to 1, then the
	 * expected summary reward is not discounted. If the value is getting smaller,
	 * then smaller amount of the expected reward is used for actions' estimates
	 * update.
	 * 
	 * @param discountFactor
	 */
	public void setDiscountFactor(double discountFactor) {
		this.gamma = Math.max(0.0, Math.min(1.0, discountFactor));
	}

	/**
	 * Get next action from the specified state.
	 * 
	 * @param state Current state to get an action for.
	 * @return Returns the action for the state.
	 */
	public int GetAction(int state) {
		return pol.ChooseAction(tablaQ[state]);
	}

	/**
	 * Update Q-function's value for the previous state-action pair.
	 * 
	 * @param previousState Previous state.
	 * @param action        Action, which leads from previous to the next state.
	 * @param reward        Reward value, received by taking specified action from
	 *                      previous state.
	 * @param nextState     Next state.
	 */
	public void UpdateState(int previousState, int action, double reward, int nextState) {
		// next state's action estimations
		//System.out.println("Obtengo como parámetro: "+previousState+", "+action+", "+reward+", "+nextState);
		double[] nextActionEstimations = tablaQ[nextState];
		// find maximum expected summary reward from the next state
		double maxNextExpectedReward = nextActionEstimations[0];

		
		
		// previous state's action estimations
		if(previousState == -1) { //Primera iteración
			previousState = nextState;
			Random aleatorio = new Random();
			int accionAleatoria = aleatorio.nextInt(4);
			maxNextExpectedReward = nextActionEstimations[accionAleatoria];
		} else {
			for (int i = 1; i < actions; i++) {
				if (nextActionEstimations[i] > maxNextExpectedReward)
					maxNextExpectedReward = nextActionEstimations[i];
			}
		}
		
		double[] previousActionEstimations = tablaQ[previousState];
		// update expexted summary reward of the previous state
		previousActionEstimations[action] *= (1.0 - alpha);
		double valor = (alpha * (reward + gamma * maxNextExpectedReward - previousActionEstimations[action]));
		previousActionEstimations[action] += valor;
		
		//pintaTablaQ
		pintaQ(this.tablaQ);
		//Ver la tabla Q
		
		//System.out.println("~~~~~~~~~~~~~~~~~~~~~~");
		//System.out.println("Tabla Q: ");
		ArrayList<Double> fila = new ArrayList<Double>();
		for (int i = 0; i < states; i++) {
			fila.clear();
			for (int j = 0; j < actions; j++) {
				fila.add(tablaQ[i][j]);
			}
			//System.out.println(fila);
		}
		//System.out.println("~~~~~~~~~~~~~~~~~~~~~~");
		
	}
	
	public void CalculaQ(int estadoAnt, int accion, double recompensa, int estadoSig) {
		double qAct = tablaQ[estadoAnt][accion];
		double maxQ = GetAction(estadoSig);
		double Nvalor = qAct + alpha * (recompensa + gamma * maxQ - qAct);
		tablaQ[estadoSig][accion] = Nvalor;
		
		pintaQ(this.tablaQ);
		//Ver la tabla Q
		ArrayList<Double> fila = new ArrayList<Double>();
		for (int i = 0; i < states; i++) {
			fila.clear();
			for (int j = 0; j < actions; j++) {
				fila.add(tablaQ[i][j]);
			}
		}
	}
	
	
	public void pintaQ(double[][] tabla) {
		try {
			FileWriter fw = new FileWriter("./Tabla/tablaQ.txt");
			for (int i = 0; i < states; i++) {
				for (int j = 0; j < actions; j++) {
					fw.append(tabla[i][j]+"\n");
				}				
			}
			fw.close();
		} catch (IOException e) {
			e.getMessage();
		}
		
	}
	

	//Leo la tabla guardada
	private double[][] leoTabla() {
		double[][] tabla = new double[states][];
		
		
		for (int i = 0; i < states; i++) {
			tabla[i] = new double[actions];
		}
		File file = new File("./Tabla/tablaQ.txt");
		Scanner sc;
		int i=0,j=0;
		try {
			sc = new Scanner(file);
			while(sc.hasNext()) {
				if(j==actions) {
					i++;
					j=0;
				}
				double valor = Double.parseDouble(sc.nextLine());
				tabla[i][j] = valor;
				j++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tabla;
	}

}
