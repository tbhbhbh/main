package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.exceptions.TagNotFoundException;

public class TagEditCommand {
    public static final String MESSAGE_DELETE_TAG_SUCCESS = "Deleted tag: %1$s";
    public static final String COMMAND_WORD = "tagdelete";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the tag identified by the tag name in the address book.\n"
            + "Parameters: TAGNAME\n"
            + "Example: " + COMMAND_WORD + " friends";

    private final String tagName;
    private Tag tagToDelete;

    public TagEditCommand(String newTagName) {
        requireNonNull(tagName);
        this.newTagName = tagName.trim();

    }


    @Override
    public CommandResult executeUndoableCommand() throws CommandException {

        List<Tag> tagList = model.getAddressBook().getTagList();

        try {
            tagToDelete = new Tag(tagName);
            if (!tagList.contains(tagToDelete)) {
                throw new CommandException(Messages.MESSAGE_INVALID_TAG_NAME);
            }
            model.deleteTag(tagToDelete);
        } catch (IllegalValueException ive) {
            assert false : "The target tag is invalid";
        } catch (PersonNotFoundException pnfe) {
            assert false : "The target person cannot be missing";
        } catch (TagNotFoundException tnfe) {
            assert false : "The target tag cannot be missing";

        }

        return new CommandResult(String.format(MESSAGE_DELETE_TAG_SUCCESS, tagToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TagDeleteCommand // instanceof handles nulls
                && this.tagName.equals(((TagDeleteCommand) other).tagName)); // state check
    }
}
