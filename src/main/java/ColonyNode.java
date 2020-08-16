
import dataStructures.Iterator;
import dataStructures.LinkedList;

/**
 * ColonyNode contains all the details for a given node in the colony grid.
 * @author Ryan McAllister-Grum
 */
final class ColonyNode {
    // The X index for this ColonyNode.
    private final Integer locationX;
    // The Y index for this ColonyNode.
    private final Integer locationY;
    // The amount of food in this node.
    private Integer foodAvailable;
    // The amount of pheromone in this node;
    // used by foragers to go directly to a food source.
    private Integer pheromoneLevel;
    // Whether the queen is in this node.
    private Boolean queenPresent;
    // The number of Forager ants in this node.
    private Integer foragerCount;
    // The number of Scout ants in this node.
    private Integer scoutCount;
    // The number of Soldier ants in this node.
    private Integer soldierCount;
    // A LinkedList of all the friendly ants in this node;
    // used by Bala ants to randomly pick a target to attack.
    private final LinkedList friendlyAnts;
    // A LinkedList of all the Bala ants in this node;
    // used by Soldier ants to randomly pick a target to attack.
    private final LinkedList enemyAnts;
    // A reference to the UI component ColonyNodeView that displays
    // information about this particular node.
    private final ColonyNodeView nodeView;

    
    /** ColonyNode constructor whose parameters contain all pertinent information
     * for populating a new ColonyNode.
     */
    ColonyNode(Integer newLocationX, Integer newLocationY, Integer newFoodAmount,
            Boolean isQueenPresent, Integer newForagerCount, Integer newScoutCount,
            Integer newSoldierCount, Integer newBalaCount) {
        // Initialize ColonyNode.
        locationX = newLocationX;
        locationY = newLocationY;
        foodAvailable = newFoodAmount;
        queenPresent = isQueenPresent;
        foragerCount = newForagerCount;
        scoutCount = newScoutCount;
        soldierCount = newSoldierCount;
        pheromoneLevel = 0;
        friendlyAnts = new LinkedList();
        enemyAnts = new LinkedList();
        
        // Setup nodeView based on quantities.
        nodeView = new ColonyNodeView();
        nodeView.setFoodAmount(foodAvailable);
        nodeView.setQueen(queenPresent);
        if (queenPresent) {
            nodeView.showQueenIcon();
            revealNode();
        }
        nodeView.setForagerCount(foragerCount);
        if (foragerCount > 0)
            nodeView.showForagerIcon();
        nodeView.setScoutCount(scoutCount);
        if (scoutCount > 0)
            nodeView.showScoutIcon();
        nodeView.setSoldierCount(soldierCount);
        if (soldierCount > 0)
            nodeView.showSoldierIcon();
        nodeView.setBalaCount(enemyAnts.size());
        if (enemyAnts.size() > 0)
            nodeView.showBalaIcon();
        nodeView.setID(locationX + "," + locationY);
    }
    
    /** getNodeView returns this node's ColonyNodeView.
     *  @return nodeView
     */
    ColonyNodeView getNodeView() {
        return nodeView;
    }
    
    /** addFriendlyAnt adds a non-Bala Ant to friendlyAnts.
     */
    void addFriendlyAnt(Ant ant) {
        if (!(ant instanceof Bala))
            if (!friendlyAnts.contains(ant)) {
                friendlyAnts.add(ant);
                if (ant instanceof Forager) {
                    if (foragerCount == 0)
                        nodeView.showForagerIcon();
                    foragerCount++;
                    nodeView.setForagerCount(foragerCount);
                } else if (ant instanceof Scout) {
                    if (scoutCount == 0)
                        nodeView.showScoutIcon();
                    scoutCount++;
                    nodeView.setScoutCount(scoutCount);
                } else if (ant instanceof Soldier) {
                    if (soldierCount == 0)
                        nodeView.showSoldierIcon();
                    soldierCount++;
                    nodeView.setSoldierCount(soldierCount);
                }
            }
    }
    
