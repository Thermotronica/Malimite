package com.lauriewired.malimite.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

import com.lauriewired.malimite.database.SQLiteDBHandler;

public class ReferencesDialog {
    private static JDialog dialog;
    private static JTable referencesTable;
    private static DefaultTableModel tableModel;
    private static SQLiteDBHandler dbHandler;

    public static void show(JFrame parent, SQLiteDBHandler handler, String functionName, String className) {
        // Check if dialog is already showing
        if (dialog != null && dialog.isVisible()) {
            dialog.toFront();
            return;
        }

        dbHandler = handler;

        // Create the dialog
        dialog = new JDialog(parent, "References", false); // Non-modal dialog
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Create main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create table model with columns
        String[] columns = {"Type", "Source", "Target", "Line"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        // Create and configure the table
        referencesTable = new JTable(tableModel);
        referencesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        referencesTable.getTableHeader().setReorderingAllowed(false);

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(referencesTable);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Create info panel at the top
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel infoLabel = new JLabel(String.format("References for %s in %s", functionName, className));
        infoLabel.setFont(infoLabel.getFont().deriveFont(Font.BOLD));
        infoPanel.add(infoLabel);
        mainPanel.add(infoPanel, BorderLayout.NORTH);

        // Create button panel at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the main panel to the dialog
        dialog.add(mainPanel);

        // Load references data
        loadReferences(functionName, className);

        // Size and position the dialog
        dialog.pack();
        dialog.setLocationRelativeTo(parent);

        // Show the dialog
        dialog.setVisible(true);
    }

    private static void loadReferences(String functionName, String className) {
        // Clear existing table data
        tableModel.setRowCount(0);

        // Get references from database
        List<Map<String, String>> references = dbHandler.getCrossReferences(functionName, className);

        // Add references to table
        for (Map<String, String> reference : references) {
            String type = reference.get("referenceType");
            String source = formatReference(reference.get("sourceFunction"), reference.get("sourceClass"));
            String target = formatReference(reference.get("targetFunction"), reference.get("targetClass"));
            String line = reference.get("lineNumber");

            tableModel.addRow(new Object[]{type, source, target, line});
        }

        // Adjust column widths
        referencesTable.getColumnModel().getColumn(0).setPreferredWidth(100); // Type
        referencesTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Source
        referencesTable.getColumnModel().getColumn(2).setPreferredWidth(200); // Target
        referencesTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Line
    }

    private static String formatReference(String function, String className) {
        return String.format("%s::%s", className, function);
    }
} 