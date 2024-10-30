import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// the main class, has GUI components
public class AdventureCalculator {
    private JFrame frame;
    private JTextField distanceField, gasMileageField, gasCostField, hotelCostField, foodCostField, daysField, attractionsField, outputField;
    private JComboBox<String> distanceUnit, gasCostUnit, gasMileageUnit;

    // Constructor for GUI set up (in order based on project instructions and example)
    public AdventureCalculator() {
        frame = new JFrame("Trip Cost Estimator"); // the main window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout()); // to organize the layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // spacing between parts
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Distance Input and grid label
        gbc.gridx = 0; gbc.gridy = 0;
        frame.add(new JLabel("Distance:"), gbc);
        distanceField = new JTextField(10);
        gbc.gridx = 1;
        frame.add(distanceField, gbc);
        distanceUnit = new JComboBox<>(new String[]{"miles", "kilometers"});
        gbc.gridx = 2;
        frame.add(distanceUnit, gbc);

        // Gas Cost Input
        gbc.gridx = 0; gbc.gridy = 1;
        frame.add(new JLabel("Gasoline Cost:"), gbc);
        gasCostField = new JTextField(10);
        gbc.gridx = 1;
        frame.add(gasCostField, gbc);
        gasCostUnit = new JComboBox<>(new String[]{"dollars/gal", "dollars/liter"});
        gbc.gridx = 2;
        frame.add(gasCostUnit, gbc);

        // Gas Mileage Input
        gbc.gridx = 0; gbc.gridy = 2;
        frame.add(new JLabel("Gas Mileage:"), gbc);
        gasMileageField = new JTextField(10);
        gbc.gridx = 1;
        frame.add(gasMileageField, gbc);
        gasMileageUnit = new JComboBox<>(new String[]{"miles/gallon", "kilometers/liter"});
        gbc.gridx = 2;
        frame.add(gasMileageUnit, gbc);

        // Number of Days Input
        gbc.gridx = 0; gbc.gridy = 3;
        frame.add(new JLabel("Number Of Days:"), gbc);
        daysField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        frame.add(daysField, gbc);
        gbc.gridwidth = 1;

        // Hotel Cost Input
        gbc.gridx = 0; gbc.gridy = 4;
        frame.add(new JLabel("Hotel Cost:"), gbc);
        hotelCostField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        frame.add(hotelCostField, gbc);
        gbc.gridwidth = 1;

        // Food Cost Input
        gbc.gridx = 0; gbc.gridy = 5;
        frame.add(new JLabel("Food Cost:"), gbc);
        foodCostField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        frame.add(foodCostField, gbc);
        gbc.gridwidth = 1;

        // Attractions Cost Input
        gbc.gridx = 0; gbc.gridy = 6;
        frame.add(new JLabel("Attractions:"), gbc);
        attractionsField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        frame.add(attractionsField, gbc);
        gbc.gridwidth = 1;

        // Calculate Button
        JButton calculateButton = new JButton("Calculate");
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 3;
        frame.add(calculateButton, gbc);
        gbc.gridwidth = 1;

        // immutable Output Field 
        gbc.gridx = 0; gbc.gridy = 8;
        frame.add(new JLabel("Total Trip Cost"), gbc);
        outputField = new JTextField(10);
        outputField.setEditable(false);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        frame.add(outputField, gbc);

        // for the calculate button
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateTripCost();
            }
        });

        frame.pack();
        frame.setVisible(true);
    }

    private void calculateTripCost() {
        try {
            // Values from fields using parsing 
            double distance = Double.parseDouble(distanceField.getText());
            double gasMileage = Double.parseDouble(gasMileageField.getText());
            double gasCost = Double.parseDouble(gasCostField.getText());
            double hotelCost = Double.parseDouble(hotelCostField.getText());
            double foodCost = Double.parseDouble(foodCostField.getText());
            int days = Integer.parseInt(daysField.getText());
            double attractionsCost = Double.parseDouble(attractionsField.getText());

            // conversion for the units
            if (distanceUnit.getSelectedItem().equals("kilometers")) {
                distance *= 0.621371; // Convert to miles
            }
            if (gasCostUnit.getSelectedItem().equals("dollars/liter")) {
                gasCost *= 3.78541; // Convert to dollars per gallon
            }
            if (gasMileageUnit.getSelectedItem().equals("kilometers/liter")) {
                gasMileage *= 0.621371; // Convert to miles per gallon
            }

            // New object to calculate the total cost (trippost)
            TripPost trip = new TripPost(distance, gasMileage, gasCost, hotelCost, foodCost, days, attractionsCost);
            double totalCost = trip.calculateTotalCost();

            // Display the result.. up to two decimal places or show error message
            outputField.setText(String.format("$%.2f", totalCost));
        } catch (NumberFormatException ex) {
            outputField.setText("Please enter valid numbers.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdventureCalculator::new); // makes sure the GUI is created..prevent issues
    }
}

// TripPost class for cost calculations
class TripPost {
    private final double distance;
    private final double gasMileage;
    private final double gasCost;
    private final double hotelCost;
    private final double foodCost;
    private final int days;
    private final double attractionsCost;

    public TripPost(double distance, double gasMileage, double gasCost, double hotelCost, double foodCost, int days, double attractionsCost) {
        this.distance = distance;
        this.gasMileage = gasMileage;
        this.gasCost = gasCost;
        this.hotelCost = hotelCost;
        this.foodCost = foodCost;
        this.days = days;
        this.attractionsCost = attractionsCost;
    }

    // calculate the total cost and return that value
    public double calculateTotalCost() {
        double gasolineCost = (distance / gasMileage) * gasCost;
        double totalTripCost = gasolineCost + ((hotelCost + foodCost) * days) + attractionsCost;
        return totalTripCost;
    }
}
