package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEDICAL_REPORT;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.medicalreport.MedicalReport;
import seedu.address.model.person.Person;

/**
 * Adds a medical report to a person.
 */
public class AddMedicalReportCommand extends Command {

    public static final String COMMAND_WORD = "addreport";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person's medical report by the index number"
            + " used in the list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_MEDICAL_REPORT + "[MEDICAL REPORT]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_MEDICAL_REPORT + "Title: Asthma, Date: 01012018,"
            + " Actions: prescribed XXX medicine, next appointment on 02022018. ";

    public static final String MESSAGE_ADD_REPORT_SUCCESS = "Added medical report to Person: %1$s";
    public static final String MESSAGE_DELETE_REPORT_SUCCESS = "Removed medical report from Person: %1$s";

    private final Index index;
    private final MedicalReport report;

    /**
     * Creates an AddMedicalReportCommand to add the specified {@code index and report}
     * @param index of the person in the filtered person list to add medical report
     * @param report details of medical report
     */
    public AddMedicalReportCommand(Index index, MedicalReport report) {
        requireNonNull(index);
        requireNonNull(report);
        this.index = index;
        this.report = report;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = new Person(personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), report, personToEdit.getTags());

        model.updatePerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.commitAddressBook();

        return new CommandResult(generateSuccessMessage(editedPerson));
    }

    /**
     * Generates a command execution success message based on whether the report is added to
     * {@code personToEdit}.

    private String generateSuccessMessage(Person personToEdit) {
        String message = (report.title.isFull() && report.date.isFull() && report.information.isFull())
                ? MESSAGE_ADD_REPORT_SUCCESS : MESSAGE_ADD_REPORT_FAILURE;
        return String.format(message, personToEdit);
    }*/

    private String generateSuccessMessage(Person personToEdit) {
        String message = (!report.value.isEmpty()) ? MESSAGE_ADD_REPORT_SUCCESS : MESSAGE_DELETE_REPORT_SUCCESS;
        return String.format(message, personToEdit);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof AddMedicalReportCommand)) {
            return false;
        }
        // state check
        AddMedicalReportCommand e = (AddMedicalReportCommand) other;
        return index.equals(e.index)
                && report.equals(e.report);
    }
}