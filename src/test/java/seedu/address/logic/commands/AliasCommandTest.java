package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class AliasCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void executeAliasCommand() throws CommandException {
        assertCommandFailure(prepareCommand("a", "add"), model, "a add");
    }

    @Test
    public void equals() {
        AliasCommand aliasAddCommand = new AliasCommand("a", "add");
        AliasCommand aliasFindCommand = new AliasCommand("f", "find");

        // same object -> returns true
        assertTrue(aliasAddCommand.equals(aliasAddCommand));

        // same values -> returns true;
        AliasCommand aliasAddCommandCopy = new AliasCommand("a", "add");
        assertTrue(aliasAddCommand.equals(aliasAddCommandCopy));

        // different types -> returns false
        assertFalse(aliasAddCommand.equals(1));

        // null -> returns false
        assertFalse(aliasAddCommand.equals(null));

        // different commands -> returns false
        assertFalse(aliasAddCommand.equals(aliasFindCommand));
    }

    /**
     * Returns an {@code EditCommand} with parameters {@code index} and {@code descriptor}
     */
    private AliasCommand prepareCommand(String alias, String command) {
        AliasCommand aliasCommand = new AliasCommand(alias, command);
        aliasCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return aliasCommand;
    }
}
