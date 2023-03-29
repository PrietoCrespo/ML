package pQLEARNING;

import java.util.ArrayList;
import java.util.Random;

import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import ontology.Types.ACTIONS;
import serialization.Observation;
import serialization.Vector2d;
import tools.ElapsedCpuTimer;

public class mainQ extends AbstractPlayer {
	QLearning ql;
	int estado = 0, estadoAnt = -1, nInfectados = 0, nInfectadosAnt = 0, posX = -1, posY = -1, posXAnt, posYAnt,
			libresL=0,libresR=0, libresLAnt = 0,libresRAnt = 0,
			libresUp=0,libresDown=0,libresUpAnt=0,libresDownAnt=0;
	double distanciatotalI = -1, distanciatotalIant = -1, distanciaJorge = -1, distanciaJorgeAnt = -1, puntosAnt = 0,
			puntosAct = 0;
	double xAnt, yAnt;
	boolean atascadoAnt = false, atascado = false, atascadoAntH = false, atascadoH = false, atascadoAntV = false, atascadoV = false;
	ArrayList<Double> posicionGeorge, posicionInfectado;
	private final int INFECTADO = 4;
	private final int NOINFECTADO = 6;
	private final int GEORGE = 7;

	// Atributos del agente
	public mainQ(StateObservation so, ElapsedCpuTimer elapsedTimer) {
		// Inicialización atributos
		// 0 InfectadoDcha
		// 1 InfectadoIzq
		// 2 InfectadoArriba
		// 3 InfectadoAbajo
		// 4 MuroDcha
		// 5 MuroIzq
		// 6 MuroArriba
		// 7 MuroAbajo
		// 8 JorgeDcha
		// 9 JorgeIzq
		// 10 JorgeArriba
		// 11 JorgeAbajo
		// 12 huecoArribaAbajo
		// 13 huecoAbajo
		// 14 huecoArriba
		// 15 huecoDchaIzq
		// 16 huecoDcha
		// 17 huecoIzq
		// 18 Infectado cerca
		// 19 Solo George
		double p = 1;
		Random r = new Random(System.nanoTime());
		double n2 = r.nextDouble(Practica_XX_exe.iteracionesT) / Practica_XX_exe.iteracionesT;
		double n = (double) ((Practica_XX_exe.iteracionesT - Practica_XX_exe.iteracionActual)
				/ Practica_XX_exe.iteracionesT);
		posicionGeorge = new ArrayList();
		posicionInfectado = new ArrayList();

		// System.out.println("Comparo " + n2 + " con " + n);
		boolean exploro = n2 >= n;
		if (exploro)
			System.out.println("Exploto");
		else
			System.out.println("Exploro");
		ql = new QLearning(20, 5, exploro);
		xAnt = so.getAvatarPosition().x;
		yAnt = so.getAvatarPosition().y;

		// CAMBIAR ESTO
		estado = 3;
	}

