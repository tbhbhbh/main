package guitests.guihandles;

import java.io.File;
import java.io.IOException;

import javafx.application.HostServices;
import javafx.stage.Stage;
import seedu.address.TestApp;
import seedu.address.commons.util.FileUtil;

/**
 * Provides a handle for {@code MainWindow}.
 */
public class MainWindowHandle extends StageHandle {

    private final PersonListPanelHandle personListPanel;
    private final ResultDisplayHandle resultDisplay;
    private final CommandBoxHandle commandBox;
    private final StatusBarFooterHandle statusBarFooter;
    private final MainMenuHandle mainMenu;
    private final BrowserPanelHandle browserPanel;
    private final TagListPanelHandle tagListPanel;

    public MainWindowHandle(Stage stage) {
        super(stage);

        personListPanel = new PersonListPanelHandle(getChildNode(PersonListPanelHandle.PERSON_LIST_VIEW_ID));
        resultDisplay = new ResultDisplayHandle(getChildNode(ResultDisplayHandle.RESULT_DISPLAY_ID));
        commandBox = new CommandBoxHandle(getChildNode(CommandBoxHandle.COMMAND_INPUT_FIELD_ID));
        statusBarFooter = new StatusBarFooterHandle(getChildNode(StatusBarFooterHandle.STATUS_BAR_PLACEHOLDER));
        mainMenu = new MainMenuHandle(getChildNode(MainMenuHandle.MENU_BAR_ID));
        browserPanel = new BrowserPanelHandle(getChildNode(BrowserPanelHandle.BROWSER_ID));
        tagListPanel = new TagListPanelHandle(getChildNode(TagListPanelHandle.TAG_LIST_VIEW_ID));
    }

    public PersonListPanelHandle getPersonListPanel() {
        return personListPanel;
    }

    public ResultDisplayHandle getResultDisplay() {
        return resultDisplay;
    }

    public CommandBoxHandle getCommandBox() {
        return commandBox;
    }

    public StatusBarFooterHandle getStatusBarFooter() {
        return statusBarFooter;
    }

    public MainMenuHandle getMainMenu() {
        return mainMenu;
    }

    public BrowserPanelHandle getBrowserPanel() {
        return browserPanel;
    }

    public TagListPanelHandle getTagListPanel() {
        return tagListPanel;
    }

    /**
     * Method to handle export command in MainWindow.
     * @param filePath is a valid file path for contacts.vcf used for testing purposes.
     * @return the File created in the valid file path.
     */
    public File handleExport(String filePath) {
        File file = new File(filePath);
        // Creates a dummy contacts.vcf file that contains no data if it does not exist
        try {
            FileUtil.createIfMissing(file);
        } catch (IOException e) {
            assert false : "File path is a valid path";
        }
        return file;
    }

    /**
     * Method to call upon HostServices {@hostServices} in MainWindow which helps to open documents
     * and show web pages in browser.
     * For example, MainWindow uses this services to open a folder directory.
     * @return a HostServices object that is provided by java Application class.
     */
    public HostServices handleServices() {
        HostServices hostServices = TestApp.getAppHostServices();
        return hostServices;
    }
}
