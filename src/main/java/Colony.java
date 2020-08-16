import dataStructures.ArrayList;
import dataStructures.Iterator;
import dataStructures.LinkedList;
import dataStructures.LinkedQueue;
import java.util.Random;

/**
 * The class Colony is used to manage the ant colony.
 * @author Ryan McAllister-Grum
 */
final public class Colony {
    // queen holds a reference to the colony's queen for ease of checking her state.
    private Queen queen;
    // queenDead is for the Simulation to easily check whether the queen is dead.
    public Boolean isQueenDead;
    // foragers is a LinkedList of the colony's forager ants.
    private final LinkedList foragers;
    // scouts is a LinkedList of the colony's scout ants.
    private final LinkedList scouts;
    // soldiers is a LinkedList of the colony's soldier ants.
    private final LinkedList soldiers;
    // balas is a LinkedList of the enemy bala ants in the colony.
    private final LinkedList balas;
    // ColonyNodes holds references to each node of the grid that makes up the colony.
    private final ArrayList colonyNodes;
    // rng is used to provide random numbers for the simulation.
    private final Random rng;
    // currentMaxId holds the most recent ID used when creating new ants.
    private Integer currentMaxId;
    // turnCounter is used to keep track of the current turn for Ant aging.
    private Integer turnCounter;
    // colonyWidth holds the width of this Colony.
    private final Integer colonyWidth;
    // colonyHeight holds the height of this Colony.
    private final Integer colonyHeight;
    // View is used to reference the ColonyView used in AntSimGUI.
    private final ColonyView view;
    // deadAnts holds a queue of dead ants to process at the end of each turn.
    // Done at the end so that Soldiers and Balas can de-reference their current
    // target if target is dead.
    private final LinkedQueue deadAnts;
    // TURNS_TO_DAYS holds the conversion amount for how many turns are in a day.
    static final Integer TURNS_TO_DAYS = 10;
    // TURNS_TO_YEAR holds the conversion amount for how many days are in a year.
    static final Integer TURNS_TO_YEAR = 365 * TURNS_TO_DAYS;
    // Sim is used to reference the Simulation, primarily for when the Queen dies.
    private static Simulation SIM;
    
    
    /** Colony constructor that specifies the colony grid size, turnCounter,
     *  and Simulation.
     *  @param width The new Colony width.
     *  @param height The new Colony height.
     *  @param newSim The Simulator to reference for when the Queen dies.
     */
    public Colony(Integer width, Integer height, Simulation newSim) {
        // Initialize colony.
        // Save the defined width and height.
        colonyWidth = width;
        colonyHeight = height;
        turnCounter = 0;

        // Set SIM to newSim.
        SIM = newSim;

        // Set Ant's COLONY to this.
        Ant.setColony(this);

        // Create a new Random object.
        rng = new Random();

        // Initialize foragers, scouts, soldiers, balas, colonyNodes, deadAnts.
        foragers = new LinkedList();
        scouts = new LinkedList();
        soldiers = new LinkedList();
        balas = new LinkedList();
        colonyNodes = new ArrayList();
        deadAnts = new LinkedQueue();

        // Initialize ColonyView.
        view = new ColonyView(width, height);

        // Then populate colonyNodes and view with the nodes.
        ColonyNode node;
        for (Integer i = 0; i < width; i++)
            for (Integer j = 0; j < height; j++) {
                // Queen and colony entrance go in the center of grid.
                if (i == width / 2 && j == height / 2)
                    node = new ColonyNode(i, j, 1000, true, 0, 0, 0, 0);
                else
                    node = new ColonyNode(i, j, 0, false, 0, 0, 0, 0);
                colonyNodes.add(node);
                view.addColonyNodeView(node.getNodeView(), i, j);
            }

        // Initialize currentMaxId.
        currentMaxId = 0;
    }
    
    /** resetColonyView resets the ColonyNodes to the default visibility
     *  (center 9 nodes visible).
     */
    private void resetColonyView() {
        // First hide all nodes.
        for (Integer i = 0; i < colonyWidth; i++)
            for (Integer j = 0; j < colonyHeight; j++)
                ((ColonyNode)(colonyNodes.get(i * colonyWidth + j))).hideNode();
        
        // Next, reveal the center 9 nodes.
        for (Integer x = (colonyWidth / 2) - 1; x < (colonyWidth / 2) + 2; x++)
            for (Integer y = (colonyHeight / 2) - 1; y < (colonyHeight / 2) + 2; y++)
                getColonyNode(x, y).revealNode();
    }
        
