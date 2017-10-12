package seedu.address.logic.commands;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Email;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * Email a person identified using it's last displayed index from the address book.
 */
public class EmailCommand extends Command {

    public static final String COMMAND_WORD = "email";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Email a person identified by the index number used in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_EMAIL_PERSON_SUCCESS = "Email Person: %1$s";

    private final Index targetIndex;

    public EmailCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {

        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        ReadOnlyPerson personToEmail = lastShownList.get(targetIndex.getZeroBased());
        Email emailAddress = personToEmail.getEmail();
        String url = "mailTo:" + emailAddress.toString();

        try {
            URI mailTo = new URI(url);
            if(Desktop.isDesktopSupported()) {
                Desktop userDesktop = Desktop.getDesktop();
                userDesktop.mail(mailTo);
            } else {
                throw new CommandException("Desktop is not supported");
            }
        } catch (URISyntaxException e) {
            assert false: "Email address of a person cannot be missing";
        } catch (IOException e) {
            throw new CommandException("User default mail client is not found or failed to launch");
        }

        return new CommandResult(String.format(MESSAGE_EMAIL_PERSON_SUCCESS, personToEmail));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EmailCommand // instanceof handles nulls
                && this.targetIndex.equals(((EmailCommand) other).targetIndex)); // state check
    }
}
