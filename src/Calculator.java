import java.util.List;

public class Calculator {
    public float calculateLineItemTotal(int quantity, double unitPrice) {
        return (float) (quantity * unitPrice);
    }

    public float calculateInvoiceTotal(List<LineItem> lineItems) {
        float total = 0;
        for (LineItem item : lineItems) {
            total += item.getTotalPrice();
        }
        return total;
    }
}