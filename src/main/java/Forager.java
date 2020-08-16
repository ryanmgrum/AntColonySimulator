import dataStructures.LinkedList;
import dataStructures.LinkedStack;
import dataStructures.ListGraph;
import java.util.Random;

/** Forager is the class encapsulating the attributes and methods
 *  that describe a Forager Ant.
 *
 * @author Ryan McAllister-Grum
 */
final class Forager extends Ant {
    // foodCarrying tracks whether a Forager is carrying any food
    // and how much.
    private Integer foodCarrying;
    // lastLocationX tracks the X-coordinate of the most recent node
    // the Forager was in.
    private Integer lastLocationX;
    // lastLocationY tracks the Y-coordinate of the most recent node
    // the Forager was in.
    private Integer lastLocationY;
    // moveHistoryX tracks the x-axis movements of the Forager while
    // they forage for nodes containing food.
    private final LinkedStack moveHistoryX;
    // moveHistoryY tracks the y-axis movements of a Forager while
    // they forage for nodes containing food.
    private final LinkedStack moveHistoryY;
    /* Locations is a ListGraph mapping the locations the Forager
     * moves to as they are searching for food. It is primarily used to
     * detect and avoid loops in the destination node.
     */
    private final ListGraph locations;
    

    
    /** Constructor that takes the Forager's new ID, max turn age, and
     *  x-and-y-coordinate location.
     *  @param id The Forager's unique id.
     *  @param maxTurnAge The maximum turn age this Forager can live
     *  for until it dies of old age.
     *  @param locationX The starting x-coordinate location in the colony
     *  for this Forager.
     *  @param locationY The starting y-coordinate location in the colony
     *  for this Forager.
     *  @return The new instance of a Forager ant.
     */
    Forager(Integer id, Integer maxTurnAge, Integer locationX, Integer locationY) {
        super(id, maxTurnAge, locationX, locationY);
        foodCarrying = 0;
        lastLocationX = 0;
        lastLocationY = 0;
        moveHistoryX = new LinkedStack();
        moveHistoryY = new LinkedStack();
        locations = new ListGraph();
    }
    
    /** kill flags this Forager as dead, adds it to the deadAnts Colony
     *  LinkedQueue, and drops any food it was carrying at the time.
     */
    @Override
    void kill() {
        super.kill();
        getNode().addFood(foodCarrying);
        moveHistoryX.clear();
        moveHistoryY.clear();
        locations.clear();
    }
    
