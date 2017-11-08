package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.ui.MainWindow.DEFAULT_DP;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.BaseEvent;
import seedu.address.commons.events.model.FileChooserEvent;
import seedu.address.commons.events.model.NewImageEvent;
import seedu.address.commons.exceptions.IllegalValueException;

//@@author JunQuann
/**
 * Represents a Person's display picture path in a addressbook
 */
public class DisplayPic {

    private String newDisplayPicPath;
    private String currentDisplayPic;
    private EventsCenter eventsCenter = EventsCenter.getInstance();
    private FileChooserEvent fileChooserEvent;
    private NewImageEvent newImageEvent;

    public DisplayPic(String displayPicName, boolean isFromStorage) throws IllegalValueException {
        requireNonNull(displayPicName);
        String trimmedDisplayPicName = displayPicName.trim();
        if (trimmedDisplayPicName.equals(DEFAULT_DP) || isFromStorage) {
            this.newDisplayPicPath = trimmedDisplayPicName;
        } else {
            initialiseDisplayPic(trimmedDisplayPicName);
        }
    }

    /**
     * @param trimmedDisplayPicName
     */
    private void initialiseDisplayPic(String trimmedDisplayPicName) {
        fileChooserEvent = new FileChooserEvent();
        raise(fileChooserEvent);
        this.currentDisplayPic = fileChooserEvent.getFilePath();
        newImageEvent = new NewImageEvent(trimmedDisplayPicName, currentDisplayPic);
        raise(newImageEvent);
        this.newDisplayPicPath = newImageEvent.getImagePath();
    }

    public String getNewDisplayPicPath() {
        return newDisplayPicPath;
    }

    public void raise(BaseEvent event) {
        eventsCenter.post(event);
    }

    @Override
    public String toString() {
        return this.newDisplayPicPath;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DisplayPic // instanceof handles nulls
                && this.newDisplayPicPath.equals(((DisplayPic) other).newDisplayPicPath)); // state check
    }

    @Override
    public int hashCode() {
        return newDisplayPicPath.hashCode();
    }
}
