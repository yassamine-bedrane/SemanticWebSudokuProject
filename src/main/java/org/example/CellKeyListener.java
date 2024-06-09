package org.example;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CellKeyListener extends KeyAdapter {
    private final Cell cell;

    public CellKeyListener(Cell cell) {
        this.cell = cell;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char keyChar = e.getKeyChar();
        if (Character.isDigit(keyChar)) {
            cell.setNumber(Character.getNumericValue(keyChar));
            cell.setManuallySet(true);
            cell.setBackground(Config.MANUALY_SET_COLOR);
        } else {
            e.consume();
            SwingUtilities.invokeLater(() -> cell.setText(""));
            cell.setNumber(0);
            cell.setManuallySet(false);
            cell.setBackground(Config.UNSET_COLOR);
        }
    }
}