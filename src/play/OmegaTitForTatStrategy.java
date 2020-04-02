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
 * New University of Lisbon (FCT NOVA | FCT/UNL)
 *
 * 1st Tournament - Prisoner's Dilemma,
 * using NOVA GTI (Game Theory Interactive) Platform
 *
 * Omega Tit For Tat Strategy (General)
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
     * The Randomness Threshold Factor of 0.25 (i.e., 25% of the Stages/Rounds) of the Game.
     *
     * NOTES:
     * - The analysis and respective adjustment for this Randomness Threshold value
     *   should be considered, in separately, for each kind of different Game Played;
     */
    private static final float RANDOMNESS_THRESHOLD_FACTOR = 0.25f;

    /**
     * The String Label for the Cooperate Move, in Upper Case.
     */
    private static final String COOPERATE = "COOPERATE";

    /**
     * The String Label for the Defect Move, in Upper Case.
     */
    private static final String DEFECT = "DEFECT";

    /**
     * The String Label for the Unknown Move, in Upper Case.
     */
    private static final String UNKNOWN = "UNKNOWN";

    /**
     * The String Label for the Cooperate Move, as Player #1
     */
    private static final String COOPERATE_AS_PLAYER_1 = "1:1:Cooperate";

    /**
     * The String Label for the Defect Move, as Player #1
     */
    private static final String DEFECT_AS_PLAYER_1 = "1:1:Defect";

    /**
     * The String Label for the Cooperate Move, as Player #2
     */
    private static final String COOPERATE_AS_PLAYER_2 = "2:1:Cooperate";

    /**
     * The String Label for the Defect Move, as Player #1
     */
    private static final String DEFECT_AS_PLAYER_2 = "2:1:Defect";

    /**
     * The total number of Game Stages/Rounds played, until the moment.
     */
    private static int TOTAL_NUM_OF_GAME_STAGES_ROUNDS;

    /**
     * The maximum number of times for allowing My Opponent's Moves changes,
     * meaning after that, I will start to play Always Defect from then.
     */
    private static int MAX_NUM_OF_TIMES_FOR_ALLOWING_MY_OPPONENT_MOVES_CHANGES;


    // Global Instance Variables:

    /**
     * The number of Game Stages/Rounds currently played.
     */
    private int numOfGameStagesRoundsCurrentlyPlayed;

    /**
     * The Last Move played by My Opponent, in the last Game Stage/Round, as Player #1.
     */
    private String myOpponentMovePlayedInLastGameStageRoundAsPlayer1;

    /**
     * The Last Move played by My Opponent, in the last Game Stage/Round, as Player #2.
     */
    private String myOpponentMovePlayedInLastGameStageRoundAsPlayer2;

    /**
     * The number of times that My Opponent's Moves changed, every time he played, as Player #1.
     */
    private int totalNumOfTimesMyOpponentMovesChangedAsPlayer1;

    /**
     * The number of times that My Opponent's Moves changed, every time he played, as Player #2.
     */
    private int totalNumOfTimesMyOpponentMovesChangedAsPlayer2;

    /**
     * The boolean value to keep the information about
     * if I am currently defecting as punishment for
     * the possible randomness behaviour from My Opponent, as Player #1.
     *
     * NOTES:
     * - This flag should be changed to true, after the Randomness Threshold Factor
     *   applied to the total number of times that My Opponent's Moves changed,
     *   as Player #1, every time he played;
     */
    private boolean defectingAsPunishmentToMyOpponentAsPlayer1;

    /**
     * The boolean value to keep the information about
     * if I am currently defecting as punishment for
     * the possible randomness behaviour from My Opponent, as Player #2.
     *
     * NOTES:
     * - This flag should be changed to true, after the Randomness Threshold Factor
     *   applied to the total number of times that My Opponent's Moves changed,
     *   as Player #2, every time he played;
     */
    private boolean defectingAsPunishmentToMyOpponentAsPlayer2;


    // Public Methods/Procedures:

    /**
     * Executes the Omega Tit For Tat Strategy, for the current Game being played.
     *
     * @throws InterruptedException an Interrupted Exception, in the case of,
     *         the current Game crashes, for any reason
     */
    @Override
    public void execute() throws InterruptedException {

        System.out.println("\n\n");

        // Waits until the Game Tree become known and available
        while(!this.isTreeKnown()) {

            System.err.println("Waiting for Game Tree to become Available...");

            //noinspection BusyWait
            Thread.sleep(1000);

        }

        GameNode finalGameNodePlayer1;
        GameNode finalGameNodePlayer2;


        this.setupOmegaTitForTatParametersConfiguration();

        this.printOmegaTitForTatParametersConfiguration();


        // Prints the Basic Information about this Strategy
        System.out.println("Start playing with the Omega Tit For Tat Strategy...\n\n");


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

            // My Play wasn't completed yet
            boolean playComplete = false;


            // While My Play isn't complete yet
            while( !playComplete ) {

                System.out.println("The current play isn't completed yet...\n\n");

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

                System.out.println("I will define My Strategy for the next Game Stage/Round...\n\n");

                finalGameNodePlayer1 = this.tree.getNodeByIndex(myStrategy.getFinalP1Node());
                finalGameNodePlayer2 = this.tree.getNodeByIndex(myStrategy.getFinalP2Node());


                System.out.println("This it's the Game Stage/Round #" +
                        ( this.numOfGameStagesRoundsCurrentlyPlayed + 1 ) +"...\n\n");


                // This is the first Game Stage/Round, so I will be nice and start Cooperating
                if ((finalGameNodePlayer1 == null) || (finalGameNodePlayer2 == null)) {

                    int actingAsPlayerNum = 1;

                    // Loop to check all the Entries of the Validation Set
                    // (i.e., a Decision Node of the Tree representing the current Game)
                    while ( currentValidationSetIterator.hasNext() ) {

                        // Creates the array to keep the information for
                        // the Probabilities for all the Available Moves
                        double[] availableMovesProbabilities = new double[ currentValidationSetIterator.next() ];

                        System.out.println("I will start nicely, and start to Cooperate as Player #" +
                                           actingAsPlayerNum + "\n\n");

                        // Sets the Probability for the Cooperate (C) Move as 1.0,
                        // because I will start nicely
                        availableMovesProbabilities[0] = 1.0;

                        // Sets the Probability for the Defect (D) Move as 0.0,
                        // because I will start nicely
                        availableMovesProbabilities[1] = 0.0;

                        // Assigns all the current Available Probabilities to all the current Available Moves
                        for (double availableMoveProbability : availableMovesProbabilities) {

                            // The Strategy's Structure doesn't match the current Game
                            if (!availableMovesLabels.hasNext()) {

                                System.err.println
                                        (
                                                "PANIC: Strategy's Structure doesn't match the current Game!!!"
                                        );

                                return;

                            }

                            String nextAvailableMovesLabel = availableMovesLabels.next();

                            System.out.println("I will assign the Probability of " + availableMoveProbability +
                                               " for the Move: " + nextAvailableMovesLabel.split(":")[2] +
                                               " as Player #" + actingAsPlayerNum);

                            // Assigns the current Available Probability to the current
                            myStrategy.put(nextAvailableMovesLabel, availableMoveProbability);

                        }

                        System.out.println("\n\n");

                        actingAsPlayerNum++;

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

                // Increases the number of the Game Stages/Rounds currently played, until now
                this.numOfGameStagesRoundsCurrentlyPlayed++;

            }

        }

    }

    /**
     * Performs the Setup of the Parameters' Configuration for the Omega Tit For Tat Strategy.
     */
    private void setupOmegaTitForTatParametersConfiguration() {

        this.myOpponentMovePlayedInLastGameStageRoundAsPlayer1 = "";
        this.myOpponentMovePlayedInLastGameStageRoundAsPlayer2 = "";

        // TODO number of rounds???
        setTotalNumOfGameStagesRounds( 20 );

        setMaxNumOfTimesForAllowingMyOpponentMovesChanges();

        this.numOfGameStagesRoundsCurrentlyPlayed = 0;

        this.totalNumOfTimesMyOpponentMovesChangedAsPlayer1 = 0;
        this.totalNumOfTimesMyOpponentMovesChangedAsPlayer2 = 0;

        this.defectingAsPunishmentToMyOpponentAsPlayer1 = false;
        this.defectingAsPunishmentToMyOpponentAsPlayer2 = false;

    }

    /**
     * Prints the Setup of the Parameters' Configuration for the Omega Tit For Tat Strategy.
     */
    private void printOmegaTitForTatParametersConfiguration() {

        System.out.println("--- INFORMATION ABOUT THE PARAMETERS' CONFIGURATION FOR " +
                           "THE OMEGA TIT FOR TAT STRATEGY ---");

        System.out.println("\n");

        System.out.println("    Total Number of Game Stages/Rounds:");
        System.out.println("    - " + TOTAL_NUM_OF_GAME_STAGES_ROUNDS);

        System.out.println("    Randomness Threshold Factor, in use:");
        System.out.println("    - " + RANDOMNESS_THRESHOLD_FACTOR);

        System.out.println("    Maximum Number of Times for allowing My Opponent's Moves Changes:");
        System.out.println("    - " + MAX_NUM_OF_TIMES_FOR_ALLOWING_MY_OPPONENT_MOVES_CHANGES);

        System.out.println("\n\n");

    }

    /**
     * Sets the Total Number of the current Game Stages/Rounds.
     *
     * @param totalNumOfGameStagesRounds the Total Number of the current Game Stages/Rounds
     */
    public static void setTotalNumOfGameStagesRounds(int totalNumOfGameStagesRounds) {

        TOTAL_NUM_OF_GAME_STAGES_ROUNDS = totalNumOfGameStagesRounds;

    }

    /**
     * Sets the Maximum Number of Times for allowing My Opponent's Moves Changes.
     */
    public static void setMaxNumOfTimesForAllowingMyOpponentMovesChanges() {

        MAX_NUM_OF_TIMES_FOR_ALLOWING_MY_OPPONENT_MOVES_CHANGES =
                ( (int) Math.round
                        (
                                (
                                        ( (double) RANDOMNESS_THRESHOLD_FACTOR )
                                                            *
                                        ( (double) TOTAL_NUM_OF_GAME_STAGES_ROUNDS)
                                )
                        )
                );

    }

    /**
     * Returns the Reverse Path, from a given current Game Node, in the tree.
     *
     * NOTE:
     * - This method/procedure it's useful for analyse My Opponent's Moves;
     *
     * @param currentGameNode current Game Node, in the tree
     *
     * @return the Reverse Path, from a given current Game Node, in the tree
     */
    private List<GameNode> getReversePath(GameNode currentGameNode) {

        try {

            GameNode ancestorOfCurrentGameNode = currentGameNode.getAncestor();

            List<GameNode> myOpponentLastMovePlayedGameNode = getReversePath(ancestorOfCurrentGameNode);

            myOpponentLastMovePlayedGameNode.add(currentGameNode);

            return myOpponentLastMovePlayedGameNode;

        }
        catch (GameNodeDoesNotExistException gameNodeDoesNotExistException) {

            List<GameNode> myOpponentLastMoveGameNode = new ArrayList<>();

            myOpponentLastMoveGameNode.add(currentGameNode);

            return myOpponentLastMoveGameNode;

        }

    }

    /**
     * Computes the Omega Tit For Tat Strategy, for the current Game, being played.
     *
     * @param listOfOpponentLastMovesAsPlayer1 the list of My Opponent Last Moves, as Player #1
     *
     * @param listOfOpponentLastMovesAsPlayer2 the list of My Opponent Last Moves, as Player #2
     *
     * @param myStrategy the strategy I'm currently using, with my moves
     *
     * @throws GameNodeDoesNotExistException an Exception to be thrown, in the case of,
     *         an accessed Game Node, in the tree, does not exist
     */
    private void computeOmegaTitForTatStrategy
            (
                    List<GameNode> listOfOpponentLastMovesAsPlayer1,
                    List<GameNode> listOfOpponentLastMovesAsPlayer2,
                    PlayStrategy myStrategy
            )
            throws GameNodeDoesNotExistException
    {

        Set<String> myOpponentMovesInLastGameStageRound = new HashSet<>();

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

                myOpponentMovesInLastGameStageRound.add( myOpponentMovesAsPlayer1GameNode.getLabel() );

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

                myOpponentMovesInLastGameStageRound.add( myOpponentMovesAsPlayer2GameNode.getLabel() );

            }
        }

        for(String myOpponentMoveInLastGameStageRound : myOpponentMovesInLastGameStageRound) {

            String[] myOpponentMoveInLastGameStageRoundParts =
                            myOpponentMoveInLastGameStageRound.split(":");

            // Analysing the Last Move Played by My Opponent, as Player #1
            if(Integer.parseInt(myOpponentMoveInLastGameStageRoundParts[0]) == 1) {

                System.out.println("Analysing the Last Move Played by My Opponent, as Player #1...\n\n");

                if( ( this.myOpponentMovePlayedInLastGameStageRoundAsPlayer1.equalsIgnoreCase("") )
                                                        &&
                    ( this.numOfGameStagesRoundsCurrentlyPlayed == 1 ) )
                {

                    this.myOpponentMovePlayedInLastGameStageRoundAsPlayer1 =
                                            myOpponentMoveInLastGameStageRoundParts[2].toUpperCase();

                }
                else {

                        if( ( !this.myOpponentMovePlayedInLastGameStageRoundAsPlayer1.equalsIgnoreCase("") )
                                                         &&
                            ( !this.myOpponentMovePlayedInLastGameStageRoundAsPlayer1
                                  .equalsIgnoreCase( myOpponentMoveInLastGameStageRoundParts[2].toUpperCase() ) ) )
                    {

                        System.out.println("My Opponent changed his behaviour (and the previous last Move played), " +
                                           "as Player #1 and I will memorise that!!!\n\n");

                        this.myOpponentMovePlayedInLastGameStageRoundAsPlayer1 =
                                myOpponentMoveInLastGameStageRoundParts[2].toUpperCase();

                        this.totalNumOfTimesMyOpponentMovesChangedAsPlayer1++;

                    }

                }

                this.checkIfMyOpponentIsBehavingRandomAsPayer1();

                // It seems that My Opponent behaved randomly as Player #1, until now,
                // so I will act severely and I am gonna punish him always from now,
                // with an All Defect Strategy
                if(this.defectingAsPunishmentToMyOpponentAsPlayer1) {

                    System.out.println("My Opponent played Moves, behaving randomly, as Player #1, " +
                                       "in the last Game Stages/Rounds...\n\n");

                    // As My Opponent, behaved randomly, as Player #1, in the previous Game Stages/Rounds,
                    // I am gonna also play a DEFECT move in this Game Stage/Round and from now,
                    // as a response to that, as Player #2
                    System.out.println("Setting the Probability of " + COOPERATE_AS_PLAYER_2 +
                                       " as " + 0.0 + "!!!");
                    System.out.println("Setting the Probability of " + DEFECT_AS_PLAYER_2 +
                                       " as " + 1.0 + "!!!");

                    System.out.println("\n\n");

                    // Setting My Strategy, accordingly to the previously mentioned description
                    myStrategy.put(COOPERATE_AS_PLAYER_2, 0.0);
                    myStrategy.put(DEFECT_AS_PLAYER_2, 1.0);

                }

                // It seems that My Opponent DO NOT behaved randomly as Player #1, until now,
                // so I will act nicely and I am gonna mimic him,
                // with a general and well known, Tit For Tat Strategy
                else {

                    // As Player #2, I will play and act, following the general and well known,
                    // Tit For Tat Strategy and mimic My Opponent's last Move played as Player #1,
                    // as response to that

                    // My Opponent played a COOPERATE move, in the last Game Stage/Round
                    if(myOpponentMoveInLastGameStageRoundParts[2].equalsIgnoreCase(COOPERATE)) {

                        System.out.println("My Opponent played " + COOPERATE + " Move, as Player #1, " +
                                           "in the last Game Stage/Round...\n\n");

                        // As My Opponent, played a COOPERATE move, in the last Game Stage/Round,
                        // I am gonna also play a COOPERATE move in this Game Stage/Round,
                        // as a response to that
                        System.out.println("Setting the Probability of " + COOPERATE_AS_PLAYER_2 +
                                           " as " + 1.0 + "!!!");
                        System.out.println("Setting the Probability of " + DEFECT_AS_PLAYER_2 +
                                           " as " + 0.0 + "!!!");

                        System.out.println("\n\n");

                        // Setting My Strategy, accordingly to the previously mentioned description
                        myStrategy.put(COOPERATE_AS_PLAYER_2, 1.0);
                        myStrategy.put(DEFECT_AS_PLAYER_2, 0.0);

                    }

                    // My Opponent played a DEFECT move, in the last Game Stage/Round
                    else if(myOpponentMoveInLastGameStageRoundParts[2].equalsIgnoreCase(DEFECT)) {

                        System.out.println("My Opponent played " + DEFECT + " Move, as Player #1, " +
                                           "in the last Game Stage/Round...\n\n");

                        // As My Opponent, played a DEFECT move, in the last Game Stage/Round,
                        // I am gonna also play DEFECT move in this Game Stage/Round,
                        // as a response to that
                        System.out.println("Setting the Probability of " + COOPERATE_AS_PLAYER_2 +
                                           " as " + 0.0 + "!!!");
                        System.out.println("Setting the Probability of " + DEFECT_AS_PLAYER_2 +
                                           " as " + 1.0 + "!!!");

                        System.out.println("\n\n");

                        // Setting My Strategy, accordingly to the previously mentioned description
                        myStrategy.put(COOPERATE_AS_PLAYER_2, 0.0);
                        myStrategy.put(DEFECT_AS_PLAYER_2, 1.0);

                    }

                    // My Opponent played an UNKNOWN move, in the last Game Stage/Round
                    // NOTE:
                    // - If My Opponent followed the rules, this is never happening;
                    else {

                        System.out.println("My Opponent played " + UNKNOWN + " Move, as Player #1, " +
                                           "in the last Game Stage/Round...\n\n");

                        // As My Opponent, played a DEFECT move, in the last Game Stage/Round,
                        // I am gonna also play DEFECT move in this Game Stage/Round,
                        // as a response to that, to punish him for not following the rules
                        System.out.println("Setting the Probability of " + COOPERATE_AS_PLAYER_2 +
                                           " as " + 0.0 + "!!!");
                        System.out.println("Setting the Probability of " + DEFECT_AS_PLAYER_2 +
                                           " as " + 1.0 + "!!!");

                        System.out.println("\n\n");

                        // Setting My Strategy, accordingly to the previously mentioned description
                        myStrategy.put(COOPERATE_AS_PLAYER_2, 0.0);
                        myStrategy.put(DEFECT_AS_PLAYER_2, 1.0);

                    }

                }

            }


            // Analysing the Last Move Played by My Opponent, as Player #2
            if(Integer.parseInt(myOpponentMoveInLastGameStageRoundParts[0]) == 2) {

                System.out.println("Analysing the Last Move Played by My Opponent, as Player #2...\n\n");

                if( ( this.myOpponentMovePlayedInLastGameStageRoundAsPlayer2.equalsIgnoreCase("") )
                                                &&
                        ( this.numOfGameStagesRoundsCurrentlyPlayed == 1 ) )
                {

                    this.myOpponentMovePlayedInLastGameStageRoundAsPlayer2 =
                            myOpponentMoveInLastGameStageRoundParts[2].toUpperCase();

                }
                else {

                    if( ( !this.myOpponentMovePlayedInLastGameStageRoundAsPlayer2.equalsIgnoreCase("") )
                                                     &&
                        ( !this.myOpponentMovePlayedInLastGameStageRoundAsPlayer2
                              .equalsIgnoreCase( myOpponentMoveInLastGameStageRoundParts[2].toUpperCase() ) ) )
                    {

                        System.out.println("My Opponent changed his behaviour (and the previous last Move played), " +
                                           "as Player #2 and I will memorise that!!!\n\n");

                        this.myOpponentMovePlayedInLastGameStageRoundAsPlayer2 =
                                myOpponentMoveInLastGameStageRoundParts[2].toUpperCase();

                        this.totalNumOfTimesMyOpponentMovesChangedAsPlayer2++;

                    }

                }

                this.checkIfMyOpponentIsBehavingRandomAsPayer2();

                // It seems that My Opponent behaved randomly as Player #2, until now,
                // so I will act severely and I am gonna punish him always from now,
                // with an All Defect Strategy, as Player #1
                if(this.defectingAsPunishmentToMyOpponentAsPlayer2) {

                    System.out.println("My Opponent played Moves, behaving randomly, as Player #2, " +
                                       "in the last Game Stages/Rounds...\n\n");

                    // As My Opponent, behaved randomly, as Player #2, in the previous Game Stages/Rounds,
                    // I am gonna also play a DEFECT move in this Game Stage/Round and from now,
                    // as a response to that, as Player #1
                    System.out.println("Setting the Probability of " + COOPERATE_AS_PLAYER_1 +
                                       " as " + 0.0 + "!!!");
                    System.out.println("Setting the Probability of " + DEFECT_AS_PLAYER_1 +
                                       " as " + 1.0 + "!!!");

                    System.out.println("\n\n");

                    // Setting My Strategy, accordingly to the previously mentioned description
                    myStrategy.put(COOPERATE_AS_PLAYER_1, 0.0);
                    myStrategy.put(DEFECT_AS_PLAYER_1, 1.0);

                }

                // It seems that My Opponent DO NOT behaved randomly as Player #2, until now,
                // so I will act nicely and I am gonna mimic him,
                // with a general and well known, Tit For Tat Strategy, as Player #1
                else {

                    // As Player #1, I will play and act, following the general and well known,
                    // Tit For Tat Strategy and mimic My Opponent's last Move played as Player #2,
                    // as response to that

                    // My Opponent played a COOPERATE move, as Player #2, in the last Game Stage/Round
                    if(myOpponentMoveInLastGameStageRoundParts[2].equalsIgnoreCase(COOPERATE)) {

                        System.out.println("My Opponent played " + COOPERATE + " Move, as Player #2, " +
                                           "in the last Game Stage/Round...\n\n");

                        // As My Opponent, played a COOPERATE move, in the last Game Stage/Round,
                        // I am gonna also play a COOPERATE move in this Game Stage/Round,
                        // as a response to that
                        System.out.println("Setting the Probability of " + COOPERATE_AS_PLAYER_1 +
                                           " as " + 1.0 + "!!!");
                        System.out.println("Setting the Probability of " + DEFECT_AS_PLAYER_1 +
                                           " as " + 0.0 + "!!!");

                        System.out.println("\n\n");

                        // Setting My Strategy, accordingly to the previously mentioned description
                        myStrategy.put(COOPERATE_AS_PLAYER_1, 1.0);
                        myStrategy.put(DEFECT_AS_PLAYER_1, 0.0);

                    }

                    // My Opponent played a DEFECT move, in the last Game Stage/Round
                    else if(myOpponentMoveInLastGameStageRoundParts[2].equalsIgnoreCase(DEFECT)) {

                        System.out.println("My Opponent played " + DEFECT + " Move, as Player #2, " +
                                           "in the last Game Stage/Round...\n\n");

                        // As My Opponent, played a DEFECT move, in the last Game Stage/Round,
                        // I am gonna also play DEFECT move in this Game Stage/Round,
                        // as a response to that
                        System.out.println("Setting the Probability of " + COOPERATE_AS_PLAYER_1 +
                                " as " + 0.0 + "!!!");
                        System.out.println("Setting the Probability of " + DEFECT_AS_PLAYER_1 +
                                " as " + 1.0 + "!!!");

                        System.out.println("\n\n");

                        // Setting My Strategy, accordingly to the previously mentioned description
                        myStrategy.put(COOPERATE_AS_PLAYER_1, 0.0);
                        myStrategy.put(DEFECT_AS_PLAYER_1, 1.0);

                    }

                    // My Opponent played an UNKNOWN move, in the last Game Stage/Round
                    // NOTE:
                    // - If My Opponent followed the rules, this is never happening;
                    else {

                        System.out.println("My Opponent played " + UNKNOWN + " Move, as Player #2, " +
                                           "in the last Game Stage/Round...\n\n");

                        // As My Opponent, played a DEFECT move, in the last Game Stage/Round,
                        // I am gonna also play DEFECT move in this Game Stage/Round,
                        // as a response to that, to punish him for not following the rules
                        System.out.println("Setting the Probability of " + COOPERATE_AS_PLAYER_1 +
                                           " as " + 0.0 + "!!!");
                        System.out.println("Setting the Probability of " + DEFECT_AS_PLAYER_1 +
                                           " as " + 1.0 + "!!!");

                        System.out.println("\n\n");

                        // Setting My Strategy, accordingly to the previously mentioned description
                        myStrategy.put(COOPERATE_AS_PLAYER_1, 0.0);
                        myStrategy.put(DEFECT_AS_PLAYER_1, 1.0);

                    }

                }

            }

        }

    }

    /**
     * Checks/Verifies if My Opponent, is behaving randomly, as Player #1.
     */
    public void checkIfMyOpponentIsBehavingRandomAsPayer1() {

        if( this.totalNumOfTimesMyOpponentMovesChangedAsPlayer1
                                    >=
            MAX_NUM_OF_TIMES_FOR_ALLOWING_MY_OPPONENT_MOVES_CHANGES )
        {

            System.out.println("My Opponent have being behaved randomly, as Player #1, " +
                               "I will start to punish him, from now!!!\n\n");

            this.defectingAsPunishmentToMyOpponentAsPlayer1 = true;

        }

    }

    /**
     * Checks/Verifies if My Opponent, is behaving randomly, as Player #2.
     */
    public void checkIfMyOpponentIsBehavingRandomAsPayer2() {

        if( this.totalNumOfTimesMyOpponentMovesChangedAsPlayer2
                                    >=
                MAX_NUM_OF_TIMES_FOR_ALLOWING_MY_OPPONENT_MOVES_CHANGES )
        {

            System.out.println("My Opponent have being behaved randomly, as Player #2, " +
                               "I will start to punish him, from now!!!\n\n");

            this.defectingAsPunishmentToMyOpponentAsPlayer2 = true;

        }

    }

}