    /** removeFriendlyAnt removes an existing non-Bala Ant from friendlyAnts.
     */
    void removeFriendlyAnt(Ant ant) {
        if (!(ant instanceof Bala))
            if (friendlyAnts.contains(ant)) {
                friendlyAnts.remove(friendlyAnts.indexOf(ant));
                if (ant instanceof Forager) {
                    foragerCount--;
                    nodeView.setForagerCount(foragerCount);
                    if (foragerCount == 0)
                        nodeView.hideForagerIcon();
                } else if (ant instanceof Scout) {
                    scoutCount--;
                    nodeView.setScoutCount(scoutCount);
                    if (scoutCount == 0)
                        nodeView.hideScoutIcon();
                } else if (ant instanceof Soldier) {
                    soldierCount--;
                    nodeView.setSoldierCount(soldierCount);
                    if (soldierCount == 0)
                        nodeView.hideSoldierIcon();
                }
            }
    }
    
    /** getFriendlyAntCount returns the number of friendly Ants in
     *  this ColonyNode. Used by Balas for checking for ants to
     *  attack.
     *  @return The ColonyNode's friendlyAntCount attribute.
     */
    Integer getFriendlyAntCount() {
        return friendlyAnts.size();
    }
    
    /** getFriendlyAnt is used by Balas to fetch an ant from
     *  friendlyAnts to try and kill.
     *  @param index The index of the friendly Ant.
     *  @return A reference to the Ant.
     */
    Ant getFriendlyAnt(Integer index) {
        // If friendlyAnts is not empty...
        if (friendlyAnts.isEmpty())
            return null;
        else {
            Iterator iter = friendlyAnts.listIterator(index);
            Ant friendlyAnt;
            while (iter.hasNext()) {
                friendlyAnt = (Ant) iter.getCurrent();
                if (!friendlyAnt.isDead())
                    return friendlyAnt;
                else
                    iter.next();
            }
            // If friendlyAnt is still dead, check from the beginning.
            iter = friendlyAnts.iterator();
            Integer subIndex = 0; // Do not count past what was already checked.
            while (iter.hasNext() && subIndex < index) {
                friendlyAnt = (Ant) iter.getCurrent();
                if (!friendlyAnt.isDead())
                    return friendlyAnt;
                else
                    iter.next();
            }

            // If still no live friendlyAnt, return null.
            return null;
        }
    }
    
    /** addBala adds a new Bala Ant to enemyAnts.
     */
    void addBala(Bala bala) {
        if (!enemyAnts.contains(bala)) {
            enemyAnts.add(bala);
            Integer size = enemyAnts.size();
            nodeView.setBalaCount(size);
            if (size > 0)
                nodeView.showBalaIcon();
        }
    }
    
    /** removeBala removes an existing Bala ant from enemyAnts.
     */
    void removeBala(Bala bala) {
        if (enemyAnts.contains(bala)) {
            enemyAnts.remove(enemyAnts.indexOf(bala));
            Integer size = enemyAnts.size();
            nodeView.setBalaCount(size);
            if (size.equals(0))
                nodeView.hideBalaIcon();
        }
    }
    
    /** getBalaCount returns the number of Bala ants in this
     *  ColonyNode. Used by Soldiers to search for Bala ants
     *  to attack.
     *  @return This ColonyNode's enemyAntCount attribute.
     */
    Integer getBalaCount() {
        return enemyAnts.size();
    }
    
    /** getBala returns a Bala from enemyAnts.
     *  @param index The index of the Bala to return.
     *  @return The chosen Bala ant.
     */
    Bala getBala(Integer index) {
        // If enemyAnts is not empty...
        if (enemyAnts.isEmpty())
            return null;
        else {
            Iterator iter = enemyAnts.listIterator(index);
            Bala enemyAnt;
            while (iter.hasNext()) {
                enemyAnt = (Bala) iter.getCurrent();
                if (!enemyAnt.isDead())
                    return enemyAnt;
                else
                    iter.next();
            }
            // If enemyAnt is still dead, check from the beginning.
            iter = enemyAnts.iterator();
            Integer subIndex = 0; // Do not count past what was already checked.
            while (iter.hasNext() && subIndex < index) {
                enemyAnt = (Bala) iter.getCurrent();
                if (!enemyAnt.isDead())
                    return enemyAnt;
                else
                    iter.next();
            }

            // If still no live enemyAnts, return null.
            return null;
        }
    }
    
    /** setQueenPresent sets whether the Queen ant is present in this node.
     *  @param present Whether the queen is present in this node.
     */
    void setQueenPresent(Boolean present) {
        queenPresent = present;
    }
    
