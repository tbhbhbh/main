package seedu.address.ui;

import static seedu.address.ui.CommandBox.DEFAULT_DISPLAY_PIC;

import java.io.File;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.commons.util.AppUtil;
import seedu.address.model.person.ReadOnlyPerson;

/**
 *
 */
public class PersonDescription extends UiPart<StackPane> {

    private static final String FXML = "PersonDescription.fxml";
    private static final String DEFAULT_DP = "/images/defaultperson.png";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private Circle displayPic;

    @FXML
    private Label name;

    @FXML
    private Label group;

    @FXML
    private Label mobile;

    @FXML
    private Label birthday;

    @FXML
    private Label email;

    @FXML
    private Label address;

    @FXML
    private Label instagram;

    @FXML
    private Label twitter;


    public PersonDescription() {
        super(FXML);
        initIcons();
        registerAsAnEventHandler(this);
    }

    /**
     *
     * @param person
     */
    private void loadPersonDescription(ReadOnlyPerson person) {
        name.setText(person.getName().fullName);
        mobile.setText(person.getPhone().value);
        birthday.setText(person.getBirthday().value);
        email.setText(person.getEmail().value);
        address.setText(person.getAddress().value);
        instagram.setText(person.getInstagramName().value);
        twitter.setText(person.getTwitterName().value);
        initDisplayPic(person);
    }

    /**
     *
     * @param person
     */
    private void initDisplayPic(ReadOnlyPerson person) {
        Image displayPictureImg;
        String profilePic = person.getDisplayPic().toString();
        if (profilePic.equals(DEFAULT_DISPLAY_PIC)) {
            displayPictureImg = AppUtil.getImage(profilePic);
        } else {
            File imgFile = new File(person.getDisplayPic().toString());
            displayPictureImg = new Image(imgFile.toURI().toString());
        }
        displayPic.setFill(new ImagePattern(displayPictureImg));
    }

    /**
     *
     */
    private void initIcons() {

    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadPersonDescription(event.getNewSelection().person);
    }


}
