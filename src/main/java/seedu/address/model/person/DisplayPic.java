package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Person's display picture path in a addressbook
 */
public class DisplayPic {

    public final String displayPicPath;

    public DisplayPic(String displayPicPath) throws IllegalValueException {
        requireNonNull(displayPicPath);
        String trimmedDisplayPicPath = displayPicPath.trim();
        this.displayPicPath = trimmedDisplayPicPath;
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
