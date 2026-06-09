import java.util.ArrayList;
import java.util.List;

public class ApplicationBad {

    // -------------------------------
    // Base component (anemic model)
    // -------------------------------
    static abstract class Component {
        String tooltipText;
    }

    static class Button extends Component {
        String label;

        Button(String label) {
            this.label = label;
        }
    }

    static class Panel extends Component {
        String modalHelpText;
        List<Component> children = new ArrayList<>();
    }

    static class Dialog extends Component {
        String wikiPageURL;
        List<Component> children = new ArrayList<>();
    }

    // -------------------------------
    // GOD METHOD: knows everything
    // -------------------------------
    static class HelpService {

        static void showHelp(Component component, Panel parentPanel, Dialog parentDialog) {

            // Button logic
            if (component instanceof Button) {
                Button btn = (Button) component;

                if (btn.tooltipText != null) {
                    System.out.println("Showing tooltip: " + btn.tooltipText);
                    return;
                }
            }

            // Panel logic
            if (parentPanel != null && parentPanel.modalHelpText != null) {
                System.out.println("Showing modal help: " + parentPanel.modalHelpText);
                return;
            }

            // Dialog logic
            if (parentDialog != null && parentDialog.wikiPageURL != null) {
                System.out.println("Opening wiki page: " + parentDialog.wikiPageURL);
                return;
            }

            System.out.println("No help available.");
        }
    }

    // -------------------------------
    // Client code
    // -------------------------------
    public static void main(String[] args) {

        Dialog dialog = new Dialog();
        dialog.wikiPageURL = "http://help.company.com/budget-reports";

        Panel panel = new Panel();
        panel.modalHelpText = "This panel shows budget statistics.";

        Button ok = new Button("OK");
        ok.tooltipText = "Confirm and save the report.";

        Button cancel = new Button("Cancel");

        panel.children.add(ok);
        panel.children.add(cancel);
        dialog.children.add(panel);

        // ❌ Client must know hierarchy manually
        HelpService.showHelp(cancel, panel, dialog);
    }
}
