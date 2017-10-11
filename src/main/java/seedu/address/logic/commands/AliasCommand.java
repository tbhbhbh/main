package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;

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

    public static final String MESSAGE_SUCCESS = "New alias added: ";

    public static final String MESSAGE_INVALID_COMMAND = "Command entered is invalid.";

    /**
     * Creates an AliasCommand to add the specified alias
     */
    public AliasCommand(String alias, String actualCommand) {
        this.alias = alias;
        this.actualCommand = actualCommand;
    }

    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {
        if (AddressBookParser.checkValidCommand(actualCommand)) {
            model.addAlias(alias, actualCommand);
            StringBuilder sb = new StringBuilder();
            sb.append(MESSAGE_SUCCESS);
            sb.append(alias);
            sb.append(" for ");
            sb.append(actualCommand);
            return new CommandResult(sb.toString());
        }
        else throw new CommandException(MESSAGE_INVALID_COMMAND);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AliasCommand // instanceof handles nulls
                && alias.equals(((AliasCommand) other).alias)
                && actualCommand.equals(((AliasCommand) other).actualCommand));
    }
}
