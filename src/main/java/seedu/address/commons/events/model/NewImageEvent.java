package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;

//@@author JunQuann
public class NewImageEvent extends BaseEvent {

    public final String imageName;
    public final String currentImagePath;
    public String imagePath;

    public NewImageEvent(String imageName, String currentImagePath) {
        this.imageName = imageName;
        this.currentImagePath = currentImagePath;
    }

    @Override
    public String toString() {
        return "Image has been copied to the designated folder.";
    }
}
