package gov.nist.toolkit.xdstools2.client.event;

import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;
import gov.nist.toolkit.xdstools2.client.event.EnvironmentChangedEvent.EnvironmentChangedEventHandler;

/**
 * Created by onh2 on 8/30/16.
 */
public class Xdstools2EventBus extends SimpleEventBus {
    /**
     * Method that handle actions that must be triggered when the environment changes.
     * @param handler
     * @return
     */
    public HandlerRegistration addEnvironmentChangedEventHandler(EnvironmentChangedEventHandler handler) {
        return addHandler(EnvironmentChangedEvent.TYPE, handler);
    }

    /**
     * Method that signals though the event bus to the application that the environment has changed.
     */
    public void fireEnvironmentChangedEvent(String selectedEnvironment) {
        fireEvent(new EnvironmentChangedEvent(selectedEnvironment));
    }

    /**
     * Notify the other tabs though the eventbus that the simulators have been updated.
     */
    public void fireSimulatorsUpdatedEvent() {
        fireEvent(new SimulatorUpdatedEvent());
    }

    /**
     * Enable to know when the event bus is notified that the simulators have been updated and the handler itself
     * tells what to do after getting notified.
     * @param handler
     * @return
     */
    public HandlerRegistration addSimulatorsUpdatedEventHandler(SimulatorUpdatedEvent.SimulatorUpdatedEventHandler handler){
        return addHandler(SimulatorUpdatedEvent.TYPE,handler);
    }

    /**
     * Notify the event bus that the actors config has changed.
     */
    public void fireActorsConfigUpdatedEvent() {
        fireEvent(new ActorConfigUpdatedEvent());
    }

    /**
     * Enable to know when the event bus is notified that the actors config has changed and the handler itself
     * tells what to do after getting notified.
     * @param handler
     * @return
     */
    public HandlerRegistration addActorsConfigUpdatedEventHandler(ActorConfigUpdatedEvent.ActorConfigUpdatedEventHandler handler){
        return addHandler(ActorConfigUpdatedEvent.TYPE,handler);
    }
}