import java.util.Random;

/** Soldier class defines everything necessary to create a Soldier ant.
 * @author Ryan McAllister-Grum
 */
final class Soldier extends Ant {
    
    /** Constructor that creates a new Soldier ant with the given id,
     *  maximum turn age, and at the given x and y location in the
     *  Colony.
     *  @param id The Soldier's new unique ID.
     *  @param maxTurnAge The Soldier's max age in turns before they die
     *  of old age.
     *  @param locationX The x-coordinate location in the Colony to create
     *  the new Soldier ant.
     *  @param locationY The y-coordinate location in the Colony to create
     *  the new Soldier ant.
     *  @return The new Soldier ant.
     */
    Soldier(Integer id, Integer maxTurnAge, Integer locationX, Integer locationY) {
        super(id, maxTurnAge, locationX, locationY);
    }
    
    /** Move is used to move this Soldier ant around the Colony.
     *  Its move preference is to move to nodes with enemy Bala
     *  ants present.
     *  @param rng Used to add randomness to the ant's movement if
     *  there is no node nearby with a Bala ant. If there are
     *  multiple nodes with Bala ants, rng is used to randomly
     *  pick a node.
     */
    private void move(Random rng) {
        /* If this Soldier is in scout mode, meaning there are no
         * Bala ants in its ColonyNode, it should move to a
         * ColonyNode containing a Bala ant, otherwise it should
         * move randomly.
         */
        if (getNode().getBalaCount().equals(0)) {
            // NodeChoices are ultimately used to decide where to move
            // this Soldier.
            Integer nodeChoiceX = -1;
            Integer nodeChoiceY = -1;
            // balaNodes is used to count the number of nearby nodes with
            // Bala ants.
            Integer balaNodes = 0;
            
            // First scan surrounding nodes for Bala ants.
            for (Integer x = getLocationX() - 1; x <= getLocationX() + 1; x++)
                for (Integer y = getLocationY() - 1; y <= getLocationY() + 1; y++)
                    // First check that current position is not outside Colony bounds.
                    if (x >= 0 && y >= 0 && x < COLONY.getColonyWidth() && y < COLONY.getColonyHeight())
                        // If there is a node with a Bala ant inside it, make a note of it.
                        if (COLONY.getColonyNode(x, y).getBalaCount() > 0
                            && !x.equals(getLocationX()) && !y.equals(getLocationY())
                            && COLONY.getColonyNode(x, y).isRevealed()
                        ) {
                            nodeChoiceX = x;
                            nodeChoiceY = y;
                            balaNodes++;
                        }

            // If there are no Bala ants nearby, move randomly.
            if (balaNodes.equals(0) || nodeChoiceX.equals(-1))
                do { 
                    nodeChoiceX = getLocationX() + (rng.nextInt(3) - 1);
                    nodeChoiceY = getLocationY() + (rng.nextInt(3) - 1);
                }
                while (
                    (nodeChoiceX.equals(getLocationX()) && nodeChoiceY.equals(getLocationY())) ||
                    nodeChoiceX < 0 || nodeChoiceY < 0 || nodeChoiceX >= COLONY.getColonyWidth() ||
                    nodeChoiceY >= COLONY.getColonyHeight()
                    || !COLONY.getColonyNode(nodeChoiceX, nodeChoiceY).isRevealed()
                );
             // Randomly pick a nearby square with a Bala ant inside it.
            else if (balaNodes > 1)
                do { 
                    nodeChoiceX = getLocationX() + (rng.nextInt(3) - 1);
                    nodeChoiceY = getLocationY() + (rng.nextInt(3) - 1);
                }
                while (
                    (nodeChoiceX.equals(getLocationX()) && nodeChoiceY.equals(getLocationY())) ||
                    nodeChoiceX < 0 || nodeChoiceY < 0 || nodeChoiceX >= COLONY.getColonyWidth() ||
                    nodeChoiceY >= COLONY.getColonyHeight()
                    || !COLONY.getColonyNode(nodeChoiceX, nodeChoiceY).isRevealed()
                    || COLONY.getColonyNode(nodeChoiceX, nodeChoiceY).getBalaCount().equals(0)
                );

            // If balaNodes == 1, move to the only node.
            
            // Move to the chosen ColonyNode.
            super.move(nodeChoiceX, nodeChoiceY);
        } else // Soldier is in attack mode, try and kill a Bala.
            attack(rng);
    }
    
    /** Attack attempts to kill an enemy Bala ant.
     *  @param rng Provides randomness for the attack success rate.
     */
    private void attack(Random rng) {
        // Can only attack if there are Bala ants in the current
        // ColonyNode.
        if (getNode().getBalaCount() > 0) {
            // Randomly select a Bala to randomly try to kill
            // (50% chance).
            Bala enemy = getEnemyAnt(rng);
            if (enemy != null)
                if (rng.nextInt(100) < 50)
                    enemy.kill();
        }
    }
    
    /** getEnemyAnt is used to get a new random Bala ant for the Soldier to target.
     *  @param rng Used to randomly select an enemy Bala ant.
     *  @return This Soldier ant's enemyAnt attribute.
     */
    private Bala getEnemyAnt(Random rng) {
        Integer index = rng.nextInt(getNode().getBalaCount());
        return getNode().getBala(index);
    }
    
    /** takeAction processes a turn for this Soldier ant.
     *  @param rng Used to introduce probability in the Soldier's actions.
     */
    void takeAction(Random rng) {
        // Cannot do anything if dead.
        if (!isDead()) {
            // If the Soldier is too old, they die.
            if (getTurnAge() > getMaxTurnAge())
                kill();
            else
                move(rng);
        }
    }
}