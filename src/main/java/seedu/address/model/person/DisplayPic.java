package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.ui.CommandBox.DEFAULT_DISPLAY_PIC;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Person's display picture path in a addressbook
 */
public class DisplayPic {

    public static final String MESSAGE_DISPLAYPIC_CONSTRAINT = "When choosing the choice or display picture, ensure that you enter either dp/Y or dp/N";
    public final String displayPicPath;

    public DisplayPic(String displayPicPath) throws IllegalValueException {
        requireNonNull(displayPicPath);
        String trimmedDisplayPicPath = displayPicPath.trim();
        if (!isValidPath(trimmedDisplayPicPath)) {
            throw new IllegalValueException(MESSAGE_DISPLAYPIC_CONSTRAINT);
        }
        this.displayPicPath = trimmedDisplayPicPath;
    }

    public static boolean isValidPath(String displayPicPath) {
        File picture = new File(displayPicPath);
        return displayPicPath.equals(DEFAULT_DISPLAY_PIC) || picture.exists();
    }

    @Override
    public String toString() {
        return this.displayPicPath;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DisplayPic // instanceof handles nulls
                && this.displayPicPath.equals(((DisplayPic) other).displayPicPath)); // state check
    }

    @Override
    public int hashCode() {
        return displayPicPath.hashCode();
    }
}
