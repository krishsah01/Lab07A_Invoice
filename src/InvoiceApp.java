import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class InvoiceApp extends JFrame {
    private JTextField customerNameTextField;
    private JTextField customerAddressTextField;
    private JTextField productNameTextField;
    private JTextField quantityTextField;
    private JTextField unitPriceTextField;
    private JButton addItemButton;
    private JTextArea invoiceDisplayArea;
    private JButton createInvoiceButton;
    private JButton clearInvoiceButton;

    private Customer customer;
    private List<LineItem> lineItems = new ArrayList<>();
    private Calculator calculator = new Calculator();
    private float totalAmount = 0.0f;

    public InvoiceApp() {
        setTitle("Invoice Creator");
        setSize(700, 600); // Increased height to accommodate address field
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(contentPanel, BorderLayout.CENTER);

        contentPanel.add(createCustomerPanel(), BorderLayout.NORTH);
        contentPanel.add(createItemEntryPanel(), BorderLayout.CENTER);
        contentPanel.add(createInvoiceDisplayPanel(), BorderLayout.SOUTH);
        add(createControlPanel(), BorderLayout.EAST); // Moved control panel to the east

        setVisible(true);
    }

    private JPanel createCustomerPanel() {
        JPanel customerPanel = new JPanel(new GridBagLayout());
        customerPanel.setBorder(new TitledBorder("Customer Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel customerNameLabel = new JLabel("Customer Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        customerPanel.add(customerNameLabel, gbc);

        customerNameTextField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        customerPanel.add(customerNameTextField, gbc);
        gbc.weightx = 0.0;

        JLabel customerAddressLabel = new JLabel("Address:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        customerPanel.add(customerAddressLabel, gbc);

        customerAddressTextField = new JTextField(30);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        customerPanel.add(customerAddressTextField, gbc);
        gbc.weightx = 0.0;

        return customerPanel;
    }

    private JPanel createItemEntryPanel() {
        JPanel itemEntryPanel = new JPanel(new GridBagLayout());
        itemEntryPanel.setBorder(new TitledBorder("Enter Invoice Item"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel productNameLabel = new JLabel("Product Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        itemEntryPanel.add(productNameLabel, gbc);

        productNameTextField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        itemEntryPanel.add(productNameTextField, gbc);
        gbc.weightx = 0.0;

        JLabel quantityLabel = new JLabel("Quantity:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        itemEntryPanel.add(quantityLabel, gbc);

        quantityTextField = new JTextField(5);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        itemEntryPanel.add(quantityTextField, gbc);
        gbc.weightx = 0.0;

        JLabel unitPriceLabel = new JLabel("Unit Price:");
        gbc.gridx = 2;
        gbc.gridy = 1;
        itemEntryPanel.add(unitPriceLabel, gbc);

        unitPriceTextField = new JTextField(10);
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        itemEntryPanel.add(unitPriceTextField, gbc);
        gbc.weightx = 0.0;

        addItemButton = new JButton("Add Item");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        addItemButton.addActionListener(e -> addItem());
        itemEntryPanel.add(addItemButton, gbc);
        gbc.gridwidth = 1;

        return itemEntryPanel;
    }

    private JPanel createInvoiceDisplayPanel() {
        JPanel invoiceDisplayPanel = new JPanel(new BorderLayout());
        invoiceDisplayPanel.setBorder(new TitledBorder("Invoice Details"));
        invoiceDisplayArea = new JTextArea(15, 50);
        invoiceDisplayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(invoiceDisplayArea);
        invoiceDisplayPanel.add(scrollPane, BorderLayout.CENTER);
        return invoiceDisplayPanel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        controlPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        createInvoiceButton = new JButton("Generate Invoice");
        createInvoiceButton.addActionListener(e -> generateInvoice());

        clearInvoiceButton = new JButton("Clear");
        clearInvoiceButton.addActionListener(e -> clearInvoiceForm());

        controlPanel.add(clearInvoiceButton);
        controlPanel.add(createInvoiceButton);

        return controlPanel;
    }

    private void addItem() {
        try {
            String productName = productNameTextField.getText();
            int quantity = Integer.parseInt(quantityTextField.getText());
            double unitPrice = Double.parseDouble(unitPriceTextField.getText());

            if (productName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Product name cannot be empty.");
                return;
            }
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than zero.");
                return;
            }
            if (unitPrice <= 0) {
                JOptionPane.showMessageDialog(this, "Unit price must be greater than zero.");
                return;
            }

            Product product = new Product(productName, unitPrice);
            LineItem lineItem = new LineItem(product, quantity, calculator);
            lineItems.add(lineItem);

            productNameTextField.setText("");
            quantityTextField.setText("");
            unitPriceTextField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity or unit price format.");
        }
    }

    private void generateInvoice() {
        if (lineItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add at least one item to the invoice.");
            return;
        }

        String customerName = customerNameTextField.getText();
        String customerAddress = customerAddressTextField.getText();
        customer = new Customer(customerName, customerAddress);

        totalAmount = calculator.calculateInvoiceTotal(lineItems);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        StringBuilder invoiceText = new StringBuilder();

        invoiceText.append("===================================================\n");
        invoiceText.append("                     INVOICE                       \n");
        invoiceText.append("===================================================\n");
        invoiceText.append("Customer Name: ").append(customer.getName()).append("\n");
        if (!customer.getAddress().isEmpty()) {
            invoiceText.append("Address: ").append(customer.getAddress()).append("\n");
        }
        invoiceText.append("---------------------------------------------------\n");
        invoiceText.append(String.format("%-30s %-10s %-10s %-10s\n", "Product", "Qty", "Price", "Total"));
        invoiceText.append("---------------------------------------------------\n");

        for (LineItem item : lineItems) {
            invoiceText.append(String.format("%-30s %-10d %-10s %-10s\n",
                    item.getProduct().getName(),
                    item.getQuantity(),
                    currencyFormat.format(item.getProduct().getUnitPrice()),
                    currencyFormat.format(item.getTotalPrice())));
        }

        invoiceText.append("---------------------------------------------------\n");
        invoiceText.append(String.format("%-50s %-10s\n", "Total Amount:", currencyFormat.format(totalAmount)));
        invoiceText.append("===================================================\n");

        invoiceDisplayArea.setText(invoiceText.toString());
    }

    private void clearInvoiceForm() {
        customerNameTextField.setText("");
        customerAddressTextField.setText("");
        productNameTextField.setText("");
        quantityTextField.setText("");
        unitPriceTextField.setText("");
        invoiceDisplayArea.setText("");
        lineItems.clear();
        totalAmount = 0.0f;
        customer = null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InvoiceApp::new);
    }
}