	public void getEstadoActual(StateObservation so) {

		ArrayList<core.game.Observation>[] posicionP = so.getPortalsPositions();
		core.game.Observation p1, p2;
		posXAnt = posX;
		posYAnt = posY;
		int ancho = (int) (so.getWorldDimension().getWidth() / so.getBlockSize());
		int alto = (int) (so.getWorldDimension().getHeight() / so.getBlockSize());

		posX = (int) (Math.round(so.getAvatarPosition().x / so.getBlockSize()));
		posY = (int) (Math.round(so.getAvatarPosition().y / so.getBlockSize()));

		posicionGeorge = new ArrayList();
		posicionInfectado = new ArrayList();
		posicionGeorge = getTipo(so, GEORGE);
		posicionInfectado = getInfectado(so);
		
		

		/*System.out.println("get(0): "+posicionGeorge.get(0));
		System.out.println("get(1): "+posicionGeorge.get(1));
		System.out.println("get(0) Infectado: "+posicionGeorge.get(0));
		System.out.println("get(1) Infectado: "+posicionGeorge.get(1));*/

		if(posicionGeorge.get(0) < 0 && posicionGeorge.get(1) < 0) {
			posicionGeorge = new ArrayList();
			posicionGeorge.add(posicionInfectado.get(0));
			posicionGeorge.add(posicionInfectado.get(1));
		}
		double xGeorge = posicionGeorge.get(0);
		double yGeorge = posicionGeorge.get(1);
		//System.out.println("Distancia x: " + xGeorge + "-" + (double) posX + "= " + Math.abs((xGeorge - (double) posX)));
		//System.out.println("Distancia y: " + yGeorge + "-" + (double) posY + "= " + Math.abs((yGeorge - (double) posY)));
		
		double distanciax = Math.pow(Math.abs(xGeorge - (double) posX), 2);
		double distanciay = Math.pow(Math.abs(yGeorge - (double) posY), 2);
		// double distanciax = (double)( posicionGeorge.get(0) - posX) * (double)(
		// posicionGeorge.get(0) - posX);
		//System.out.println("Potencia x: "+distanciax);
		//System.out.println("Potencia y: "+distanciay);
		

		distanciaJorgeAnt = distanciaJorge;
		distanciaJorge = Math.sqrt(distanciax + distanciay);
		//System.out.println("Distancia Jorge: \n\tRaiz de: " + distanciax + " + " + distanciay + " = " + distanciaJorge);

		
		// System.out.println("Infectado mas cercano: \n\tx:

		double xInfectado = posicionInfectado.get(0);
		double yInfectado = posicionInfectado.get(1);

		//System.out.println("Infectado Distancia x: " + xInfectado + "-" + (double) posX + "= " + Math.abs((xInfectado - (double) posX)));
		//System.out.println("Infectado Distancia y: " + yInfectado + "-" + (double) posY + "= " + Math.abs((yInfectado - (double) posY)));
		
		double distanciaxI = Math.pow(Math.abs(xInfectado - (double) posX), 2);
		double distanciayI = Math.pow(Math.abs(yInfectado - (double) posY), 2);
		distanciatotalIant = distanciatotalI;
		distanciatotalI = Math.sqrt(distanciaxI + distanciayI);
		// double distanciaxI = ( posicionInfectado.get(0) - posX) * (
		// posicionInfectado.get(0) - posX);
		
	
		// System.out.println("Distancia con el infectado más cercano:
		// "+distanciatotalI);

		boolean muroEncontrado = false;
		int i = posX, j = posY;
		ArrayList posicionMuro = new ArrayList();
		posicionMuro.add(-1);
		posicionMuro.add(-1);

		/*
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("Posicion Jorge: \n\t x: " + posicionGeorge.get(0) + " y: " + posicionGeorge.get(1));
		System.out.println("Distancia con Jorge: " + distanciaJorge);
		System.out.println("Distancia con Jorge ant: " + distanciaJorgeAnt);
		System.out.println("Distancia con infectado más cercano: " + distanciatotalI);
		System.out.println("Distancia con infectado más cercano ant: " + distanciatotalIant);
		System.out.println("Numero infectados: " + nInfectados);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		 */
		
		atascadoAntH = atascadoH;
		atascadoAntV = atascadoV;
		atascadoAnt = atascado;
		atascadoH = false;
		atascadoV = false;
		atascado = false;
		
		
		//Cuento huecos arriba y abajo
		int contadorArriba = posY, contadorAbajo = posY;
		libresDownAnt = libresDown;
		libresUpAnt = libresUp;
		libresUp = 0;
		libresDown = 0;
		contadorArriba++;
		while (contadorArriba < alto && so.getObservationGrid()[posX][contadorArriba].size() == 0) {
			libresUp++;
			contadorArriba++;
		}

		contadorAbajo--;
		while (contadorAbajo > 0 && so.getObservationGrid()[posX][contadorAbajo].size() == 0) {
			libresDown++;
			contadorAbajo--;
		}
		// Termino de contar huecos
		
		//Cuento huecos derecha e izquierda
		int contadorDcha = posX, contadorIzq = posX;

		libresLAnt = libresL;
		libresRAnt = libresR;
		libresL = 0;
		libresR = 0;
		contadorDcha++;
		while (contadorDcha < ancho && so.getObservationGrid()[contadorDcha][posY].size() == 0) {
			libresR++;
			contadorDcha++;
		}

		contadorIzq--;
		while (contadorIzq > 0 && so.getObservationGrid()[contadorIzq][posY].size() == 0) {
			libresL++;
			contadorIzq--;
		}		
		//Termino de contar huecos
		
		/*System.out.println("Huecos Arriba: "+libresUp);
		System.out.println("Huecos Abajo: "+libresDown);
		System.out.println("Huecos izquierda: "+libresL);
		System.out.println("Huecos derecha: "+libresR);
		*/
		
		/*if (nInfectados == 0) {
			estado = 19;
		} else */
		if (distanciaJorge < 4 || nInfectados == 0) { // Jorge cerca
			// System.out.println("JORGE CERCA");
			if (Math.abs(posX - posicionGeorge.get(0)) > Math.abs(posY - posicionGeorge.get(1))) {
				// if (distanciax > distanciay) { // Jorge derecha o izq
				if (posicionGeorge.get(0) > posX) { // Jorge derecha
					if (so.getObservationGrid()[posX - 1][posY].size() > 0
							&& so.getObservationGrid()[posX - 1][posY].get(0).itype == 0) { // Tengo muro detras
						atascado = true;
						atascadoV = true;
						boolean muroArriba = false, muroAbajo = false;
						if (so.getObservationGrid()[posX][posY - 1].size() > 0
								&& so.getObservationGrid()[posX][posY - 1].get(0).itype == 0) { // Muro arriba
							muroArriba = true;
							estado = 13;// Hueco abajo
							//System.out.println("Hueco abajo");
						}
						if (so.getObservationGrid()[posX][posY + 1].size() > 0
								&& so.getObservationGrid()[posX][posY + 1].get(0).itype == 0) { // Muro abajo
							muroAbajo = true;
							estado = 14; // Hueco arriba
							//System.out.println("Hueco arriba");
						}

						if (!muroAbajo && !muroArriba) {
							
							if (libresUp >= libresDown) { // Mas hueco arriba
								estado = 14;
								//System.out.println("Hueco arriba");
							} else {
								estado = 13;
								//System.out.println("Hueco abajo");
							}
						}
					} else {
						//System.out.println("Jorge dcha");
						estado = 8;
					}
				} else { // Jorge izq
					if (so.getObservationGrid()[posX + 1][posY].size() > 0
							&& so.getObservationGrid()[posX + 1][posY].get(0).itype == 0) { // Tengo muro detras
						atascado = true;
						atascadoV = true;
						boolean muroArriba = false, muroAbajo = false;
						if (so.getObservationGrid()[posX][posY - 1].size() > 0
								&& so.getObservationGrid()[posX][posY - 1].get(0).itype == 0) { // Muro arriba
							muroArriba = true;
							estado = 13;// Hueco abajo
							//System.out.println("Hueco abajo");
						}
						if (so.getObservationGrid()[posX][posY + 1].size() > 0
								&& so.getObservationGrid()[posX][posY + 1].get(0).itype == 0) { // Muro abajo
							muroAbajo = true;
							estado = 14; // Hueco arriba
							//System.out.println("Hueco arriba");
						}

						if (!muroAbajo && !muroArriba) {
							
							if (libresUp >= libresDown) { // Mas hueco arriba
								estado = 14;
								//System.out.println("Hueco arriba");
							} else {
								estado = 13;
								//System.out.println("Hueco abajo");
							}
						}
					} else {
						estado = 9;
						//System.out.println("Jorge izq");
					}
				}
			} else { // Jorge arriba o abajo
				if (posicionGeorge.get(1) > posY) { // Jorge abajo

					if (so.getObservationGrid()[posX][posY - 1].size() > 0
							&& so.getObservationGrid()[posX][posY - 1].get(0).itype == 0) { // Tengo muro detras
						atascado = true;
						atascadoH = true;
						boolean muroDerecha = false, muroIzq = false;
						if (so.getObservationGrid()[posX + 1][posY].size() > 0
								&& so.getObservationGrid()[posX + 1][posY].get(0).itype == 0) { // Muro Dcha
							muroDerecha = true;
							estado = 17;// Hueco izq
							//System.out.println("Hueco izq");
						}
						if (so.getObservationGrid()[posX - 1][posY].size() > 0
								&& so.getObservationGrid()[posX - 1][posY].get(0).itype == 0) { // Muro izq
							muroIzq = true;
							estado = 16; // Hueco dcha
							//System.out.println("Hueco derecha");
						}

						if (!muroDerecha && !muroIzq) {
							
							if (libresR >= libresL) { // Mas hueco derecha
								estado = 16;
								//System.out.println("Hueco dcha");
							} else {
								estado = 17;
								//System.out.println("Hueco izq");
							}
						}
					} else {
						estado = 11;
						//System.out.println("Jorge abajo");
					}

				} else { // Jorge arriba
					if (so.getObservationGrid()[posX][posY + 1].size() > 0
							&& so.getObservationGrid()[posX][posY + 1].get(0).itype == 0) { // Tengo muro detras
						atascado = true;
						atascadoH = true;
						boolean muroDerecha = false, muroIzq = false;
						if (so.getObservationGrid()[posX + 1][posY].size() > 0
								&& so.getObservationGrid()[posX + 1][posY].get(0).itype == 0) { // Muro Dcha
							muroDerecha = true;
							estado = 17;// Hueco izq
							//System.out.println("Hueco izq");
						}
						if (so.getObservationGrid()[posX - 1][posY].size() > 0
								&& so.getObservationGrid()[posX - 1][posY].get(0).itype == 0) { // Muro izq
							muroIzq = true;
							estado = 16; // Hueco dcha
							//System.out.println("Hueco dcha");
						}

						if (!muroDerecha && !muroIzq) {
							
							if (libresR >= libresL) { // Mas hueco derecha
								estado = 16;
								//System.out.println("Hueco dcha");
							} else {
								estado = 17;
								//System.out.println("Hueco izq");
							}
						}
					} else {
						estado = 10;
						//System.out.println("Jorge cerca arriba");
					}
				}

			}
		}else if (Math.abs(posX - posicionInfectado.get(0)) > Math.abs(posY - posicionInfectado.get(1))) {
			// else if (distanciaxI > distanciayI) { // Infectado a la dcha o izq
			if (posicionInfectado.get(0) > posX) { // Infectado más cercano a la dcha
				if (posX < ancho) { // Si no estoy pegado al borde derecho, puedo mirar la derecha
					if (so.getObservationGrid()[posX + 1][posY].size() > 0 // Si a la derecha hay algo
							&& so.getObservationGrid()[posX + 1][posY].get(0).itype == 0) { // Y este "algo" es un muro
						estado = 4; // Muro a la derecha
						//System.out.println("Muro a la derecha");
					} else if (so.getObservationGrid()[posX + 1][posY].size() > 0 // Infectado justo a la dcha
							&& so.getObservationGrid()[posX + 1][posY].get(0).itype == 4) {
						//System.out.println("infectado a 1 paso");
						estado = 18;
					} else {
						estado = 0;
						//System.out.println("Infectado a la dcha");
					}
				}
			} else { // Infectado más cercano a la izq
				if (posX > 1) { // Si no estoy pegado a la izquierda puedo mirar la izq
					if (so.getObservationGrid()[posX - 1][posY].size() > 0 // Si a la izquierda hay algo
							&& so.getObservationGrid()[posX - 1][posY].get(0).itype == 0) {// Y este "algo" es un muro
						//System.out.println("Muro a la izquierda");
						estado = 5;
					} else if (so.getObservationGrid()[posX - 1][posY].size() > 0 // Infectado justo a la izq
							&& so.getObservationGrid()[posX - 1][posY].get(0).itype == 4) {
						//System.out.println("infectado a 1 paso");
						estado = 18;
					} else {
						estado = 1;
						//System.out.println("Infectado a la izq");
					}
				}
			}

		} else { // Si el infectado está arriba/abajo
			if (posicionInfectado.get(1) > posY && posY < alto) { // Si no estoy pegado abajo puedo mirar hacia
																	// abajo
				if (so.getObservationGrid()[posX][posY + 1].size() > 0 // Si abajo hay algo
						&& so.getObservationGrid()[posX][posY + 1].get(0).itype == 0) {// Y este "algo" es un muro
					//System.out.println("Muro abajo");
					estado = 7;
				} else if (so.getObservationGrid()[posX][posY + 1].size() > 0 // Infectado justo abajo
						&& so.getObservationGrid()[posX][posY + 1].get(0).itype == 4) {
					//System.out.println("Infectado a 1 paso");
					estado = 18;
				} else {
					// Infectado abajo
					estado = 3;
					//System.out.println("Infectado abajo");
				}
			} else {
				if (posY > 1) { // Si no estoy pegado arriba puedo mirar hacia arriba
					if (so.getObservationGrid()[posX][posY - 1].size() > 0 // Si arriba hay algo
							&& so.getObservationGrid()[posX][posY - 1].get(0).itype == 0) {// Y este "algo" es un muro
						//System.out.println("Muro arriba");
						estado = 6;
					} else if (so.getObservationGrid()[posX][posY - 1].size() > 0 // Si arriba hay algo
							&& so.getObservationGrid()[posX][posY - 1].get(0).itype == 4) {
						//System.out.println("Infectado a 1 paso");
						estado = 18;
					} else {
						estado = 2;
						//System.out.println("Infectado arriba");
					}
				}
			}

		}

		//System.out.println("Estado devuelto: " + estado);
	}