    /** Move moves this Forager ant to one of the adjacent colony squares.
     *  @param rng Used to add random probability to the movement,
     *  unless the Forager is following a pheromone trail.
     *  If multiple squares contain the same amount of pheromone
     *  above zero, choose one of them randomly.
     */
    private void move(Random rng) {
        // First, if foraging, check if there is a single node with
        // the largest amount of pheromone nearby.
        if (foraging()) {
            // maxPheromoneNodes counts the number of surrounding nodes
            // with the highest level of pheromone.
            Integer maxPheromoneNodes = 0;
            // maxPheromoneLevel tracks the currently highest level of
            // pheromone in the surrounding nodes.
            Integer maxPheromoneLevel = 0;
            // maxPheromoneNode X and Y hold the last max node's XY-coordinates.
            Integer maxPheromoneNodeX = 0;
            Integer maxPheromoneNodeY = 0;
            
            // Search the surrounding ColonyNodes' pheromoneLevel.
            for (Integer x = getLocationX() - 1; x <= getLocationX() + 1; x++)
                for (Integer y = getLocationY() - 1; y <= getLocationY() + 1; y++)
                    // First check that current position is not outside Colony bounds.
                    if (x >= 0 && y >= 0 && x < COLONY.getColonyWidth() && y < COLONY.getColonyHeight())
                        // If there is a node with a higher pheromone level, make a note of it.
                        // Avoid moving to the previous node.
                        if (COLONY.getColonyNode(x, y).getPheromone() > maxPheromoneLevel
                            && !x.equals(getLocationX()) && !y.equals(getLocationY())
                            && !x.equals(lastLocationX) && !y.equals(lastLocationY)
                            && COLONY.getColonyNode(x, y).isRevealed()
                        ) {
                            maxPheromoneNodeX = x;
                            maxPheromoneNodeY = y;
                            maxPheromoneLevel = COLONY.getColonyNode(x, y).getPheromone();
                            maxPheromoneNodes = 1;
                        /* Else if there is a node with the same amount of pheromone
                         * and the max pheromone is not currently zero or the
                         * Forager's current location, include it
                         * in the count of maxPheromoneNodes.
                         */
                        } else if (COLONY.getColonyNode(x, y).getPheromone().equals(maxPheromoneLevel)
                                  && maxPheromoneLevel > 0
                                  && !x.equals(getLocationX()) && !y.equals(getLocationY())
                                  && !x.equals(lastLocationX) && !y.equals(lastLocationY)
                                  && COLONY.getColonyNode(x, y).isRevealed()
                          ) {
                            maxPheromoneNodeX = x;
                            maxPheromoneNodeY = y;
                            maxPheromoneNodes++;
                        }
            
            
            // Next, decide where to move.
            /* If we have more than one square with the same amount of pheromone,
             * randomly pick between them.
             * Otherwise, if there is only one, continue to the actual move phase.
             */
            Integer nodeChoiceX = 0;
            Integer nodeChoiceY = 0;
            if (maxPheromoneNodes > 1) {
                do {
                    nodeChoiceX = getLocationX() + (rng.nextInt(3) - 1);
                    nodeChoiceY = getLocationY() + (rng.nextInt(3) - 1);
                }
                while (
                    ((nodeChoiceX.equals(getLocationX()) && nodeChoiceY.equals(getLocationY())) ||
                    (nodeChoiceX.equals(lastLocationX) && nodeChoiceY.equals(lastLocationY))) ||
                    nodeChoiceX < 0 || nodeChoiceY < 0 || nodeChoiceX >= COLONY.getColonyWidth() ||
                    nodeChoiceY >= COLONY.getColonyHeight() ||
                    !COLONY.getColonyNode(nodeChoiceX, nodeChoiceY).getPheromone().equals(maxPheromoneLevel)
                    || !COLONY.getColonyNode(nodeChoiceX, nodeChoiceY).isRevealed()
                );
                
            /* If there is no pheromone around, pick a node at random
             * that is not the most recent node or the Forager's
             * current location.
             */
            } else if (maxPheromoneNodes.equals(0)) {
                // If only one surrounding node is revealed and it is
                // lastLocation, move to it.
                Integer revealedNodeCount = 0;
                for (Integer x = getLocationX() - 1; x <= getLocationX() + 1; x++)
                    for (Integer y = getLocationY() - 1; y <= getLocationY() + 1; y++)
                        // First check that current position is not outside Colony bounds.
                        if (x >= 0 && y >= 0 && x < COLONY.getColonyWidth() && y < COLONY.getColonyHeight())
                            if (COLONY.getColonyNode(x, y).isRevealed() &&
                                !x.equals(getLocationX()) && !y.equals(getLocationY())
                            )
                                revealedNodeCount++;
                
                // If there is only one revealed node nearby,
                // it must be the previous location, so go to it.
                if (revealedNodeCount.equals(1)) {
                    nodeChoiceX = lastLocationX;
                    nodeChoiceY = lastLocationY;
                } else
                    do { 
                        nodeChoiceX = getLocationX() + (rng.nextInt(3) - 1);
                        nodeChoiceY = getLocationY() + (rng.nextInt(3) - 1);
                    }
                    while (
                        ((nodeChoiceX.equals(getLocationX()) && nodeChoiceY.equals(getLocationY())) ||
                        (nodeChoiceX.equals(lastLocationX) && nodeChoiceY.equals(lastLocationY))) ||
                        nodeChoiceX < 0 || nodeChoiceY < 0 || nodeChoiceX >= COLONY.getColonyWidth() ||
                        nodeChoiceY >= COLONY.getColonyHeight()
                        || !COLONY.getColonyNode(nodeChoiceX, nodeChoiceY).isRevealed()
                    );
                
            } else { // maxPheromoneNodes == 1, set nodeChoice to maxPheromoneNodes.
                nodeChoiceX = maxPheromoneNodeX;
                nodeChoiceY = maxPheromoneNodeY;
            }
            
            /* Before committing to the move, check and see whether the
             * destination node will result in a loop. Want to avoid looping
             * around.
             */
            avoidLoop(nodeChoiceX, nodeChoiceY, rng);

                
            // Move the Forager ant.
            lastLocationX = getLocationX();
            lastLocationY = getLocationY();
            moveHistoryX.push(getLocationX());
            moveHistoryY.push(getLocationY());
            super.move(nodeChoiceX, nodeChoiceY);
            
            // Finally, remove any loop that is on the move history stack.
            removeLoop();
            
        } else { // Follow moveHistory back to the Queen.
            if (!moveHistoryX.isEmpty()) {
                super.move((Integer) moveHistoryX.pop(), (Integer) moveHistoryY.pop());
                // Once we reach the Queen, clear locations.
                if (COLONY.getColonyNode(getLocationX(), getLocationY()).isQueenPresent()) {
                    moveHistoryX.clear();
                    moveHistoryY.clear();
                    locations.clear();
                    lastLocationX = 0;
                    lastLocationY = 0;
                }
            }
        }
    }
    
