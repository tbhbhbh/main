package seedu.address.ui;

import static seedu.address.testutil.TypicalTags.getTypicalTags;
import static seedu.address.ui.testutil.GuiTestAssert.assertBoxDisplayTag;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.TagBoxHandle;
import guitests.guihandles.TagListPanelHandle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.tag.Tag;

public class TagListPanelTest extends GuiUnitTest {
    private static final ObservableList<Tag> TYPICAL_TAGS = FXCollections.observableList(getTypicalTags());
    private TagListPanelHandle tagListPanelHandle;

    @Before
    public void setUp() {
        TagListPanel tagListPanel = new TagListPanel(TYPICAL_TAGS);
        uiPartRule.setUiPart(tagListPanel);

        tagListPanelHandle = new TagListPanelHandle(getChildNode(tagListPanel.getRoot(),
                TagListPanelHandle.TAG_LIST_VIEW_ID));
    }

    @Test
    public void display() {
        for (int i = 0; i < TYPICAL_TAGS.size(); i++) {
            tagListPanelHandle.navigateToTag(TYPICAL_TAGS.get(i));
            Tag expectedTag = TYPICAL_TAGS.get(i);
            TagBoxHandle actualTagBox = tagListPanelHandle.getTagBoxHandle(i);

            assertBoxDisplayTag(expectedTag, actualTagBox);
        }
    }
}
