package play.iteratedremovaldominated.mixed;

import gametree.GameNode;
import play.NormalFormGame;
import play.PlayStrategy;
import play.Strategy;
import play.exception.InvalidStrategyException;
import scpsolver.constraints.Constraint;
import scpsolver.constraints.LinearBiggerThanEqualsConstraint;
import scpsolver.constraints.LinearEqualsConstraint;
import scpsolver.constraints.LinearSmallerThanEqualsConstraint;
import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.lpsolver.SolverFactory;
import scpsolver.problems.LinearProgram;

import java.util.*;

import static play.prisonersdilemma.utils.PrisonersDilemmaStrategyCommonUtils.isATerminalGameNode;

/**
 *
 * Computational Game Theory - 2019/2020
 * Faculty of Sciences and Technology of
 * New University of Lisbon (FCT NOVA | FCT/UNL)
 *
 * Iterated Removal of Strictly Dominated Mixed Strategies,
 * using NOVA GTI (Game Theory Interactive) Platform
 *
 * Iterated Removal of Strictly Dominated Mixed Strategies,
 * using Linear Programming
 *
 * Authors:
 * - Pedro Lamarao Pais (Student no. 48247)
 *   - pg.pais@campus.fct.unl.pt
 * - Ruben Andre Barreiro (Student no. 42648)
 *   - r.barreiro@campus.fct.unl.pt
 *
 */

public class IteratedRemovalOfStrictlyDominatedMixedStrategiesUsingLinearProgramming extends Strategy {

    private GameNode gameRootNode;

    private int totalNumMovesPlayer1;
    private int totalNumMovesPlayer2;

    String[] initialLabelsMovesPlayer1;
    String[] initialLabelsMovesPlayer2;

    String[] labelsMovesPlayer1;
    String[] labelsMovesPlayer2;

    private int[][] currentUtilitiesPayoffsNormalFormMatrixPlayer1;
    private int[][] currentUtilitiesPayoffsNormalFormMatrixPlayer2;

    private NormalFormGame normalFormGame;

    private int numOfDominatedRows;

    private int numOfDominatedColumns;

    private LinearProgram linearProgram;

    private double[] linearProgramSolvedSolution;

    private double linearProgramSolvedSolutionSum;


    public void startIteratedRemovalOfStrictlyDominatedStrategies(GameNode gameRootNode) {

        this.gameRootNode = gameRootNode;

        this.totalNumMovesPlayer1 = this.gameRootNode.numberOfChildren();
        this.totalNumMovesPlayer2 = this.gameRootNode.getChildren().next()
                .numberOfChildren();

        this.currentUtilitiesPayoffsNormalFormMatrixPlayer1 =
                        new int[this.totalNumMovesPlayer1][this.totalNumMovesPlayer2];
        this.currentUtilitiesPayoffsNormalFormMatrixPlayer2 =
                        new int[this.totalNumMovesPlayer1][this.totalNumMovesPlayer2];

        this.initialLabelsMovesPlayer1 = this.labelsMovesPlayer1 = new String[totalNumMovesPlayer1];
        this.initialLabelsMovesPlayer2 = this.labelsMovesPlayer2 = new String[totalNumMovesPlayer2];

        this.buildUtilitiesPayoffsNormalFormMatrix();

        this.showInitialGameInformationForPlayers();

        this.normalFormGame =
                new NormalFormGame
                        (
                                this.currentUtilitiesPayoffsNormalFormMatrixPlayer1,
                                this.currentUtilitiesPayoffsNormalFormMatrixPlayer2,
                                this.labelsMovesPlayer1,
                                this.labelsMovesPlayer2
                        );

        this.numOfDominatedRows = 0;
        this.numOfDominatedColumns = 0;

        this.linearProgramSolvedSolutionSum = 0.0;

    }

    private void buildUtilitiesPayoffsNormalFormMatrix() {

        GameNode gameRootChildrenNodeMovePlayer1;
        GameNode gameRootChildrenNodeMovePlayer2;

        Iterator<GameNode> gameRootChildrenNodesPlayer1 =
                this.gameRootNode.getChildren();

        int utilitiesPayoffsNormalFormMatrixRow = 0;

        while (gameRootChildrenNodesPlayer1.hasNext()) {

            gameRootChildrenNodeMovePlayer1 = gameRootChildrenNodesPlayer1.next();

            this.initialLabelsMovesPlayer1[utilitiesPayoffsNormalFormMatrixRow] =
                    this.labelsMovesPlayer1[utilitiesPayoffsNormalFormMatrixRow] =
                            gameRootChildrenNodeMovePlayer1.getLabel();

            int utilitiesPayoffsNormalFormMatrixColumn = 0;

            Iterator<GameNode> gameRootChildrenNodesPlayer2 =
                    gameRootChildrenNodeMovePlayer1.getChildren();

            while (gameRootChildrenNodesPlayer2.hasNext()) {

                gameRootChildrenNodeMovePlayer2 = gameRootChildrenNodesPlayer2.next();

                if (utilitiesPayoffsNormalFormMatrixRow == 0) {

                    this.initialLabelsMovesPlayer2[utilitiesPayoffsNormalFormMatrixColumn] =
                            this.labelsMovesPlayer2[utilitiesPayoffsNormalFormMatrixColumn] =
                                    gameRootChildrenNodeMovePlayer2.getLabel();

                }

                this.currentUtilitiesPayoffsNormalFormMatrixPlayer1
                        [utilitiesPayoffsNormalFormMatrixRow]
                        [utilitiesPayoffsNormalFormMatrixColumn] =
                        gameRootChildrenNodeMovePlayer2.getPayoffP1();

                this.currentUtilitiesPayoffsNormalFormMatrixPlayer2
                        [utilitiesPayoffsNormalFormMatrixRow]
                        [utilitiesPayoffsNormalFormMatrixColumn] =
                        gameRootChildrenNodeMovePlayer2.getPayoffP2();

                utilitiesPayoffsNormalFormMatrixColumn++;

            }

            utilitiesPayoffsNormalFormMatrixRow++;

        }

    }

    public void showInitialGameInformationForPlayers() {

        this.showPlayerActionMoves(true, 1, this.labelsMovesPlayer1);
        this.showPlayerActionMoves(true, 2, this.labelsMovesPlayer2);

        this.showPlayerPayoffUtilities(true, 1, this.currentUtilitiesPayoffsNormalFormMatrixPlayer1);
        this.showPlayerPayoffUtilities(true, 2, this.currentUtilitiesPayoffsNormalFormMatrixPlayer2);

    }

