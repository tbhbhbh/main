package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.TagDeleteCommand;
import seedu.address.logic.commands.TagEditCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new TagEditCommand object
 */
public class TagEditCommandParser {
    /**
     * Parses the given {@code String} of arguments in the context of the TagDeleteCommand
     * and returns a TagEditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public TagEditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String[] stringArr = args.split(" ");

        String oldTag = stringArr[1].trim();
        String newTag = stringArr[2].trim();
        System.out.println("old: " + oldTag + " " + "new: " + newTag);
        if (oldTag.isEmpty() || newTag.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagDeleteCommand.MESSAGE_USAGE));
        }

        return new TagEditCommand(oldTag, newTag);
    }
}