	// Devuelve la posicion del infectado más cercano
	public ArrayList<Double> getInfectado(StateObservation stateObs) {

		int ancho = (int) (stateObs.getWorldDimension().getWidth() / stateObs.getBlockSize());
		int alto = (int) (stateObs.getWorldDimension().getHeight() / stateObs.getBlockSize());

		// posXAnt = posX;
		// posYAnt = posY;
		int posX = (int) (Math.round(stateObs.getAvatarPosition().x / stateObs.getBlockSize()));
		int posY = (int) (Math.round(stateObs.getAvatarPosition().y / stateObs.getBlockSize()));
		ArrayList posicion = new ArrayList<Double>();
		int distancia = 99999999;
		// Añado en la posicion 0 y en la 1 un -1, para poder comprobar si he encontrado
		// el tipo o no después
		posicion.add((double) -1);
		posicion.add((double) -1);
		// System.out.println("Ancho: " + ancho + " Alto: " + alto);

		this.nInfectadosAnt = this.nInfectados;

		nInfectados = 0;
		for (int i = 1; i < alto; i++) {
			for (int j = 1; j < ancho; j++) {
				if (stateObs.getObservationGrid()[j][i].size() > 0
						&& stateObs.getObservationGrid()[j][i].get(0).itype == INFECTADO) { // Infectado encontrado
					int distanciax = Math.abs(j - posX), distanciay = Math.abs(i - posY);
					this.nInfectados++;
					if ((distanciax + distanciay) < distancia) {
						distancia = distanciax + distanciay;
						posicion.set(0, (double) j);
						posicion.add(1, (double) i);
					}

				}
			}
		}

		nInfectados = nInfectados / 4;
		return posicion;
	}

