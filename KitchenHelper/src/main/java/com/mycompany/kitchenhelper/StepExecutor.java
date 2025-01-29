package com.mycompany.kitchenhelper;

import gr.hua.dit.oop2.countdown.Countdown;
import gr.hua.dit.oop2.countdown.CountdownFactory;
import gr.hua.dit.oop2.countdown.Notifier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StepExecutor extends MyFrame {
    private Recipe recipe;
    private int people;
    private int currentStepIndex = 0;
    private JTextArea stepDisplay;
    private JButton nextStepButton;
    private JProgressBar progressBar;
    private Countdown countdown;
    private TimeConverter timeConverter;
    private boolean ingredientsDisplayed = false; // Flag for displaying ingredients

    public StepExecutor(Recipe recipe, int people) {
        this.recipe = recipe;
        this.people = people;
        this.timeConverter = new TimeConverter();

        setTitle("Step Executor");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initializeGUI();
        displayStep();
    }

    private void initializeGUI() {
        // Text area for displaying steps
        stepDisplay = new JTextArea();
        stepDisplay.setEditable(false);
        stepDisplay.setFont(new Font("SansSerif", Font.PLAIN, 14));
        stepDisplay.setWrapStyleWord(true);
        stepDisplay.setLineWrap(true);

        JScrollPane scrollPane = new JScrollPane(stepDisplay);
        add(scrollPane, BorderLayout.CENTER);

        // Progress bar for time tracking
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(400, 20));
        add(progressBar, BorderLayout.NORTH);

        // Button for moving to the next step
        nextStepButton = new JButton("Next Step");
        nextStepButton.addActionListener(new NextStepAction());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(nextStepButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(500, 400);
        setLocationRelativeTo(null); // Center the window
    }

    private void displayStep() {
        if (currentStepIndex >= recipe.getSteps().size()) {
            JOptionPane.showMessageDialog(this, "Recipe completed!", "Done", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // close the frame
            return;
        }

        StringBuilder stepText = new StringBuilder();

        // Display ingredients only on the first step
        if (!ingredientsDisplayed) {
            stepText.append("Ingredients:\n");
            for (Ingredient ingredient : recipe.getIngredients()) {
                double scaledQuantity = ingredient.getQuantity() * people;
                stepText.append(String.format("- %.2f %s %s\n",
                        scaledQuantity,
                        ingredient.getUnit(),
                        ingredient.getName()));
            }
            stepText.append("\n");
            ingredientsDisplayed = true;
        }

        // Display current step
        stepText.append(String.format("Step %d: %s\n",
                currentStepIndex + 1,
                recipe.getSteps().get(currentStepIndex)));

        // Add time for the step
        Time stepTime = recipe.getTimeForStep(currentStepIndex);
        if (stepTime != null && stepTime.getValue() > 0) {
            stepText.append(String.format("Time required: %.1f %s\n",
                    stepTime.getValue(),
                    stepTime.getUnit()));

            nextStepButton.setEnabled(false); // Disable button during countdown
            startCountdown(timeConverter.convertToSeconds(stepTime));
        }

        stepDisplay.setText(stepText.toString());
    }

    private void startCountdown(long seconds) {
        // Create the countdown timer and start it
        countdown = CountdownFactory.countdown(seconds);
        progressBar.setMaximum((int) seconds);
        progressBar.setValue(0);

        countdown.addNotifier(new Notifier() {
            @Override
            public void finished(Countdown c) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(StepExecutor.this,
                            "Time is up for this step!",
                            "Time's Up",
                            JOptionPane.INFORMATION_MESSAGE);
                    nextStepButton.setEnabled(true); // Enable button when time is up
                });
            }
        });

        // Update progress bar in a separate thread
        new Thread(() -> {
            countdown.start();
            while (countdown.secondsRemaining() > 0) {
                try {
                    long remaining = countdown.secondsRemaining();
                    SwingUtilities.invokeLater(() -> {
                        progressBar.setValue((int) (seconds - remaining));
                    });
                    Thread.sleep(1000); // Update every second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            SwingUtilities.invokeLater(() -> progressBar.setValue((int) seconds)); // Complete progress bar
        }).start();

        nextStepButton.setEnabled(false); // disable button during countdown
    }

    private class NextStepAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (countdown != null) {
                countdown.stop(); // stop the countdown when moving to the next step
            }

            currentStepIndex++;
            if (currentStepIndex >= recipe.getSteps().size()) {
                JOptionPane.showMessageDialog(StepExecutor.this, "Recipe completed!", "Done", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Close the frame
            } else {
                displayStep();
            }
        }
    }
}