    /** pickUpFood makes this Forager take one unit of food from its
     *  current ColonyNode.
     */
    private void pickUpFood() {
        // Can only take food if food is present.
        // Cannot take food from the Queen's node.
        // Also cannot take food if already have food.
        if (getNode().getFoodAvailable() > 0 && !getNode().isQueenPresent()
            && foodCarrying.equals(0)) {
            getNode().takeFood(1);
            foodCarrying = 1;
        }
    }
    
    /** dropFood makes the Forager place their food in their current
     *  ColonyNode square.
     */
    private void dropFood() {
        // Can only drop food if this Forager is carrying food.
        if (foodCarrying.equals(1)) {
            getNode().addFood(foodCarrying);
            foodCarrying = 0;
        }
    }
    
    /** dropPheromone adds pheromone to the current ColonyNode square.
     */
    private void dropPheromone() {
        // Can only drop pheromone if this Forager is carrying food.
        // Do not drop pheromone where the Queen is located.
        // Do not drop pheromone if > 1000.
        if (!foraging() && !getNode().isQueenPresent() &&
            getNode().getPheromone() < 1000) {
            getNode().addPheromone(10);
        }
    }
    
    /** foraging is used to check whether the Forager has food,
     *  and is thus not foraging.
     *  @return Whether the ant is foraging for food.
     */
    private Boolean foraging() {
        return foodCarrying.equals(0);
    }
    
