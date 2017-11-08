package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;

/**
 */
public class FileChooserEvent extends BaseEvent {

    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "Select your image from the file chooser";
    }

}
