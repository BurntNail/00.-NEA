package classes.CustomActionListeners;

import classes.CustomActionListeners.BooleanChangeEvent;

import java.util.EventListener;

public interface BooleanChangeListener extends EventListener {
    public void stateChanged (BooleanChangeEvent event);
}