    /** QueenTest sets up the colony to only have a Queen.
     */
    public void queenTest() {
        // Remove all ants and reset Colony.
        destroy();
        
        // Reset the ColonyView to the default nine squares.
        resetColonyView();

        // Create a new Queen ant.
        queen = new Queen(getNextId(), TURNS_TO_YEAR * 20, colonyWidth / 2, colonyHeight / 2);
        addAnt(queen);
        isQueenDead = false;
        queen.getNode().addFood(1000);
        
        // Spawn food for the eventual Foragers to collect.
        spawnFood();
    }

    /** ScoutTest tests the functionality of Scouts.
     *  The colony will be setup with only the Queen and Scouts.
     */
    public void scoutTest() {
        // Remove all ants and reset Colony.
        destroy();
        
        // Reset the ColonyView to the default nine squares.
        resetColonyView();

        // Create a new Queen ant.
        queen = new Queen(getNextId(), TURNS_TO_YEAR * 20, colonyWidth / 2, colonyHeight / 2);
        addAnt(queen);
        isQueenDead = false;
        queen.getNode().addFood(1000);

        // Create 10 new Scout ants.
        for (Integer i = 0; i < 10; i++)
            addAnt(new Scout(getNextId(), TURNS_TO_YEAR, queen.getLocationX(), queen.getLocationY()));
    }

    /** foragerTest tests the functionality of Forager ants.
     */
    public void foragerTest() {
        // Remove all ants and reset Colony.
        destroy();

        // Create a new Queen ant.
        queen = new Queen(getNextId(), TURNS_TO_YEAR * 20, colonyWidth / 2, colonyHeight / 2);
        addAnt(queen);
        isQueenDead = false;
        queen.getNode().addFood(1000);
        
        // Add food to Colony.
        spawnFood();

        // Create 100 new Forager ants.
        for (Integer i = 0; i < 100; i++)
            addAnt(new Forager(getNextId(), TURNS_TO_YEAR, queen.getLocationX(), queen.getLocationY()));
        
        // Reveal the whole colony for Foragers to move around.
        for (Integer i = 0; i < colonyWidth; i++)
            for (Integer j = 0; j < colonyHeight; j++)
                getColonyNode(i, j).hideNode();
        resetColonyView();
        
        // Reveal corner nodes to test movement logic.
        getColonyNode(11, 11).revealNode();
        getColonyNode(15, 11).revealNode();
        getColonyNode(11, 15).revealNode();
        getColonyNode(15, 15).revealNode();
    }
    
    /** soldierTest tests the functionality of Soldier ants.
     */
    public void soldierTest() {
        // Remove all ants and reset Colony.
        destroy();
        
        // Reset the ColonyView to the default nine squares.
        resetColonyView();

        // Create a new Queen ant.
        queen = new Queen(getNextId(), TURNS_TO_YEAR * 20, colonyWidth / 2, colonyHeight / 2);
        addAnt(queen);
        isQueenDead = false;
        queen.getNode().addFood(1000);

        // Create 20 new Soldier ants.
        for (Integer i = 0; i < 20; i++)
            addAnt(new Soldier(getNextId(), TURNS_TO_YEAR, queen.getLocationX(), queen.getLocationY()));
        
        // Create 20 new Bala ants.
        for (Integer i = 0; i < 20; i++)
            addAnt(new Bala(getNextId(), TURNS_TO_YEAR, 12, 14));
    }
    
    /** addDead adds an Ant to the deadAnts queue.
     *  @param ant The Ant to queue up as dead.
     */
    void addDead(Ant ant) {
        // If Queen, end Simulation.
        if (ant instanceof Queen) {
            isQueenDead = true;
            queen.getNode().removeFriendlyAnt(ant);
            SIM.getGui().setTime("Queen is dead, simulation over!");
            SIM.stop();
        } else
            deadAnts.enqueue(ant);
    }
    
    /** processDead handles removing all dead ants from all lists and ColonyNodes.
     */
    private void processDead() {
        while(!deadAnts.isEmpty()) {
            Ant ant = (Ant) deadAnts.dequeue();
            if (ant instanceof Forager) {
                foragers.remove(ant);
                ant.getNode().removeFriendlyAnt(ant);
            } else if (ant instanceof Scout) {
                scouts.remove(ant);
                ant.getNode().removeFriendlyAnt(ant);
            } else if (ant instanceof Soldier) {
                soldiers.remove(ant);
                ant.getNode().removeFriendlyAnt(ant);
            } else if (ant instanceof Bala) {
                balas.remove(ant);
                ant.getNode().removeBala((Bala)ant);
            }
        }
    }
    
