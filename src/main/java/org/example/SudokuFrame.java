package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class SudokuFrame {


    public SudokuFrame(SudokuGrid grid) {
        JFrame frame = new JFrame("Sudoku");
        frame.getContentPane().add(grid);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }



}
