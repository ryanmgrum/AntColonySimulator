import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

// Simulation is the overarching class that manages the simulation of the ant colony.
final public class Simulation implements ActionListener, SimulationEventListener {
    // Colony is a reference to the actual ant colony.
    private final Colony colony;
    // Step is a flag that specifies whether the simulation is stepping through
    // each turn.
    private Boolean step;
    // Stopped is a flag to determine whether the simulation is still running.
    private Boolean stopped;
    // SwingTimer is used to pace the simulation at the GUI level.
    private final Timer swingTimer;
    // GUI is a reference to the AntSimGUI UI.
    private final AntSimGUI gui;
    
    /** Default Simulation constructor.
     */
    public Simulation() {
        swingTimer = new Timer(125, this);
        step = false;
        stopped = true;
        colony = new Colony(27, 27, this);
        gui = new AntSimGUI();
        gui.initGUI(colony.getColonyView());
        gui.addSimulationEventListener(this);
    }
    
    /** Start begins the colony simulation.
     */
    public void start() {
        swingTimer.start();
    }
    
    /** Stop halts the simulation by setting stopped to true and stopping swingTimer.
     */
    public void stop() {
        stopped = true;
    }
    
    /** stopTimer stops the swingTimer.
     */
    public void stopTimer() {
        swingTimer.stop();
    }
    
    /** isSimStopped is used to check whether the simulation is still running.
     * @return Whether the simulation is still running.
     */
    public boolean isSimStopped() {
        return stopped;
    }
    
    
    /** Destructor for Simulation.
     */
    public void end() {
        stop();
        stopTimer();
        colony.reset();
    }
    
    /** Run executes the simulation.
     */
    public void run() {
        if (!isSimStopped())
            colony.processTurn();
    }
    
    /** Step is used to set the simulation to step through each turn.
     */
    public void step() {
        colony.processTurn();
    }
    
    /** Inherited from interface ActionListener.
     * @param e An ActionEvent to be processed.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (colony.isQueenDead)
            stop();
        else
            run();
    }

    /** Inherited from interface SimulationEventListener.
     * @param simEvent A SimulationEvent to be processed.
     */
    @Override
    public void simulationEventOccurred(SimulationEvent simEvent) {
        switch(simEvent.getEventType()) {
            case SimulationEvent.NORMAL_SETUP_EVENT:
                colony.reset();
                stopped = false;
                stopTimer();
                break;
            case SimulationEvent.RUN_EVENT:
                if (!isSimStopped()) {
                    if (!swingTimer.isRunning())
                        start();
                    step = false;
                    run();
                }
                break;
            case SimulationEvent.STEP_EVENT:
                if (!isSimStopped()) {
                    if (swingTimer.isRunning())
                        stopTimer();
                    step();
                }
                break;
            case SimulationEvent.QUEEN_TEST_EVENT:
                colony.queenTest();
                stopped = false;
                stopTimer();
                break;
            case SimulationEvent.FORAGER_TEST_EVENT:
                colony.foragerTest();
                stopped = false;
                stopTimer();
                break;
            case SimulationEvent.SCOUT_TEST_EVENT:
                colony.scoutTest();
                stopped = false;
                stopTimer();
                break;
            case SimulationEvent.SOLDIER_TEST_EVENT:
                colony.soldierTest();
                stopped = false;
                stopTimer();
        }
    }
    
    /** getGui returns the Simulation's AntSimGUI gui attribute.
     *  @return The Simulation's gui attribute.
     */
    AntSimGUI getGui() {
        return gui;
    }
}