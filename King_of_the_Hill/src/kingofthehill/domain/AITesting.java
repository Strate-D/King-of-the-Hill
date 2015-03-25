/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.domain;

/**
 *
 * @author Bas
 */
public class AITesting {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        GameManager gm = new GameManager(new Player("Host", 20));
        //gm.setDebugLevelAI(true);
        
        AI ai = (AI)gm.getPlayers().get(1);
        for(int i = 0; i<10; i++)
        {
            System.out.println();
            System.out.println();

            
            System.out.println("Turn: " + (i + 1));
            gm.doStep();
            
            System.out.println(" -------------- ");
            System.out.println("|              |");
            for(int j = 0; j<4; j++)
            {
                System.out.println("|              |[" + checkPlayerBaseSpot(ai, j*4) + "][" + checkPlayerBaseSpot(ai, j*4+1) + "][" + checkPlayerBaseSpot(ai, j*4+2) + "][" + checkPlayerBaseSpot(ai, j*4+3) + "]|" + ai.getAttackAtLane(j));
            }
            System.out.println("|              |");
            System.out.println(" -------------- ");
            
            for(int j = 0; j<4; j++)
            {
                System.out.println("  [" + checkPlayerBaseSpot(ai, 16+j) + "][" + checkPlayerBaseSpot(ai, 16+j+4) + "][" + checkPlayerBaseSpot(ai, 16+j+8) + "][" + checkPlayerBaseSpot(ai, 16+j+12) + "]");
            }
            System.out.println("  ------------");
            System.out.println("   " + ai.getAttackAtLane(4) + "  " + ai.getAttackAtLane(5) + "  " + ai.getAttackAtLane(6) + "  " + ai.getAttackAtLane(7));
            System.out.println("");
        }
    }
    
    private static String checkPlayerBaseSpot(AI player, int spot)
    {
        Unit u = player.getBase().getUnit(spot);
        if(u == null)
            return " ";
        else if(u instanceof Defence)
            return "D";
        else if(u instanceof Melee)
            return "M";
        else
            return "R";
    }
}
