package com.lauriewired.malimite.ui;

import javax.swing.*;

import com.lauriewired.malimite.utils.NodeOperations;
import com.lauriewired.malimite.configuration.Config;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class ApplicationMenu {
    private final JFrame parentFrame;
    private final JTree fileTree;
    private final Config config;

    public ApplicationMenu(JFrame parentFrame, JTree fileTree, Config config) {
        this.parentFrame = parentFrame;
        this.fileTree = fileTree;
        this.config = config;
    }

    public JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createViewMenu());
        menuBar.add(createHelpMenu());
        return menuBar;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        addMenuItem(fileMenu, "Save Analysis...", e -> 
            JOptionPane.showMessageDialog(parentFrame, "Save Analysis feature coming soon!"),
            KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK)
        );

        fileMenu.addSeparator();

        addMenuItem(fileMenu, "Preferences...", e -> {
            SwingUtilities.invokeLater(() -> PreferencesDialog.show(parentFrame, config));
        }, KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, KeyEvent.CTRL_DOWN_MASK));        

        fileMenu.addSeparator();

        addMenuItem(fileMenu, "Close Window", e -> {
            parentFrame.dispose();
        }, KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK));

        return fileMenu;
    }

    private JMenu createViewMenu() {
        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);

        if (fileTree != null) {
            addMenuItem(viewMenu, "Expand All", e -> 
                NodeOperations.expandAllTreeNodes(fileTree),
                KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK)
            );

            viewMenu.addSeparator();

            addMenuItem(viewMenu, "Collapse All", e -> 
                NodeOperations.collapseAllTreeNodes(fileTree),
                KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK)
            );
        }

        return viewMenu;
    }

    private JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);

        addMenuItem(helpMenu, "About", e -> 
            JOptionPane.showMessageDialog(parentFrame,
                "Malimite - iOS Malware Analysis Tool\nVersion 1.0\n© 2024",
                "About Malimite",
                JOptionPane.INFORMATION_MESSAGE)
        );

        return helpMenu;
    }

    private void addMenuItem(JMenu menu, String text, ActionListener action, KeyStroke accelerator) {
        JMenuItem menuItem = new JMenuItem(text);
        if (accelerator != null) {
            menuItem.setAccelerator(accelerator);
        }
        menuItem.addActionListener(e -> SafeMenuAction.execute(() -> action.actionPerformed(e)));
        menu.add(menuItem);
    }

    private void addMenuItem(JMenu menu, String text, ActionListener action) {
        addMenuItem(menu, text, action, null);
    }
} 