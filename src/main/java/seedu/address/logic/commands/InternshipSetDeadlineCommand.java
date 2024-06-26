package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SELECT_TASK;
import static seedu.address.model.InternshipModel.PREDICATE_SHOW_ALL_INTERNSHIPS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.InternshipMessages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.InternshipModel;
import seedu.address.model.internship.Deadline;
import seedu.address.model.internship.Internship;

/**
 * Adds a deadline to a task in an internship, or replaces the deadline if there already is one.
 */
public class InternshipSetDeadlineCommand extends InternshipCommand {

    public static final String COMMAND_WORD = "setdeadline";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Add a deadline to the task of the internship "
            + "identified by the index number used in the displayed internship data. "
            + "Parameters: INDEX_INTERNSHIP (must be a positive integer)\n"
            + PREFIX_SELECT_TASK + " INDEX_TASK (must be a positive integer)\n"
            + PREFIX_DEADLINE + " DEADLINE\n"
            + Deadline.MESSAGE_CONSTRAINTS + "\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_SELECT_TASK + " 1 "
            + PREFIX_DEADLINE + " 20/04/2024";

    public static final String MESSAGE_ADD_DEADLINE_SUCCESS = "Deadline Added: %1$s";
    private final Index internshipIndex;
    private final Index taskIndex;
    private final Deadline deadline;

    /**
     * @param internshipIndex index of the internship in the filtered internship list to edit
     * @param taskIndex index of the task in the selected internship to edit
     * @param deadline deadline of the task to be added
     */
    public InternshipSetDeadlineCommand(Index internshipIndex, Index taskIndex, Deadline deadline) {
        requireNonNull(internshipIndex);
        requireNonNull(taskIndex);
        requireNonNull(deadline);

        this.internshipIndex = internshipIndex;
        this.taskIndex = taskIndex;
        this.deadline = deadline;
    }

    @Override
    public CommandResult execute(InternshipModel model) throws CommandException {
        requireNonNull(model);
        List<Internship> lastShownList = model.getFilteredInternshipList();

        if (internshipIndex.getOneBased() > lastShownList.size()) {
            throw new CommandException(InternshipMessages.MESSAGE_INVALID_INTERNSHIP_DISPLAYED_INDEX);
        }

        Internship internshipToSetDeadline = lastShownList.get(internshipIndex.getZeroBased());

        if (taskIndex.getOneBased() > internshipToSetDeadline.getTaskList().getTaskListSize()) {
            throw new CommandException(InternshipMessages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
        internshipToSetDeadline.getTaskList().getTask(taskIndex.getZeroBased()).setDeadline(deadline);

        // This is necessary to trigger the UI to update the displayed deadline
        model.setInternship(internshipToSetDeadline, internshipToSetDeadline);

        model.updateFilteredInternshipList(PREDICATE_SHOW_ALL_INTERNSHIPS);

        return new CommandResult(String.format(MESSAGE_ADD_DEADLINE_SUCCESS,
                deadline));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof InternshipSetDeadlineCommand)) {
            return false;
        }

        InternshipSetDeadlineCommand otherSetDeadlineCommand = (InternshipSetDeadlineCommand) other;
        return internshipIndex.equals(otherSetDeadlineCommand.internshipIndex)
                && taskIndex.equals(otherSetDeadlineCommand.taskIndex)
                && deadline.equals(otherSetDeadlineCommand.deadline);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("internshipIndex", internshipIndex)
                .add("taskIndex", taskIndex)
                .add("deadline", deadline)
                .toString();
    }
}
