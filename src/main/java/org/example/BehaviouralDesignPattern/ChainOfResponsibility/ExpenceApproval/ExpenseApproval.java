package org.example.BehaviouralDesignPattern.ChainOfResponsibility.ExpenceApproval;

public class ExpenseApproval {

    // 1. Handler interface
    interface Approver {
        void setNext(Approver next);
        void approve(double amount);
    }

    // 2. Abstract base class (optional but useful)
    static abstract class AbstractApprover implements Approver {
        protected Approver next;

        @Override
        public void setNext(Approver next) {
            this.next = next;
        }
    }

    // 3. Concrete handlers
    static class Manager extends AbstractApprover {
        
        @Override
        public void approve(double amount) {
            if (amount <= 1000) {
                System.out.println("Manager approved expense: ₹" + amount);
            } else if (next != null) {
                next.approve(amount);
            }
        }
    }

    static class Director extends AbstractApprover {

        @Override
        public void approve(double amount) {
            if (amount <= 5000) {
                System.out.println("Director approved expense: ₹" + amount);
            } else if (next != null) {
                next.approve(amount);
            }
        }
    }

    static class VicePresident extends AbstractApprover {

        @Override
        public void approve(double amount) {
            if (amount <= 20000) {
                System.out.println("Vice President approved expense: ₹" + amount);
            } else {
                System.out.println("Expense rejected: ₹" + amount);
            }
        }
    }

    public static void main(String[] args) {

        // 4. Build the chain
        Approver manager = new Manager();
        Approver director = new Director();
        Approver vp = new VicePresident();

        manager.setNext(director);
        director.setNext(vp);

        // 5. Client only talks to first handler
        manager.approve(800);
        manager.approve(3000);
        manager.approve(12000);
        manager.approve(50000);
    }
}