    public void showPlayerActionMoves(boolean initialGame, int numPlayer, String[] playerActionMovesLabels) {

        if(initialGame) {

            System.out.println("Initial Possible Action Moves for Player #" + numPlayer + ":\n");

        }
        else {

            System.out.println("Possible Action Moves for Player #" + numPlayer +
                               ", after Iterated Removal of Strictly Dominated Mixed Strategies:\n");

        }

        for (String playerActionMovesLabel : playerActionMovesLabels) {

            System.out.println("   " +
                    this.showPlayerActionMoveLabel
                            (
                                    playerActionMovesLabel
                            )
            );

        }

        System.out.println("\n\n");

    }

    public String showPlayerActionMoveLabel(String label) {

        return label.substring(label.lastIndexOf(':') + 1);

    }


    public void showPlayerPayoffUtilities(boolean initialGame, int numPlayer, int[][] utilitiesPayoffsNormalFormMatrix) {

        int numColumns = utilitiesPayoffsNormalFormMatrix[0].length;

        if(initialGame) {

            System.out.println("Initial Utilities from Payoffs for Player " + numPlayer + ":");

        }
        else {

            System.out.println("Current Utilities from Payoffs for Player " + numPlayer + ":");

        }

        for (int[] payoffsNormalFormMatrix : utilitiesPayoffsNormalFormMatrix) {

            for (int currentColumn = 0; currentColumn < numColumns; currentColumn++) {

                System.out.print
                        (
                                "| " +
                                        payoffsNormalFormMatrix[currentColumn] + " "
                        );

            }

            System.out.println("|");

        }

        System.out.println("\n\n");

    }

    public void tryToEliminateDominatedStrategies() {

        do {

            System.out.println("Trying to Eliminate Dominated Strategies in this Game...\n\n");

        }
        while
        (
                this.findIteratedDominatedStrategiesByRows()
                                    ||
                this.findIteratedDominatedStrategiesByColumns()
        );

        System.out.println("Procedure of Iterated Removal of Strictly Dominated Strategies done!!!\n\n");

    }

    public boolean findIteratedDominatedStrategiesByRows() {

        int numRows = ( this.totalNumMovesPlayer1 - numOfDominatedRows );
        int numColumns = ( this.totalNumMovesPlayer2 - this.numOfDominatedColumns );

        boolean allStrategyRowsChecked = false;
        boolean someDominatedStrategyRowFound = false;

        if (numRows > 1) {

            System.out.println("Trying to check for any Dominated Strategy Row...\n\n");

            int numRowToVerifyIfIsDominated = 0;

            while(!allStrategyRowsChecked) {

                if (numRows > 1) {

                    if (numRowToVerifyIfIsDominated < numRows) {

                        System.out.println("Row " + numRowToVerifyIfIsDominated +
                                " it's gonna be checked if it's dominated or not...\n\n");

                        this.normalFormGame.showGame();

                        int minimumCoefficientRows = this.getMinimumCoefficientRows(numRowToVerifyIfIsDominated);

                        if (minimumCoefficientRows >= 0.0) {

                            System.out.println("It wasn't necessary any Coefficient Adjustment to the Rows...\n");

                        } else {

                            System.out.println("It was necessary a Coefficient Adjustment:\n");
                            System.out.println(" - Minimum Coefficient found: " + minimumCoefficientRows + "\n\n");
                            System.out.println("It will be added " + (-1.0 * minimumCoefficientRows) +
                                    " to all the Coefficients of the Rows...\n\n");

                        }

                        System.out.println("Number of Dominated Rows founded, until now: " + this.numOfDominatedRows);
                        System.out.println("Number of Dominated Columns founded, until now: " + this.numOfDominatedColumns);


                        this.adjustMinimumCoefficientRows(true, minimumCoefficientRows);

                        double[] functionToMinimize = new double[(numRows - 1)];

                        Arrays.fill(functionToMinimize, 1.0);


                        double[] limitingConstraintsDominatedStrategy =
                                this.getLimitingConstraintsRowDominatedStrategy(numRowToVerifyIfIsDominated);


                        double[] lowerBoundaries = new double[(numRows - 1)];

                        Arrays.fill(lowerBoundaries, 0.0);

                        ArrayList<Double> limitingConstraintsDominatingStrategiesList = new ArrayList<>();

                        for (int currentColumn = 0; currentColumn < numColumns; currentColumn++) {

                            for (int currentRow = 0; currentRow < numRows; currentRow++) {

                                if (currentRow != numRowToVerifyIfIsDominated) {

                                    double currentPayoffUtilityAdjusted =
                                            this.currentUtilitiesPayoffsNormalFormMatrixPlayer1
                                                    [currentRow][currentColumn];

                                    limitingConstraintsDominatingStrategiesList.add(currentPayoffUtilityAdjusted);

                                }

                            }

                        }


                        double[][] limitingConstraintsDominatingStrategies =
                                new double[numColumns]
                                        [(numRows - 1)];

                        int limitingConstraintsDominatingStrategiesListIndex = 0;

                        for (int currentColumn = 0; currentColumn < numColumns; currentColumn++) {

                            for (int currentRow = 0; currentRow < numRows - 1; currentRow++) {

                                limitingConstraintsDominatingStrategies[currentColumn][currentRow] =
                                        limitingConstraintsDominatingStrategiesList
                                                .get(limitingConstraintsDominatingStrategiesListIndex);

                                limitingConstraintsDominatingStrategiesListIndex++;

                            }

                        }

                        this.linearProgram = new LinearProgram(functionToMinimize);

                        this.linearProgram.setMinProblem(true);

                        for
                        (
                                int numConstraint = 0;
                                numConstraint < limitingConstraintsDominatedStrategy.length;
                                numConstraint++
                        ) {
                            this.linearProgram.addConstraint
                                    (
                                            new LinearBiggerThanEqualsConstraint
                                                    (
                                                            limitingConstraintsDominatingStrategies
                                                                    [numConstraint],
                                                            limitingConstraintsDominatedStrategy
                                                                    [numConstraint],
                                                            "c" + numConstraint
                                                    )
                                    );
                        }

                        this.linearProgram.setLowerbound(lowerBoundaries);

                        this.showLinearPrograming();

                        this.solveLinearPrograming();

                        this.showSolution();


                        if (this.linearProgramSolvedSolution != null && this.linearProgramSolvedSolutionSum < 1.0) {

                            System.out.println("Row " + numRowToVerifyIfIsDominated +
                                    " is Strictly Dominated by a Mixed Strategy!!!\n\n");

                            ArrayList<String> newLabelsMovesPlayer1List = new ArrayList<>();

                            for (int currentRow = 0; currentRow < numRows; currentRow++) {

                                if (currentRow != numRowToVerifyIfIsDominated) {

                                    newLabelsMovesPlayer1List.add(this.labelsMovesPlayer1[currentRow]);

                                }

                            }

                            this.labelsMovesPlayer1 = newLabelsMovesPlayer1List.toArray(new String[0]);

                            this.normalFormGame.setNormalFormGameRowPlayer1AsNotConsidered(numRowToVerifyIfIsDominated);

                            this.adjustMinimumCoefficientRows(false, minimumCoefficientRows);

                            this.deleteRow(numRowToVerifyIfIsDominated);

                            this.numOfDominatedRows++;

                            this.normalFormGame = new NormalFormGame(this.currentUtilitiesPayoffsNormalFormMatrixPlayer1,
                                                                     this.currentUtilitiesPayoffsNormalFormMatrixPlayer2,
                                                                     this.labelsMovesPlayer1, this.labelsMovesPlayer2);

                            this.currentUtilitiesPayoffsNormalFormMatrixPlayer1 =
                                    this.normalFormGame.utilitiesPayoffsNormalFormMatrixPlayer1;

                            this.currentUtilitiesPayoffsNormalFormMatrixPlayer2 =
                                    this.normalFormGame.utilitiesPayoffsNormalFormMatrixPlayer2;

                            System.out.println("The current Game changed!!!\n\n");

                            numRows = (this.totalNumMovesPlayer1 - this.numOfDominatedRows);

                            someDominatedStrategyRowFound = true;

                            this.showPlayerActionMoves(false, 1, this.labelsMovesPlayer1);

                            this.showPlayerPayoffUtilities(false, 1,
                                                           this.currentUtilitiesPayoffsNormalFormMatrixPlayer1);

                        }
                        else {

                            System.out.println("Row " + numRowToVerifyIfIsDominated +
                                    " is not Strictly Dominated by a Mixed Strategy!!!\n\n");

                            numRowToVerifyIfIsDominated++;

                            someDominatedStrategyRowFound = false;

                        }

                        if (someDominatedStrategyRowFound) {

                            numRowToVerifyIfIsDominated = 0;

                        }

                        allStrategyRowsChecked = (numRowToVerifyIfIsDominated == numRows);

                    }

                }
                else {

                    allStrategyRowsChecked = true;

                }

            }

        }

        System.out.println("No more Dominated Row Strategies found!!!\n\n");

        return someDominatedStrategyRowFound;

    }

