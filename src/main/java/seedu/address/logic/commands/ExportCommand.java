package seedu.address.logic.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.ExportRequestEvent;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.IndexArrayUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.Vcard;

/**
 * Exports all contacts or specified person identified using it's last displayed index from the address book.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Export all contacts or specified person identified by the index number "
            + "used in the last person listing into a VCard file.\n"
            + "Parameters: all or INDEX [INDEX]... (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 2" + " or " + COMMAND_WORD + " all";

    public static final String MESSAGE_EMAIL_PERSON_SUCCESS = "Export Person: %1$s\n"
            + " Please close the app before moving the vcf file to another location.";

    public static final String DEFAULT_FILE_DIR = "./data/";
    public static final String DEFAULT_FILE_NAME = "contacts.vcf";

    private final Index[] targetIndices;

    public ExportCommand() {
        this.targetIndices = new Index[0];
    }

    public ExportCommand(Index[] targetIndexes) {
        this.targetIndices = targetIndexes;
    }

    @Override
    public CommandResult execute() throws CommandException {
        List<ReadOnlyPerson> fullList = model.getAddressBook().getPersonList();
        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();
        StringBuilder persons = new StringBuilder();
        ArrayList<Vcard> listOfvCards = new ArrayList<>();
        if (targetIndices.length > 0) {
            for (Index targetIndex : targetIndices) {
                if (targetIndex.getZeroBased() >= lastShownList.size()) {
                    throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX + ": "
                            + targetIndex.getOneBased());
                }
                ReadOnlyPerson personToExport = lastShownList.get(targetIndex.getZeroBased());
                persons.append(", ");
                persons.append(personToExport.getName().toString());
                Vcard vCard = new Vcard(personToExport);
                listOfvCards.add(vCard);
            }
        } else {
            for (ReadOnlyPerson person : fullList) {
                persons.append(", ");
                persons.append(person.getName().toString());
                Vcard vCard = new Vcard(person);
                listOfvCards.add(vCard);
            }
        }
        String allPersons = persons.toString().trim().substring(2, persons.length());

        //Create a new VCard format file to store all the VCard information.
        File file = new File(DEFAULT_FILE_DIR, DEFAULT_FILE_NAME);
        try {
            FileUtil.createIfMissing(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Write information from a list of VCards into the VCard format file.
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            assert false :  "File is always created when exporting";
        }
        for (Vcard vCard : listOfvCards) {
            try {
                fos.write(vCard.getCardDetails().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        EventsCenter.getInstance().post(new ExportRequestEvent());
        return new CommandResult(String.format(MESSAGE_EMAIL_PERSON_SUCCESS, allPersons));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ExportCommand // instanceof handles nulls
                && IndexArrayUtil.compareIndexArrays(this.targetIndices, (
                (ExportCommand) other).targetIndices)); // state check
    }
}
