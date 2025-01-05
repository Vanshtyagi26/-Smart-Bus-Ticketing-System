import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class BusReservationAndTicketingSystemGUI {

    private static JFrame frame;
    private static CardLayout cardLayout;
    private static JPanel mainPanel;
    private static JTextField usernameField, passwordField, passengerNameField;
    private static JComboBox<String> destinationComboBox;
    private static JSpinner passengerCountSpinner, discountSpinner;
    private static JLabel seatAvailableLabel, totalFareLabel, messageLabel;
    private static int[] availableSeats = {0, 30, 30, 30, 30, 30}; // initial seats for destinations
    private static double[] fares = {0, 10, 25, 40, 60, 80}; // fares for destinations
    private static String[] destinations = {"Noida Sector 62", "Noida Sector 51", "Botanical Garden", "Noida Electronic City", "Delhi "};

    // Arrays to store ticket information
    private static String[][] ticketS = new String[100][5]; // Passenger name, destination, status, payment method, date & time
    private static int[][] ticketI = new int[100][2]; // Total passengers, discounted passengers
    private static double[][] ticketD = new double[100][3]; // Fare per passenger, discounted fare, total fare
    private static int ticketCount = 0; // Counter to track the number of tickets purchased

    // In-memory storage for user credentials
    private static HashMap<String, String> userCredentials = new HashMap<>();

    public static void main(String[] args) {
        // Set up the main frame
        frame = new JFrame("Bus Ticketing System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add login panel
        mainPanel.add(createLoginPanel(), "Login");

        // Add main menu panel
        mainPanel.add(createMenuPanel(), "MainMenu");

        // Add signup panel
        mainPanel.add(createSignupPanel(), "Signup");

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel(new GridLayout(7, 2, 10, 10)); // Adjusted to 7 rows to accommodate company name
        JLabel companyLabel = new JLabel("Jet Stream Bus Service", JLabel.CENTER);
        companyLabel.setFont(new Font("Arial", Font.BOLD, 24));
        JLabel userLabel = new JLabel("Username: ");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password: ");
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Sign Up");

        loginButton.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(((JPasswordField) passwordField).getPassword()); // Get password from JPasswordField
            if (userCredentials.containsKey(user) && userCredentials.get(user).equals(pass)) {
                cardLayout.show(mainPanel, "MainMenu");
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password!");
            }
        });

        signupButton.addActionListener(e -> cardLayout.show(mainPanel, "Signup"));
	loginPanel.add(companyLabel); // Add company name label
        loginPanel.add(new JLabel()); // Empty space

        loginPanel.add(userLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(signupButton);
        loginPanel.add(loginButton);

        return loginPanel;
    }

    private static JPanel createSignupPanel() {
        JPanel signupPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JLabel newUserLabel = new JLabel("New Username: ");
        JTextField newUsernameField = new JTextField();
        JLabel newPasswordLabel = new JLabel("New Password: ");
        JPasswordField newPasswordField = new JPasswordField();
        JButton signupSubmitButton = new JButton("Sign Up");
        JButton backButton = new JButton("Back");

        signupSubmitButton.addActionListener(e -> {
            String newUser = newUsernameField.getText();
            String newPass = new String(((JPasswordField) newPasswordField).getPassword());

            if (userCredentials.containsKey(newUser)) {
                JOptionPane.showMessageDialog(frame, "Username already exists!");
            } else if (newUser.isEmpty() || newPass.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Username or Password cannot be empty!");
            } else {
                userCredentials.put(newUser, newPass);
                JOptionPane.showMessageDialog(frame, "Sign up successful! You can now log in.");
                cardLayout.show(mainPanel, "Login");
            }
        });

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Login"));

        signupPanel.add(newUserLabel);
        signupPanel.add(newUsernameField);
        signupPanel.add(newPasswordLabel);
        signupPanel.add(newPasswordField);
        signupPanel.add(backButton);
        signupPanel.add(signupSubmitButton);

        return signupPanel;
    }

    private static JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        JButton viewDestButton = new JButton("View Destinations");
        JButton buyTicketButton = new JButton("Buy Ticket");
        JButton viewTicketsButton = new JButton("View Purchased Tickets");
        JButton exitButton = new JButton("Exit");

        viewDestButton.addActionListener(e -> displayDestinations());
        buyTicketButton.addActionListener(e -> displayTicketPurchaseForm());
        viewTicketsButton.addActionListener(e -> viewTickets());
        exitButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Thanks for visiting", "Exit", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
});

        menuPanel.add(viewDestButton);
        menuPanel.add(buyTicketButton);
        menuPanel.add(viewTicketsButton);
        menuPanel.add(exitButton);

        return menuPanel;
    }

    private static void displayDestinations() {
        StringBuilder destinationsDisplay = new StringBuilder("**   DESTINATION   |  FARE  |  SEAT  **\n");
        for (int i = 1; i <= 5; i++) {
            destinationsDisplay.append(i).append(") ").append(destinations[i - 1])
                    .append("   |   Rs").append(fares[i]).append("   |   ").append(availableSeats[i]).append(" seats\n");
        }
        JOptionPane.showMessageDialog(frame, destinationsDisplay.toString(), "Available Destinations", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void displayTicketPurchaseForm() {
        // Create the panel with proper layout
        JPanel ticketPanel = new JPanel(new GridLayout(13, 2, 10, 10)); // Adjusted to 13 rows for better layout

        // Passenger Name Section (now on a separate row)
        JLabel passengerNameLabel = new JLabel("Passenger's Name: ");
        passengerNameField = new JTextField();

        ticketPanel.add(passengerNameLabel);
        ticketPanel.add(passengerNameField);

        // Destination Selection Section (now on a separate row)
        JLabel destinationLabel = new JLabel("Destination: ");
        destinationComboBox = new JComboBox<>(destinations);

        ticketPanel.add(destinationLabel);
        ticketPanel.add(destinationComboBox);

        // Available Seats Section (updates based on selected destination)
        JLabel seatLabel = new JLabel("Available Seats: ");
        seatAvailableLabel = new JLabel("" + availableSeats[destinationComboBox.getSelectedIndex() + 1]);

        ticketPanel.add(seatLabel);
        ticketPanel.add(seatAvailableLabel);

        // Number of Passengers Section
        JLabel passengersLabel = new JLabel("How Many Passengers: ");
        passengerCountSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));

        ticketPanel.add(passengersLabel);
        ticketPanel.add(passengerCountSpinner);

        // Discounted Passengers Section
        JLabel discountLabel = new JLabel("Passengers with Discount: ");
        discountSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));

        ticketPanel.add(discountLabel);
        ticketPanel.add(discountSpinner);

        // Discount Information Section
        JLabel discountInfoLabel = new JLabel("** Students and Senior Citizens get a 20% discount! **");
        ticketPanel.add(new JLabel()); // Empty space for alignment
        ticketPanel.add(discountInfoLabel);

        // Payment Method Section
        JLabel paymentLabel = new JLabel("Payment Method: ");
        String[] paymentMethods = {"Credit Card", "Debit Card", "UPI", "Cash"};
        JComboBox<String> paymentComboBox = new JComboBox<>(paymentMethods);

        ticketPanel.add(paymentLabel);
        ticketPanel.add(paymentComboBox);

        // Total Fare and Message Section
        totalFareLabel = new JLabel("Total Fare: Rs 0");
        messageLabel = new JLabel();

        ticketPanel.add(new JLabel()); // Empty space for alignment
        ticketPanel.add(totalFareLabel);

        ticketPanel.add(new JLabel()); // Empty space for alignment
        ticketPanel.add(messageLabel);

        // Calculate Fare Button Section
        JButton calculateFareButton = new JButton("Calculate Fare");
        ticketPanel.add(new JLabel()); // Empty space for alignment
        ticketPanel.add(calculateFareButton);

        // Pay Now Button Section
        JButton payNowButton = new JButton("Pay Now");
        ticketPanel.add(new JLabel()); // Empty space for alignment
        ticketPanel.add(payNowButton);

        // Action for updating seat availability based on destination selection
        destinationComboBox.addActionListener(e -> {
            int selectedIndex = destinationComboBox.getSelectedIndex() + 1;
            seatAvailableLabel.setText(String.valueOf(availableSeats[selectedIndex]));
        });

        calculateFareButton.addActionListener(e -> calculateFare());

        // Action to handle "Pay Now" button
        payNowButton.addActionListener(e -> {
            String paymentMethod = paymentComboBox.getSelectedItem().toString();
            promptForPaymentDetails(paymentMethod);
        });

        int result = JOptionPane.showConfirmDialog(frame, ticketPanel, "Buy Ticket", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // If user clicks OK, proceed with ticket purchase
        if (result == JOptionPane.OK_OPTION) {
            String paymentMethod = paymentComboBox.getSelectedItem().toString();
            processTicketPurchase(paymentMethod);
        }
    }

    private static void promptForPaymentDetails(String paymentMethod) {
        JPanel paymentPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField inputField1 = new JTextField();
        JTextField inputField2 = new JTextField();
        JLabel firstPrompt = new JLabel();
        JLabel secondPrompt = new JLabel();

        switch (paymentMethod) {
            case "Credit Card":
                firstPrompt.setText("Enter Credit Card Number:");
                secondPrompt.setText("Enter CVV:");
                paymentPanel.add(firstPrompt);
                paymentPanel.add(inputField1);
                paymentPanel.add(secondPrompt);
                paymentPanel.add(inputField2);
                break;
            case "Debit Card":
                firstPrompt.setText("Enter Debit Card Number:");
                secondPrompt.setText("Enter OTP:");
                paymentPanel.add(firstPrompt);
                paymentPanel.add(inputField1);
                paymentPanel.add(secondPrompt);
                paymentPanel.add(inputField2);
                break;
            case "UPI":
                firstPrompt.setText("Enter UPI ID:");
                paymentPanel.add(firstPrompt);
                paymentPanel.add(inputField1);
                paymentPanel.add(new JLabel()); // Empty space
                paymentPanel.add(new JLabel()); // Empty space
                break;
            case "Cash":
                JOptionPane.showMessageDialog(frame, "No additional details required.", "Payment Details", JOptionPane.INFORMATION_MESSAGE);
                return;
        }

        int result = JOptionPane.showConfirmDialog(frame, paymentPanel, "Payment Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String detail1 = inputField1.getText();
            String detail2 = inputField2.getText();

            if (paymentMethod.equals("UPI")) {
                detail2 = "";
            }

            messageLabel.setText("Payment details entered: " + detail1 + (detail2.isEmpty() ? "" : ", " + detail2));
        }
    }

    private static void calculateFare() {
        int destinationIndex = destinationComboBox.getSelectedIndex() + 1;
        int totalPassengers = (int) passengerCountSpinner.getValue();
        int discountedPassengers = (int) discountSpinner.getValue();

        if (availableSeats[destinationIndex] < totalPassengers) {
            JOptionPane.showMessageDialog(frame, "Not enough available seats!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            double farePerPassenger = fares[destinationIndex];
            double fullFare = (totalPassengers - discountedPassengers) * farePerPassenger;
            double discountedFare = discountedPassengers * (farePerPassenger * 0.8);
            double totalFare = fullFare + discountedFare;

            totalFareLabel.setText("Total Fare: Rs " + totalFare);
        }
    }

    private static void processTicketPurchase(String paymentMethod) {
        String passengerName = passengerNameField.getText();
        int destinationIndex = destinationComboBox.getSelectedIndex() + 1;
        int totalPassengers = (int) passengerCountSpinner.getValue();
        int discountedPassengers = (int) discountSpinner.getValue();

        if (availableSeats[destinationIndex] < totalPassengers) {
            JOptionPane.showMessageDialog(frame, "Not enough available seats!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            availableSeats[destinationIndex] -= totalPassengers;

            double farePerPassenger = fares[destinationIndex];
            double fullFare = (totalPassengers - discountedPassengers) * farePerPassenger;
            double discountedFare = discountedPassengers * (farePerPassenger * 0.8);

            // Store ticket information
            ticketS[ticketCount][0] = passengerName; // Passenger name
            ticketS[ticketCount][1] = destinations[destinationIndex - 1]; // Destination
            ticketS[ticketCount][2] = "Paid"; // Status "Paid"
            ticketS[ticketCount][3] = paymentMethod; // Payment method

            ticketI[ticketCount][0] = totalPassengers; // Total passengers
            ticketI[ticketCount][1] = discountedPassengers; // Discounted passengers

            ticketD[ticketCount][0] = farePerPassenger; // Fare per passenger
            ticketD[ticketCount][1] = discountedFare; // Discounted fare
            ticketD[ticketCount][2] = fullFare + discountedFare; // Total fare

            // Get the current date and time
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            String currentDateTime = dtf.format(now);
            ticketS[ticketCount][4] = currentDateTime; // Date and time

            // Display confirmation with date, time, and payment method
            JOptionPane.showMessageDialog(frame, "Ticket purchased successfully!\nTotal Fare: Rs " + (fullFare + discountedFare) +
                    "\nPayment Method: " + paymentMethod +
                    "\nDate and Time: " + currentDateTime, "Success", JOptionPane.INFORMATION_MESSAGE);

            // Increment ticket count for next purchase
            ticketCount++;
        }
    }

    private static void viewTickets() {
        if (ticketCount == 0) {
            JOptionPane.showMessageDialog(frame, "No tickets purchased yet.", "Tickets", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder ticketsDisplay = new StringBuilder("Passenger Name | Destination | Total Fare | Status | Payment Method | Date and Time\n");
        for (int i = 0; i < ticketCount; i++) {
            ticketsDisplay.append(ticketS[i][0]).append(" | ") // Passenger Name
                    .append(ticketS[i][1]).append(" | Rs ") // Destination
                    .append(ticketD[i][2]).append(" | ") // Total Fare
                    .append(ticketS[i][2]).append(" | ") // Status
                    .append(ticketS[i][3]).append(" | ") // Payment Method
                    .append(ticketS[i][4]).append("\n"); // Date and Time
        }

        JOptionPane.showMessageDialog(frame, ticketsDisplay.toString(), "Purchased Tickets", JOptionPane.INFORMATION_MESSAGE);
    }
}
