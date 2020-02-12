package classes.util.CustomActionListeners;

import java.util.EventObject;

public class BooleanChangeEvent extends EventObject {
    private final BooleanChangeDispatcher dispatcher;

    public BooleanChangeEvent (BooleanChangeDispatcher dispatcher) {
        super(dispatcher);
        this.dispatcher = dispatcher;

    }

}
