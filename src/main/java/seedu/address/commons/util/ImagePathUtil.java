package seedu.address.commons.util;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DP;

import seedu.address.ui.CommandBox;

/**
 * Appends selected image path for inputs
 */

public class ImagePathUtil {

    public static String requireFileChooser(String arguments, CommandBox commandBox) {
        if (arguments.contains(PREFIX_DP.getPrefix())) {
            arguments = appendDpPath(arguments, commandBox);
        }
        return arguments;
    }

    private static String appendDpPath(String arguments, CommandBox commandBox) {
        int prefixIndex = findPrefixPosition(arguments, PREFIX_DP.getPrefix(), 0);
        if (prefixIndex != -1) {
            StringBuilder sb = new StringBuilder((CharSequence) arguments);
            String selectedPath = commandBox.getDisplayPicPath();
            sb.insert(prefixIndex + PREFIX_DP.getPrefix().length(), selectedPath);
            arguments = sb.toString();
        }
        return arguments;
    }

    private static int findPrefixPosition(String argsString, String prefix, int fromIndex) {
        int prefixIndex = argsString.indexOf(" " + prefix, fromIndex);
        return prefixIndex == -1 ? -1
                : prefixIndex + 1; // +1 as offset for whitespace
    }


}