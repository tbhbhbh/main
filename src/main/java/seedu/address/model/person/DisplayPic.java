package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.ui.MainWindow.DEFAULT_DP;

import java.io.File;

import javax.activation.MimetypesFileTypeMap;

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
    public static final String MESSAGE_DISPLAYPIC_CONSTRAINTS = "Please ensure that you choose a valid image file";

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
     * Validates given display pic
     *
     * @throws IllegalValueException if given display pic path is invalid
     */
    private void initialiseDisplayPic(String trimmedDisplayPicName) throws IllegalValueException {
        fileChooserEvent = new FileChooserEvent();
        raise(fileChooserEvent);
        if (!isValidPicPath(fileChooserEvent.getFilePath())) {
            throw new IllegalValueException(MESSAGE_DISPLAYPIC_CONSTRAINTS);
        }
        this.currentDisplayPic = fileChooserEvent.getFilePath();
        newImageEvent = new NewImageEvent(trimmedDisplayPicName, currentDisplayPic);
        raise(newImageEvent);
        this.newDisplayPicPath = newImageEvent.getImagePath();
    }

    public String getNewDisplayPicPath() {
        return newDisplayPicPath;
    }

    /**
     * Returns if a given string is a valid picture path
     */
    public static boolean isValidPicPath(String displayPicPath) {
        File displayPicFile = new File(displayPicPath);

        MimetypesFileTypeMap mtftp = new MimetypesFileTypeMap();
        mtftp.addMimeTypes("image png jpg jpeg");

        String mimeType = mtftp.getContentType(displayPicFile).split("/")[0];
        boolean isPicture = mimeType.equals("image");

        return isPicture && displayPicFile.exists();
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
