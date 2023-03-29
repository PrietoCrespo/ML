package pQLEARNING;

import java.util.Iterator;
import java.util.Random;


public class Politica {
	// amount of possible states
	private int states;
	// amount of possible actions
	private int actions;
	// q-values
	private double[][] tablaQ;
	boolean exploro;

	public Politica(int estados, int acciones,double[][] tabla,boolean expl) {
		this.tablaQ = tabla;
		this.states = estados;
		this.actions = acciones;
		this.exploro = expl;
	}
	
	public int ChooseAction(double[] ds) {
		int  devuelto = -1,a=-1;
		double valor = -999999;
		
		if(exploro) {
			//System.out.println("Valores:");
			for (int i = 0; i < ds.length; i++) { //Exploto
				//System.out.println("\tValor "+i+": "+ds[i]);
				if (ds[i] > valor) {
					devuelto = i;
					valor = ds[i];
				}
			}
		} else {
			Random r = new Random(System.nanoTime());
			devuelto = r.nextInt(ds.length);
 
		}
		
		return devuelto;
	}
}