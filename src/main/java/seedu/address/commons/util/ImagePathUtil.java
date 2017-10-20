package seedu.address.commons.util;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DP;
import static seedu.address.ui.CommandBox.DEFAULT_DISPLAY_PIC;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.ui.CommandBox;

/**
 * Appends selected image path for inputs
 */

public class ImagePathUtil {

    private static final String ERROR_MESSAGE = "Please make sure that your input includes either dp/Y or dp/N";

    private static final int PREFIX_LENGTH = PREFIX_DP.getPrefix().length();

    public static String setPath(String arguments, CommandBox commandBox) throws ParseException {
        System.out.println("Before append: " + arguments);
        try {
            int prefixIndex = findPrefixPosition(arguments, PREFIX_DP.getPrefix(), 0);
            StringBuilder sb = new StringBuilder(arguments);
            String choice = sb.substring(prefixIndex + PREFIX_LENGTH, prefixIndex + PREFIX_LENGTH + 1);
            if (requireFileChooser(choice)) {
                String selectedPath = commandBox.getDisplayPicPath();
                sb.replace(prefixIndex, prefixIndex + PREFIX_LENGTH + 1, PREFIX_DP.getPrefix() + selectedPath + " ");
                arguments = sb.toString();
            }
            else {
                sb.replace(prefixIndex, prefixIndex + PREFIX_LENGTH + 1, PREFIX_DP.getPrefix() + DEFAULT_DISPLAY_PIC + " ");
                arguments = sb.toString();
            }
            return arguments;
        } catch (StringIndexOutOfBoundsException sioe) {
            throw new ParseException(ERROR_MESSAGE, sioe);
        }
    }



    private static boolean requireFileChooser(String input) {
        String trimmedInput = input.trim();
        if (trimmedInput.equalsIgnoreCase("y")) {
            return true;
        }
        return false;
    }

    private static int findPrefixPosition(String argsString, String prefix, int fromIndex) {
        int prefixIndex = argsString.indexOf(" " + prefix, fromIndex);
        return prefixIndex == -1 ? -1
                : prefixIndex + 1; // +1 as offset for whitespace
    }

}