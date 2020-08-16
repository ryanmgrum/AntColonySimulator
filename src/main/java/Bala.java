
import java.util.Random;

/** The Bala class contains all the implementation details to create
 *  a new Bala ant.
 * @author Ryan McAllister-Grum
 */
final class Bala extends Ant {
    
    /** Constructor that creates a new Bala ant with
     *  the given unique id, maximum turn age, and x and y
     *  coordinate location.
     *  @param id The Bala's ant's unique ID.
     *  @param maxTurnAge The oldest this Bala ant can in turn counts.
     *  @param locationX The x-coordinate to spawn the Bala ant.
     *  @param locationY The y-coordinate to spawn the Bala ant.
     *  @return A reference to this new Bala ant.
     */
    Bala(Integer id, Integer maxTurnAge, Integer locationX, Integer locationY) {
        super(id, maxTurnAge, locationX, locationY);
    }
    
    /** Move moves this Bala ant around the Colony grid while using the
     *  rng parameter to randomize the movement.
     *  @param rng Used to randomize the movement.
     */
    private void move(Random rng) {
        // Bala ants should always move randomly.
        // NodeChoices are ultimately used to decide where to move
        // this Bala.
        Integer nodeChoiceX;
        Integer nodeChoiceY;
        
        // Randomly select a surrounding ColonyNode.
        do { 
            nodeChoiceX = getLocationX() + (rng.nextInt(3) - 1);
            nodeChoiceY = getLocationY() + (rng.nextInt(3) - 1);
        }
        while (
            (nodeChoiceX.equals(getLocationX()) && nodeChoiceY.equals(getLocationY())) ||
            nodeChoiceX < 0 || nodeChoiceY < 0 || nodeChoiceX >= COLONY.getColonyWidth() ||
            nodeChoiceY >= COLONY.getColonyHeight()
        );
        
        // Move the Bala to the chosen node.
        super.move(nodeChoiceX, nodeChoiceY);
    }
    
    /** Attack picks a randomly friendly Ant to try and kill.
     *  @param rng Used to add probability to the attack and
     *  Ant selection.
     */
    private void attack(Random rng) {
        // Can only attack if there is a friendly Ant
        // in the current ColonyNode.
        if (getNode().getFriendlyAntCount() > 0) {
            // Randomly select a friendly Ant to randomly try to kill
            // (50% chance).
            Ant ant = getFriendlyAnt(rng);
            if (ant != null)
                if (rng.nextInt(100) < 50)
                    ant.kill();
        }
    }
    
    /** getFriendlyAnt is used to pick a random friendly Ant to attack.
     *  @param rng Used to randomly pick a target.
     *  @return The new friendly Ant to target.
     */
    private Ant getFriendlyAnt(Random rng) {
        Integer index = rng.nextInt(getNode().getFriendlyAntCount());
        return getNode().getFriendlyAnt(index);
    }
    
    /** takeAction processes this Bala ant's turn.
     *  @param rng Used in adding probability and randomness to actions.
     */
    void takeAction(Random rng) {
        // Cannot do anything if dead.
        if (!isDead()) {
            // If the Bala is too old, they die.
            if (getTurnAge() > getMaxTurnAge())
                kill();
            else if (getNode().getFriendlyAntCount() > 0)
                attack(rng);
            else
                move(rng);
        }
    }
}