    /** isQueenPresent returns whether the Queen is in this ColonyNode.
     *  @return Whether the Queen ant is in this node.
     */
    Boolean isQueenPresent() {
        return queenPresent;
    }
    
    /** addFood adds a quantity of food to this ColonyNode
     *  (happens when Foragers drop food voluntarily or when they die).
     *  @param amt The amount of food to add to the node.
     *  @return The new total amount of food in the node.
     */
    void addFood(Integer amt) {
        // Add amt to foodAvailable and return new amount.
        // If an overflow would occur from adding, set to max.
        if (foodAvailable + amt < 0)
            foodAvailable = Integer.MAX_VALUE;
        else
            foodAvailable += amt;
        nodeView.setFoodAmount(foodAvailable);
    }
    
    /** takeFood reduces the foodAmount in this node by the
     *  specified quantity in the parameter amt.
     *  @param amt The amount of food to remove from the node.
     *  @return Whether food was successfully taken from node.
     */
    Boolean takeFood(Integer amt) {
        // Subtract amt from foodAvailable and return new amount.
        // If foodAvailable would fall below zero, set it to zero.
        if (foodAvailable > 0) {
            if (foodAvailable - amt < 0)
                foodAvailable = 0;
            else
                foodAvailable -= amt;
            nodeView.setFoodAmount(foodAvailable);
            return true;
        } else
            return false;
    }
    
    /** getFoodAvailable returns the foodAvailable attribute.
     *  @return The foodAvailable attribute.
     */
    Integer getFoodAvailable() {
        return foodAvailable;
    }
    
    /** addPheromone adds the specified amount of pheromone to
     *  this node's pheromone level.
     *  @param amt The amount of pheromone being dropped by a Forager ant.
     */
    void addPheromone(Integer amt) {
        pheromoneLevel += amt;
        nodeView.setPheromoneLevel(pheromoneLevel);
    }
    
    /** reducePheromone subtracts the specified amount of pheromone in
     *  the amt parameter from this node's pheromone level.
     *  @param amt The amount of pheromone to remove.
     *  @return The new pheromone level.
     */
    void reducePheromone(Integer amt) {
        // If removal would cause pheromoneLevel to fall below zero,
        // set it to zero.
        if (pheromoneLevel - amt < 0)
            pheromoneLevel = 0;
        else
            pheromoneLevel -= amt;
        nodeView.setPheromoneLevel(pheromoneLevel);
    }
    
    /** getPheromone returns the value of pheromone currently in this ColonyNode.
     *  @return The pheromoneLevel attribute.
     */
    Integer getPheromone() {
        return pheromoneLevel;
    }
    
    /** isRevealed returns whether this node is visible.
     *  @return Whether the node is visible.
     */
    Boolean isRevealed() {
        return nodeView.isVisible();
    }
    
    /** revealNode makes this node visible in the GUI.
     */
    void revealNode() {
        nodeView.showNode();
    }
    
    /** hideNode makes this node invisible in the GUI.
     */
    void hideNode() {
        nodeView.hideNode();
    }
    
    /** resetNode sets this node and its UI component to zero values and
     *  removes all Ant references.
     */
    void resetNode() {
        // First clear out the friendly and enemy ants.
        while (!friendlyAnts.isEmpty())
            friendlyAnts.remove(0);
        while (!enemyAnts.isEmpty())
            enemyAnts.remove(0);
        
        // Now set all counts to zero in this and NodeView.
        takeFood(foodAvailable);
        nodeView.setFoodAmount(foodAvailable);
        
        reducePheromone(pheromoneLevel);
        nodeView.setPheromoneLevel(pheromoneLevel);
        
        setQueenPresent(false);
        
        
        nodeView.setBalaCount(0);
        nodeView.hideBalaIcon();
        
        
        foragerCount = 0;
        nodeView.setForagerCount(foragerCount);
        nodeView.hideForagerIcon();
        
        scoutCount = 0;
        nodeView.setScoutCount(scoutCount);
        nodeView.hideScoutIcon();
        
        soldierCount = 0;
        nodeView.setSoldierCount(soldierCount);
        nodeView.hideSoldierIcon();
        

        // Finally, hide the node.
        hideNode();
    }
}