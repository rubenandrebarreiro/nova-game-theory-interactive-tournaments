package play.iteratedremovaldominated;

import gametree.GameNode;
import play.NormalFormGame;
import play.PlayStrategy;
import play.Strategy;
import play.exception.InvalidStrategyException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static play.prisonersdilemma.utils.PrisonersDilemmaStrategyCommonUtils.isATerminalGameNode;

/**
 *
 * Computational Game Theory - 2019/2020
 * Faculty of Sciences and Technology of
 * New University of Lisbon (FCT NOVA | FCT/UNL)
 *
 * Iterated Removal of Strictly Dominated Strategies,
 * using NOVA GTI (Game Theory Interactive) Platform
 *
 * Iterated Removal of Strictly Dominated Strategies
 *
 * Authors:
 * - Pedro Lamarao Pais (Student no. 48247)
 *   - pg.pais@campus.fct.unl.pt
 * - Ruben Andre Barreiro (Student no. 42648)
 *   - r.barreiro@campus.fct.unl.pt
 *
 */

public class IteratedRemovalOfStrictlyDominatedStrategies extends Strategy {

    private GameNode gameRootNode;

    private int totalNumMovesPlayer1;
    private int totalNumMovesPlayer2;

    String[] labelsMovesPlayer1;
    String[] labelsMovesPlayer2;

    private int[][] utilitiesPayoffsNormalFormMatrixPlayer1;
    private int[][] utilitiesPayoffsNormalFormMatrixPlayer2;

    private NormalFormGame normalFormGame;

    //TODO static LinearProgram linearProgram;

    public void startIteratedRemovalOfStrictlyDominatedStrategies(GameNode gameRootNode) {

        this.gameRootNode = gameRootNode;

        this.totalNumMovesPlayer1 = this.gameRootNode.numberOfChildren();
        this.totalNumMovesPlayer2 = this.gameRootNode.getChildren().next()
                .numberOfChildren();

        this.utilitiesPayoffsNormalFormMatrixPlayer1 = new int[this.totalNumMovesPlayer1][this.totalNumMovesPlayer2];
        this.utilitiesPayoffsNormalFormMatrixPlayer2 = new int[this.totalNumMovesPlayer1][this.totalNumMovesPlayer2];

        this.labelsMovesPlayer1 = new String[totalNumMovesPlayer1];
        this.labelsMovesPlayer2 = new String[totalNumMovesPlayer2];

        this.buildUtilitiesPayoffsNormalFormMatrix();

        this.showInitialGameInformationForPlayers();

         this.normalFormGame =
                new NormalFormGame
                        (
                                this.utilitiesPayoffsNormalFormMatrixPlayer1,
                                this.utilitiesPayoffsNormalFormMatrixPlayer2,
                                this.labelsMovesPlayer1,
                                this.labelsMovesPlayer2
                        );



    }

    private void buildUtilitiesPayoffsNormalFormMatrix() {

        GameNode gameRootChildrenNodeMovePlayer1;
        GameNode gameRootChildrenNodeMovePlayer2;

        Iterator<GameNode> gameRootChildrenNodesPlayer1 =
                this.gameRootNode.getChildren();

        int utilitiesPayoffsNormalFormMatrixRow = 0;

        while (gameRootChildrenNodesPlayer1.hasNext()) {

            gameRootChildrenNodeMovePlayer1 = gameRootChildrenNodesPlayer1.next();

            this.labelsMovesPlayer1[utilitiesPayoffsNormalFormMatrixRow] =
                    gameRootChildrenNodeMovePlayer1.getLabel();

            int utilitiesPayoffsNormalFormMatrixColumn = 0;

            Iterator<GameNode> gameRootChildrenNodesPlayer2 =
                    gameRootChildrenNodeMovePlayer1.getChildren();

            while (gameRootChildrenNodesPlayer2.hasNext()) {

                gameRootChildrenNodeMovePlayer2 = gameRootChildrenNodesPlayer2.next();

                if (utilitiesPayoffsNormalFormMatrixRow == 0) {

                    labelsMovesPlayer2[utilitiesPayoffsNormalFormMatrixColumn] =
                            gameRootChildrenNodeMovePlayer2.getLabel();

                }

                utilitiesPayoffsNormalFormMatrixPlayer1
                        [utilitiesPayoffsNormalFormMatrixRow]
                        [utilitiesPayoffsNormalFormMatrixColumn] =
                        gameRootChildrenNodeMovePlayer2.getPayoffP1();

                utilitiesPayoffsNormalFormMatrixPlayer2
                        [utilitiesPayoffsNormalFormMatrixRow]
                        [utilitiesPayoffsNormalFormMatrixColumn] =
                        gameRootChildrenNodeMovePlayer2.getPayoffP2();

                utilitiesPayoffsNormalFormMatrixColumn++;

            }

            utilitiesPayoffsNormalFormMatrixRow++;

        }

    }

