package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EmailCommand;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the EmailCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the EmailCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class EmailCommandParserTest {

    private EmailCommandParser parser = new EmailCommandParser();
    private Index[] indexArray;

    @Test
    public void parse_validArgs_returnsEmailCommand() {
        assertParseSuccess(parser, "1", prepareCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_multipleValidArgs_returnsEmailCommand() {
        assertParseSuccess(parser, "1 2", prepareCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));
    }

    /**
     * Returns a {@code EmailCommand} with the parameter {@code index}.
     */
    private EmailCommand prepareCommand(Index index) {
        indexArray = new Index[1];
        indexArray[0] = index;

        EmailCommand emailCommand = new EmailCommand(indexArray);

        return emailCommand;
    }

    /**
     * Returns a {@code EmailCommand} with more than one parameter {@code index}.
     */
    private EmailCommand prepareCommand(Index firstIndex, Index secondIndex) {
        indexArray = new Index[2];
        indexArray[0] = firstIndex;
        indexArray[1] = secondIndex;

        EmailCommand emailCommand = new EmailCommand(indexArray);

        return emailCommand;
    }
}
