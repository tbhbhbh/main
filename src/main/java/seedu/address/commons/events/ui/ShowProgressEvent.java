package seedu.address.commons.events.ui;

import javafx.concurrent.Task;
import seedu.address.commons.events.BaseEvent;

/**
 * An event requesting to view the authentication page.
 */
public class ShowProgressEvent extends BaseEvent {

    private Task task;

    public ShowProgressEvent(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
