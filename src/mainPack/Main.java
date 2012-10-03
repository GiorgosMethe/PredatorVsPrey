package mainPack;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import agentsPack.QPredator;
import agentsPack.SarsaPredator;
import environmentPack.Coordinate;
import functionsPack.PolicyEvaluation;
import functionsPack.PolicyIteration;
import functionsPack.RandomSimulation;
import functionsPack.ValueIteration;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));

		int choiceInt = 0;

		System.out.println("Welcome to the PredatorVsPrey V.2");
		System.out.println("------------------------------------");
		do {

			System.out.println("1. Simple simulation");
			System.out.println("2. Value Iteration");
			System.out.println("3. Policy Evaluation");
			System.out.println("4. Policy Iteration");
			System.out.println("5. Q-Learning");
			System.out.println("6. Sarsa-Learning");
			System.out.println("7. Exit");

			String choice = null;
			do {
				System.out.print("\nGive your choice (1-6):");
				try {
					choice = reader.readLine();
					choiceInt = Integer.parseInt(choice);
				} catch (Exception e) {

				}
			} while (choiceInt < 1 || choiceInt > 7);

			if (choiceInt == 1) {

				RandomSimulation.runSimulation();

			} else if (choiceInt == 2) {

				String gammaStr = null;
				double gamma = 999;
				do {
					System.out.print("\nGive a positive discount factor < 1 :");
					try {
						gammaStr = reader.readLine();
						gamma = Double.parseDouble(gammaStr);
					} catch (Exception e) {
						System.err.println("Oh no you broke the program!");
					}
				} while (gamma > 1 || gamma <= 0);

				String thetaStr = null;
				double theta = -1;
				do {
					System.out
							.print("\nGive a positive number for theta value :");
					try {
						thetaStr = reader.readLine();
						theta = Double.parseDouble(thetaStr);
					} catch (Exception e) {
						System.err.println("Oh no you broke the program!");
					}
				} while (theta < 0);

				String xStr = null;
				int x = 11;
				do {
					System.out
							.print("\nGive the prey's x-position on the grid world (0-10):");
					try {
						xStr = reader.readLine();
						x = Integer.parseInt(xStr);
					} catch (Exception e) {

					}
				} while (x < 0 || x > 10);

				String yStr = null;
				int y = 11;
				do {
					System.out
							.print("\nGive the prey's y-position on the grid world (0-10):");
					try {
						yStr = reader.readLine();
						y = Integer.parseInt(yStr);
					} catch (Exception e) {

					}
				} while (y < 0 || y > 10);

				ValueIteration.Run(gamma, theta, new Coordinate(x, y));

			} else if (choiceInt == 3) {

				String gammaStr = null;
				double gamma = 999;
				do {
					System.out.print("\nGive a positive discount factor < 1 :");
					try {
						gammaStr = reader.readLine();
						gamma = Double.parseDouble(gammaStr);
					} catch (Exception e) {
						System.err.println("Oh no you broke the program!");
					}
				} while (gamma > 1 || gamma <= 0);

				String thetaStr = null;
				double theta = -1;
				do {
					System.out
							.print("\nGive a positive number for theta value :");
					try {
						thetaStr = reader.readLine();
						theta = Double.parseDouble(thetaStr);
					} catch (Exception e) {
						System.err.println("Oh no you broke the program!");
					}
				} while (theta < 0);

				String xStr = null;
				int x = 11;
				do {
					System.out
							.print("\nGive the prey's x-position on the grid world (0-10):");
					try {
						xStr = reader.readLine();
						x = Integer.parseInt(xStr);
					} catch (Exception e) {

					}
				} while (x < 0 || x > 10);

				String yStr = null;
				int y = 11;
				do {
					System.out
							.print("\nGive the prey's y-position on the grid world (0-10):");
					try {
						yStr = reader.readLine();
						y = Integer.parseInt(yStr);
					} catch (Exception e) {

					}
				} while (y < 0 || y > 10);

				PolicyEvaluation.Run(gamma, theta, new Coordinate(x, y));

			} else if (choiceInt == 4) {

				String gammaStr = null;
				double gamma = 999;
				do {
					System.out.print("\nGive a positive discount factor < 1 :");
					try {
						gammaStr = reader.readLine();
						gamma = Double.parseDouble(gammaStr);
					} catch (Exception e) {
						System.err.println("Oh no you broke the program!");
					}
				} while (gamma > 1 || gamma <= 0);

				String thetaStr = null;
				double theta = -1;
				do {
					System.out
							.print("\nGive a positive number for theta value :");
					try {
						thetaStr = reader.readLine();
						theta = Double.parseDouble(thetaStr);
					} catch (Exception e) {
						System.err.println("Oh no you broke the program!");
					}
				} while (theta < 0);

				PolicyIteration.Run(gamma, theta);

			} else if (choiceInt == 5) {
				
				String numberStr = null;
				int number = 999;
				do {
					System.out.print("\nGive a positive integer >0 for the number of steps:");
					try {
						numberStr = reader.readLine();
						number = Integer.parseInt(numberStr);
					} catch (Exception e) {
						System.err.println("Oh no you broke the program!");
					}
				} while (number <= 0);
				
				
				String alphaStr = null;
				double alpha = 999;
				do {
					System.out.print("\nGive a positive <1 learning rate for the predator:");
					try {
						alphaStr = reader.readLine();
						alpha = Double.parseDouble(alphaStr);
					} catch (Exception e) {
						System.err.println("Oh no you broke the program!");
					}
				} while (alpha > 1 || alpha <= 0);
				
				String gammaStr = null;
				double gamma = 999;
				do {
					System.out.print("\nGive a positive discount factor for < 1 :");
					try {
						gammaStr = reader.readLine();
						gamma = Double.parseDouble(gammaStr);
					} catch (Exception e) {
						System.err.println("Oh no you broke the program!");
					}
				} while (gamma > 1 || gamma <= 0);
				
				String policy = "";
				do {
					System.out.print("\nType (s) for SoftMax policy or (e) for e-Greedy:");
					try {
						policy = reader.readLine();
					} catch (Exception e) {
						System.err.println("Oh no you broke the program!");
					}
				} while (!policy.equalsIgnoreCase("e") && !policy.equalsIgnoreCase("s"));
				
				String policyParStr = null;
				double policyPar = 999;
				do {
					System.out.print("\nGive a positive <1 policy parameter, alpha for e-Greedy, temperature for softMax:");
					try {
						policyParStr = reader.readLine();
						policyPar = Double.parseDouble(policyParStr);
					} catch (Exception e) {
						System.err.println("Oh no you broke the program!");
					}
				} while (policyPar > 1 || policyPar < 0);

				QPredator.RunQLearning(number, alpha, gamma, policy, policyPar);

			} else if (choiceInt == 6) {

				String numberStr = null;
				int number = 999;
				do {
					System.out.print("\nGive a positive integer >0 for the number of steps:");
					try {
						numberStr = reader.readLine();
						number = Integer.parseInt(numberStr);
					} catch (Exception e) {
						System.err.println("Oh no you broke the program!");
					}
				} while (number <= 0);
				
				
				String alphaStr = null;
				double alpha = 999;
				do {
					System.out.print("\nGive a positive <1 learning rate for the predator:");
					try {
						alphaStr = reader.readLine();
						alpha = Double.parseDouble(alphaStr);
					} catch (Exception e) {
						System.err.println("Oh no you broke the program!");
					}
				} while (alpha > 1 || alpha <= 0);
				
				String gammaStr = null;
				double gamma = 999;
				do {
					System.out.print("\nGive a positive discount factor for < 1 :");
					try {
						gammaStr = reader.readLine();
						gamma = Double.parseDouble(gammaStr);
					} catch (Exception e) {
						System.err.println("Oh no you broke the program!");
					}
				} while (gamma > 1 || gamma <= 0);
				
				String policy = "";
				do {
					System.out.print("\nType (s) for SoftMax policy or (e) for e-Greedy:");
					try {
						policy = reader.readLine();
					} catch (Exception e) {
						System.err.println("Oh no you broke the program!");
					}
				} while (!policy.equalsIgnoreCase("e") && !policy.equalsIgnoreCase("s"));
				
				String policyParStr = null;
				double policyPar = 999;
				do {
					System.out.print("\nGive a positive <1 policy parameter, alpha for e-Greedy, temperature for softMax:");
					try {
						policyParStr = reader.readLine();
						policyPar = Double.parseDouble(policyParStr);
					} catch (Exception e) {
						System.err.println("Oh no you broke the program!");
					}
				} while (policyPar > 1 || policyPar < 0);
				
				
				SarsaPredator.RunSarsa(number, alpha, gamma, policy, policyPar);

			} else if (choiceInt == 7) {

				System.out.println("Oh whyy???!!! :(");

			} else {

				System.err.println("Wrong choice, please give a number 1-5");

			}

		} while (choiceInt != 7);

	}

}
