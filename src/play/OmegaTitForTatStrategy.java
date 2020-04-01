package play;

import gametree.GameNode;
import gametree.GameNodeDoesNotExistException;
import play.exception.InvalidStrategyException;

import java.util.*;

import static play.StrategyCommonUtils.isATerminalGameNode;

/**
 *
 * Computational Game Theory - 2019/2020
 * Faculty of Sciences and Technology of
 * New University of Lisbon (FCT NOVA)
 *
 * 1st Tournament - Prisoner's Dilemma,
 * using NOVA GTI (Game Theory Interactive) Platform
 *
 * Omega Tit For Tat Strategy
 *
 * Authors:
 * - Pedro Lamarao Pais (Student no. 48247)
 *   - pgp@campus.fct.unl.pt
 * - Ruben Andre Barreiro (Student no. 42648)
 *   - r.barreiro@campus.fct.unl.pt
 *
 */

public class OmegaTitForTatStrategy extends Strategy {

    // Constants:

    /**
     * The Randomness Threshold Factor of 0.25 (i.e., 25% of the rounds) of the Game.
     *
     * NOTES:
     * - The analysis and respective adjustment for this Threshold value
     *   should be considered, in separately, for each kind of different Game Played;
     */
    private static final float RANDOMNESS_THRESHOLD_FACTOR = 0.25f;

    /**
     * The total number of Game Stages/Rounds played, until the moment.
     */
    private static int TOTAL_NUM_OF_GAME_STAGES_ROUNDS;

    /**
     * The maximum number of times for allowing My Opponent's Moves changes,
     * meaning after that, I will start to play Always Defect from then.
     */
    private static int MAX_NUM_OF_TIMES_FOR_ALLOWING_MY_OPPONENT_MOVES_CHANGES =
            Math.round( ( RANDOMNESS_THRESHOLD_FACTOR * TOTAL_NUM_OF_GAME_STAGES_ROUNDS) );


    // Global Instance Variables:

    /**
     * The number of Game Stages/Rounds currently played.
     */
    private int numOfGameStagesRoundsCurrentlyPlayed;

    /**
     * The number of times that My Opponent's Moves changed, every time he played.
     */
    private int totalNumOfTimesMyOpponentMovesChanged;

    /**
     * The boolean value to keep the information about
     * if I am currently defecting as punishment for
     * the possible randomness behaviour from My Opponent.
     *
     * NOTES:
     * - This flag should be changed to true, after the Randomness Threshold Factor
     *   applied to the total number of times that My Opponent's Moves changed,
     *   every time he played;
     */
    private boolean defectingAsPunishment;


    // Public Methods/Procedures:

