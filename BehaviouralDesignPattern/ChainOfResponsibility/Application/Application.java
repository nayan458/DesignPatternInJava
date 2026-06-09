import java.util.ArrayList;
import java.util.List;

// -----------------------------------------------------
// 1. Handler interface
// -----------------------------------------------------
interface ComponentWithContextualHelp {
    void showHelp();
}

// -----------------------------------------------------
// 2. Base Component class
// -----------------------------------------------------
abstract class Component implements ComponentWithContextualHelp {

    protected String tooltipText;

    // Next handler in the chain
    protected Container container;

    @Override
    public void showHelp() {
        if (tooltipText != null) {
            System.out.println("Showing tooltip: " + tooltipText);
        } else if (container != null) {
            container.showHelp();
        }
    }
}

// -----------------------------------------------------
// 3. Container class (can have children)
// -----------------------------------------------------
abstract class Container extends Component {

    protected List<Component> children = new ArrayList<>();

    public void add(Component child) {
        children.add(child);
        child.container = this;
    }
}

// -----------------------------------------------------
// 4. Primitive Component
// -----------------------------------------------------
class Button extends Component {

    private final String label;

    public Button(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

// -----------------------------------------------------
// 5. Complex Component: Panel
// -----------------------------------------------------
class Panel extends Container {

    protected String modalHelpText;

    @Override
    public void showHelp() {
        if (modalHelpText != null) {
            System.out.println("Showing modal help: " + modalHelpText);
        } else {
            super.showHelp();
        }
    }
}

// -----------------------------------------------------
// 6. Complex Component: Dialog
// -----------------------------------------------------
class Dialog extends Container {

    protected String wikiPageURL;
    private final String title;

    public Dialog(String title) {
        this.title = title;
    }

    @Override
    public void showHelp() {
        if (wikiPageURL != null) {
            System.out.println("Opening wiki page: " + wikiPageURL);
        } else {
            super.showHelp();
        }
    }
}

// -----------------------------------------------------
// 7. Client code
// -----------------------------------------------------
public class Application {

    private Component focusedComponent;

    public void createUI() {

        Dialog dialog = new Dialog("Budget Reports");
        dialog.wikiPageURL = "http://help.company.com/budget-reports";

        Panel panel = new Panel();
        panel.modalHelpText = "This panel shows budget statistics.";

        Button ok = new Button("OK");
        ok.tooltipText = "Confirm and save the report.";

        Button cancel = new Button("Cancel");
        // No tooltip → will delegate

        panel.add(ok);
        panel.add(cancel);
        dialog.add(panel);

        // Simulate mouse focus
        focusedComponent = cancel;
    }

    public void onF1KeyPress() {
        focusedComponent.showHelp();
    }

    public static void main(String[] args) {
        Application app = new Application();
        app.createUI();
        app.onF1KeyPress();
    }
}