	// Devuelve la posicion en la que se encuentra el elemento de cierto tipo
	public ArrayList getTipo(StateObservation stateObs, int tipo) {
		int ancho = (int) (stateObs.getWorldDimension().getWidth() / stateObs.getBlockSize());
		int alto = (int) (stateObs.getWorldDimension().getHeight() / stateObs.getBlockSize());
		boolean encontrado = false;
		int i = 0, j = 0;
		ArrayList posicion = new ArrayList<>();
		// Añado en la posicion 0 y en la 1 un -1, para poder comprobar si he encontrado
		// el tipo o no después
		posicion.add((double) -1);
		posicion.add((double) -1);
		// System.out.println("Ancho: " + ancho + " Alto: " + alto);

		while (i < alto && !encontrado) {
			// System.out.println("i: "+i);
			j = 0;
			while (j < ancho && !encontrado) {
				// System.out.println("j: "+j);
				if (stateObs.getObservationGrid()[j][i].size() > 0
						&& stateObs.getObservationGrid()[j][i].get(0).itype == tipo) {
					encontrado = true;
					posicion.set(0, (double) j);
					posicion.add(1, (double) i);
				}
				j++;
			}
			i++;
		}
		return posicion;

	}

	public int calculaRecompensa(StateObservation so) {
		int recompensa = 0;

		this.puntosAnt = this.puntosAct;
		puntosAct = so.getGameScore();

		// System.out.println("Infectados ant: " + nInfectadosAnt);
		// System.out.println("Infectados act: " + nInfectados);
		//System.out.println("Posicion actual:\n\t x: " + posX + ", y: " + posY);
		//System.out.println("Posicion anterior:\n\t x: " + posXAnt + ", y: " + posYAnt);
		
		//System.out.println("Distancia con el infectado: "+distanciatotalI);
		
		if(distanciaJorgeAnt > 3 && distanciatotalIant < 2 && so.getAvatarLastAction() == ACTIONS.ACTION_USE){
			//System.out.println("He dado cigarro bien");
			recompensa = 20;
		}else if (so.getAvatarLastAction() !=ACTIONS.ACTION_USE && this.posX == posXAnt && this.posY == posYAnt ) {
			//System.out.println("Me he quedado parado");
			recompensa = -20;
		}else if ((nInfectados == 0 || distanciaJorgeAnt < 6) && distanciaJorge > distanciaJorgeAnt) { // Me he alejado de Jorge
			recompensa = 20;
			//System.out.println("Me he alejado de Jorge (Recompensa)");
			// System.out.println("Distancia anterior: " + distanciaJorgeAnt);
		} else if (!atascado && distanciaJorgeAnt < 4  && distanciaJorge < distanciaJorgeAnt) {
			//System.out.println("Me he acercado a Jorge (Castigo)");
			recompensa = -20;
		}else if(nInfectados > 0 && distanciatotalI < distanciatotalIant){
			//System.out.println("Me he acercado al infectado");
			recompensa = 20;
		}else if(so.getAvatarLastAction() == ACTIONS.ACTION_USE && distanciatotalI >= 2){
			//System.out.println("Cigarro sin sentido");
			recompensa = -20;
		}
		/*else if(atascadoAnt && !atascado && distanciaJorge >= distanciaJorgeAnt && so.getAvatarLastAction() != ACTIONS.ACTION_USE) { //Me he desatascado moviendome
			System.out.println("Me he desatascado");
			recompensa = 20;
		}else if( ((atascadoAntH && xAnt != posX) || (atascadoAntV && yAnt != posY)) && so.getAvatarLastAction() != ACTIONS.ACTION_USE) { //Si estoy atascado y me muevo bien
			System.out.println("Me he desatascado");
			recompensa = 20;
		}*/else if ((atascadoV && libresDownAnt > libresUpAnt && yAnt < posY) ||
				 (atascadoV && libresDownAnt <= libresUpAnt && yAnt > posY) ||
				 (atascadoH && libresLAnt > libresRAnt && xAnt > posX) ||
				 (atascadoH && libresLAnt <= libresRAnt && xAnt < posX)) { //Evito que vaya a las esquinas
			//System.out.println("He evitado tirar a las esquinas");
			recompensa = 20;
		} else {
			recompensa = -20;
			//System.out.println("Else random");
		}
		return recompensa;
	}

