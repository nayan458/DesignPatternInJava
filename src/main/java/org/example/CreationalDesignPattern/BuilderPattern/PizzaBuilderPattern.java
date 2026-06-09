import java.util.ArrayList;
import java.util.List;

class Pizza {
    private String dough;
    private String sauce;
    private String cheese;
    private List<String> toppings;
    private String crust;
    private String size;

    public void setDough(String dough) { this.dough = dough; };
    public void setSauce(String sauce) { this.sauce = sauce; };
    public void setCheese(String cheese) { this.cheese = cheese; };
    public void setToppings(List<String> toppings) { this.toppings = toppings; };
    public void setCrust(String crust) { this.crust = crust; };
    public void setSize(String size) { this.size = size; };

    @Override
    public String toString() {
        return "Pizza [" + 
            "dough" + this.dough +
            ",sauce" + this.sauce +
            ",cheese" + this.cheese +
            ",toppings" + this.toppings +
            ",crust" + this.crust +
            ",size" + this.size +
            "]";
    }
}

interface PizzaBuilder {
    void buildDough();
    void buildSauce();
    void buildCheese();
    void buildToppings();
    void buildCrust();
    void buildSize();

    Pizza getPizza();
}

class MargheritaPizzaBuilder implements PizzaBuilder {
    private final Pizza pizza;

    public MargheritaPizzaBuilder() { this.pizza = new Pizza();}
    @Override
    public void buildDough() { this.pizza.setDough("maida"); }
    @Override
    public void buildSauce() { this.pizza.setSauce("tomato"); }
    @Override
    public void buildCheese() { this.pizza.setCheese("mozzarella"); }
    @Override
    public void buildToppings() { this.pizza.setToppings(new ArrayList<>());}
    @Override
    public void buildCrust() { this.pizza.setCrust("thin"); }
    @Override
    public void buildSize() { this.pizza.setSize("medium"); }

    @Override
    public Pizza getPizza() { return this.pizza; };
}

class PepperoniPizzaBuilder implements PizzaBuilder {
    private final Pizza pizza;

    public PepperoniPizzaBuilder() { this.pizza = new Pizza();}
    @Override
    public void buildDough() { this.pizza.setDough("atta"); }
    @Override
    public void buildSauce() { this.pizza.setSauce("spicy tomato"); }
    @Override
    public void buildCheese() { this.pizza.setCheese("mozzarella"); }
    @Override
    public void buildToppings() { this.pizza.setToppings(new ArrayList<>(List.of("Pepperoni")));}
    @Override
    public void buildCrust() { this.pizza.setCrust("thick"); }
    @Override
    public void buildSize() { this.pizza.setSize("large"); }

    @Override
    public Pizza getPizza() { return this.pizza; };
}

class PizzaDirector {
    private final PizzaBuilder builder;

    public PizzaDirector(PizzaBuilder builder) { this.builder = builder; }

    public Pizza buildPizza() {
        builder.buildDough();
        builder.buildSauce();
        builder.buildToppings();
        builder.buildCheese();
        builder.buildCrust();
        builder.buildSize();

        return builder.getPizza();
    }
}



public class PizzaBuilderPattern {
    public static void main(String[] args) {
        PizzaBuilder margheritaPizzaBuilder = new MargheritaPizzaBuilder();
        PizzaDirector margheritaPizzaDirector = new PizzaDirector(margheritaPizzaBuilder);
        Pizza margheritaPizza = margheritaPizzaDirector.buildPizza();
        System.out.println(margheritaPizza);
        
        PizzaBuilder pepperoniPizzaBuilder = new PepperoniPizzaBuilder();
        PizzaDirector pepperoniPizzaDirector = new PizzaDirector(pepperoniPizzaBuilder);
        Pizza pepperoniPizza = pepperoniPizzaDirector.buildPizza();
        System.out.println(pepperoniPizza);
    }
}