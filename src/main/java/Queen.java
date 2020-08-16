import java.util.Random;

/**
 * Queen class defines the Queen Ant of the colony.
 * @author Ryan McAllister-Grum
 */
final class Queen extends Ant {
    /** Queen constructor creates a new queen ant with the given id
     *  and maximum age at the given xy-coordinate location in the colony.
     *  @param id The Queen ant's ID.
     *  @param maxTurnAge The Queen ant's maximum turn age.
     *  @param locationX Create the queen ant at the given x-coordinate location in the colony.
     *  @param locationY Create the queen ant at the given y-coordinate location in the colony.
     *  @return the new Queen ant.
     */
    Queen(Integer id, Integer maxTurnAge, Integer locationX, Integer locationY) {
        super(id, maxTurnAge, locationX, locationY);
    }
    
    /** hatchAnt creates a new Ant for the colony.
     *  Percentages to spawn each Ant type is as follows:
     *  Forager: 50% (50-99)
     *  Scout: 25% (25-49)
     *  Soldier: 25% (0-24)
     *  @param rng The Colony's rng attribute to randomly create a new Ant.
     *  @return The new Ant.
     */
    private Ant hatchAnt(Random rng) {
        Integer guess = rng.nextInt(100);
        if (guess < 25) // 0-24: Soldier
            return new Soldier(COLONY.getNextId(), Colony.TURNS_TO_YEAR, getLocationX(), getLocationY());
        else if (guess >= 25 && guess <= 49) // 25-49: Scout
            return new Scout(COLONY.getNextId(), Colony.TURNS_TO_YEAR, getLocationX(), getLocationY());
        else // Forager
            return new Forager(COLONY.getNextId(), Colony.TURNS_TO_YEAR, getLocationX(), getLocationY());
    }
    
    /** eatFood makes the Queen consume one unit of food at her given location.
     *  @return Whether the Queen was able to eat a unit of food. If unable, she
     *  dies.
     */
    private Boolean eatFood() {
        // If the Queen dies from starvation (takeFood returns false), end the simulation.
        return getNode().takeFood(1);
    }
    
    /** takeAction processes a turn for the Queen Ant.
     *  @param rng Random used as part of adding random probability to actions.
     */
    void takeAction(Random rng) {
        // If the queen is too old or runs out of food, she dies.
        if (getTurnAge() > getMaxTurnAge() || !eatFood())
            kill();
        
        // Spawn a new Ant every 10 turns (ignore first turn).
        if (COLONY.getTurnCounter() != 0 && COLONY.getTurnCounter() % Colony.TURNS_TO_DAYS == 0)
            COLONY.addAnt(hatchAnt(rng));
    }
}