	public void verMapa(StateObservation stateObs) {
		int ancho = (int) (stateObs.getWorldDimension().getWidth() / stateObs.getBlockSize());
		int alto = (int) (stateObs.getWorldDimension().getHeight() / stateObs.getBlockSize());

		for (int i = 0; i < alto; i++) {
			// System.out.print("\n: " + i + ": ");
			for (int j = 0; j < ancho; j++) {
				if (stateObs.getObservationGrid()[j][i].size() == 0) {
					System.out.print("9");
				} else {
					System.out.print("");
					System.out.print(stateObs.getObservationGrid()[j][i].get(0).itype);
				}
			}
		}
		// System.out.println("\n");
	}

	@Override
	public ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
		// TODO Auto-generated method stub
		// Decisión y envío de la acción correspondiente
		ACTIONS accion = ACTIONS.ACTION_NIL;
		// System.out.println("----------------------------------------------------------------------------\n--------------------------------------SIGUIENTE
		// ITERACION--------------------------------------");
		int ultimaAccion = -1, recompensa;

		if (stateObs.getAvatarLastAction() == ACTIONS.ACTION_UP)
			ultimaAccion = 0;
		else if (stateObs.getAvatarLastAction() == ACTIONS.ACTION_DOWN)
			ultimaAccion = 1;
		else if (stateObs.getAvatarLastAction() == ACTIONS.ACTION_LEFT)
			ultimaAccion = 2;
		else if (stateObs.getAvatarLastAction() == ACTIONS.ACTION_RIGHT)
			ultimaAccion = 3;
		else if (stateObs.getAvatarLastAction() == ACTIONS.ACTION_USE)
			ultimaAccion = 4;
		else
			ultimaAccion = 1;

