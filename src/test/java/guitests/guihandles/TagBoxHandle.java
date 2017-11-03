//@@author conantteo
package guitests.guihandles;

import javafx.scene.Node;
import javafx.scene.control.Label;

/**
 * Provides a handle to a tag box in the tag list panel.
 */
public class TagBoxHandle extends NodeHandle<Node> {
    private static final String TAG_FIELD_ID = "#tagsName";

    private final Label tagLabel;

    private boolean isTagBoxClicked = false;

    public TagBoxHandle(Node boxNode) {
        super(boxNode);
        this.isTagBoxClicked = false;
        this.tagLabel = getChildNode(TAG_FIELD_ID);
    }

    // isTagBoxClicked returns true after guiRobot has click the rootNode
    @Override
    public void click() {
        guiRobot.clickOn(getRootNode());
        this.isTagBoxClicked = true;
    }

    public boolean isClicked() {
        return isTagBoxClicked;
    }

    public String getTagName() {
        return tagLabel.getText();
    }
}
