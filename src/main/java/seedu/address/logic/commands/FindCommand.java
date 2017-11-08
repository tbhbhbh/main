package seedu.address.logic.commands;

import java.util.function.Predicate;

import seedu.address.model.person.PersonContainsBirthdayPredicate;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindCommand extends Command {
    //@@author tbhbhbh
    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": finds all persons whose information contain "
            + "any of the specified keywords and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "(HitMeUp only supports finding 1 keyword for name. Finding multiple tags is supported)\n"
            + "Example: " + COMMAND_WORD + " alice colleagues";

    private final Predicate<ReadOnlyPerson> searchPredicate;

    public FindCommand(PersonContainsKeywordsPredicate searchPredicate) {
        this.searchPredicate = searchPredicate;
    }
    //@@author conantteo
    public FindCommand(PersonContainsBirthdayPredicate searchPredicate) {
        this.searchPredicate = searchPredicate;
    }
    //@@author tbhbhbh
    @Override
    public CommandResult execute() {
        model.updateFilteredPersonList(searchPredicate);
        return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindCommand // instanceof handles nulls
                && this.searchPredicate.equals(((FindCommand) other).searchPredicate)); // state check
    }
}