    public boolean findIteratedDominatedStrategiesByColumns() {

        int numRows = ( this.totalNumMovesPlayer1 - numOfDominatedRows );
        int numColumns = ( this.totalNumMovesPlayer2 - this.numOfDominatedColumns );

        boolean allStrategyColumnsChecked = false;

        boolean someDominatedStrategyColumnFound = false;

        if(numColumns > 1) {

            System.out.println("Trying to check for any Dominated Strategy Column...\n\n");

            int numColumnToVerifyIfIsDominated = 0;

            while(!allStrategyColumnsChecked) {

                if(numColumns > 1) {

                    if (numColumnToVerifyIfIsDominated < numColumns) {

                        System.out.println("Column " + numColumnToVerifyIfIsDominated +
                                " it's gonna be checked if it's dominated or not...\n\n");

                        this.normalFormGame.showGame();

                        int minimumCoefficientColumns = this.getMinimumCoefficientColumns(numColumnToVerifyIfIsDominated);

                        if (minimumCoefficientColumns >= 0.0) {

                            System.out.println("It wasn't necessary any Coefficient Adjustment to the Columns...\n");

                        } else {

                            System.out.println("It was necessary a Coefficient Adjustment:\n");
                            System.out.println(" - Minimum Coefficient found: " + minimumCoefficientColumns + "\n\n");
                            System.out.println("It will be added " + (-1.0 * minimumCoefficientColumns) +
                                    " to all the Coefficients of the Columns...\n\n");

                        }

                        System.out.println("Number of Dominated Rows founded, until now: " + this.numOfDominatedRows);
                        System.out.println("Number of Dominated Columns founded, until now: " + this.numOfDominatedColumns);


                        this.adjustMinimumCoefficientColumns(true, minimumCoefficientColumns);

                        double[] functionToMinimize = new double[(numColumns - 1)];

                        Arrays.fill(functionToMinimize, 1.0);


                        double[] limitingConstraintsDominatedStrategy =
                                this.getLimitingConstraintsColumnDominatedStrategy(numColumnToVerifyIfIsDominated);


                        double[] lowerBoundaries = new double[(numColumns - 1)];

                        Arrays.fill(lowerBoundaries, 0.0);

                        ArrayList<Double> limitingConstraintsDominatingStrategiesList = new ArrayList<>();

                        for (int currentRow = 0; currentRow < numRows; currentRow++) {

                            for (int currentColumn = 0; currentColumn < numColumns; currentColumn++) {

                                if (currentColumn != numColumnToVerifyIfIsDominated) {

                                    double currentPayoffUtilityAdjusted =
                                            this.currentUtilitiesPayoffsNormalFormMatrixPlayer2
                                                    [currentRow][currentColumn];

                                    limitingConstraintsDominatingStrategiesList.add(currentPayoffUtilityAdjusted);

                                }

                            }

                        }


                        double[][] limitingConstraintsDominatingStrategies =
                                new double[numRows]
                                        [(numColumns - 1)];

                        int limitingConstraintsDominatingStrategiesListIndex = 0;

                        for (int currentRow = 0; currentRow < numRows; currentRow++) {

                            for (int currentColumn = 0; currentColumn < numColumns - 1; currentColumn++) {

                                limitingConstraintsDominatingStrategies[currentRow][currentColumn] =
                                        limitingConstraintsDominatingStrategiesList
                                                .get(limitingConstraintsDominatingStrategiesListIndex);

                                limitingConstraintsDominatingStrategiesListIndex++;

                            }

                        }

                        this.linearProgram = new LinearProgram(functionToMinimize);

                        this.linearProgram.setMinProblem(true);

                        for
                        (
                                int numConstraint = 0;
                                numConstraint < limitingConstraintsDominatedStrategy.length;
                                numConstraint++
                        ) {
                            this.linearProgram.addConstraint
                                    (
                                            new LinearBiggerThanEqualsConstraint
                                                    (
                                                            limitingConstraintsDominatingStrategies
                                                                    [numConstraint],
                                                            limitingConstraintsDominatedStrategy
                                                                    [numConstraint],
                                                            "c" + numConstraint
                                                    )
                                    );
                        }

                        this.linearProgram.setLowerbound(lowerBoundaries);

                        this.showLinearPrograming();

                        this.solveLinearPrograming();

                        this.showSolution();


                        if (this.linearProgramSolvedSolution!= null && this.linearProgramSolvedSolutionSum < 1.0) {

                            System.out.println("Column " + numColumnToVerifyIfIsDominated +
                                    " is Strictly Dominated by a Mixed Strategy!!!\n\n");

                            ArrayList<String> newLabelsMovesPlayer2List = new ArrayList<>();

                            for (int currentColumn = 0; currentColumn < numColumns; currentColumn++) {

                                if (currentColumn != numColumnToVerifyIfIsDominated) {

                                    newLabelsMovesPlayer2List.add(this.labelsMovesPlayer2[currentColumn]);

                                }

                            }

                            this.labelsMovesPlayer2 = newLabelsMovesPlayer2List.toArray(new String[0]);

                            this.normalFormGame.setNormalFormGameColumnPlayer2AsNotConsidered(numColumnToVerifyIfIsDominated);

                            this.adjustMinimumCoefficientColumns(false, minimumCoefficientColumns);

                            this.deleteColumn(numColumnToVerifyIfIsDominated);

                            this.numOfDominatedColumns++;

                            this.normalFormGame = new NormalFormGame(this.currentUtilitiesPayoffsNormalFormMatrixPlayer1,
                                    this.currentUtilitiesPayoffsNormalFormMatrixPlayer2,
                                    this.labelsMovesPlayer1, this.labelsMovesPlayer2);

                            this.currentUtilitiesPayoffsNormalFormMatrixPlayer1 =
                                    this.normalFormGame.utilitiesPayoffsNormalFormMatrixPlayer1;

                            this.currentUtilitiesPayoffsNormalFormMatrixPlayer2 =
                                    this.normalFormGame.utilitiesPayoffsNormalFormMatrixPlayer2;

                            System.out.println("The current Game changed!!!\n\n");

                            numColumns = (this.totalNumMovesPlayer2 - this.numOfDominatedColumns);

                            someDominatedStrategyColumnFound = true;

                            this.showPlayerActionMoves(false, 2, this.labelsMovesPlayer2);

                            this.showPlayerPayoffUtilities(false, 2,
                                                           this.currentUtilitiesPayoffsNormalFormMatrixPlayer2);

                        }
                        else {

                            System.out.println("Column " + numColumnToVerifyIfIsDominated +
                                    " is not Strictly Dominated by a Mixed Strategy!!!\n\n");

                            numColumnToVerifyIfIsDominated++;

                            someDominatedStrategyColumnFound = false;

                        }

                        if (someDominatedStrategyColumnFound) {

                            numColumnToVerifyIfIsDominated = 0;

                        }

                        allStrategyColumnsChecked = (numColumnToVerifyIfIsDominated == numColumns);

                    }

                }
                else {

                    allStrategyColumnsChecked = true;

                }
            }

        }

        System.out.println("No more Dominated Column Strategies found!!!\n\n");

        return someDominatedStrategyColumnFound;

    }

