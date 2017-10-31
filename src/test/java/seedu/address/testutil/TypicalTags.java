//@@author conantteo
package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.tag.Tag;

/**
 * A utility class containing a list of {@code Tag} objects to be used in tests.
 */
public class TypicalTags {

    public static final Tag GOOGLE = new TagBuilder().withSpecifiedTagName("Google");

    public static final Tag FACEBOOK = new TagBuilder().withSpecifiedTagName("Facebook");

    public static final Tag CLASSMATES = new TagBuilder().withSpecifiedTagName("Classmates");

    public static final Tag FAMILY = new TagBuilder().withSpecifiedTagName("Family");

    private TypicalTags() {} // prevents instantiation

    public static List<Tag> getTypicalTags() {
        return new ArrayList<>(Arrays.asList(GOOGLE, FACEBOOK, CLASSMATES, FAMILY));
    }
}