    /** processTurn processes a turn in the Colony for all Ants.
     *  All friendly ants take their turn before Balas.
     */
    public void processTurn() {
        // Set GUI heading to the current day turn count.
        SIM.getGui().setTime("Day " + ((turnCounter / 10) + 1) + ", turn " + ((turnCounter % 10) + 1));
        
        // If it is the first turn of a day, reduce pheromone
        // levels by half.
        if (turnCounter % TURNS_TO_DAYS == 0 && turnCounter > 0)
            for (Integer i = 0; i < getColonyWidth(); i++)
                for (Integer j = 0; j < getColonyHeight(); j++)
                    getColonyNode(i, j).reducePheromone(
                            getColonyNode(i, j).getPheromone().equals(1) ? 1 :
                                    getColonyNode(i, j).getPheromone() / 2
                    );
        
                
        // First process turns for all friendly Ants.
        queen.takeAction(rng);
        
        // Next, Scouts.
        if (!scouts.isEmpty()) {
            Iterator iter = scouts.iterator();
            while (iter.hasNext()) {
                Scout scout = (Scout) iter.getCurrent();
                scout.takeAction(rng);
                iter.next();
            }
        }
        
        // Then Foragers.
        if (!foragers.isEmpty()) {
            Iterator iter = foragers.iterator();
            while (iter.hasNext()) {
                Forager forager = (Forager) iter.getCurrent();
                forager.takeAction(rng);
                iter.next();
            }
        }
        
        // Then Soldiers.
        if (!soldiers.isEmpty()) {
            Iterator iter = soldiers.iterator();
            while (iter.hasNext()) {
                Soldier soldier = (Soldier) iter.getCurrent();
                soldier.takeAction(rng);
                iter.next();
            }
        }
        
        // Then process turns for Bala ants.
        if (!balas.isEmpty()) {
            Iterator iter = balas.iterator();
            while (iter.hasNext()) {
                Bala bala = (Bala) iter.getCurrent();
                bala.takeAction(rng);
                iter.next();
            }
        }
        
        // Process any dead Ants.
        processDead();
        
        // Each turn, there is a 3% chance to spawn a Bala at the edge
        // of the Colony.
        if (rng.nextInt(100) < 3)
            createBala(rng);
        
        // Increment turn counter.
        turnCounter++;
    }
    
    /** Reset resets the Colony to the default state.
     */
    public void reset() {
        // First, clear the Colony.
        destroy();
        
        // Then set turnCounter and currentMaxId to zero.
        turnCounter = 0;
        currentMaxId = 0;
        
        // Setup the initial state of ant colony in the center:
        // 1 Queen
        // 10 Soldiers
        // 50 Foragers
        // 4 Scouts
        // First create the queen (lives for 20 years).
        queen = new Queen(getNextId(), TURNS_TO_YEAR * 20, colonyWidth / 2, colonyHeight / 2);
        addAnt(queen);
        isQueenDead = false;
        queen.getNode().addFood(1000);
        
        // Then the Soldiers (all other ants live 1 year).
        for (Integer i = 0; i < 10; i++)
            addAnt(new Soldier(getNextId(), TURNS_TO_YEAR, queen.getLocationX(), queen.getLocationY()));
        
        // Then the Foragers.
        for (Integer i = 0; i < 50; i++)
            addAnt(new Forager(getNextId(), TURNS_TO_YEAR, queen.getLocationX(), queen.getLocationY()));

        // Finally, the Scouts.
        for (Integer i = 0; i < 4; i++)
            addAnt(new Scout(getNextId(), TURNS_TO_YEAR, queen.getLocationX(), queen.getLocationY()));

        // Next, randomly disburse up to 1000 food to ColonyNodes (10% chance).
        spawnFood();

        // Finally, only reveal the center 9 nodes.
        resetColonyView();
    }
    
    /** spawnFood randomly disburse between 500 to 1000 food to ColonyNodes (25% chance).
     *  Ignores the node where the Queen resides.
     */
    private void spawnFood() {
        // Loop through colonyNodes and add food.
        for (Integer i = 0; i < colonyWidth; i++)
            for (Integer j = 0; j < colonyHeight; j++)
                if (rng.nextInt(100) < 25 && !i.equals(queen.getLocationX()) && !j.equals(queen.getLocationY()))
                    getColonyNode(i, j).addFood(rng.nextInt(501) + 500);
    }
    
    /** getNextId gets the next currentMaxId for an Ant and then increments it.
     *  @return The current currentMaxId.
     */
    Integer getNextId() {
        return currentMaxId++;
    }
    
