package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;

public class AliasCommand extends UndoableCommand{

    public static final String COMMAND_WORD = "alias";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an alias for a command. "
            + "Parameters: "
            + "USER DEFINED ALIAS "
            + "COMMAND "
            + "Example: " + COMMAND_WORD + " "
            + "a "
            + "add ";

    private final String alias;
    private final String actualCommand;

    public AliasCommand(String alias, String actualCommand) {
        this.alias = alias;
        this.actualCommand = actualCommand;
    }

    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {
        throw new CommandException(alias + " " + actualCommand);
    }
}