    public void deleteRow(int numRow) {

        List<int[]> currentUtilitiesPayoffsNormalFormMatrixPlayer1List =
                new ArrayList<>(Arrays.asList(this.currentUtilitiesPayoffsNormalFormMatrixPlayer1));

        currentUtilitiesPayoffsNormalFormMatrixPlayer1List.remove(numRow);

        this.currentUtilitiesPayoffsNormalFormMatrixPlayer1 =
                currentUtilitiesPayoffsNormalFormMatrixPlayer1List.toArray(new int[][]{});


        List<int[]> currentUtilitiesPayoffsNormalFormMatrixPlayer2List =
                new ArrayList<>(Arrays.asList(this.currentUtilitiesPayoffsNormalFormMatrixPlayer2));

        currentUtilitiesPayoffsNormalFormMatrixPlayer2List.remove(numRow);

        this.currentUtilitiesPayoffsNormalFormMatrixPlayer2 =
                currentUtilitiesPayoffsNormalFormMatrixPlayer2List.toArray(new int[][]{});

    }

    public void deleteColumn(int numColumn) {

        int[][] currentUtilitiesPayoffsNormalFormMatrixPlayer1Transposed =
                transposeMatrix(this.currentUtilitiesPayoffsNormalFormMatrixPlayer1);

        List<int[]> currentUtilitiesPayoffsNormalFormMatrixPlayer1TransposedList =
                new ArrayList<>(Arrays.asList(currentUtilitiesPayoffsNormalFormMatrixPlayer1Transposed));

        currentUtilitiesPayoffsNormalFormMatrixPlayer1TransposedList.remove(numColumn);

        currentUtilitiesPayoffsNormalFormMatrixPlayer1Transposed =
                currentUtilitiesPayoffsNormalFormMatrixPlayer1TransposedList.toArray(new int[][]{});

        this.currentUtilitiesPayoffsNormalFormMatrixPlayer1 =
                transposeMatrix(currentUtilitiesPayoffsNormalFormMatrixPlayer1Transposed);


        int[][] currentUtilitiesPayoffsNormalFormMatrixPlayer2Transposed =
                transposeMatrix(this.currentUtilitiesPayoffsNormalFormMatrixPlayer2);

        List<int[]> currentUtilitiesPayoffsNormalFormMatrixPlayer2TransposedList =
                new ArrayList<>(Arrays.asList(currentUtilitiesPayoffsNormalFormMatrixPlayer2Transposed));

        currentUtilitiesPayoffsNormalFormMatrixPlayer2TransposedList.remove(numColumn);

        currentUtilitiesPayoffsNormalFormMatrixPlayer2Transposed =
                currentUtilitiesPayoffsNormalFormMatrixPlayer2TransposedList.toArray(new int[][]{});

        this.currentUtilitiesPayoffsNormalFormMatrixPlayer2 =
                transposeMatrix(currentUtilitiesPayoffsNormalFormMatrixPlayer2Transposed);

    }

    public int[][] transposeMatrix(int[][] originalMatrix) {

        if (originalMatrix == null || originalMatrix.length == 0) {

            return originalMatrix;

        }

        int numRows = originalMatrix.length;
        int numColumns = originalMatrix[0].length;

        int[][] transposedMatrix = new int[numColumns][numRows];

        for (int currentRow = 0; currentRow < numRows; currentRow++) {

            for (int currentColumn = 0; currentColumn < numColumns; currentColumn++) {

                transposedMatrix[currentColumn][currentRow] = originalMatrix[currentRow][currentColumn];

            }

        }

        return transposedMatrix;

    }

    private int getMinimumCoefficientRows(int numRowToVerifyIfIsDominated) {

        int minimumCoefficientRows = Integer.MAX_VALUE;

        int numRows = ( this.totalNumMovesPlayer1 - this.numOfDominatedRows );
        int numColumns = ( this.totalNumMovesPlayer2 - this.numOfDominatedColumns );

        for (int currentRow = 0; currentRow < numRows; currentRow++) {

           if (currentRow != numRowToVerifyIfIsDominated) {

               for (int currentColumn = 0; currentColumn < numColumns; currentColumn++) {

                    if
                    (
                            this.currentUtilitiesPayoffsNormalFormMatrixPlayer1
                                    [currentRow][currentColumn]
                                                <
                                     minimumCoefficientRows
                    )
                    {

                            minimumCoefficientRows =
                                    this.currentUtilitiesPayoffsNormalFormMatrixPlayer1
                                                [currentRow][currentColumn];

                    }

                }

            }

        }

        return minimumCoefficientRows;

    }