    /** Destroy flags the colony as destroyed for the purposes of the main
     *  simulation loop, removes all references to all Ants, and zeroes
     *  all ColonyNodes.
     */
    public void destroy() {
        // First, kill the queen.
        // Queen can be null on initial setup.
        if (queen != null) {
            queen.kill();
            queen.getNode().removeFriendlyAnt(queen);
            queen = null;
        }
        
        // Then the rest of the ants.
        if (!foragers.isEmpty()) {
            Iterator iter = foragers.iterator();
            while (iter.hasNext()) {
                Forager forager = (Forager) iter.getCurrent();
                addDead(forager);
                iter.next();
            }
        }
        if (!scouts.isEmpty()) {
            Iterator iter = scouts.iterator();
            while (iter.hasNext()) {
                Scout scout = (Scout) iter.getCurrent();
                addDead(scout);
                iter.next();
            }
        }
        if (!soldiers.isEmpty()) {
            Iterator iter = soldiers.iterator();
            while (iter.hasNext()) {
                Soldier soldier = (Soldier) iter.getCurrent();
                addDead(soldier);
                iter.next();
            }
        }
        if (!balas.isEmpty()) {
            Iterator iter = balas.iterator();
            while (iter.hasNext()) {
                Bala bala = (Bala) iter.getCurrent();
                addDead(bala);
                iter.next();
            }
        }
        
        // Process all the dead ants.
        processDead();
        
        // Next, zero all ColonyNodes.
        for (Integer i = 0; i < colonyWidth; i++)
            for (Integer j = 0; j < colonyHeight; j++) {
                getColonyNode(i, j).reducePheromone(getColonyNode(i, j).getPheromone());
                getColonyNode(i, j).takeFood(getColonyNode(i, j).getFoodAvailable());
            }
        
        // Clear the GUI's message.
        SIM.getGui().setTime("");
        
        // Finally, stop the Simulation.
        SIM.stop();
    }
    
    /** addAnt adds an Ant to their specified ColonyNode and appropriate
     *  LinkedList of Ant type (Scout, Forager, Soldier, Bala).
     *  @param ant The Ant to add.
     */
    void addAnt(Ant ant) {
        // Depending on Ant's type, add it to the appropriate LinkedList group,
        // as well as its designated ColonyNode, based on its x and y location.
        if (ant instanceof Queen)
            ant.getNode().addFriendlyAnt(ant);
        else if (ant instanceof Forager) {
            foragers.add(ant);
            ant.getNode().addFriendlyAnt(ant);
        } else if (ant instanceof Scout) {
            scouts.add(ant);
            ant.getNode().addFriendlyAnt(ant);
        } else if (ant instanceof Soldier) {
            soldiers.add(ant);
            ant.getNode().addFriendlyAnt(ant);
        } else if (ant instanceof Bala) {
            balas.add(ant);
            ant.getNode().addBala((Bala)ant);
        }
    }
    
    /** createBala spawns a new Bala at the edge of the Colony.
     *  @param rng Used to pick a random location along the edge.
     */
    void createBala(Random rng) {
        // First determine where the Bala will spawn.
        Integer locationX = rng.nextInt(27);
        Integer locationY;
        // If somewhere between top and bottom, then
        // locationY has to be top or bottom.
        if (locationX > 0 && locationX < 27)
            locationY = rng.nextInt(2) == 1 ? 26 : 0;
        else // locationY can be any number between 0 and 27.
            locationY = rng.nextInt(27);
        
        Bala bala = new Bala(getNextId(), TURNS_TO_YEAR, locationX, locationY);
        addAnt(bala);
    }
    
    /** getNodeView returns the ColonyNodeView from the specified ColonyNode.
     *  @param index The ColonyNode to fetch the ColonyNodeView.
     *  @return the ColonyNode's ColonyNodeView.
     */
    ColonyNodeView getNodeView(Integer index) {
        return ((ColonyNode)colonyNodes.get(index)).getNodeView();
    }
    
    /** getColonyView returns this Colony's ColonyView reference.
     *  @return The Colony's ColonyView attribute.
     */
    public ColonyView getColonyView() {
        return view;
    }
    
    /** getColonyNode returns the specified x,y ColonyNode.
     *  @param locationX The x-coordinate for the ColonyNode.
     *  @param locationY The y-coordinate for the ColonyNode.
     *  @return The specified ColonyNode.
     */
    ColonyNode getColonyNode(Integer locationX, Integer locationY) {
        // In a grid, position is calculated x * width + y.
        return ((ColonyNode)colonyNodes.get(locationX * colonyWidth + locationY));
    }
    
    /** getTurnCounter returns the Colony's turnCounter attribute.
     *  @return The Colony's turnCounter attribute.
     */
    Integer getTurnCounter() {
        return turnCounter;
    }
    
    /** getColonWidth returns the Colony's colonyWidth attribute.
     *  @return The Colony's colonyWidth attribute.
     */
    Integer getColonyWidth() {
        return colonyWidth;
    }
    
    /** getColonyHeight returns the Colony's colonyHeight attribute.
     *  @return The Colony's colonyHeight attribute.
     */
    Integer getColonyHeight() {
        return colonyHeight;
    }
}