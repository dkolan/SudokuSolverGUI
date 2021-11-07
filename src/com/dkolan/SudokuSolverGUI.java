package com.dkolan;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SudokuSolverGUI {

    // Swing fields
    private JFrame frame;
    private JPanel container;
    private JPanel boardPanel;
    private Border fieldBorder;
    private Border rightSubgridBorder;
    private Border bottomSubgridBorder;
    private Border bottomRightSubgridBorder;
    private JTextField textField;
    private Font numFont = new Font("Arial", Font.BOLD, 32);
    private Font buttonFont = new Font("Arial", Font.BOLD, 24);
    private JPanel controlPanel;
    private JButton solveButton;
    private JButton clearButton;
    private JLabel solutionCount;

    // Sudoku fields
    private Board theBoard = new Board(new int[][] {
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
    }, 9);

    private BacktrackingSolver solver = new BacktrackingSolver(theBoard);

    public SudokuSolverGUI() {
        // Set basic layout
        frame = new JFrame();

        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

        boardPanel = new JPanel();
        boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        boardPanel.setLayout(new GridLayout(9,0));
        boardPanel.setPreferredSize(new Dimension(720, 720));

        // Instantiate JPanel for controls (solve) and dialogs
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        solveButton = new JButton("Solve");
        solveButton.setPreferredSize(new Dimension(120,40));
        solveButton.setFont(buttonFont);
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> textFields = new ArrayList<>();

                for (Component c : boardPanel.getComponents()) {
                    if (c instanceof JTextField) {
                        String str = ((JTextField) c).getText();
                        if (str.equals("")) {
                            textFields.add("0");
                        } else if (isNum(str)) {
                            textFields.add(((JTextField) c).getText());
                        } else {
                            solutionCount.setText(" Invalid board." );
                            return;
                        }
                    }
                }

                int[][] inputBoard = new int[9][9];

                int index = 0;
                for (int i = 0; i < inputBoard.length; i++) {
                    for (int j = 0; j < inputBoard.length; j++) {
                        try {
                            inputBoard[i][j] = Integer.valueOf(textFields.get(index));
                            index++;
                        } catch (IndexOutOfBoundsException outOfBoundsException) {
                            solutionCount.setText(" Invalid board." );
                        }
                    }
                }

                theBoard = new Board(inputBoard, 9);

                boolean solved = solver.solve(theBoard);

                if (!solved) {
                    solutionCount.setText(" No solution found in " + solver.getElapsedTime() + " nanoseconds ");
                } else {
                    boardPanel.removeAll();
                    createGrid();
                    solutionCount.setText(" Solved in " + solver.getElapsedTime() + " nanoseconds ");
                }
            }
        });

        controlPanel.add(solveButton);

        solutionCount = new JLabel();
        solutionCount.setFont(buttonFont);
        controlPanel.add(solutionCount);

        clearButton = new JButton("Clear");
        clearButton.setPreferredSize(new Dimension(120,40));
        clearButton.setFont(buttonFont);
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theBoard = new Board(new int[][] {
                        {0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0},
                }, 9);
                boardPanel.removeAll();
                createGrid();
                solutionCount.setText("");

            }
        });
        controlPanel.add(clearButton);

        // Instantiate border objects for different sudoku cells
        fieldBorder = BorderFactory.createLineBorder(Color.BLACK);
        rightSubgridBorder = BorderFactory.createMatteBorder(1, 1, 1, 3, Color.BLACK);
        bottomSubgridBorder = BorderFactory.createMatteBorder(1, 1, 3, 1, Color.BLACK);
        bottomRightSubgridBorder = BorderFactory.createMatteBorder(1, 1, 3, 3, Color.BLACK);

        createGrid();

        frame.add(container);
        container.add(boardPanel);
        container.add(controlPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Sudoku Solver");
        frame.pack();
        frame.setSize(new Dimension(800, 800));
        frame.setVisible(true);

    }

    private void createGrid() {
        // Create 9x9 grid with text fields and borders
        String num;
        for (int row = 0; row < theBoard.getSize(); row++) {
            for (int col = 0; col < theBoard.getSize(); col++) {
                if (theBoard.getBoard()[row][col] == 0) {
                    num = "";
                } else {
                    num = String.valueOf(theBoard.getBoard()[row][col]);
                }
                textField = new JTextField(num, 2);
                textField.setFont(numFont);
                textField.setHorizontalAlignment(JTextField.CENTER);
                if ((row == 0 || row == 1 || row == 3 || row == 4 || row == 6 || row == 7 || row == 8)
                        && (col == 2 || col == 5)) {
                    textField.setBorder(rightSubgridBorder);
                } else if ((row == 2 || row == 5) && (col == 0 || col == 1 || col == 3 || col == 4
                        || col == 6 || col == 7 || col == 8)) {
                    textField.setBorder(bottomSubgridBorder);
                } else if ((row == 2 || row == 5) && (col == 2 || col == 5)) {
                    textField.setBorder(bottomRightSubgridBorder);
                } else {
                    textField.setBorder(fieldBorder);
                }
                boardPanel.add(textField);
            }
        }
    }

    private static boolean isNum(String str) {
        if (str == null) {
            return false;
        }
        try {
            int i = Integer.parseInt(str);
            if (i > 0 && i < 10) {
                return true;
            } else return false;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static void main(String[] args) {
        SudokuSolverGUI gui = new SudokuSolverGUI();
    }
}