    private int getMinimumCoefficientColumns(int numColumnToVerifyIfIsDominated) {

        int minimumCoefficientColumns = Integer.MAX_VALUE;

        int numRows = ( this.totalNumMovesPlayer1 - this.numOfDominatedRows );
        int numColumns = ( this.totalNumMovesPlayer2 - this.numOfDominatedColumns );

        for (int currentRow = 0; currentRow < numRows; currentRow++) {

            for (int currentColumn = 0; currentColumn < numColumns; currentColumn++) {

                if (currentColumn != numColumnToVerifyIfIsDominated) {

                    if
                    (
                            this.currentUtilitiesPayoffsNormalFormMatrixPlayer2
                                    [currentRow][currentColumn]
                                                <
                                    minimumCoefficientColumns
                    )
                    {

                        minimumCoefficientColumns =
                                this.currentUtilitiesPayoffsNormalFormMatrixPlayer2
                                        [currentRow][currentColumn];

                    }

                }

            }

        }

        return minimumCoefficientColumns;

    }

    private void adjustMinimumCoefficientRows(boolean adjustBySum, double minimumCoefficientRows) {

        int numRows = ( this.totalNumMovesPlayer1 - this.numOfDominatedRows );
        int numColumns = ( this.totalNumMovesPlayer2 - this.numOfDominatedColumns );

        if(minimumCoefficientRows < 0.0) {

            for (int currentRow = 0; currentRow < numRows; currentRow++) {

                for (int currentColumn = 0; currentColumn < numColumns; currentColumn++) {

                    if(adjustBySum) {

                            this.currentUtilitiesPayoffsNormalFormMatrixPlayer1
                                    [currentRow][currentColumn] += Math.abs(minimumCoefficientRows);

                    }
                    else {

                        this.currentUtilitiesPayoffsNormalFormMatrixPlayer1
                                [currentRow][currentColumn] -= Math.abs(minimumCoefficientRows);

                    }

                }

            }

        }

    }

    private void adjustMinimumCoefficientColumns(boolean adjustBySum, double minimumCoefficientColumns) {

        int numRows = ( this.totalNumMovesPlayer1 - this.numOfDominatedRows );
        int numColumns = ( this.totalNumMovesPlayer2 - this.numOfDominatedColumns );

        if(minimumCoefficientColumns < 0.0) {

            for (int currentRow = 0; currentRow < numRows; currentRow++) {

                for (int currentColumn = 0; currentColumn < numColumns; currentColumn++) {

                    if(adjustBySum) {

                            this.currentUtilitiesPayoffsNormalFormMatrixPlayer2
                                    [currentRow][currentColumn] += Math.abs(minimumCoefficientColumns);

                    }
                    else {

                        this.currentUtilitiesPayoffsNormalFormMatrixPlayer2
                                [currentRow][currentColumn] -= Math.abs(minimumCoefficientColumns);

                    }
                }

            }

        }

    }

    public double[] getLimitingConstraintsRowDominatedStrategy(int numRowToVerifyIfIsDominated) {

        int numColumns = ( this.totalNumMovesPlayer2 - this.numOfDominatedColumns );

        double[] limitingConstraintsDominatedStrategy = new double[ numColumns ];

        for(int currentColumn = 0; currentColumn < numColumns; currentColumn++) {

               limitingConstraintsDominatedStrategy[currentColumn] =
                            currentUtilitiesPayoffsNormalFormMatrixPlayer1
                                    [numRowToVerifyIfIsDominated][currentColumn];


        }

        return limitingConstraintsDominatedStrategy;

    }

    public double[] getLimitingConstraintsColumnDominatedStrategy(int numColumnToVerifyIfIsDominated) {

        int numRows = ( this.totalNumMovesPlayer1 - this.numOfDominatedRows );

        double[] limitingConstraintsDominatedStrategy = new double[ numRows ];

        for(int currentRow = 0; currentRow < numRows; currentRow++) {

            limitingConstraintsDominatedStrategy[currentRow] =
                    currentUtilitiesPayoffsNormalFormMatrixPlayer2
                            [currentRow][numColumnToVerifyIfIsDominated];


        }

        return limitingConstraintsDominatedStrategy;

    }

    public void solveLinearPrograming() {

        LinearProgramSolver linearProgramSolver  = SolverFactory.newDefault();

        this.linearProgramSolvedSolution = linearProgramSolver.solve(this.linearProgram);

        System.out.println("\n\n");

    }

    public void showSolution() {

        if (this.linearProgramSolvedSolution == null) {

            System.out.println("*********** NO SOLUTION FOUND ***********");

        }
        else {

            System.out.println("*********** SOLUTION ***********");

            this.linearProgramSolvedSolutionSum = 0.0;

            for
            (
                    int linearProgramSolvedSolutionVariable = 0;
                    linearProgramSolvedSolutionVariable
                                     <
                    this.linearProgramSolvedSolution.length;
                    linearProgramSolvedSolutionVariable++
            )
            {

                System.out.println
                        (
                                "x[" + linearProgramSolvedSolutionVariable + "] = " +
                                 this.linearProgramSolvedSolution[linearProgramSolvedSolutionVariable]
                        );


                this.linearProgramSolvedSolutionSum +=
                        this.linearProgramSolvedSolution[linearProgramSolvedSolutionVariable];

            }

            System.out.println("f(x) = " + this.linearProgram.evaluate(this.linearProgramSolvedSolution));

        }

        System.out.println("\n\n");

    }

