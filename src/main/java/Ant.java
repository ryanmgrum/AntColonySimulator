/**
 * Ant is the overarching superclass for the different ant types
 * (Queen, Forager, Scout, Soldier, Bala).
 * @author Ryan McAllister-Grum
 */
public class Ant implements Comparable {
    // ID is the unique id for each Ant.
    private final Integer id;
    // turnAge keeps track of how old an ant is in turns.
    private Integer turnAge;
    // maxTurnAge maintains how old an ant can be until they die
    // (Queen 20 years, rest of the ants one year).
    private final Integer maxTurnAge;
    // locationX tracks the ant's x-coordinate position in the colony.
    private Integer locationX;
    // locationY tracks the ant's y-coordinate position in the colony.
    private Integer locationY;
    // Dead flags whether the ant is dead; used for skipping their turn
    // and eventual cleanup.
    private Boolean dead;
    // Colony is a static reference to the ant colony for all Ant's use.
    protected static Colony COLONY;
    // ColonyNode is a convenience attribute to the Ant's current ColonyNode location.
    private ColonyNode node;
    
    
    /** Constructor that creates a new Ant at the given x,y location with
     * with the given max turn age and id.
     * @param id The ant's new unique ID.
     * @param maxTurnAge The ant's maximum turn age before they die of old age.
     * @param newLocationX The ant's initial x-coordinate location in the colony.
     * @param newLocationY The ant's initial y-coordinate location in the colony.
     * @return The new Ant object.
     */
    Ant(Integer id, Integer maxTurnAge, Integer newLocationX, Integer newLocationY) {
        // Create new Ant.
        this.id = id;
        locationX = newLocationX;
        locationY = newLocationY;
        turnAge = 0;
        this.maxTurnAge = maxTurnAge;
        dead = false;
        node = COLONY.getColonyNode(locationX, locationY);
    }
    
    /** Kill destroys an Ant by flagging it as dead and adding it to the
     *  deadAnts Colony queue for eventual deletion.
     */
    void kill() {
        dead = true;
        COLONY.addDead(this);
    }
    
    /** Move relocates an Ant to a new ColonyNode based on its xy-coordinates.
     * @param newLocationX The X-coordinate of the new ColonyNode.
     * @param newLocationY The Y-coordinate of the new ColonyNode.
     */
    protected void move(Integer newLocationX, Integer newLocationY) {
        // Depending on the Ant type, remove it from the appropriate
        // enemy or friendly Ant collection in the ColonyNode.
        if (this instanceof Bala)
            getNode().removeBala((Bala) this);
        else
            getNode().removeFriendlyAnt(this);
        
        // Set the new coordinates for the ant.
        locationX = newLocationX;
        locationY = newLocationY;
        
        // Fetch the new ColonyNode for ease of reference.
        setNode();
        
        
        // Depending on the Ant type, add it to the appropriate
        // enemy or friendly Ant collection in the ColonyNode.
        if (this instanceof Bala)
            getNode().addBala((Bala) this);
        else
            getNode().addFriendlyAnt(this);
    }
    
    
    /** getId returns the Ant's ID.
     * @return The ant's id attribute.
     */
    Integer getId() {
        return id;
    }
    
    /** getLocationX returns the Ant's x-coordinate location.
     * @return The ant's locationX attribute.
     */
    Integer getLocationX() {
        return locationX;
    }
    
    /** getLocationY returns the Ant's y-coordinate location.
     * @return The ant's locationY attribute.
     */
    Integer getLocationY() {
        return locationY;
    }
    
    /** getTurnAge returns the Ant's current turn age.
     * @return The Ant's turnAge attribute.
     */
    Integer getTurnAge() {
        return turnAge;
    }
    
    /** incrementTurnAge ages the Ant by one turn; if the Ant is too old, it
     *  dies instead.
     */
    void incrementTurnAge() {
        turnAge++;
        if (turnAge >= maxTurnAge)
            kill();
    }
    
    /** getMaxTurnAge returns the Ant's maximum age in turns.
     * @return The Ant's maxTurnAge attribute.
     */
    Integer getMaxTurnAge() {
        return maxTurnAge;
    }
    
    /** isDead returns whether the Ant is currently dead.
     * @return The Ant's dead attribute.
     */
    Boolean isDead() {
        return dead;
    }
    
    /** getColony returns the attribute Colony for internal (to the Ant's)
     *  access to its methods.
     *  @return The COLONY attribute.
     */
    protected Colony getColony() {
        return COLONY;
    }
    
    /** setColony sets the static attribute Colony so that each Ant can
     *  reference the overarching Colony's methods.
     *  @param colony The new Colony.
     */
    static protected void setColony(Colony colony) {
        COLONY = colony;
    }
    
    /** getNode returns this Ant's ColonyNode.
     *  @return The Ant's node attribute.
     */
    protected ColonyNode getNode() {
        return node;
    }
    
    /** setNode sets this Ant's ColonyNode.
     */
    private void setNode() {
        node = COLONY.getColonyNode(locationX, locationY);
    }
    
    
    /** compareTo compares the Ants' IDs.
     * @param obj The other Ant to compare.
     * @return Whether this ID is less than, equal to, or greater than, obj's ID.
     */
    @Override
    public int compareTo(Object obj) {
        Ant otherAnt = (Ant) obj;
        if (this.getId() < otherAnt.getId())
            return -1;
        else if (this.equals(otherAnt))
            return 0;
        else
            return 1;
    }
    
    /** Equals checks whether ant is the same as this Ant via their ID.
     * @param obj The ant to compare to this ant.
     * @return Whether the Ant's IDs are one in the same.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            throw new IllegalArgumentException(
                "Error while comparing Ants in equals: " +
                "obj parameter is null!"
            );
        else if (!(obj instanceof Ant))
            throw new IllegalArgumentException(
                "Error while comparing Ants in equals: " +
                "obj is not a type of Ant!"
            );
        return this.getId().equals(((Ant)obj).getId());
    }

    /** hashCode overridden from Object.
     * @return The Ant's id as the hash code since each ID is unique.
     */
    @Override
    public int hashCode() {
        return id;
    }
}