    /** avoidLooping iteratively checks whether the Forager ant
     *  is going in a circle (will end up in a ColonyNode
     *  it visited previously). If so, the node is avoided
     *  and the other surrounding nodes are checked. The
     *  first non-loop node from top-left to bottom-right
     *  is randomly chosen. If no options are available, set
     *  nextLocations to lastLocations.
     *  @param nextLocationX The x-location it plans to move.
     *  @param nextLocationY The y-location it plans to move.
     *  @param rng Random used to randomly choose surrounding
     *  nodes.
     */
    private void avoidLoop(Integer nextLocationX, Integer nextLocationY, Random rng) {
        /* First, try and add the next location to locations.
         * If it fails, start searching the other surrounding
         * nodes for a valid move. If none are available,
         * set nextLocations to lastLocations.
         */
        if (locations.add(COLONY.getColonyNode(nextLocationX, nextLocationY)))
            if (locations.size() > 1)
                locations.addEdge(
                    COLONY.getColonyNode(lastLocationX, lastLocationY),
                    COLONY.getColonyNode(nextLocationX, nextLocationY),
                    0
                );
        else { // Unable to add location, start searching for next valid location.
            Boolean added = false;
            /* Create a temporary list of surrounding node values (0-7)
             * so we can try and randomly select each one once. If they
             * all fail (attempts becomes empty), added remains false,
             * and we go to lastLocations.
             */
            LinkedList attempts = new LinkedList();
            for (Integer i = 0; i < 8; i++)
                attempts.add(i);
            
            while (!attempts.isEmpty() && !added) {
                // First select a node randomly.
                // If only one remaining, select it.
                Integer nodeChoice = -1;
                while (nodeChoice.equals(-1)) {
                    if (attempts.size() == 1) {
                        nodeChoice = (Integer) attempts.get();
                        attempts.remove();
                    } else {
                        Integer tmp = rng.nextInt(8);
                        if (tmp < attempts.size()) {
                            nodeChoice = (Integer) attempts.get(tmp);
                            attempts.remove(tmp);
                        }
                    }
                }
            
                // Get the node coordinates from the surrounding valid nodes.
                Integer nodeChoiceX = 0;
                Integer nodeChoiceY = 0;
                for (Integer i = 0; i <= nodeChoice;)
                    for (Integer x = getLocationX() - 1; x <= getLocationX() + 1; x++) {
                        for (Integer y = getLocationY() - 1; y <= getLocationY() + 1; y++) {
                            // First check that current position is not outside Colony bounds.
                            if (x >= 0 && y >= 0 && x < COLONY.getColonyWidth() && y < COLONY.getColonyHeight())
                                // Avoid the current node, last node, and hidden nodes.
                                if (!x.equals(getLocationX()) && !y.equals(getLocationY())
                                    && !x.equals(lastLocationX) && !y.equals(lastLocationY)
                                    && COLONY.getColonyNode(x, y).isRevealed()
                                ) {
                                    nodeChoiceX = x;
                                    nodeChoiceY = y;
                                    i++;
                                }
                            if (i > nodeChoice)
                                break;
                        }
                        if (i > nodeChoice)
                            break;
                    }
                
                // Check whether the choice is a valid move in locations.
                if (COLONY.getColonyNode(nodeChoiceX, nodeChoiceY).isRevealed())
                    if (locations.add(COLONY.getColonyNode(nodeChoiceX, nodeChoiceY))) {
                        if (locations.size() > 1)
                            locations.addEdge(
                                COLONY.getColonyNode(lastLocationX, lastLocationY),
                                COLONY.getColonyNode(nodeChoiceX, nodeChoiceY),
                                0
                            );
                        nextLocationX = nodeChoiceX;
                        nextLocationY = nodeChoiceY;
                        added = true;
                    }
            }
        
            // If we failed to find a valid node, go to last location.
            if (!added) {
                nextLocationX = lastLocationX;
                nextLocationY = lastLocationY;
            }
        }
    }
    
    /** removeLoop removes any loop present on the moveHistoryX and Y stacks.
     *  They do occur when foraging, but should not be present when returning.
     *  This should remove any lengthy return trips that occur from loops.
     */
    private void removeLoop() {
        // If moveHistoryX is empty, so is Y.
        if (!moveHistoryX.isEmpty()) {
            LinkedStack tempX = new LinkedStack();
            LinkedStack tempY = new LinkedStack();
            Boolean loop = false;
            while(!moveHistoryX.isEmpty() && !loop) {
                Integer x = (Integer) moveHistoryX.pop();
                Integer y = (Integer) moveHistoryY.pop();
                if (!(x.equals(getLocationX()) && y.equals(getLocationY()))) {
                    tempX.push(x);
                    tempY.push(y);
                } else
                    loop = true;
            }
            // If there is a loop, discard what was popped off,
            // otherwise put it back on moveHistoryX and Y.
            if (!loop)
                while(!tempX.isEmpty()) {
                    moveHistoryX.push(tempX.pop());
                    moveHistoryY.push(tempY.pop());
                }
        }
    }
    
    /** takeAction processes this Forager ant's turn.
     *  @param rng Used to add randomness to the actions.
     */
    void takeAction(Random rng) {
        // Cannot act if dead.
        if (!isDead()) {
            // First check if Forager should die of old age.
            if (getTurnAge() > getMaxTurnAge())
                kill();
            // If in Return-to-Next mode, drop pheromone, then move.
            else if (!foraging()) {
                dropPheromone();
                move(rng);
                if (getNode().isQueenPresent()) // Drop food
                    dropFood();
            } else { // Foraging for food
                move(rng);
                // If new ColonyNode contains food, pick up 1 unit.
                if (getNode().getFoodAvailable() > 0)
                    pickUpFood();
            }
        }
    }
}