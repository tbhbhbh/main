//@@author conantteo
package seedu.address.ui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.ui.testutil.GuiTestAssert.assertBoxDisplayTag;

import org.junit.Test;

import guitests.guihandles.TagBoxHandle;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.TagBuilder;

public class TagBoxTest extends GuiUnitTest {

    @Test
    public void display() {
        // Label with a tag
        Tag testTag = new TagBuilder().withSpecifiedTagName("alpha");
        TagBox tagBox = new TagBox(testTag);
        uiPartRule.setUiPart(tagBox);

        assertTagBoxDisplay(tagBox, testTag);

        // Changes label to reflect another new tag
        Tag anotherTag = new TagBuilder().withSpecifiedTagName("beta");
        tagBox = new TagBox(anotherTag);
        uiPartRule.setUiPart(tagBox);

        assertTagBoxDisplay(tagBox, anotherTag);
    }

    @Test
    public void equals() {
        Tag tag = new TagBuilder().withSpecifiedTagName("test");
        TagBox tagBox = new TagBox(tag);

        // same object -> returns true
        assertTrue(tagBox.equals(tagBox));

        // null -> returns false
        assertFalse(tagBox.equals(null));

        // different types -> returns false
        Tag differentTag = new TagBuilder().withSpecifiedTagName("anotherTest");
        assertFalse(tagBox.equals(new TagBox(differentTag)));
    }

    /**
     * Asserts that {@code tagBox} displays the {@code tag} correctly.
     */
    private void assertTagBoxDisplay(TagBox tagBox, Tag tag) {
        guiRobot.pauseForHuman();

        TagBoxHandle tagBoxHandle = new TagBoxHandle(tagBox.getRoot());

        assertBoxDisplayTag(tag, tagBoxHandle);
    }
}