    public void showInitialGameInformationForPlayers() {

        this.showInitialPlayerActionMoves(1, this.labelsMovesPlayer1);
        this.showInitialPlayerActionMoves(2, this.labelsMovesPlayer2);

        this.showInitialPlayerPayoffUtilities(1, this.utilitiesPayoffsNormalFormMatrixPlayer1);
        this.showInitialPlayerPayoffUtilities(2, this.utilitiesPayoffsNormalFormMatrixPlayer2);

    }

    public void showInitialPlayerActionMoves(int numPlayer, String[] playerActionMovesLabels) {

        System.out.println("Possible Action Moves for Player #" + numPlayer + ":");

        for (String playerActionMovesLabel : playerActionMovesLabels) {

            System.out.println("   " +
                    this.showInitialPlayerActionMoveLabel
                            (
                                    playerActionMovesLabel
                            )
            );

        }

    }

    public String showInitialPlayerActionMoveLabel(String label) {

        return label.substring(label.lastIndexOf(':') + 1);

    }


    public void showInitialPlayerPayoffUtilities(int numPlayer, int[][] utilitiesPayoffsNormalFormMatrix) {

        int numColumns = utilitiesPayoffsNormalFormMatrix[0].length;

        System.out.println("Initial Utilities from Payoffs for Player " + numPlayer + ":");

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

    }

    public void tryToEliminateDominatedStrategies() {

        do {

            System.out.println("Trying to Eliminate Dominated Strategies in this Game...\n\n");

        }
        while
        (
                this.searchForIteratedDominatedStrategiesByRows()
                                        &&
                this.searchForIteratedDominatedStrategiesByColumns()
        );

        System.out.println("Procedure of Iterated Removal of Strictly Dominated Strategies done!!!\n\n");

    }

    public boolean searchForIteratedDominatedStrategiesByRows() {

        boolean iteratedDominatedStrategyByRowFound = false;

        for(int currentRow = 0; currentRow < this.totalNumMovesPlayer1; currentRow++) {

            boolean isCurrentRowADominatingStrategyUntilNow = false;

            if(!this.normalFormGame.getNormalFormGameRowPlayer1Considered(currentRow)) {

                continue;

            }

            for(int otherCurrentRow = 0; otherCurrentRow < this.totalNumMovesPlayer1; otherCurrentRow++) {

                if(!this.normalFormGame.getNormalFormGameRowPlayer1Considered(otherCurrentRow)) {

                    continue;

                }
                else {

                    if (currentRow == otherCurrentRow) {

                        continue;

                    }
                    else {

                        for (int currentColumn = 0; currentColumn < this.totalNumMovesPlayer2; currentColumn++) {

                            if(this.normalFormGame.getNormalFormGameColumnPlayer2Considered(currentColumn)) {

                                isCurrentRowADominatingStrategyUntilNow =
                                        (
                                                this.utilitiesPayoffsNormalFormMatrixPlayer1
                                                        [currentRow][currentColumn]
                                                                    >
                                                this.utilitiesPayoffsNormalFormMatrixPlayer1
                                                        [otherCurrentRow][currentColumn]
                                        );


                                if (!isCurrentRowADominatingStrategyUntilNow) {

                                    iteratedDominatedStrategyByRowFound = false;

                                    break;

                                }

                                if
                                (
                                        (currentColumn == (this.totalNumMovesPlayer2 - 1))
                                )
                                {


                                    iteratedDominatedStrategyByRowFound = true;

                                    String removalOfStrictlyDominatedStrategyInformationString =
                                            String.format
                                                    (
                                                            "The Row for the Player #1's Action Move %s dominates " +
                                                            "the Row for the Player #1's Action Move %s!!!\n" +
                                                            "The Row %s will be removed from the Current Game in " +
                                                            "a Normal-Form...\n\n",
                                                            this.labelsMovesPlayer1[currentColumn],
                                                            this.labelsMovesPlayer1[otherCurrentRow],
                                                            this.labelsMovesPlayer1[otherCurrentRow]
                                                    );

                                    System.out.println(removalOfStrictlyDominatedStrategyInformationString);

                                    this.normalFormGame.setNormalFormGameRowPlayer1AsNotConsidered(otherCurrentRow);

                                }

                            }

                        }

                    }
                }

                if (isCurrentRowADominatingStrategyUntilNow) {

                    break;

                }

            }

            if (isCurrentRowADominatingStrategyUntilNow) {

                break;

            }

        }

        return iteratedDominatedStrategyByRowFound;

    }

