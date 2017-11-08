# conantteo
###### \java\guitests\AddressBookGuiTest.java
``` java
    protected TagListPanelHandle getTagListPanel() {
        return mainWindowHandle.getTagListPanel();
    }
```
###### \java\guitests\guihandles\MainWindowHandle.java
``` java
    public TagListPanelHandle getTagListPanel() {
        return tagListPanel;
    }

}
```
###### \java\guitests\guihandles\TagBoxHandle.java
``` java
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
```
###### \java\guitests\guihandles\TagListPanelHandle.java
``` java
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
```
###### \java\seedu\address\commons\util\IndexArrayUtilTest.java
``` java
package seedu.address.commons.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;

import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.index.Index;

public class IndexArrayUtilTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void compareIndexArrays_atLeastOneNullArray_throwException() {
        assertExceptionThrown(NullPointerException.class, new Index[1], null, Optional.empty());
    }

    @Test
    public void compareIndexArrays_differentLengthArrays_invalidComparison() {
        assertFalse(assertComparison(new Index[0], new Index[1]));
    }

    @Test
    public void compareIndexArrays_sameLengthDiffElements_invalidComparison() {
        Index[] arr1 = {INDEX_FIRST_PERSON};
        Index[] arr2 = {INDEX_SECOND_PERSON};
        assertFalse(assertComparison(arr1, arr2));
    }

    @Test
    public void compareIndexArrays_sameLengthSameElements_validComparison() {
        Index[] arr1 = {INDEX_FIRST_PERSON};
        Index[] arr2 = {INDEX_FIRST_PERSON};
        assertTrue(assertComparison(arr1, arr2));
    }

    @Test
    public void swapFirstWithSecond_positionChanged_successful() {
        Index[] beforeSwap = {INDEX_THIRD_PERSON, INDEX_SECOND_PERSON, INDEX_FIRST_PERSON};
        Index[] afterSwap = {INDEX_SECOND_PERSON, INDEX_THIRD_PERSON, INDEX_FIRST_PERSON};
        IndexArrayUtil.swapElements(beforeSwap, 0);
        assertTrue(beforeSwap[0].equals(afterSwap[0]));
        assertTrue(beforeSwap[1].equals(afterSwap[1]));
        assertTrue(beforeSwap[2].equals(afterSwap[2]));
    }

    @Test
    public void array_isDistinct() {
        Index[] distinctArray = {INDEX_FIRST_PERSON, INDEX_SECOND_PERSON, INDEX_THIRD_PERSON};
        assertTrue(IndexArrayUtil.isDistinct(distinctArray));
    }

    @Test
    public void array_isNotDistinct() {
        Index[] notDistinctArray = {INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, INDEX_THIRD_PERSON};
        assertFalse(IndexArrayUtil.isDistinct(notDistinctArray));
    }

    private void assertExceptionThrown(Class<? extends Throwable> exceptionClass, Index[] arr1, Index[] arr2,
                                       Optional<String> errorMessage) {
        thrown.expect(exceptionClass);
        errorMessage.ifPresent(message -> thrown.expectMessage(message));
        IndexArrayUtil.compareIndexArrays(arr1, arr2);
    }

    private boolean assertComparison(Index[] arr1, Index[] arr2) {
        return IndexArrayUtil.compareIndexArrays(arr1, arr2);
    }
}
```
###### \java\seedu\address\logic\commands\EmailCommandTest.java
``` java
package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.showFirstPersonOnly;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Rule;
import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.EmailRequestEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.ui.testutil.EventsCollectorRule;

public class EmailCommandTest {

    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    //Model simply provides the required names of recipients and are not modified in any way.
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Index[] indexArray;

    @Test
    public void execute_emailRequestEvent_success() throws CommandException {
        EmailCommand emailCommand = prepareCommand(INDEX_FIRST_PERSON);
        CommandResult result = emailCommand.execute();
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof EmailRequestEvent);
        assertTrue(eventsCollectorRule.eventsCollector.getSize() == 1);

    }

    @Test
    public void execute_validIndexUnfilteredList_success() throws CommandException {
        ReadOnlyPerson personToEmail = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EmailCommand emailCommand = prepareCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(EmailCommand.MESSAGE_EMAIL_PERSON_SUCCESS,
                personToEmail.getName().toString());
        CommandResult result = emailCommand.execute();

        assertEquals(expectedMessage, result.feedbackToUser);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EmailCommand emailCommand = prepareCommand(outOfBoundIndex);
        String expectedMessage = Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX
                + outOfBoundIndex.getOneBased();

        assertCommandFailure(emailCommand, model, expectedMessage);
    }

    @Test
    public void execute_validIndexFilteredList_success() throws CommandException {
        showFirstPersonOnly(model);

        ReadOnlyPerson personToEmail = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EmailCommand emailCommand = prepareCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(EmailCommand.MESSAGE_EMAIL_PERSON_SUCCESS,
                personToEmail.getName().toString());
        CommandResult result = emailCommand.execute();

        assertEquals(expectedMessage, result.feedbackToUser);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showFirstPersonOnly(model);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        EmailCommand emailCommand = prepareCommand(outOfBoundIndex);
        String expectedMessage = Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX
                + outOfBoundIndex.getOneBased();

        assertCommandFailure(emailCommand, model, expectedMessage);
    }

    @Test
    public void execute_multipleValidIndex_success() throws CommandException {
        ReadOnlyPerson firstPersonToEmail = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        ReadOnlyPerson secondPersonToEmail = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        EmailCommand emailCommand = prepareCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);

        String expectedMessage = String.format(EmailCommand.MESSAGE_EMAIL_PERSON_SUCCESS,
                firstPersonToEmail.getName().toString() + ", " + secondPersonToEmail.getName().toString());
        CommandResult result = emailCommand.execute();

        assertEquals(expectedMessage, result.feedbackToUser);
    }

    @Test
    public void execute_validIndexWithoutEmail_throwsCommandException() {
        EmailCommand emailCommand = prepareCommand(INDEX_THIRD_PERSON);
        String expectedMessage = Messages.MESSAGE_INVALID_PERSON_TO_EMAIL
                + INDEX_THIRD_PERSON.getOneBased();

        assertCommandFailure(emailCommand, model, expectedMessage);
    }

    @Test
    public void equals() {
        EmailCommand emailFirstCommand = prepareCommand(INDEX_FIRST_PERSON);
        EmailCommand emailSecondCommand = prepareCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(emailFirstCommand.equals(emailFirstCommand));

        // same values -> returns true
        EmailCommand emailFirstCommandCopy = prepareCommand(INDEX_FIRST_PERSON);
        assertTrue(emailFirstCommand.equals(emailFirstCommandCopy));

        // different types -> returns false
        assertFalse(emailFirstCommand.equals(1));

        // null -> returns false
        assertFalse(emailFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(emailFirstCommand.equals(emailSecondCommand));
    }

    /**
     * Returns a {@code EmailCommand} using one index parameter {@code index}
     */
    private EmailCommand prepareCommand(Index index) {
        indexArray = new Index[1];
        indexArray[0] = index;

        EmailCommand emailCommand = new EmailCommand(indexArray);
        emailCommand.setData(model, new CommandHistory(), new UndoRedoStack());

        return emailCommand;
    }
    /**
     * Returns a {@code EmailCommand} using two index parameter {@code index1, index2}
     */
    private EmailCommand prepareCommand(Index index1, Index index2) {
        indexArray = new Index[2];
        indexArray[0] = index1;
        indexArray[1] = index2;

        EmailCommand emailCommand = new EmailCommand(indexArray);
        emailCommand.setData(model, new CommandHistory(), new UndoRedoStack());

        return emailCommand;
    }
}
```
###### \java\seedu\address\logic\commands\ExportCommandTest.java
``` java
package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.showFirstPersonOnly;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.MULTIPLE_INDEX;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Rule;
import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.ExportRequestEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.ui.testutil.EventsCollectorRule;

public class ExportCommandTest {

    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    //Model simply provides the required details of recipients and are not modified in any way.
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Index[] indexArray;

    @Test
    public void execute_exportRequestEvent_success() throws CommandException {
        ExportCommand exportCommand = prepareCommand(INDEX_FIRST_PERSON);
        CommandResult result = exportCommand.execute();
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof ExportRequestEvent);
        assertTrue(eventsCollectorRule.eventsCollector.getSize() == 1);
    }

    @Test
    public void execute_validIndexUnfilteredList_success() throws CommandException {
        ReadOnlyPerson personToExport = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        ExportCommand exportCommand = prepareCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(ExportCommand.MESSAGE_EXPORT_PERSON_SUCCESS,
                personToExport.getName().toString());
        CommandResult result = exportCommand.execute();

        assertEquals(expectedMessage, result.feedbackToUser);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        ExportCommand exportCommand = prepareCommand(outOfBoundIndex);
        String expectedMessage = String.format(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX
                + outOfBoundIndex.getOneBased());

        assertCommandFailure(exportCommand, model, expectedMessage);
    }

    @Test
    public void execute_validIndexFilteredList_success() throws CommandException {
        showFirstPersonOnly(model);

        ReadOnlyPerson personToExport = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        ExportCommand exportCommand = prepareCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(ExportCommand.MESSAGE_EXPORT_PERSON_SUCCESS,
                personToExport.getName().toString());
        CommandResult result = exportCommand.execute();

        assertEquals(expectedMessage, result.feedbackToUser);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showFirstPersonOnly(model);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        ExportCommand exportCommand = prepareCommand(outOfBoundIndex);
        String expectedMessage = Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX
                + outOfBoundIndex.getOneBased();

        assertCommandFailure(exportCommand, model, expectedMessage);
    }

    @Test
    public void execute_multipleValidIndex_success() throws CommandException {
        ReadOnlyPerson firstPersonToExport = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        ReadOnlyPerson secondPersonToExport = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        ExportCommand exportCommand = prepareCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);

        String expectedMessage = String.format(ExportCommand.MESSAGE_EXPORT_PERSON_SUCCESS,
                firstPersonToExport.getName().toString() + ", " + secondPersonToExport.getName().toString());
        CommandResult result = exportCommand.execute();

        assertEquals(expectedMessage, result.feedbackToUser);
    }

    @Test
    public void execute_all_success() throws CommandException {
        // Preparing command to export all contacts
        ExportCommand exportCommand = new ExportCommand();
        exportCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        // StringBuilder for building the names of each person
        StringBuilder allPerson = new StringBuilder();
        model.getFilteredPersonList().stream().forEach((
                person) -> allPerson.append(person.getName().toString() + ", "));
        // expectedMessage to remove the trailing ',' when building the message
        String expectedMessage = String.format(ExportCommand.MESSAGE_EXPORT_PERSON_SUCCESS,
                allPerson.toString().substring(0, allPerson.length() - 2));
        CommandResult result = exportCommand.execute();

        assertEquals(expectedMessage, result.feedbackToUser);
    }
    @Test
    public void equals() {
        ExportCommand exportAllCommand = new ExportCommand();
        ExportCommand exportSomeCommand = new ExportCommand(MULTIPLE_INDEX);

        // same object -> returns true
        assertTrue(exportAllCommand.equals(exportAllCommand));
        assertTrue(exportSomeCommand.equals(exportSomeCommand));

        // same values -> returns true
        ExportCommand exportSomeCommandCopy = new ExportCommand(MULTIPLE_INDEX);
        assertTrue(exportSomeCommand.equals(exportSomeCommandCopy));

        // different types -> returns false
        assertFalse(exportSomeCommand.equals(1));

        //null -> returns false
        assertFalse(exportSomeCommand.equals(null));

        // different commands -> returns false
        assertFalse(exportAllCommand.equals(exportSomeCommand));
    }

    /**
     * Returns a {@code ExportCommand} using one index parameter {@code index}
     */
    private ExportCommand prepareCommand(Index index) {
        indexArray = new Index[1];
        indexArray[0] = index;

        ExportCommand exportCommand = new ExportCommand(indexArray);
        exportCommand.setData(model, new CommandHistory(), new UndoRedoStack());

        return exportCommand;
    }

    /**
     * Returns a {@code ExportCommand} using two index parameter {@code index1, index2}
     */
    private ExportCommand prepareCommand(Index index1, Index index2) {
        indexArray = new Index[2];
        indexArray[0] = index1;
        indexArray[1] = index2;

        ExportCommand exportCommand = new ExportCommand(indexArray);
        exportCommand.setData(model, new CommandHistory(), new UndoRedoStack());

        return exportCommand;
    }
}
```
###### \java\seedu\address\logic\commands\FindCommandTest.java
``` java
    @Test
    public void execute_birthdayMonth_personFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        FindCommand command = prepareSearchBirthdayCommand("05");
        assertCommandSuccess(command, expectedMessage, Collections.singletonList(CARL));
    }

    /**
     * Parses {@code userInput} which is a birthday month represented by 2 digits into a {@code FindCommand}.
     */
    public FindCommand prepareSearchBirthdayCommand(String userInput) {
        FindCommand command = new FindCommand(new PersonContainsBirthdayPredicate(userInput));
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

```
###### \java\seedu\address\logic\parser\EmailCommandParserTest.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EmailCommand;

