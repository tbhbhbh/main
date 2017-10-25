package seedu.address.ui;

import static seedu.address.ui.CommandBox.DEFAULT_DISPLAY_PIC;

import java.io.File;
import java.util.HashMap;
import java.util.Random;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.commons.util.AppUtil;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    private static HashMap<String, String> tagColours = new HashMap<String, String>();
    private static String[] colours = { "red", "brown", "grey", "yellow", "blue", "pink", "green", "maroon", "orange" };
    private static Random rand = new Random();

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final ReadOnlyPerson person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label birthday;
    @FXML
    private FlowPane tags;
    @FXML
    private StackPane imagePane;
    @FXML
    private ImageView displayPic;

    public PersonCard(ReadOnlyPerson person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        initTags(person);
        initImage(person);
        bindListeners(person);
    }

    /**
     * Binds the individual UI elements to observe their respective {@code Person} properties
     * so that they will be notified of any changes.
     */
    private void bindListeners(ReadOnlyPerson person) {
        name.textProperty().bind(Bindings.convert(person.nameProperty()));
        phone.textProperty().bind(Bindings.convert(person.phoneProperty()));
        address.textProperty().bind(Bindings.convert(person.addressProperty()));
        email.textProperty().bind(Bindings.convert(person.emailProperty()));
        birthday.textProperty().bind(Bindings.convert(person.birthdayProperty()));
        person.displayPicProperty().addListener((observable, oldValue, newValue) -> {
            initImage(person);
        });
        person.tagProperty().addListener((observable, oldValue, newValue) -> {
            tags.getChildren().clear();
            initTags(person);
        });
    }

    /**
     * Initialise the image in PersonCard display
     * @param person
     */
    private void initImage(ReadOnlyPerson person) {
        if (person.getDisplayPic().toString().equals(DEFAULT_DISPLAY_PIC)) {
            Image displayPicture = AppUtil.getImage(DEFAULT_DISPLAY_PIC);
            displayPic = new ImageView(displayPicture);
        } else {
            File personImg = new File(person.getDisplayPic().toString());
            String imgUrl = personImg.toURI().toString();
            Image displayPicture = new Image(imgUrl);
            displayPic = new ImageView(displayPicture);
        }
        displayPic.setFitHeight(50);
        displayPic.setFitWidth(50);
        imagePane.getChildren().add(displayPic);
    }

    /**
     * Create new labels and bind a colour to it
     * @param person
     */
    private void initTags(ReadOnlyPerson person) {
        person.getTags().forEach(tag -> {
            Label newTag = new Label(tag.tagName);
            newTag.setStyle("-fx-background-color: "
                    + getTagColours(tag.tagName));
            tags.getChildren().add(newTag);
        });
    }

    private String getTagColours(String tagName) {
        if (!tagColours.containsKey(tagName)) {
            tagColours.put(tagName, colours[rand.nextInt(colours.length)]);
        }
        return tagColours.get(tagName);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonCard)) {
            return false;
        }

        // state check
        PersonCard card = (PersonCard) other;
        return id.getText().equals(card.id.getText())
                && person.equals(card.person);
    }
}
