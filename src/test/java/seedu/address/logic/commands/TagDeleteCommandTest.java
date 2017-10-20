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
