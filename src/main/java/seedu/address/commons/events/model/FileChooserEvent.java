package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;

public class FileChooserEvent extends BaseEvent {

    public String filePath;

    @Override
    public String toString() {
        return "Select your image from the file chooser";
    }

}
