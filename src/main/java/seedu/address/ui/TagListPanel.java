//@@author conantteo
package seedu.address.ui;

import org.fxmisc.easybind.EasyBind;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.model.tag.Tag;

/**
 * Panel containing the list of unique tags
 */
public class TagListPanel extends UiPart<Region> {
    private static final String FXML = "TagListPanel.fxml";

    @FXML
    private ListView tagListView;

    public TagListPanel(ObservableList<Tag> allTagsList) {
        super(FXML);
        bindTags(allTagsList);
    }

    /**
     * Creating bindings for each tag to each ListCell in the ListView
     * @param allTagsList is a valid list of all unique tags to be displayed.
     */
    private void bindTags(ObservableList<Tag> allTagsList) {
        ObservableList<TagBox> mappedList = EasyBind.map(
                allTagsList, (tag) -> new TagBox(tag));
        tagListView.setItems(mappedList);
        tagListView.setCellFactory(listView -> new TagListViewCell());

    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code TagBox}.
     */
    class TagListViewCell extends ListCell<TagBox> {

        @Override
        protected void updateItem(TagBox tagBox, boolean empty) {
            super.updateItem(tagBox, empty);

            Platform.runLater(() -> {
                if (empty || tagBox == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setGraphic(tagBox.getRoot());
                }

            });

        }
    }
}
