import java.util.Random;

/** Scout encapsulates all the functionality specific to a Scout ant.
 * @author Ryan McAllister-Grum
 */
final class Scout extends Ant {
    /** Constructor that creates a new Scout ant with the given unique id,
     *  maximum turn age, and at the given x-and-y-coordinates.
     *  @param id The Scout's unique ID.
     *  @param maxTurnAge The maximum turn age for this Scout ant.
     *  @param locationX The x-coordinate location to instantiate
     *  this new Scout ant in the colony.
     *  @param locationY The y-coordinate location to instantiate
     *  this new Scout ant in the colony.
     *  @return The new Scout ant.
     */
    Scout(Integer id, Integer maxTurnAge, Integer locationX, Integer locationY) {
        super(id, maxTurnAge, locationX, locationY);
    }
    
    /** scout makes this Scout ant randomly move to uncover hidden ColonyNodes.
     *  @param rng Used to make the movement random.
     */
    private void scout(Random rng) {
        // First determine where to move.
        Integer nodeChoiceX;
        Integer nodeChoiceY;
        do { 
            nodeChoiceX = getLocationX() + (rng.nextInt(3) - 1);
            nodeChoiceY = getLocationY() + (rng.nextInt(3) - 1);
        }
        while (
            ((nodeChoiceX.equals(getLocationX()) && nodeChoiceY.equals(getLocationY()))) ||
            nodeChoiceX < 0 || nodeChoiceY < 0 || nodeChoiceX >= COLONY.getColonyWidth() ||
            nodeChoiceY >= COLONY.getColonyHeight()
        );
        
        // Move there, and then reveal the node if it is hidden.
        super.move(nodeChoiceX, nodeChoiceY);
        if (!getNode().isRevealed())
            getNode().revealNode();
    }
    
    /** takeAction processes a turn for this Scout.
     *  @param rng Used to make scouting random.
     */
    void takeAction(Random rng) {
        // Cannot act if dead.
        if (!isDead())
            // If the Scout is too old, they die.
            if (getTurnAge() > getMaxTurnAge())
                kill();
            else
                scout(rng);
    }
}