package seedu.address.ui;

import static seedu.address.MainApp.getAppHostServices;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.HostServices;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.commons.core.Config;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.email.EmailRequestEvent;
import seedu.address.commons.events.ui.CloseProgressEvent;
import seedu.address.commons.events.ui.ExitAppRequestEvent;
import seedu.address.commons.events.ui.ExportRequestEvent;
import seedu.address.commons.events.ui.ShowHelpRequestEvent;
import seedu.address.commons.events.ui.ShowProgressEvent;
import seedu.address.commons.util.FxViewUtil;
import seedu.address.logic.Logic;
import seedu.address.model.UserPrefs;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Region> {

    private static final String ICON = "/images/Icon.png";
    private static final String FXML = "MainWindow.fxml";
    private static final int MIN_HEIGHT = 600;
    private static final int MIN_WIDTH = 450;
    private static final String EMAIL_URI_PREFIX = "mailTo:";
    private static final String EXPORT_FILE_ABSOLUTE_PATH = "./data/";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private BrowserPanel browserPanel;
    private PersonListPanel personListPanel;
    private TagListPanel tagListPanel;
    private PersonDescription personDescriptionPanel;
    private Config config;
    private UserPrefs prefs;

    @FXML
    private StackPane browserPlaceholder;

    @FXML
    private Pane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane personListPanelPlaceholder;

    @FXML
    private StackPane personDescriptionPlaceHolder;

    @FXML
    private Pane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    @FXML
    private StackPane tagListPanelPlaceholder;

    private ProgressWindow pWindow;

    public MainWindow(Stage primaryStage, Config config, UserPrefs prefs, Logic logic) {
        super(FXML);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;
        this.config = config;
        this.prefs = prefs;

        // Configure the UI
        setTitle(config.getAppTitle());
        setIcon(ICON);
        setWindowMinSize();
        setWindowDefaultSize(prefs);
        Scene scene = new Scene(getRoot());
        primaryStage.setScene(scene);

        setAccelerators();
        registerAsAnEventHandler(this);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        browserPanel = new BrowserPanel();
        browserPlaceholder.getChildren().add(browserPanel.getRoot());

        personListPanel = new PersonListPanel(logic.getFilteredPersonList());
        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());

        personDescriptionPanel = new PersonDescription();
        personDescriptionPlaceHolder.getChildren().add(personDescriptionPanel.getRoot());

        ResultDisplay resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(prefs.getAddressBookFilePath(),
                logic.getFilteredPersonList().size());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(logic, primaryStage);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());

        tagListPanel = new TagListPanel(logic.getAllTags());
        tagListPanelPlaceholder.getChildren().add(tagListPanel.getRoot());
    }

    void hide() {
        primaryStage.hide();
    }

    private void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    /**
     * Sets the given image as the icon of the main window.
     * @param iconSource e.g. {@code "/images/help_icon.png"}
     */
    private void setIcon(String iconSource) {
        FxViewUtil.setStageIcon(primaryStage, iconSource);
    }

    /**
     * Sets the default size based on user preferences.
     */
    private void setWindowDefaultSize(UserPrefs prefs) {
        primaryStage.setHeight(prefs.getGuiSettings().getWindowHeight());
        primaryStage.setWidth(prefs.getGuiSettings().getWindowWidth());
        if (prefs.getGuiSettings().getWindowCoordinates() != null) {
            primaryStage.setX(prefs.getGuiSettings().getWindowCoordinates().getX());
            primaryStage.setY(prefs.getGuiSettings().getWindowCoordinates().getY());
        }
    }

    private void setWindowMinSize() {
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
    }

    /**
     * Returns the current size and the position of the main Window.
     */
    GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
    }

    /**
     * Opens the help window.
     */
    @FXML
    public void handleHelp() {
        HelpWindow helpWindow = new HelpWindow();
        helpWindow.show();
    }

    /**
     * This method will invoke the user's default email client and set the recipients field with all the
     * email addresses specified by the user.
     * @param allEmailAddresses is a string of all valid email addresses user request to email to.
     * @throws Exception when user's OS cannot support Desktop operations.
     */
    public void handleEmail(String allEmailAddresses) throws Exception {
        try {
            URI mailTo = new URI(EMAIL_URI_PREFIX + allEmailAddresses);
            if (Desktop.isDesktopSupported()) {
                Desktop userDesktop = Desktop.getDesktop();
                userDesktop.mail(mailTo);
            } else {
                throw new Exception("Desktop is not supported");
            }
        } catch (Exception e) {
            throw new Exception("User default mail application is not found or failed to launch");
        }
    }

    /**
     * Opens the progress window.
     */
    @FXML
    public void handleProgress(ReadOnlyDoubleProperty progress) {
        pWindow = new ProgressWindow(progress);
        primaryStage.toFront();
        pWindow.show();
    }

    /**
     * Closes the progress window.
     */
    @FXML
    public void handleCloseProgress() {
        pWindow.getDialogStage().close();
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Opens a file window which shows the directory where contacts.vcf file is found.
     */
    public void handleExport() {
        File file = new File(EXPORT_FILE_ABSOLUTE_PATH);
        HostServices hostServices = getAppHostServices();
        hostServices.showDocument(file.getAbsolutePath());
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }

    public PersonListPanel getPersonListPanel() {
        return this.personListPanel;
    }

    void releaseResources() {
        browserPanel.freeResources();
    }

    @Subscribe
    private void handleShowHelpEvent(ShowHelpRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        handleHelp();
    }

    @Subscribe
    private void handleEmailRequestEvent(EmailRequestEvent event) throws Exception {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        handleEmail(event.getAllEmailAddresses());
    }

    @Subscribe
    private void handleShowProgressEvent(ShowProgressEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        handleProgress(event.getProgress());

    }

    @Subscribe
    private void handleCloseProgressEvent(CloseProgressEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        handleCloseProgress();

    }

    @Subscribe
    private void handleExportRequestEvent(ExportRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        handleExport();
    }
}
