# tbhbhbh
###### \java\seedu\address\logic\commands\AddCommandTest.java
``` java
        @Override
        public void deleteTag(Tag tag) {
            fail("This method should not be called.");
        }

        @Override
        public void editTag(Tag oldTag, Tag newTag) {
            fail("This method should not be called.");
        }
```
###### \java\seedu\address\logic\commands\SearchCommandTest.java
``` java
package seedu.address.logic.commands;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.model.person.ReadOnlyPerson;

public class SearchCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        PersonContainsKeywordsPredicate firstPredicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList("first"));
        PersonContainsKeywordsPredicate secondPredicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList("second"));

        SearchCommand searchFirstCommand = new SearchCommand(firstPredicate);
        SearchCommand searchSecondCommand = new SearchCommand(secondPredicate);

        // same object -> returns true
        assertTrue(searchFirstCommand.equals(searchFirstCommand));

        // same values -> returns true
        SearchCommand findFirstCommandCopy = new SearchCommand(firstPredicate);
        assertTrue(searchFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(searchFirstCommand.equals(1));

        // null -> returns false
        assertFalse(searchFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(searchFirstCommand.equals(searchSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        SearchCommand command = prepareCommand(" ");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_tagThenName_personFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        SearchCommand command = prepareCommand("owesMoney Benson");
        assertCommandSuccess(command, expectedMessage, Collections.singletonList(BENSON));

    }

    @Test
    public void execute_nameThenTag_personFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        SearchCommand command = prepareCommand("Benson owesMoney");
        assertCommandSuccess(command, expectedMessage, Collections.singletonList(BENSON));
    }

    /**
     * Parses {@code userInput} into a {@code SearchCommand}.
     */
    public SearchCommand prepareCommand(String userInput) {
        SearchCommand command =
                new SearchCommand(new PersonContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+"))));
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<ReadOnlyPerson>} is equal to {@code expectedList}<br>
     *     - the {@code AddressBook} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(SearchCommand command,
                                      String expectedMessage, List<ReadOnlyPerson> expectedList) {
        AddressBook expectedAddressBook = new AddressBook(model.getAddressBook());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredPersonList());
        assertEquals(expectedAddressBook, model.getAddressBook());
    }
}
```
###### \java\seedu\address\logic\commands\SocialCommandTest.java
``` java
package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.SocialCommand.MESSAGE_IF_MISSING;
import static seedu.address.logic.commands.SocialCommand.MESSAGE_INSTAGRAM;
import static seedu.address.logic.commands.SocialCommand.MESSAGE_SOCIAL_UNSUPPORTED;
import static seedu.address.logic.commands.SocialCommand.MESSAGE_TWITTER;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Rule;
import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.ui.testutil.EventsCollectorRule;

public class SocialCommandTest {

    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    //Model provides required names of recipients and are not modified in any way
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_socialRequestInstagramEvent_success() throws CommandException {
        ReadOnlyPerson personToCheckSocial = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        SocialCommand socialCommand = prepareCommand(INDEX_FIRST_PERSON, "ig");

        String expectedMessagePrefix = String.format(socialCommand.getMessageSocialSuccess(),
                personToCheckSocial.getName().toString());
        String expectedMessage = expectedMessagePrefix
                + MESSAGE_INSTAGRAM
                + MESSAGE_IF_MISSING;

        CommandResult result = socialCommand.execute();
        assertEquals(expectedMessage, result.feedbackToUser);

    }

    @Test
    public void execute_socialRequestTwitterEvent_success() throws CommandException {
        ReadOnlyPerson personToCheckSocial = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        SocialCommand socialCommand = prepareCommand(INDEX_FIRST_PERSON, "tw");

        String expectedMessagePrefix = String.format(socialCommand.getMessageSocialSuccess(),
                personToCheckSocial.getName().toString());
        String expectedMessage = expectedMessagePrefix
                + MESSAGE_TWITTER
                + MESSAGE_IF_MISSING;

        CommandResult result = socialCommand.execute();
        assertEquals(expectedMessage, result.feedbackToUser);

    }

    @Test
    public void execute_validIndexWithInvalidSocialMedia_throwsCommandException() throws Exception {
        SocialCommand socialCommand = prepareCommand(INDEX_FIRST_PERSON, "fb");

        String expectedMessage = MESSAGE_SOCIAL_UNSUPPORTED + "fb";

        assertCommandFailure(socialCommand, model, expectedMessage);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        SocialCommand socialCommand = prepareCommand(outOfBoundIndex, "ig");
        String expectedMessage = Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX + ": "
                + outOfBoundIndex.getOneBased();

        assertCommandFailure(socialCommand, model, expectedMessage);
    }

    @Test
    public void equals() {
        SocialCommand socialFirstInstagramCommand = prepareCommand(INDEX_FIRST_PERSON, "ig");
        SocialCommand socialSecondInstagramCommand = prepareCommand(INDEX_SECOND_PERSON, "ig");
        SocialCommand socialFirstTwitterCommand = prepareCommand(INDEX_FIRST_PERSON, "tw");

        // same object -> returns true
        assertTrue(socialFirstInstagramCommand.equals(socialFirstInstagramCommand));

        // same vaues -> returns true
        SocialCommand socialFirstInstagramCommandCopy = prepareCommand(INDEX_FIRST_PERSON,
                "ig");
        assertTrue(socialFirstInstagramCommand.equals(socialFirstInstagramCommandCopy));

        // different types -> returns false
        assertFalse(socialFirstInstagramCommand.equals(1));

        // null -> returns false
        assertFalse(socialFirstInstagramCommand.equals(null));

        // different social media -> returns false
        assertFalse(socialFirstInstagramCommand.equals(socialFirstTwitterCommand));

        // different person -> returns false
        assertFalse(socialFirstInstagramCommand.equals(socialSecondInstagramCommand));

    }

    /**
     * Returns a {@code SocialCommand} using two parameters {@code index, string}
     */
    private SocialCommand prepareCommand(Index index, String socialMedia) {
        SocialCommand socialCommand = new SocialCommand(index, socialMedia);
        socialCommand.setData(model, new CommandHistory(), new UndoRedoStack());

        return socialCommand;
    }
}
```
###### \java\seedu\address\logic\commands\TagDeleteCommandTest.java
``` java
package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.tag.Tag;

public class TagDeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_deleteValidTagUnfilteredList_success() throws Exception {
        Set<Tag> tagSet = new HashSet<>(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()).getTags());
        String tagName = tagSet.toArray()[0].toString().substring(1, 8);
        TagDeleteCommand tagDeleteCommand = prepareCommand(tagSet.toArray()[0].toString().substring(1, 8));

        String expectedMessage = String.format(TagDeleteCommand.MESSAGE_DELETE_TAG_SUCCESS, tagName);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteTag(new Tag(tagSet.toArray()[0].toString().substring(1, 8)));

        assertCommandSuccess(tagDeleteCommand, model, expectedMessage, expectedModel);
    }

    /**
     * Returns a {@code TagDeleteCommand} with the parameter {@code tagName}.
     */
    private TagDeleteCommand prepareCommand(String tagName) {
        TagDeleteCommand tagDeleteCommand = new TagDeleteCommand(tagName);
        tagDeleteCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return tagDeleteCommand;
    }
}
```
###### \java\seedu\address\logic\commands\TagEditCommandTest.java
``` java
package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.TagEditCommand.MESSAGE_EDIT_TAG_SUCCESS;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class TagEditCommandTest {

    public static final String DUMMY_TAG = "dummy";
    public static final String DUMMY_TAG_TWO = "dummytwo";
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_editValidTagUnfilteredList_success() throws Exception {
        Set<Tag> tagSet = new HashSet<>(model.getFilteredPersonList()
                .get(INDEX_SECOND_PERSON.getZeroBased()).getTags());
        String oldTagName = tagSet.toArray()[0].toString().substring(1, 10);
        Tag oldTag = new Tag(oldTagName);
        String newTagName = DUMMY_TAG;
        Tag newTag = new Tag(newTagName);

        TagEditCommand tagEditCommand = prepareCommand(oldTagName, newTagName);

        String expectedMessage = String.format(MESSAGE_EDIT_TAG_SUCCESS, oldTag.toString(), newTag.toString());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.editTag(oldTag, newTag);

        assertCommandSuccess(tagEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editInvalidTagUnfilteredList_throwsCommandException() throws Exception {
        Set<Tag> tagSet = new HashSet<>(model.getFilteredPersonList()
                .get(INDEX_SECOND_PERSON.getZeroBased()).getTags());
        String oldTagName = DUMMY_TAG;
        Tag oldTag = new Tag(oldTagName);
        String newTagName = DUMMY_TAG_TWO;
        Tag newTag = new Tag(newTagName);

        TagEditCommand tagEditCommand = prepareCommand(oldTagName, newTagName);

        String expectedMessage = Messages.MESSAGE_UNKNOWN_TAG_NAME;

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.editTag(oldTag, newTag);

        assertCommandFailure(tagEditCommand, model, expectedMessage);
    }

    @Test
    public void equals() {
        Person aliceFriend = new PersonBuilder().withName("Alice").withTags("friends").build();
        Person bobColleague = new PersonBuilder().withName("Bob").withTags("colleagues").build();
        TagEditCommand editFriendsCommand = new TagEditCommand("friends", DUMMY_TAG);
        TagEditCommand editColleaguesCommand = new TagEditCommand("colleagues", DUMMY_TAG);

        // same object -> returns true
        assertTrue(editFriendsCommand.equals(editFriendsCommand));

        // same values -> returns true
        TagEditCommand editFriendsCommandCopy = new TagEditCommand("friends", DUMMY_TAG);
        assertTrue(editFriendsCommand.equals(editFriendsCommandCopy));

        // different types -> returns false
        assertFalse(editColleaguesCommand.equals(1));

        // null -> returns false
        assertFalse(editColleaguesCommand.equals(null));

        // different person -> returns false
        assertFalse(editColleaguesCommand.equals(editFriendsCommand));
    }

    /**
     * Returns a {@code TagEditCommand} with the parameters {@code oldTagName, newTagName}.
     */
    private TagEditCommand prepareCommand(String oldTagName, String newTagName) {
        TagEditCommand tagEditCommand = new TagEditCommand(oldTagName, newTagName);
        tagEditCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return tagEditCommand;
    }
}
```
###### \java\seedu\address\logic\parser\SearchCommandParserTest.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.Test;

