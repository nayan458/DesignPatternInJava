public class ExpenseApprovalBadcode {

    static class ExpenseService {

        public void approveExpense(double amount) {

            if (amount <= 1000) {
                System.out.println("Manager approved expense: ₹" + amount);

            } else if (amount <= 5000) {
                System.out.println("Director approved expense: ₹" + amount);

            } else if (amount <= 20000) {
                System.out.println("Vice President approved expense: ₹" + amount);

            } else {
                System.out.println("Expense rejected: ₹" + amount);
            }
        }
    }

    public static void main(String[] args) {
        ExpenseService service = new ExpenseService();

        service.approveExpense(800);
        service.approveExpense(3000);
        service.approveExpense(12000);
        service.approveExpense(50000);
    }
}
