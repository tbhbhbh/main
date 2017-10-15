package seedu.address.logic.commands;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.commands.exceptions.CommandException;

public class ImportCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void executeUndoableCommand_throwsException() throws Exception {
        thrown.expect(CommandException.class);
        new ImportCommand("Google").executeUndoableCommand();
    }
}