    public boolean searchForIteratedDominatedStrategiesByColumns() {

        boolean iteratedDominatedStrategyByColumnFound = false;

        for(int currentColumn = 0; currentColumn < this.totalNumMovesPlayer2; currentColumn++) {

            boolean isCurrentColumnADominatingStrategyUntilNow = false;

            if(!this.normalFormGame.getNormalFormGameColumnPlayer2Considered(currentColumn)) {

                continue;

            }

            for(int otherCurrentColumn = 0; otherCurrentColumn < this.totalNumMovesPlayer2; otherCurrentColumn++) {

                if(!this.normalFormGame.getNormalFormGameColumnPlayer2Considered(otherCurrentColumn)) {

                    continue;

                }
                else {

                    if (currentColumn == otherCurrentColumn) {

                        continue;

                    }
                    else {

                        for (int currentRow = 0; currentRow < this.totalNumMovesPlayer1; currentRow++) {

                            if(this.normalFormGame.getNormalFormGameRowPlayer1Considered(currentRow)) {

                                isCurrentColumnADominatingStrategyUntilNow =
                                        (
                                                this.utilitiesPayoffsNormalFormMatrixPlayer2
                                                        [currentRow][currentColumn]
                                                                    >
                                                this.utilitiesPayoffsNormalFormMatrixPlayer2
                                                        [currentRow][otherCurrentColumn]
                                        );


                                if (!isCurrentColumnADominatingStrategyUntilNow) {

                                    iteratedDominatedStrategyByColumnFound = false;

                                    break;

                                }

                                if
                                (
                                        ( currentRow == ( this.totalNumMovesPlayer1 - 1 ) )
                                )
                                {

                                    iteratedDominatedStrategyByColumnFound = true;

                                    String removalOfStrictlyDominatedStrategyInformationString =
                                            String.format
                                                    (
                                                            "The Column for the Player #2's Action Move %s dominates " +
                                                            "the Column for the Player #2's Action Move %s!!!\n" +
                                                            "The Column %s will be removed from the Current Game in " +
                                                            "a Normal-Form...\n\n",
                                                            this.labelsMovesPlayer2[currentColumn],
                                                            this.labelsMovesPlayer2[otherCurrentColumn],
                                                            this.labelsMovesPlayer2[otherCurrentColumn]
                                                    );

                                    System.out.println(removalOfStrictlyDominatedStrategyInformationString);

                                    this.normalFormGame.setNormalFormGameColumnPlayer2AsNotConsidered(otherCurrentColumn);

                                }

                            }

                        }

                    }

                }

                if (isCurrentColumnADominatingStrategyUntilNow) {

                    break;

                }

            }

            if (isCurrentColumnADominatingStrategyUntilNow) {

                break;

            }

        }

        return iteratedDominatedStrategyByColumnFound;

    }

    public List<int[]> getNashEquilibriumAfterIteratedRemovalOfStrictlyDominatedStrategiesList() {

        List<int[]> nashEquilibriumAfterIteratedRemovalOfStrictlyDominatedStrategiesList = new ArrayList<>();

        for (int currentRow = 0; currentRow < this.totalNumMovesPlayer1; currentRow++) {

            if( this.normalFormGame.getNormalFormGameRowPlayer1Considered(currentRow) ) {

                for (int currentColumn = 0; currentColumn < this.totalNumMovesPlayer2; currentColumn++) {

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

                // The Labels for All the Available Moves
                Iterator<String> availableMovesLabels = myStrategy.keyIterator();

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

                    if(numberOfNashEquilibriumAfterIteratedRemovalOfStrictlyDominatedStrategies == 0) {

                        // NOTE:
                        // - This never should happen because, if it's allowed Mixed-Strategies,
                        //   then every game with a finite number of players in which each player can choose from
                        //   finitely many pure strategies has at least one Nash equilibrium
                        System.out.println("It wasn't found any Pure Nash Equilibrium!!!\n\n");

                    }
                    else if (numberOfNashEquilibriumAfterIteratedRemovalOfStrictlyDominatedStrategies == 1) {

                        System.out.println("It was found one Nash Equilibrium!!!\n\n");

                        int[] nashEquilibriumIndexes =
                              nashEquilibriumAfterIteratedRemovalOfStrictlyDominatedStrategiesList.get(0);

                        System.out.println("The Nash Equilibrium of this Game is: " +
                                           "(" +
                                            this.showInitialPlayerActionMoveLabel
                                                    (
                                                            this.labelsMovesPlayer1[ nashEquilibriumIndexes[0] ]
                                                    ) +
                                            "," +
                                            this.showInitialPlayerActionMoveLabel
                                                    (
                                                            this.labelsMovesPlayer2[ nashEquilibriumIndexes[1] ]
                                                    ) +
                                            ")");

                        if (playerNum == 1) {

                            availableMovesProbabilities[ nashEquilibriumIndexes[0] ] = 1.0;

                        }
                        else {

                            availableMovesProbabilities[ nashEquilibriumIndexes[1] ] = 1.0;

                        }

                    }
                    else {

                        System.out.println("It was found more than one Nash Equilibrium!!!");
                        System.out.println("It's needed to be used a Mixed-Strategy!!!\n");

                        // TODO - Mixed Strategies

                    }

                    // Assigns all the current Available Probabilities to all the current Available Moves
                    for(double availableMoveProbability : availableMovesProbabilities) {

                        // The Strategy's Structure doesn't match the current Game
                        if(!availableMovesLabels.hasNext()) {

                            System.err.println("PANIC: Strategy's Structure doesn't match the current Game!!!");

                            return;

                        }

                        String availableMovesLabel = availableMovesLabels.next();

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
