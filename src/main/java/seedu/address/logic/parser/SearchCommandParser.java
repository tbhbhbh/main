//@@author tbhbhbh
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.model.person.Birthday.BIRTHDAY_MONTH_REGEX;

import java.util.Arrays;

import seedu.address.commons.util.StringUtil;
import seedu.address.logic.commands.SearchCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.PersonContainsBirthdayPredicate;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new SearchCommand object
 */
public class SearchCommandParser implements Parser<SearchCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SearchCommand
     * and returns an SearchCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format of keywords or birthday month.
     */
    public SearchCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.KEYWORD_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");
        // Single keyword as argument and check that it contains an non-zero integer
        if (nameKeywords.length == 1 && StringUtil.isNonZeroUnsignedInteger(nameKeywords[0])) {
            if (nameKeywords[0].matches(BIRTHDAY_MONTH_REGEX)) {
                return new SearchCommand(new PersonContainsBirthdayPredicate(nameKeywords[0]));
            } else {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.BIRTHDAY_USAGE));
            }
        }
        return new SearchCommand(new PersonContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }
}
