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
public class PersonDescription extends UiPart<Region> {

    private static final String FXML = "PersonDescription.fxml";
    private static final String BIRTHDAY_ICON = "/images/birthday_icon.png";
    private static final String PHONE_ICON = "/images/phone_icon.png";
    private static final String EMAIL_ICON = "/images/email_icon.png";
    private static final String AVATAR = "/images/avatar.png";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private StackPane avatarPane;
    @FXML
    private ImageView avatar;
    @FXML
    private Circle displayPic;
    @FXML
    private TextFlow name;
    @FXML
    private Label birthdayIcon;
    @FXML
    private Label birthday;
    @FXML
    private Label phoneIcon;
    @FXML
    private Label phone;
    @FXML
    private Label emailIcon;
    @FXML
    private Label email;

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
        name.getChildren().clear();
        name.getChildren().add(new Text(person.getName().fullName));
        birthday.setText(person.getBirthday().value);
        phone.setText(person.getPhone().value);
        email.setText(person.getEmail().value);
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
        //Image avatarPic = AppUtil.getImage(AVATAR);
        Image phoneIconPic = AppUtil.getImage(PHONE_ICON);
        Image birthdayIconPic = AppUtil.getImage(BIRTHDAY_ICON);
        Image emailIconPic = AppUtil.getImage(EMAIL_ICON);
        //avatar = new ImageView(avatarPic);
        //avatarPane.getChildren().add(avatar);
        phoneIcon.setGraphic(new ImageView(birthdayIconPic));
        birthdayIcon.setGraphic(new ImageView(phoneIconPic));
        emailIcon.setGraphic(new ImageView(emailIconPic));
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadPersonDescription(event.getNewSelection().person);
    }


}
