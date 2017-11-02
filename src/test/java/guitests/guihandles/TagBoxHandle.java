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

    public TagBoxHandle(Node boxNode) {
        super(boxNode);

        this.tagLabel = getChildNode(TAG_FIELD_ID);
    }

    public String getTagName() {
        return tagLabel.getText();
    }
}
