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
public enum LastAIAction 
{
    NOTHING (0),
    PLACED_DEFENCE (1),
    PLACED_ATTACK (2);
    
    private final int id;
    
    private LastAIAction(int id)
    {
        this.id = id;
    }
    
    public int getId()
    {
       return id;
    }
}
