//@@author conantteo
package seedu.address.ui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalTags.CLASSMATES;
import static seedu.address.testutil.TypicalTags.FACEBOOK;
import static seedu.address.testutil.TypicalTags.FRIENDS;
import static seedu.address.testutil.TypicalTags.GOOGLE;
import static seedu.address.testutil.TypicalTags.OWE_MONEY;
import static seedu.address.ui.testutil.GuiTestAssert.assertBoxDisplayTag;

import org.junit.Test;

import guitests.guihandles.TagBoxHandle;
import seedu.address.model.tag.Tag;

public class TagBoxTest extends GuiUnitTest {

    private Tag testTag;
    private TagBox testTagBox;

    @Test
    public void display() {
        // Label with a tag
        testTag = GOOGLE;
        testTagBox = new TagBox(testTag);
        uiPartRule.setUiPart(testTagBox);

        assertTagBoxDisplay(testTagBox, testTag);

        // Changes label to reflect another new tag
        Tag anotherTag = FACEBOOK;
        testTagBox = new TagBox(anotherTag);
        uiPartRule.setUiPart(testTagBox);

        assertTagBoxDisplay(testTagBox, anotherTag);
    }

    @Test
    public void mouseEventHandler_tagBoxIsClicked() {
        testTag = OWE_MONEY;
        testTagBox = new TagBox(testTag);
        uiPartRule.setUiPart(testTagBox);

        TagBoxHandle tagBoxHandle = new TagBoxHandle(testTagBox.getRoot());
        tagBoxHandle.click();

        guiRobot.waitForEvent(tagBoxHandle::isClicked);

        assertTrue(tagBoxHandle.isClicked());
    }

    @Test
    public void equals() {
        testTag = FRIENDS;
        TagBox tagBox = new TagBox(testTag);

        // same object -> returns true
        assertTrue(tagBox.equals(tagBox));

        // null -> returns false
        assertFalse(tagBox.equals(null));

        // different types -> returns false
        Tag differentTag = CLASSMATES;
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