    public void showLinearPrograming() {

        System.out.println("*********** LINEAR PROGRAMMING PROBLEM ***********");

        String formattedString;

        System.out.print("  Minimize: ");

        double[] functionToMinimize = this.linearProgram.getC();

        for (int coefficient = 0; coefficient < functionToMinimize.length; coefficient++)  {

            if (functionToMinimize[coefficient] != 0) {

                formattedString = String.format
                        (
                                Locale.US,"%+7.1f",
                                functionToMinimize[coefficient]
                        );

                System.out.print(formattedString + "*x[" + coefficient + "]");

            }

        }

        System.out.println();
        System.out.print("Subject To: ");

        ArrayList<Constraint> limitingConstraintsDominatingStrategiesList =
                              this.linearProgram.getConstraints();

        double limitingConstraintsDominatingStrategiesCell;

        double[] limitingConstraintsDominatingStrategiesRow = null;

        String inequalityString = null;

        for
        (
                int limitingConstraintsDominatingStrategiesIndex = 0;
                limitingConstraintsDominatingStrategiesIndex
                                      <
                limitingConstraintsDominatingStrategiesList.size();
                limitingConstraintsDominatingStrategiesIndex++
        )
        {

            if
            (
                    limitingConstraintsDominatingStrategiesList
                    .get(limitingConstraintsDominatingStrategiesIndex)
                    instanceof LinearSmallerThanEqualsConstraint
            )
            {

                inequalityString = " <= ";
                limitingConstraintsDominatingStrategiesRow =
                        ((LinearSmallerThanEqualsConstraint)
                                limitingConstraintsDominatingStrategiesList
                                        .get(limitingConstraintsDominatingStrategiesIndex))
                                        .getC();

            }

            if
            (
                    limitingConstraintsDominatingStrategiesList
                    .get(limitingConstraintsDominatingStrategiesIndex)
                            instanceof LinearBiggerThanEqualsConstraint
            )
            {

                inequalityString = " >= ";
                limitingConstraintsDominatingStrategiesRow =
                        ((LinearBiggerThanEqualsConstraint)
                                limitingConstraintsDominatingStrategiesList
                                        .get(limitingConstraintsDominatingStrategiesIndex))
                                        .getC();

            }

            if
            (
                    limitingConstraintsDominatingStrategiesList
                    .get(limitingConstraintsDominatingStrategiesIndex)
                            instanceof LinearEqualsConstraint
            )
            {

                inequalityString = " == ";
                limitingConstraintsDominatingStrategiesRow =
                        ((LinearEqualsConstraint)
                                limitingConstraintsDominatingStrategiesList
                                        .get(limitingConstraintsDominatingStrategiesIndex))
                                        .getC();

            }

            inequalityString = inequalityString +
                               String.format
                                       (
                                               Locale.US,"%6.1f",
                                               limitingConstraintsDominatingStrategiesList
                                                       .get(limitingConstraintsDominatingStrategiesIndex)
                                                       .getRHS()
                                       );

            if (limitingConstraintsDominatingStrategiesIndex != 0) {

                System.out.print("            ");

            }

            for
            (
                    int limitingConstraintsDominatingStrategiesVariable = 0;
                    limitingConstraintsDominatingStrategiesVariable
                                       <
                    this.linearProgram.getDimension();
                    limitingConstraintsDominatingStrategiesVariable++
            )
            {

                assert limitingConstraintsDominatingStrategiesRow != null;

                limitingConstraintsDominatingStrategiesCell =
                        limitingConstraintsDominatingStrategiesRow[limitingConstraintsDominatingStrategiesVariable];

                if (limitingConstraintsDominatingStrategiesCell != 0) {

                    formattedString = String.format(Locale.US,"%+7.1f",
                                                    limitingConstraintsDominatingStrategiesCell);
                    System.out.print(formattedString + "*x[" + limitingConstraintsDominatingStrategiesVariable + "]");

                }
                else {

                    System.out.print("            ");

                }

            }

            System.out.println(inequalityString);

        }

        System.out.println("\n\n");

    }

    public List<int[]> getNashEquilibriumAfterIteratedRemovalOfStrictlyDominatedStrategiesList() {

        int numRows = ( this.totalNumMovesPlayer1 - this.numOfDominatedRows );
        int numColumns = ( this.totalNumMovesPlayer2 - this.numOfDominatedColumns );

        this.showPlayerActionMoves(true, 1, this.initialLabelsMovesPlayer1);
        this.showPlayerActionMoves(false, 1, this.labelsMovesPlayer1);

        this.showPlayerActionMoves(true, 2, this.initialLabelsMovesPlayer2);
        this.showPlayerActionMoves(false, 2, this.labelsMovesPlayer2);

        List<int[]> nashEquilibriumAfterIteratedRemovalOfStrictlyDominatedStrategiesList = new ArrayList<>();

        for (int currentRow = 0; currentRow < numRows; currentRow++) {

            if( this.normalFormGame.getNormalFormGameRowPlayer1Considered(currentRow) ) {

                for (int currentColumn = 0; currentColumn < numColumns; currentColumn++) {

                    if ( this.normalFormGame.getNormalFormGameColumnPlayer2Considered(currentColumn) ) {

                        int[] nashEquilibriumPayoffMatrixIndexes = new int[2];

                        nashEquilibriumPayoffMatrixIndexes[0] = currentRow;
                        nashEquilibriumPayoffMatrixIndexes[1] = currentColumn;

                        nashEquilibriumAfterIteratedRemovalOfStrictlyDominatedStrategiesList
                                .add(nashEquilibriumPayoffMatrixIndexes);

                    }
                }


            }

        }

        return nashEquilibriumAfterIteratedRemovalOfStrictlyDominatedStrategiesList;

    }