    /**
     * Executes the Omega Tit For Tat Strategy, for the current Game being played.
     *
     * @throws InterruptedException an Interrupted Exception, in the case of,
     *         the current Game crashes, for any reason
     */
    @Override
    public void execute() throws InterruptedException {

        // Waits until the Game Tree become known and available
        while(!this.isTreeKnown()) {

            System.err.println("Waiting for Game Tree to become Available...");

            //noinspection BusyWait
            Thread.sleep(1000);

        }

        GameNode finalGameNodePlayer1 = null;
        GameNode finalGameNodePlayer2 = null;

        // Infinite Loop
        while(true) {

            PlayStrategy myStrategy = this.getStrategyRequest();

            // The Strategy chosen by me become NULL,
            // what means (probably) that the Game
            // was terminated by an outside event
            if (myStrategy == null) {

                // Breaks the Infinite Loop
                break;

            }

            // Prints the Basic Information about this Strategy
            System.out.println("Start playing with the Omega Tit For Tat Strategy...");

            // My Play wasn't completed yet
            boolean playComplete = false;

            // While My Play isn't complete yet
            while( !playComplete ) {

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

                // The Game doesn't have any final Game Node for the last Game Stage/Round // TODO
                if( ( myStrategy.getFinalP1Node() != -1 ) && ( myStrategy.getFinalP2Node() != -1 ) ) {

                    finalGameNodePlayer1 = this.tree.getNodeByIndex(myStrategy.getFinalP1Node());
                    finalGameNodePlayer2 = this.tree.getNodeByIndex(myStrategy.getFinalP2Node());

                    // This is the first Game Stage/Round, so I will be nice and start Cooperating
                    if ((finalGameNodePlayer1 == null) || (finalGameNodePlayer2 == null)) {

                        // Loop to check all the Entries of the Validation Set
                        // (i.e., a Decision Node of the Tree representing the current Game)
                        while (currentValidationSetIterator.hasNext()) {

                            // Creates the array to keep the information for
                            // the Probabilities for all the Available Moves
                            double[] availableMovesProbabilities = new double[currentValidationSetIterator.next()];

                            // Sets the Probability for the Cooperate (C) Move as 1.0
                            availableMovesProbabilities[0] = 1.0;

                            // Sets the Probability for the Defect (D) Move as 0.0
                            availableMovesProbabilities[1] = 0.0;


                            // Assigns all the current Available Probabilities to all the current Available Moves
                            for (double availableMoveProbability : availableMovesProbabilities) {

                                // The Strategy's Structure doesn't match the current Game
                                if (!availableMovesLabels.hasNext()) {

                                    System.err.println("PANIC: Strategy's Structure doesn't match the current Game!!!");

                                    return;

                                }

                                // Assigns the current Available Probability to the current
                                myStrategy.put(availableMovesLabels.next(), availableMoveProbability);

                            }

                        }
                    }
                    // This are the remaining Game Stages/Rounds, so I need to analyse/infer what My Opponent
                    // did in the previous Game Stages/Rounds, specially in the last one
                    else {

                        List<GameNode> listOfOpponentLastMovesAsPlayer1 = getReversePath( finalGameNodePlayer1 );
                        List<GameNode> listOfOpponentLastMovesAsPlayer2 = getReversePath( finalGameNodePlayer2 );

                        try {

                            computeOmegaTitForTatStrategy
                                    (
                                            listOfOpponentLastMovesAsPlayer1,
                                            listOfOpponentLastMovesAsPlayer2,
                                            myStrategy
                                    );

                        }
                        catch (GameNodeDoesNotExistException gameNodeDoesNotExistException) {

                            System.err.println("PANIC: Strategy's Structure doesn't match the current Game!!!");

                        }

                    }

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

    private List<GameNode> getReversePath(GameNode currentGameNode) {

        try {

            GameNode ancestorOfCurrentGameNode = currentGameNode.getAncestor();
            List<GameNode> l =  getReversePath(ancestorOfCurrentGameNode);
            l.add(currentGameNode);

            return l;

        }
        catch (GameNodeDoesNotExistException e) {

            List<GameNode> l = new ArrayList<GameNode>();
            l.add(currentGameNode);

            return l;

        }

    }

    private void computeOmegaTitForTatStrategy
            (
                    List<GameNode> listOfOpponentLastMovesAsPlayer1,
                    List<GameNode> listOfOpponentLastMovesAsPlayer2,
                    PlayStrategy myStrategy
            )
            throws GameNodeDoesNotExistException
    {

        Set<String> myOpponentMoves = new HashSet<String>();

        // As I played as Player 1, I am gonna check what were the moves
        // of My Opponent as Player 2, in the last Game Stage/Round
        for(GameNode myOpponentMovesAsPlayer1GameNode : listOfOpponentLastMovesAsPlayer1) {

            if ( ( myOpponentMovesAsPlayer1GameNode.isNature() )
                    ||
                    (myOpponentMovesAsPlayer1GameNode.isRoot() ) )
            {

                continue;

            }

            // Check/Verify that My Opponent, really played as Player 2,
            // in the last Game Stage/Round
            if( myOpponentMovesAsPlayer1GameNode.getAncestor().isPlayer2() ) {

                myOpponentMoves.add( myOpponentMovesAsPlayer1GameNode.getLabel() );

            }

        }

        // As I played as Player 2, I am gonna check what were the moves
        // of My Opponent as Player 1, in the last Game Stage/Round
        for(GameNode myOpponentMovesAsPlayer2GameNode : listOfOpponentLastMovesAsPlayer2) {

            if( ( myOpponentMovesAsPlayer2GameNode.isNature() )
                    ||
                    ( myOpponentMovesAsPlayer2GameNode.isRoot() ) )
            {

                continue;

            }

            // Check/Verify that My Opponent, really played as Player 1,
            // in the last Game Stage/Round
            if(myOpponentMovesAsPlayer2GameNode.getAncestor().isPlayer1()) {

                myOpponentMoves.add( myOpponentMovesAsPlayer2GameNode.getLabel() );

            }
        }

        // Now, I'm gonna set my Omega Tit For Tat Strategy to define
        // the probabilities of my Moves, considering the Moves used
        // by My Opponent, in the previous round
        Iterator<String> myMovesLabels = myStrategy.keyIterator();

        while( myMovesLabels.hasNext() ) {

            String myCurrentMoveLabel = myMovesLabels.next();

            if( myOpponentMoves.contains(myCurrentMoveLabel) ) {

                double value = 1.0;

                myStrategy.put(myCurrentMoveLabel, value);
                System.err.println("Setting " + myCurrentMoveLabel + " to probability as " + value);

            }
            else {

                double value = 0.0;

                myStrategy.put(myCurrentMoveLabel, value);

                System.err.println("Setting " + myCurrentMoveLabel + " to probability as " + value);

            }

        }


        //The following piece of code has the goal of checking if there was a portion
        //of the game for which we could not infer the moves of the adversary (because
        //none of the games in the previous round pass through those paths)
        Iterator<Integer> validationSetIterator = tree.getValidationSet().iterator();

        myMovesLabels = myStrategy.keyIterator();

        while(validationSetIterator.hasNext()) {

            int possibleMoves = validationSetIterator.next();

            String[] labels = new String[possibleMoves];

            double[] values = new double[possibleMoves];

            double sum = 0;

            for(int i = 0; i < possibleMoves; i++) {

                labels[i] = myMovesLabels.next();

                values[i] = myStrategy.get(labels[i]);

                sum += values[i];

            }

            // In the previous game we could not infer what the adversary played here
            if(sum != 1) {

                //Random move on this validation set
                sum = 0;

                for(int i = 0; i < values.length - 1; i++) {

                    values[i] = 0.0; //random.nextDouble();

                    while(sum + values[i] >= 1) values[i] = 0.0; //random.nextDouble();

                    sum = sum + values[i];

                }


                values[values.length-1] = ((double) 1) - sum;

                for(int i = 0; i < possibleMoves; i++) {

                    myStrategy.put(labels[i], values[i]);
                    System.err.println("Unexplored path: Setting " + labels[i] + " to prob " + values[i]);

                }

            }

        }

    }

    public void checkIfMyOpponentIsBehavingRandom() {

        if( this.totalNumOfTimesMyOpponentMovesChanged
                                >=
            MAX_NUM_OF_TIMES_FOR_ALLOWING_MY_OPPONENT_MOVES_CHANGES )
        {

            this.defectingAsPunishment = true;

        }

    }

}
