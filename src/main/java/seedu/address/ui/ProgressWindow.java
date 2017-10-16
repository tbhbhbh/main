package seedu.address.ui;

import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.FxViewUtil;

public class ProgressWindow extends UiPart<Region> {
        private static final Logger logger = LogsCenter.getLogger(ProgressWindow.class);
        private static final String ICON = "";
        private static final String FXML = "ProgressWindow.fxml";
        private static final String TITLE = "Import Progress";

        private final Stage dialogStage;

        @FXML
        private ProgressBar pbar;

        private Task task;

        public ProgressWindow(Task task) {
            super(FXML);
            this.task = task;
            Scene scene = new Scene(getRoot());
            //Null passed as the parent stage to make it non-modal.
            dialogStage = createDialogStage(TITLE, null, scene);
            pbar = (ProgressBar) scene.lookup("#progressBar");
            FxViewUtil.setStageIcon(dialogStage, ICON);
            bindListeners(task.progressProperty());
        }

    /**
     * Binds the progress bar to a provided Float object
     * so that it will be notified of any changes.
     */
    private void bindListeners(ReadOnlyDoubleProperty progress) {
        pbar.progressProperty().bind(progress);
    }

        /**
         * Shows a progress window.
         * @throws IllegalStateException
         * <ul>
         *     <li>
         *         if this method is called on a thread other than the JavaFX Application Thread.
         *     </li>
         *     <li>
         *         if this method is called during animation or layout processing.
         *     </li>
         *     <li>
         *         if this method is called on the primary stage.
         *     </li>
         *     <li>
         *         if {@code dialogStage} is already showing.
         *     </li>
         * </ul>
         */
        public void show() {
            logger.fine("Showing progress window.");
            dialogStage.show();
        }
}
