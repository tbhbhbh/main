package seedu.address.logic.commands;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.commands.exceptions.CommandException;

public class AliasCommandTest {

    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void executeAliasCommand() {
        thrown.expect(CommandException.class);
    }
}