public class EmailCommandParserTest {

    private EmailCommandParser parser = new EmailCommandParser();
    private Index[] indexArray;

    @Test
    public void parse_validArgs_returnsEmailCommand() {
        assertParseSuccess(parser, "1", prepareCommand(INDEX_FIRST_PERSON));
        assertParseSuccess(parser, "1 2", prepareCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicatedArgs_throwsParseException() {
        assertParseFailure(parser, "1 1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ParserUtil.MESSAGE_INDEX_DUPLICATES));
    }

    /**
     * Returns a {@code EmailCommand} with the parameter {@code index}.
     */
    private EmailCommand prepareCommand(Index index) {
        indexArray = new Index[1];
        indexArray[0] = index;

        EmailCommand emailCommand = new EmailCommand(indexArray);

        return emailCommand;
    }

    /**
     * Returns a {@code EmailCommand} with more than one parameter {@code index}.
     */
    private EmailCommand prepareCommand(Index firstIndex, Index secondIndex) {
        indexArray = new Index[2];
        indexArray[0] = firstIndex;
        indexArray[1] = secondIndex;

        EmailCommand emailCommand = new EmailCommand(indexArray);

        return emailCommand;
    }
}
```
###### \java\seedu\address\logic\parser\ExportCommandParserTest.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.MULTIPLE_INDEX;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ExportCommand;

public class ExportCommandParserTest {

    private ExportCommandParser parser = new ExportCommandParser();

    @Test
    public void parse_validArgs_returnsExportCommand() {
        assertParseSuccess(parser, "1", prepareCommand(INDEX_FIRST_PERSON));
        assertParseSuccess(parser, "all", new ExportCommand());
        assertParseSuccess(parser, "1 2 3", new ExportCommand(MULTIPLE_INDEX));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicatedArgs_throwsParseException() {
        assertParseFailure(parser, "1 1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ParserUtil.MESSAGE_INDEX_DUPLICATES));
    }

    /**
     * Returns a {@code ExportCommand} with the parameter {@code index}.
     * @param index is an index specified by user input.
     * @return a ExportCommand.
     */
    private ExportCommand prepareCommand(Index index) {
        Index[] indexArray = new Index[1];
        indexArray[0] = index;

        ExportCommand exportCommand = new ExportCommand(indexArray);

        return exportCommand;
    }
}
```
###### \java\seedu\address\model\person\BirthdayTest.java
``` java
package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BirthdayTest {

    @Test
    public void isValidBirthday() {
        // invalid birthday format
        assertFalse(Birthday.isValidBirthday("")); // empty string
        assertFalse(Birthday.isValidBirthday(" ")); // spaces only
        assertFalse(Birthday.isValidBirthday("02/03/190")); // less than 8 digits
        assertFalse(Birthday.isValidBirthday("02/03/19000")); // more than 8 digits
        assertFalse(Birthday.isValidBirthday("phone")); // non-numeric
        assertFalse(Birthday.isValidBirthday("9011p041")); // alphanumeric
        assertFalse(Birthday.isValidBirthday("02.03-1990")); // invalid characters between digits
        assertFalse(Birthday.isValidBirthday("02031990")); // exactly 8 digits without any forward slash
        assertFalse(Birthday.isValidBirthday("29/02/1900")); // invalid leap day

        // valid birthday format
        assertTrue(Birthday.isValidBirthday("29/02/2000")); // valid leap day
        assertTrue(Birthday.isValidBirthday("02/03/1990")); // DD/MM/YYYY format consists of 8 digits and 2 '/'
    }

    @Test
    public void isValidBirthdayMonth() {
        // invalid birthday month
        assertFalse(Birthday.isValidMonth("111")); // more than 2 digits
        assertFalse(Birthday.isValidMonth("1")); // less than 2 digits
        assertFalse(Birthday.isValidMonth("00")); // exactly 2 digits but less than the range of [01 to 12]
        assertFalse(Birthday.isValidMonth("13")); // exactly 2 digits but above the range of [01 to 12]

        // valid birthday month
        assertTrue(Birthday.isValidMonth("01")); // exactly 2 digits between 01 to 12
    }
}
```
###### \java\seedu\address\model\person\VcardTest.java
``` java
package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import org.junit.Test;

public class VcardTest {

    private final String vCardBirthdayRegex = "\\b\\d{4}\\b\\-\\b\\d{2}\\b\\-\\b\\d{2}\\b";

    @Test
    public void valid_buildBirthdayString() {
        // invalid birthday string built
        assertFalse("".matches(vCardBirthdayRegex)); // empty string
        assertFalse(" ".matches(vCardBirthdayRegex)); // spaces only
        assertFalse("03071990".matches(vCardBirthdayRegex)); // only numeric characters
        assertFalse("03/07/1990".matches(vCardBirthdayRegex)); // contains invalid characters.
        assertFalse("03-07-1990".matches(vCardBirthdayRegex)); // wrong DD-MM-YYYY format
        assertFalse("03-07-199000".matches(vCardBirthdayRegex)); // contains invalid number of digits

        // valid birthday string
        assertTrue("1990-07-03".matches(vCardBirthdayRegex)); // YYYY-MM-DD format
    }

    @Test
    public void equals() {
        // same values -> return true
        Vcard uniqueVcard = createVcard(ALICE);
        Vcard uniqueVcardCopy = createVcard(ALICE);
        assertTrue(uniqueVcard.equals(uniqueVcardCopy));

        // same object -> return true
        assertTrue(uniqueVcard.equals(uniqueVcard));

        // null -> returns false
        assertFalse(uniqueVcard.equals(null));

        // different types -> returns false
        assertFalse(uniqueVcard.equals(5));

        // different person -> return false
        Vcard anotherUnqiueVcard = createVcard(BENSON);
        assertFalse(uniqueVcard.equals(anotherUnqiueVcard));
    }

    /**
     * Creates a new Vcard for a {@code person} using the person's details.
     * @param person is a valid person.
     * @return the a Vcard for a person.
     */
    private Vcard createVcard(ReadOnlyPerson person) {
        Vcard vCard = new Vcard(person);
        return vCard;
    }
}
```
###### \java\seedu\address\storage\XmlAddressBookStorageTest.java
``` java
        //Save in a backup file and read back
        xmlAddressBookStorage.backupAddressBook(original);
        readBack = xmlAddressBookStorage.readAddressBook(filePath + "-backup.xml").get();
        assertEquals(original, new AddressBook(readBack));

    }
```
###### \java\seedu\address\testutil\TagBuilder.java
``` java
package seedu.address.testutil;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;

/**
 * A utility class to create Tag objects
 */
public class TagBuilder {

    public static final String DEFAULT_TAG_NAME = "friends";

    private Tag tag;

    public TagBuilder() {
        try {
            this.tag = new Tag(DEFAULT_TAG_NAME);
        } catch (IllegalValueException e) {
            throw new AssertionError("Default tag name is invalid");
        }
    }

    /**
     * Create a new Tag {@code tag} with the specified tag name {@code tagName}
     * @param tagName must be non-null and is a valid tag name
     * @return a new Tag of specified name.
     */
    public Tag withSpecifiedTagName(String tagName) {
        requireNonNull(tagName);
        try {
            tag = new Tag(tagName);
        } catch (IllegalValueException e) {
            throw new AssertionError("Specified tag name is invalid");
        }
        return tag;
    }

}
```
###### \java\seedu\address\testutil\TypicalIndexes.java
``` java
    public static final Index[] MULTIPLE_INDEX = {INDEX_FIRST_PERSON, INDEX_SECOND_PERSON, INDEX_THIRD_PERSON};
}
```
###### \java\seedu\address\testutil\TypicalTags.java
``` java
package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.tag.Tag;

/**
 * A utility class containing a list of {@code Tag} objects to be used in tests.
 */
public class TypicalTags {

    public static final Tag GOOGLE = new TagBuilder().withSpecifiedTagName("Google");

    public static final Tag FACEBOOK = new TagBuilder().withSpecifiedTagName("Facebook");

    public static final Tag CLASSMATES = new TagBuilder().withSpecifiedTagName("Classmates");

    public static final Tag FRIENDS = new TagBuilder().withSpecifiedTagName("friends");

    public static final Tag OWE_MONEY = new TagBuilder().withSpecifiedTagName("owesMoney");

    private TypicalTags() {} // prevents instantiation

    public static List<Tag> getTypicalTags() {
        return new ArrayList<>(Arrays.asList(GOOGLE, FACEBOOK, CLASSMATES, FRIENDS, OWE_MONEY));
    }
}
```
###### \java\seedu\address\ui\TagBoxTest.java
``` java
package seedu.address.ui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalTags.CLASSMATES;
import static seedu.address.testutil.TypicalTags.FACEBOOK;
import static seedu.address.testutil.TypicalTags.FRIENDS;
import static seedu.address.testutil.TypicalTags.GOOGLE;
import static seedu.address.testutil.TypicalTags.OWE_MONEY;
import static seedu.address.ui.testutil.GuiTestAssert.assertBoxDisplayTag;

import org.junit.Test;

import guitests.guihandles.TagBoxHandle;
import seedu.address.model.tag.Tag;

public class TagBoxTest extends GuiUnitTest {

    private Tag testTag;
    private TagBox testTagBox;

    @Test
    public void display() {
        // Label with a tag
        testTag = GOOGLE;
        testTagBox = new TagBox(testTag);
        uiPartRule.setUiPart(testTagBox);

        assertTagBoxDisplay(testTagBox, testTag);

        // Changes label to reflect another new tag
        Tag anotherTag = FACEBOOK;
        testTagBox = new TagBox(anotherTag);
        uiPartRule.setUiPart(testTagBox);

        assertTagBoxDisplay(testTagBox, anotherTag);
    }

    @Test
    public void mouseEventHandler_tagBoxIsClicked() {
        testTag = OWE_MONEY;
        testTagBox = new TagBox(testTag);
        uiPartRule.setUiPart(testTagBox);

        TagBoxHandle tagBoxHandle = new TagBoxHandle(testTagBox.getRoot());
        tagBoxHandle.click();

        guiRobot.waitForEvent(tagBoxHandle::isClicked);

        assertTrue(tagBoxHandle.isClicked());
    }

    @Test
    public void equals() {
        testTag = FRIENDS;
        TagBox tagBox = new TagBox(testTag);

        // same object -> returns true
        assertTrue(tagBox.equals(tagBox));

        // null -> returns false
        assertFalse(tagBox.equals(null));

        // different types -> returns false
        Tag differentTag = CLASSMATES;
        assertFalse(tagBox.equals(new TagBox(differentTag)));
    }

    /**
     * Asserts that {@code tagBox} displays the {@code tag} correctly.
     */
    private void assertTagBoxDisplay(TagBox tagBox, Tag tag) {
        guiRobot.pauseForHuman();

        TagBoxHandle tagBoxHandle = new TagBoxHandle(tagBox.getRoot());

        assertBoxDisplayTag(tag, tagBoxHandle);
    }
}
```
###### \java\seedu\address\ui\TagListPanelTest.java
``` java
package seedu.address.ui;

import static seedu.address.testutil.TypicalTags.getTypicalTags;
import static seedu.address.ui.testutil.GuiTestAssert.assertBoxDisplayTag;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.TagBoxHandle;
import guitests.guihandles.TagListPanelHandle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.tag.Tag;

public class TagListPanelTest extends GuiUnitTest {
    private static final ObservableList<Tag> TYPICAL_TAGS = FXCollections.observableList(getTypicalTags());
    private TagListPanelHandle tagListPanelHandle;

    @Before
    public void setUp() {
        TagListPanel tagListPanel = new TagListPanel(TYPICAL_TAGS);
        uiPartRule.setUiPart(tagListPanel);

        tagListPanelHandle = new TagListPanelHandle(getChildNode(tagListPanel.getRoot(),
                TagListPanelHandle.TAG_LIST_VIEW_ID));
    }

    @Test
    public void display() {
        for (int i = 0; i < TYPICAL_TAGS.size(); i++) {
            tagListPanelHandle.navigateToTag(TYPICAL_TAGS.get(i));
            Tag expectedTag = TYPICAL_TAGS.get(i);
            TagBoxHandle actualTagBox = tagListPanelHandle.getTagBoxHandle(i);

            assertBoxDisplayTag(expectedTag, actualTagBox);
        }
    }
}
```
