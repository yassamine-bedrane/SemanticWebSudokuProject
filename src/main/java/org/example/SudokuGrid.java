package org.example;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;


public class SudokuGrid extends JPanel {

    private final Cell[][] grid;
    private final SudokuSolver solver;
    private final int dimension;
    private final JPanel gridPanel;
    private final JButton solveButton;

    SudokuGrid(int dimension) {
        this.grid = new Cell[dimension][dimension];
        this.solver = new SudokuSolver(this.grid);
        this.dimension = dimension;

        this.populateGridObject();

        this.gridPanel = new JPanel();
        JPanel buttonPanel = new JPanel();


        this.gridPanel.setLayout(new GridLayout(this.dimension, this.dimension));
        this.gridPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 4));

        this.drawBoard();

        JButton clearButton = new JButton("Réinitialiser");
        solveButton = new JButton("Résoudre");
        
        clearButton.setBackground(new Color(0, 123, 255)); 
        clearButton.setForeground(Color.WHITE);             
        solveButton.setBackground(new Color(40, 167, 69));  
        solveButton.setForeground(Color.WHITE);  
        
        
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(clearButton, BorderLayout.WEST);
        buttonPanel.add(solveButton, BorderLayout.EAST);
        this.setLayout(new BorderLayout());
        this.add(buttonPanel, BorderLayout.SOUTH);
        this.add(gridPanel, BorderLayout.CENTER);


        clearButton.addActionListener(e -> clearGrid());

        solveButton.addActionListener(e -> solveSudoku());
    }


    public void drawBoard(){
        this.gridPanel.removeAll();
        Border border = BorderFactory.createLineBorder(Color.BLUE, 4);
        Dimension fieldDimension = new Dimension(70, 70);
        for (int y = 0; y < this.dimension; ++y) {
            for (int x = 0; x < this.dimension; ++x) {
                Cell cell =  grid[y][x];
                cell.setBorder(border);
                cell.setPreferredSize(fieldDimension);
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(new Font("Arial", Font.PLAIN, 20));


                int number = cell.getNumber();

                if(cell.isManuallySet){
                    cell.setBackground(Config.MANUALY_SET_COLOR);
                    cell.setText(number+"");
                }
                else if(number >0){
                    cell.setBackground(Config.REASONER_SET_VALUE);
                    cell.setText(number+"");
                }
                this.gridPanel.add(cell);
            }
        }
    }

    public void populateGridObject(){
        for (int row = 0; row < this.dimension; ++row) {
            for (int col = 0; col < this.dimension; ++col) {
                int block = SudokuUtils.getBlockNumber(row + 1, col + 1);
                int[] coordInBlock = SudokuUtils.getIndexesInBlockMatrix(block, row + 1, col + 1);
                grid[row][col] = new Cell(block, coordInBlock[0], coordInBlock[1]);
            }
        }
    }

    private void clearGrid() {
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                grid[row][col].setText("");
                grid[row][col].setManuallySet(false);
                grid[row][col].setNumber(0);
                grid[row][col].setBackground(Config.UNSET_COLOR);
            }
        }
        this.solveButton.setEnabled(true);
    }

    private void solveSudoku() {
        try {
            ArrayList<String> manuallySetCellsLabels = new ArrayList<>();
            for (Cell[] cells : grid) {
                for (Cell cell : cells) {
                    if (cell.isManuallySet) {
                        manuallySetCellsLabels.add(cell.getLabel());
                    }
                }
            }
            this.solver.solve(manuallySetCellsLabels);
            this.drawBoard();
            this.solveButton.setEnabled(false);
        } catch (OWLOntologyCreationException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

}