    @Override
    public void execute() throws InterruptedException {

        // Waits until the Game Tree become known and available
        while(!this.isTreeKnown()) {

            System.err.println("Waiting for Game Tree to become Available...");

            //noinspection BusyWait
            Thread.sleep(1000);

        }

        System.out.println();

        // Prints the Basic Information about this Strategy
        System.out.println("Start playing with the Iterated Removal Of Strictly Dominated Strategies...\n\n");

        // Infinite Loop
        while(true) {

            PlayStrategy myStrategy = this.getStrategyRequest();

            // The Strategy chosen by me become NULL,
            // what means (probably) that the Game
            // was terminated by an outside event
            if(myStrategy == null) {

                // Breaks the Infinite Loop
                break;

            }

            this.startIteratedRemovalOfStrictlyDominatedStrategies( this.tree.getRootNode() );

            // My Play wasn't completed yet
            boolean playComplete = false;

            // While My Play isn't complete yet
            while(! playComplete ) {

                // Verify if the current Game Nodes for the Plays as both,
                // Player #1 and Player #2, are Terminal Game Nodes or not
                isATerminalGameNode
                        (
                                myStrategy,
                                this.tree.getNodeByIndex(myStrategy.getFinalP1Node()),
                                this.tree.getNodeByIndex(myStrategy.getFinalP2Node())
                        );

                // The Current Validation Set for the Available Moves
                Iterator<Integer> currentValidationSetIterator = tree.getValidationSet().iterator();

                int playerNum = 1;

                // Loop to check all the Entries of the Validation Set
                // (i.e., a Decision Node of the Tree representing the current Game)
                while(currentValidationSetIterator.hasNext()) {

                    this.tryToEliminateDominatedStrategies();

                    List<int[]> nashEquilibriumAfterIteratedRemovalOfStrictlyDominatedStrategiesList =
                                this.getNashEquilibriumAfterIteratedRemovalOfStrictlyDominatedStrategiesList();

                    int numberOfNashEquilibriumAfterIteratedRemovalOfStrictlyDominatedStrategies =
                            nashEquilibriumAfterIteratedRemovalOfStrictlyDominatedStrategiesList.size();

                    // Creates the array to keep the information for
                    // the Probabilities for all the Available Moves
                    double[] availableMovesProbabilities = new double[ currentValidationSetIterator.next() ];


                    String[] initialLabelsMovesPlayer1List = this.initialLabelsMovesPlayer1;

                    String[] initialLabelsMovesPlayer2List = this.initialLabelsMovesPlayer2;


                    if (numberOfNashEquilibriumAfterIteratedRemovalOfStrictlyDominatedStrategies == 0) {

                        // NOTE:
                        // - This never should happen because, if it's allowed Mixed-Strategies,
                        //   then every game with a finite number of players in which each player can choose from
                        //   finitely many pure strategies has at least one Nash equilibrium
                        System.out.println("It wasn't found any Pure Nash Equilibrium!!!\n\n");

                    }

                    if (numberOfNashEquilibriumAfterIteratedRemovalOfStrictlyDominatedStrategies == 1) {

                        System.out.println("It was found one Nash Equilibrium!!!\n\n");

                        int[] nashEquilibriumIndexes =
                              nashEquilibriumAfterIteratedRemovalOfStrictlyDominatedStrategiesList.get(0);

                        System.out.println("The Nash Equilibrium of this Game is: " +
                                           "(" +
                                            this.showPlayerActionMoveLabel
                                                    (
                                                            this.labelsMovesPlayer1[ nashEquilibriumIndexes[0] ]
                                                    ) +
                                            "," +
                                            this.showPlayerActionMoveLabel
                                                    (
                                                            this.labelsMovesPlayer2[ nashEquilibriumIndexes[1] ]
                                                    ) +
                                            ")\n\n");

                        int availableMovesProbability = 0;

                        if (playerNum == 1) {

                            for(String initialLabelMovePlayer1 : initialLabelsMovesPlayer1List) {

                                if
                                (
                                        this.showPlayerActionMoveLabel
                                                (initialLabelMovePlayer1)
                                                .equals
                                                        (
                                                                this.showPlayerActionMoveLabel
                                                                        (
                                                                            this.labelsMovesPlayer1
                                                                                    [ nashEquilibriumIndexes[0] ]
                                                                        )
                                                        )
                                )
                                {

                                    availableMovesProbabilities[ availableMovesProbability ] = 1.0;

                                }

                                availableMovesProbability++;

                            }

                        }
                        else {

                            for(String initialLabelMovePlayer2 : initialLabelsMovesPlayer2List) {

                                if
                                (
                                        this.showPlayerActionMoveLabel
                                                (initialLabelMovePlayer2)
                                                .equals
                                                        (
                                                                this.showPlayerActionMoveLabel
                                                                        (
                                                                                this.labelsMovesPlayer2
                                                                                        [ nashEquilibriumIndexes[1] ]
                                                                        )
                                                        )
                                )
                                {

                                    availableMovesProbabilities[ availableMovesProbability ] = 1.0;

                                }

                                availableMovesProbability++;

                            }
                        }

                    }

                    if (numberOfNashEquilibriumAfterIteratedRemovalOfStrictlyDominatedStrategies >= 2) {

                        System.out.println("It was found more than one Nash Equilibrium!!!");
                        System.out.println("It's needed to be used a Uniform Distribution of Probabilities " +
                                           "for all the Available Moves!!!\n\n");

                        List<String> nashEquilibriumMovesPlayer1 = new ArrayList<>();
                        List<String> nashEquilibriumMovesPlayer2 = new ArrayList<>();

                        System.out.println("The Nash Equilibrium for this Game are:\n");

                        for(int[] nashEquilibriumIndexes :
                                    nashEquilibriumAfterIteratedRemovalOfStrictlyDominatedStrategiesList) {

                            System.out.println("- (" +
                                    this.showPlayerActionMoveLabel
                                            (
                                                    this.labelsMovesPlayer1[nashEquilibriumIndexes[0]]
                                            ) +
                                    "," +
                                    this.showPlayerActionMoveLabel
                                            (
                                                    this.labelsMovesPlayer2[nashEquilibriumIndexes[1]]
                                            ) +
                                    ")\n\n");

                            if
                            (!nashEquilibriumMovesPlayer1.contains
                                    (
                                            this.showPlayerActionMoveLabel
                                                    (
                                                            this.labelsMovesPlayer1[nashEquilibriumIndexes[0]]
                                                    )
                                    )
                            ) {

                                System.out.println
                                        (
                                                "Adding " + this.showPlayerActionMoveLabel
                                                        (
                                                                this.labelsMovesPlayer1[nashEquilibriumIndexes[0]]
                                                        ) +
                                                        " to the list of Player #1 Moves, in Nash Equilibrium...\n\n"
                                        );

                                nashEquilibriumMovesPlayer1.add
                                        (
                                                this.showPlayerActionMoveLabel
                                                        (
                                                                this.labelsMovesPlayer1[nashEquilibriumIndexes[0]]
                                                        )
                                        );

                            }

                            if
                            (!nashEquilibriumMovesPlayer2.contains
                                    (
                                            this.showPlayerActionMoveLabel
                                                    (
                                                            this.labelsMovesPlayer2[nashEquilibriumIndexes[1]]
                                                    )
                                    )
                            ) {

                                System.out.println
                                        (
                                                "Adding " + this.showPlayerActionMoveLabel
                                                        (
                                                                this.labelsMovesPlayer2[nashEquilibriumIndexes[1]]
                                                        ) +
                                                        " to the list of Player #2 Moves, in Nash Equilibrium...\n\n"
                                        );

                                nashEquilibriumMovesPlayer2.add
                                        (
                                                this.showPlayerActionMoveLabel
                                                        (
                                                                this.labelsMovesPlayer2[nashEquilibriumIndexes[1]]
                                                        )
                                        );

                            }
                        }

                        if(nashEquilibriumMovesPlayer1.size() <= 2 || nashEquilibriumMovesPlayer2.size() <= 2) {

                            int availableMovesProbability = 0;

                            if(playerNum == 1) {

                                if(nashEquilibriumMovesPlayer1.size() == 1) {

                                    for(String initialLabelMovePlayer1 : initialLabelsMovesPlayer1List) {

                                        if
                                        (
                                                this.showPlayerActionMoveLabel
                                                        (initialLabelMovePlayer1)
                                                        .equals( nashEquilibriumMovesPlayer1.get(0) )
                                        )
                                        {

                                            availableMovesProbabilities[ availableMovesProbability ] = 1.0;

                                        }

                                        availableMovesProbability++;

                                    }

                                }
                                else if(nashEquilibriumMovesPlayer1.size() == 2) {

                                    int[][] utilitiesPayoffsNormalFormMatrixPlayer2 =
                                            this.normalFormGame.utilitiesPayoffsNormalFormMatrixPlayer2;

                                    if (nashEquilibriumMovesPlayer2.size() == 1) {

                                        double probabilityOfMovesPlayer1 =
                                                ( 1.0 / (double) nashEquilibriumMovesPlayer1.size() );

                                        for(String initialLabelMovePlayer1 : initialLabelsMovesPlayer1List) {

                                            for (String nashEquilibriumMovePlayer1 : nashEquilibriumMovesPlayer1) {

                                                if (this.showPlayerActionMoveLabel
                                                        (initialLabelMovePlayer1).equals(nashEquilibriumMovePlayer1)) {

                                                    availableMovesProbabilities
                                                            [availableMovesProbability] =
                                                            probabilityOfMovesPlayer1;

                                                    break;
                                                }

                                            }

                                            availableMovesProbability++;

                                        }

                                    }
                                    else if (nashEquilibriumMovesPlayer2.size() == 2) {

                                        double probabilityOfPlayingFirstMove =
                                                (double) (
                                                        utilitiesPayoffsNormalFormMatrixPlayer2[1][1]
                                                                           -
                                                        utilitiesPayoffsNormalFormMatrixPlayer2[1][0]
                                                )
                                                /
                                                (double) (
                                                        utilitiesPayoffsNormalFormMatrixPlayer2[0][0]
                                                                           -
                                                        utilitiesPayoffsNormalFormMatrixPlayer2[1][0]
                                                                           -
                                                        utilitiesPayoffsNormalFormMatrixPlayer2[0][1]
                                                                           +
                                                        utilitiesPayoffsNormalFormMatrixPlayer2[1][1]
                                                );


                                        for(String initialLabelMovePlayer1 : initialLabelsMovesPlayer1List) {

                                            if
                                            (
                                                    this.showPlayerActionMoveLabel
                                                            (initialLabelMovePlayer1).equals(nashEquilibriumMovesPlayer1.get(0))
                                            )
                                            {

                                                availableMovesProbabilities[availableMovesProbability] =
                                                        probabilityOfPlayingFirstMove;

                                            }

                                            if
                                            (
                                                    this.showPlayerActionMoveLabel
                                                            (initialLabelMovePlayer1).equals(nashEquilibriumMovesPlayer1.get(1))
                                            )
                                            {

                                                availableMovesProbabilities[availableMovesProbability] =
                                                        ( 1.0 - probabilityOfPlayingFirstMove );

                                            }

                                            availableMovesProbability++;

                                        }

                                    }

                                }

                            }

                            if(playerNum == 2) {

                                if(nashEquilibriumMovesPlayer2.size() == 1) {

                                    for(String initialLabelMovePlayer2 : initialLabelsMovesPlayer2List) {

                                        if
                                        (
                                                this.showPlayerActionMoveLabel
                                                        (initialLabelMovePlayer2)
                                                        .equals( nashEquilibriumMovesPlayer2.get(0) )
                                        )
                                        {

                                            availableMovesProbabilities[ availableMovesProbability ] = 1.0;

                                        }

                                        availableMovesProbability++;

                                    }

                                }
                                else if(nashEquilibriumMovesPlayer2.size() == 2) {

                                    int[][] utilitiesPayoffsNormalFormMatrixPlayer1 =
                                            this.normalFormGame.utilitiesPayoffsNormalFormMatrixPlayer1;

                                    if (nashEquilibriumMovesPlayer1.size() == 1) {

                                        double probabilityOfMovesPlayer2 =
                                                ( 1.0 / (double) nashEquilibriumMovesPlayer2.size() );

                                        for(String initialLabelMovePlayer2 : initialLabelsMovesPlayer2List) {

                                            for (String nashEquilibriumMovePlayer2 : nashEquilibriumMovesPlayer2) {

                                                if (this.showPlayerActionMoveLabel
                                                        (initialLabelMovePlayer2).equals(nashEquilibriumMovePlayer2)) {

                                                    availableMovesProbabilities
                                                            [availableMovesProbability] =
                                                                    probabilityOfMovesPlayer2;

                                                    break;
                                                }

                                            }

                                            availableMovesProbability++;

                                        }

                                    }
                                    else if (nashEquilibriumMovesPlayer1.size() == 2) {

                                        double probabilityOfPlayingFirstMove =
                                                (double) (
                                                        utilitiesPayoffsNormalFormMatrixPlayer1[1][1]
                                                                            -
                                                        utilitiesPayoffsNormalFormMatrixPlayer1[0][1]
                                                )
                                                /
                                                (double) (
                                                        utilitiesPayoffsNormalFormMatrixPlayer1[0][0]
                                                                            -
                                                        utilitiesPayoffsNormalFormMatrixPlayer1[0][1]
                                                                            -
                                                        utilitiesPayoffsNormalFormMatrixPlayer1[1][0]
                                                                            +
                                                        utilitiesPayoffsNormalFormMatrixPlayer1[1][1]
                                                );


                                        for(String initialLabelMovePlayer2 : initialLabelsMovesPlayer2List) {

                                            if
                                            (
                                                    this.showPlayerActionMoveLabel
                                                            (initialLabelMovePlayer2).equals(nashEquilibriumMovesPlayer2.get(0))
                                            )
                                            {

                                                availableMovesProbabilities[availableMovesProbability] =
                                                        probabilityOfPlayingFirstMove;

                                            }

                                            if
                                            (
                                                    this.showPlayerActionMoveLabel
                                                            (initialLabelMovePlayer2).equals(nashEquilibriumMovesPlayer2.get(1))
                                            )
                                            {

                                                availableMovesProbabilities[availableMovesProbability] =
                                                        ( 1.0 - probabilityOfPlayingFirstMove );

                                            }

                                            availableMovesProbability++;

                                        }

                                    }

                                }

                            }

                        }

                    }

                    String[] availableMovesLabels;

                    if(playerNum == 1) {

                        availableMovesLabels = this.initialLabelsMovesPlayer1;

                    }
                    else {

                        availableMovesLabels = this.initialLabelsMovesPlayer2;

                    }


                    Iterator<String> availableMovesLabelsIterator = Arrays.asList(availableMovesLabels).iterator();


                    // Assigns all the current Available Probabilities to all the current Available Moves
                    for(double availableMoveProbability : availableMovesProbabilities) {

                        // The Strategy's Structure doesn't match the current Game
                        if(!availableMovesLabelsIterator.hasNext()) {

                            System.err.println("PANIC: Strategy's Structure doesn't match the current Game!!!");

                            return;

                        }

                        String availableMovesLabel = availableMovesLabelsIterator.next();

                        // Prints the assignment the current Available Probability to the current Move
                        System.out.println("Setting the Probability of " + availableMovesLabel +
                                " as " + availableMoveProbability + "!!!\n");

                        // Assigns the current Available Probability to the current Move
                        myStrategy.put(availableMovesLabel, availableMoveProbability );

                    }

                    System.out.println("\n\n");

                    playerNum++;

                }

                try {

                    // Sets and provides the final Strategy
                    this.provideStrategy(myStrategy);

                    // Sets My Play as completed, as long I'm finished playing
                    playComplete = true;

                }
                catch (InvalidStrategyException invalidStrategyException) {

                    System.err.println("Invalid Strategy: " + invalidStrategyException.getMessage());
                    invalidStrategyException.printStackTrace(System.err);

                }

            }

        }

    }

}