		if (estadoAnt >= 0) {
			this.estadoAnt = this.estado;
		}
		
		/*System.out.println("------------------------------------------------------------");
		System.out.println("--------------------Nueva iteracion-------------------------");
		System.out.println("------------------------------------------------------------");*/
		getEstadoActual(stateObs);
		recompensa = calculaRecompensa(stateObs);
		//System.out.println("------------------------------------------------------------");
		// verMapa(stateObs);
		// TODO
		ql.UpdateState(this.estadoAnt, ultimaAccion, recompensa, this.estado);
		estadoAnt = estado;

		//System.out.println("Accion:");
		switch (ql.GetAction(estado)) {
		case 0:
			accion = ACTIONS.ACTION_UP;
			//System.out.println("Up");
			break;
		case 1:
			accion = ACTIONS.ACTION_DOWN;
			//System.out.println("Down");
			break;
		case 2:
			accion = ACTIONS.ACTION_LEFT;
			//System.out.println("Left");
			break;
		case 3:
			accion = ACTIONS.ACTION_RIGHT;
			//System.out.println("Right");
			break;
		case 4:
			accion = ACTIONS.ACTION_USE;
			//System.out.println("Cigarro");
			break;
		}
		xAnt = stateObs.getAvatarPosition().x;
		yAnt = stateObs.getAvatarPosition().y;

		return accion;
	}

}
