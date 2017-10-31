//@@author conantteo
package guitests.guihandles;

import java.util.List;
import java.util.Optional;

import javafx.scene.control.ListView;
import seedu.address.model.tag.Tag;
import seedu.address.ui.TagBox;

/**
 * Provides a handle for {@code TagListPanel} containing a list of {@code TagBox}
 */
public class TagListPanelHandle extends NodeHandle<ListView<TagBox>> {
    public static final String TAG_LIST_VIEW_ID = "#tagListView";

    public TagListPanelHandle(ListView<TagBox> tagListPanelNode) {
        super(tagListPanelNode);
    }

    /**
     * Navigates the list view to display and select the particular tag {@param toTag}
     */
    public void navigateToTag(Tag toTag) {
        List<TagBox> tagBoxes = getRootNode().getItems();
        Optional<TagBox> matchingTag = tagBoxes.stream().filter(tagBox -> tagBox.tag.equals(toTag)).findFirst();

        if (!matchingTag.isPresent()) {
            throw new IllegalArgumentException("Tag does not exists");
        }

        guiRobot.interact(() -> {
            getRootNode().scrollTo(matchingTag.get());
            getRootNode().getSelectionModel().select(matchingTag.get());
        });
        guiRobot.pauseForHuman();
    }

    /**
     * Returns the TagBoxhandle of a tag in this list.
     */
    public TagBoxHandle getTagBoxHandle(int index) {
        return getTagBoxHandle(getRootNode().getItems().get(index).tag);
    }

    public TagBoxHandle getTagBoxHandle(Tag tag) {
        Optional<TagBoxHandle> handle = getRootNode().getItems().stream()
                .filter(box -> box.tag.equals(tag))
                .map(box -> new TagBoxHandle(box.getRoot()))
                .findFirst();
        return handle.orElseThrow(() -> new IllegalArgumentException("Tag does not exist."));
    }
}
