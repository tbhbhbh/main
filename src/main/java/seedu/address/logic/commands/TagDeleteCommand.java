package seedu.address.logic.commands;

import java.util.List;
import java.util.Observable;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.exceptions.TagNotFoundException;

import javax.swing.text.html.HTML;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.tag.Tag.MESSAGE_TAG_CONSTRAINTS;
import static seedu.address.model.tag.Tag.isValidTagName;

/**
 * Deletes a tag identified using its tag name from the address book.
 */
public class TagDeleteCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "tag delete";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the tag identified by the tag name in the address book.\n"
            + "Parameters: TAGNAME\n"
            + "Example: " + COMMAND_WORD + " friends";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted tag: %1$s";

    private final String tagName;
    private final Tag tagToDelete;

    public TagDeleteCommand(String tagName) throws IllegalValueException {
        requireNonNull(tagName);
        String trimmedName = tagName.trim();
        if (!isValidTagName(trimmedName)) {
            throw new IllegalValueException(MESSAGE_TAG_CONSTRAINTS);
        }

        this.tagName = tagName;
        this.tagToDelete = new Tag(tagName);
    }


    @Override
    public CommandResult executeUndoableCommand() throws CommandException {

        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();
        List<Tag> tagList = model.getAddressBook().getTagList();

        if (!tagList.contains(tagToDelete)) {
            throw new CommandException(Messages.MESSAGE_INVALID_TAG_NAME);
        }

        try {
            model.deleteTag(tagToDelete);
        } catch (PersonNotFoundException pnfe) {
            assert false : "The target person cannot be missing";
        } catch (DuplicatePersonException dpe) {
            throw new CommandException(
        } catch (TagNotFoundException tnfe) {
            throw

        }

        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, personToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TagDeleteCommand // instanceof handles nulls
                && this.tagName.equals(((TagDeleteCommand) other).tagName)); // state check
    }
}
