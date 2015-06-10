/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.domain;

import java.rmi.RemoteException;

/**
 * Runnable code for testing the AI thinking pattern
 *
 * @author Bas
 */
public class AITesting {

    /**
     * @param args the command line arguments
     * @throws java.rmi.RemoteException When somthing goes wrong communicating
     * with the server
     */
    public static void main(String[] args) throws RemoteException {
        GameManager gm = new GameManager();
        AI ai1 = (AI) (gm.getPlayers().get(1));
        ai1.setAIType(AIState.AGRESSIVE);

        AI ai = (AI) gm.getPlayers().get(1);
        for (int i = 0; i < 500; i++) {
            System.out.println();
            System.out.println();

            System.out.println("Turn: " + (i + 1));
            //gm.doStep();

            System.out.println(" -------------- ");
            System.out.println("|              |");
            for (int j = 0; j < 4; j++) {
                System.out.println("|              |[" + checkPlayerBaseSpot(ai, j * 4) + "][" + checkPlayerBaseSpot(ai, j * 4 + 1) + "][" + checkPlayerBaseSpot(ai, j * 4 + 2) + "][" + checkPlayerBaseSpot(ai, j * 4 + 3) + "]|" + ai.getAttackAtLane(j));
            }
            System.out.println("|              |");
            System.out.println(" -------------- ");

            for (int j = 0; j < 4; j++) {
                System.out.println("  [" + checkPlayerBaseSpot(ai, 16 + j) + "][" + checkPlayerBaseSpot(ai, 16 + j + 4) + "][" + checkPlayerBaseSpot(ai, 16 + j + 8) + "][" + checkPlayerBaseSpot(ai, 16 + j + 12) + "]");
            }
            System.out.println("  ------------");
            System.out.println("   " + ai.getAttackAtLane(4) + "  " + ai.getAttackAtLane(5) + "  " + ai.getAttackAtLane(6) + "  " + ai.getAttackAtLane(7));
            System.out.println("");
        }

        System.out.println(ai1.getMoney());
    }

    /**
     * Check the type of the unit and print it to the base output
     *
     * @param player The current player
     * @param spot The spot of the base
     * @return A String with the type op unit
     */
    private static String checkPlayerBaseSpot(AI player, int spot) {
        Unit u = player.getBase().getUnit(spot);
        if (u == null) {
            return " ";
        } else if (u instanceof Defence) {
            return "D";
        } else if (u instanceof Melee) {
            return "M";
        } else {
            return "R";
        }
    }
}
