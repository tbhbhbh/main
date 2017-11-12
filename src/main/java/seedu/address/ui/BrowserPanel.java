package seedu.address.ui;

import static seedu.address.logic.commands.SocialCommand.INSTAGRAM_URL_PREFIX;
import static seedu.address.logic.commands.SocialCommand.TWITTER_URL_PREFIX;

import java.net.URL;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    public static final String DEFAULT_PAGE = "default.html";
    public static final String GOOGLE_SEARCH_URL_PREFIX = "https://www.google.com.sg/search?safe=off&q=";
    public static final String GOOGLE_SEARCH_URL_SUFFIX = "&cad=h";

    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private WebView browser;

    public BrowserPanel() {
        super(FXML);

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        loadDefaultPage();
        registerAsAnEventHandler(this);
<<<<<<< HEAD
    }
    //@@author tbhbhbh
    /**
     * Loads the person's Instagram, Twitter and then a Google search page for the person's name, in that order,
=======

        // Allows Google sign in to work in WebView as
        // Same-Origin Policy would prevent it otherwise
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
    }
    //@@author tbhbhbh
    /**
     * Loads the person's Instagram or Twitter
>>>>>>> 080bb00b598ce7886e1063e844da0c82741f89bc
     * depending on if the person has the social media fields filled in.
     */
    private void loadPersonPage(ReadOnlyPerson person) {
        if (!person.getInstagramName().toString().isEmpty()) {
            loadPage(INSTAGRAM_URL_PREFIX +  person.getInstagramName());
        } else if (!person.getTwitterName().toString().isEmpty()) {
            loadPage(TWITTER_URL_PREFIX + person.getTwitterName());
<<<<<<< HEAD
        } else {
            loadPage(GOOGLE_SEARCH_URL_PREFIX + person.getName().fullName.replaceAll(" ", "+")
                    + GOOGLE_SEARCH_URL_SUFFIX);
=======
>>>>>>> 080bb00b598ce7886e1063e844da0c82741f89bc
        }
    }
    //@@author
    public void loadPage(String url) {
        Platform.runLater(() -> browser.getEngine().load(url));
    }

    /**
     * Loads a default HTML file with a background that matches the general theme.
     */
    private void loadDefaultPage() {
        URL defaultPage = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE);
        loadPage(defaultPage.toExternalForm());
    }

    /**
     * Frees resources allocated to the browser.
     */
    public void freeResources() {
        browser = null;
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadPersonPage(event.getNewSelection().person);
    }
}
