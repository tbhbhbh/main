package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalIndexes.MULTIPLE_INDEX;

import org.junit.Test;

public class ExportCommandTest {

    @Test
    public void equals() {
        ExportCommand exportAllCommand = new ExportCommand();
        ExportCommand exportSomeCommand = new ExportCommand(MULTIPLE_INDEX);

        // same object -> returns true
        assertTrue(exportAllCommand.equals(exportAllCommand));
        assertTrue(exportSomeCommand.equals(exportSomeCommand));

        // same values -> returns true
        ExportCommand exportSomeCommandCopy = new ExportCommand(MULTIPLE_INDEX);
        assertTrue(exportSomeCommand.equals(exportSomeCommandCopy));

        // different types -> returns false
        assertFalse(exportSomeCommand.equals(1));

        //null -> returns false
        assertFalse(exportSomeCommand.equals(null));

        // different commands -> returns false
        assertFalse(exportAllCommand.equals(exportSomeCommand));
    }

}
