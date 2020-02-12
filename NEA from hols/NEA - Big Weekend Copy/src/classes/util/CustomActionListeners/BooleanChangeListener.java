package classes.util.CustomActionListeners;

import java.util.EventListener;

public interface BooleanChangeListener extends EventListener {
    public void stateChanged (BooleanChangeEvent event);
}