public class LineItem {
    private Product product;
    private int quantity;
    private float totalPrice;
    private Calculator calculator;

    public LineItem(Product product, int quantity, Calculator calculator) {
        this.product = product;
        this.quantity = quantity;
        this.calculator = calculator;
        this.totalPrice = calculator.calculateLineItemTotal(quantity, product.getUnitPrice());
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalPrice = calculator.calculateLineItemTotal(quantity, product.getUnitPrice());
    }

    public float calculateTotal() {
        this.totalPrice = calculator.calculateLineItemTotal(this.quantity, this.product.getUnitPrice());
        return this.totalPrice;
    }
}