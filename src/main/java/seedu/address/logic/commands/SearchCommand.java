package seedu.address.logic.commands;

import java.util.function.Predicate;

import seedu.address.model.person.PersonContainsBirthdayPredicate;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class SearchCommand extends Command {
    //@@author tbhbhbh
    public static final String COMMAND_WORD = "search";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Searches for all persons whose information contain "
            + "any of the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice colleagues";

    private final Predicate<ReadOnlyPerson> searchPredicate;

    public SearchCommand(PersonContainsKeywordsPredicate searchPredicate) {
        this.searchPredicate = searchPredicate;
    }
    //@@author conantteo
    public SearchCommand(PersonContainsBirthdayPredicate searchPredicate) {
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
                || (other instanceof SearchCommand // instanceof handles nulls
                && this.searchPredicate.equals(((SearchCommand) other).searchPredicate)); // state check
    }
}