import seedu.address.logic.commands.SearchCommand;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

public class SearchCommandParserTest {

    private SearchCommandParser parser = new SearchCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsSearchCommand() {
        // no leading and trailing whitespaces
        SearchCommand expectedSearchCommand =
                new SearchCommand(new PersonContainsKeywordsPredicate(Arrays.asList("Alice", "friends")));
        assertParseSuccess(parser, "Alice friends", expectedSearchCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t friends  \t", expectedSearchCommand);
    }
}
```
###### \java\seedu\address\logic\parser\SocialCommandParserTest.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.SocialCommand;

public class SocialCommandParserTest {

    private SocialCommandParser parser = new SocialCommandParser();


    @Test
    public void parse_validArgs_returnsSocialCommand() {
        assertParseSuccess(parser, "1 ig", prepareCommand(INDEX_FIRST_PERSON, "ig"));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "",
                String.format((MESSAGE_INVALID_COMMAND_FORMAT + SocialCommand.MESSAGE_USAGE), ""));
    }

    /**
     * Returns a {@code SocialCommand} with the parameters {@code index and socialMedia}.
     */
    private SocialCommand prepareCommand(Index index, String socialMedia) {

        SocialCommand socialCommand = new SocialCommand(index, socialMedia);

        return socialCommand;
    }

}
```
