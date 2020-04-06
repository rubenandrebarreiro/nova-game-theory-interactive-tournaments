package play.prisonersdilemma.titfortat;

import gametree.GameNode;
import gametree.GameNodeDoesNotExistException;
import play.PlayStrategy;
import play.Strategy;
import play.exception.InvalidStrategyException;
import play.prisonersdilemma.titfortat.utils.OmegaTitForTatStrategyCommonUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static play.prisonersdilemma.utils.PrisonersDilemmaStrategyCommonUtils.*;

/**
 *
 * Computational Game Theory - 2019/2020
 * Faculty of Sciences and Technology of
 * New University of Lisbon (FCT NOVA | FCT/UNL)
 *
 * 1st Tournament - Prisoner's Dilemma,
 * using NOVA GTI (Game Theory Interactive) Platform
 *
 * Omega Tit For Tat Strategy, for Unknown Number of Rounds with Probability
 *
 * Authors:
 * - Pedro Lamarao Pais (Student no. 48247)
 *   - pg.pais@campus.fct.unl.pt
 * - Ruben Andre Barreiro (Student no. 42648)
 *   - r.barreiro@campus.fct.unl.pt
 *
 */

@SuppressWarnings("all")
public class OmegaTitForTatForUnknownNumRoundsWithProbabilityStrategy extends Strategy {


    // Constants:

    /**
     * The Payoff I will gain, if both Me and My Opponent play Cooperate Moves.
     */
    private static final int BOTH_COOPERATE_PAYOFF = 3;

    /**
     * The Payoff I will gain, if both Me and My Opponent play Defect Moves.
     */
    private static final int BOTH_DEFECT_PAYOFF = 1;

    /**
     * The Payoff I will gain, if I play a Defect Move and My Opponent play a Cooperate Move.
     */
    private static final int I_DEFECT_AND_MY_OPPONENT_COOPERATE_PAYOFF = 4;


    // Global Instance Variables:

    /**
     * The boolean value to keep information about if
     * the Omega Tit For Tat Strategy's Common Utils, it was initialized or not.
     */
    private boolean isMyOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtilsInitialized;

    /**
     * The Omega Tit For Tat For Unknown Number of Rounds with Probability Strategy's Common Utils.
     */
    private OmegaTitForTatStrategyCommonUtils myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtils;


    // Public Methods/Procedures:

    /**
     * Executes the Omega Tit For Tat For Unknown Number of Rounds with Probability Strategy,
     * for the current Game being played.
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


        // The boolean value to keep information about if
        // the Omega Tit For Tat Strategy's Common Utils, it was initialized or not
        boolean isMyOmegaTitForTatStrategyCommonUtilsInitialized = false;

        // Prints the Basic Information about this Strategy
        System.out.println("Start playing with the Omega Tit For Tat Strategy...\n\n");


        // Infinite Loop
        while(true) {

            PlayStrategy myOmegaTitForTatStrategy = this.getStrategyRequest();

            // The Strategy chosen by me become NULL,
            // what means (probably) that the Game
            // was terminated by an outside event
            if (myOmegaTitForTatStrategy == null) {

                // Breaks the Infinite Loop
                break;

            }

            if( !this.isMyOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtilsInitialized) {


                this.myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtils =
                        new OmegaTitForTatStrategyCommonUtils(myOmegaTitForTatStrategy);

                this.myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtils.setupOmegaTitForTatStrategyParametersConfiguration();

                this.myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtils.printOmegaTitForTatStrategyParametersConfiguration();

                this.isMyOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtilsInitialized = true;

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
                                myOmegaTitForTatStrategy,
                                this.tree.getNodeByIndex(myOmegaTitForTatStrategy.getFinalP1Node()),
                                this.tree.getNodeByIndex(myOmegaTitForTatStrategy.getFinalP2Node())
                        );

                // The Current Validation Set for the Available Moves
                Iterator<Integer> currentValidationSetIterator = tree.getValidationSet().iterator();

                // The Labels for All the Available Moves
                Iterator<String> availableMovesLabels = myOmegaTitForTatStrategy.keyIterator();

                System.out.println("I will define My Strategy for the next Game Stage/Round...\n\n");

                finalGameNodePlayer1 = this.tree.getNodeByIndex(myOmegaTitForTatStrategy.getFinalP1Node());
                finalGameNodePlayer2 = this.tree.getNodeByIndex(myOmegaTitForTatStrategy.getFinalP2Node());


                System.out.println
                        (
                                "This it's the Game Stage/Round #" +
                                ( this.myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtils
                                      .getNumOfGameStagesRoundsCurrentlyPlayed() + 1 ) +
                                "...\n\n"
                        );


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
                            myOmegaTitForTatStrategy.put(nextAvailableMovesLabel, availableMoveProbability);

                        }

                        System.out.println("\n\n");

                        actingAsPlayerNum++;

                    }

                }
                // This are the remaining Game Stages/Rounds, so I need to analyse/infer what My Opponent
                // did in the previous Game Stages/Rounds, specially in the last one
                else {

                    List<GameNode> listOfOpponentLastMovesAsPlayer1 =
                            getReversePath( finalGameNodePlayer1 );
                    List<GameNode> listOfOpponentLastMovesAsPlayer2 =
                            getReversePath( finalGameNodePlayer2 );

                    try {

                        computeOmegaTitForTatStrategy
                                (
                                        listOfOpponentLastMovesAsPlayer1,
                                        listOfOpponentLastMovesAsPlayer2,
                                        myOmegaTitForTatStrategy
                                );

                    }
                    catch (GameNodeDoesNotExistException gameNodeDoesNotExistException) {

                        System.err.println("PANIC: Strategy's Structure doesn't match the current Game!!!");

                    }

                }


                try {

                    // Sets and provides the final Strategy
                    this.provideStrategy(myOmegaTitForTatStrategy);

                    // Sets My Play as completed, as long I'm finished playing
                    playComplete = true;



                }
                catch (InvalidStrategyException invalidStrategyException) {

                    System.err.println("Invalid Strategy: " + invalidStrategyException.getMessage());
                    invalidStrategyException.printStackTrace(System.err);

                }

                // Increases the number of the Game Stages/Rounds currently played, until now
                this.myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtils.setNumOfGameStagesRoundsCurrentlyPlayed
                        (
                                this.myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtils.getNumOfGameStagesRoundsCurrentlyPlayed() + 1
                        );

            }

        }

    }

    /**
     * Computes the Omega Tit For Tat Strategy, for the current Game, being played.
     *
     * @param listOfOpponentLastMovesAsPlayer1 the list of My Opponent Last Moves, as Player #1
     *
     * @param listOfOpponentLastMovesAsPlayer2 the list of My Opponent Last Moves, as Player #2
     *
     * @param myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategy the Omega Tit For Tat
     *        For Unknown Number of Rounds With Probability Strategy I'm currently using, with my moves
     *
     * @throws GameNodeDoesNotExistException an Exception to be thrown, in the case of,
     *         an accessed Game Node, in the tree, does not exist
     */
    private void computeOmegaTitForTatStrategy
            (
                    List<GameNode> listOfOpponentLastMovesAsPlayer1,
                    List<GameNode> listOfOpponentLastMovesAsPlayer2,
                    PlayStrategy myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategy
            )
            throws GameNodeDoesNotExistException
    {

        double probabilityOfContinueToTheNextGameStageRound = getProbabilityOfContinueToTheNextGameStageRound();


        int gainFromDefect = ( I_DEFECT_AND_MY_OPPONENT_COOPERATE_PAYOFF - BOTH_COOPERATE_PAYOFF );

        int probabilisticLossFromDefect =
                ( (int) (
                        (
                                ( BOTH_COOPERATE_PAYOFF - BOTH_DEFECT_PAYOFF )
                                        *
                                        ( probabilityOfContinueToTheNextGameStageRound )
                        )
                                /
                                ( 1 - probabilityOfContinueToTheNextGameStageRound )
                )
                );

        // The Expected Gain from playing a Defect Move it's higher than
        // the both determinist and probabilistic Expected Loss from
        // playing a Defect Move, in the next Game Stage/Round,
        // so it's more benefit to play a Defect Move, acting as both, Players #1 and #2
        if( gainFromDefect > probabilisticLossFromDefect ) {

            System.out.println("I'm gonna to play a Defect Move now, " +
                    "because the Expected Utility Gain from Defect could be, maybe, much worthy!!!\n\n");

            // As playing a Cooperate it's not more worthy, I will play a Defect Move,
            // in the next Game Stage/Round, acting as Players #1
            System.out.println("Setting the Probability of " + COOPERATE_AS_PLAYER_1 +
                    " as " + 0.0 + "!!!");
            System.out.println("Setting the Probability of " + DEFECT_AS_PLAYER_1 +
                    " as " + 1.0 + "!!!");

            // Sets the probability of playing Cooperate and Defect Moves,
            // as 0.0 and 1.0, respectively, as Player #1
            myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategy.put(COOPERATE_AS_PLAYER_1, 0.0);
            myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategy.put(DEFECT_AS_PLAYER_1, 1.0);

            System.out.println();

            // As playing a Cooperate it's not more worthy, I will play a Defect Move,
            // in the next Game Stage/Round, acting as Players #2
            System.out.println("Setting the Probability of " + COOPERATE_AS_PLAYER_2 +
                    " as " + 0.0 + "!!!");
            System.out.println("Setting the Probability of " + DEFECT_AS_PLAYER_2 +
                    " as " + 1.0 + "!!!");

            // Sets the probability of playing Cooperate and Defect Moves,
            // as 0.0 and 1.0, respectively, as Player #2
            myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategy.put(COOPERATE_AS_PLAYER_2, 0.0);
            myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategy.put(DEFECT_AS_PLAYER_2, 1.0);

            System.out.println();

        }
        // The Expected Gain from playing a Defect Move it's lower or equal than
        // the both deterministic and probabilist Expected Loss from
        // playing a Defect Move, in the next Game Stage/Round,
        // so can be more benefit to, maybe, play a Cooperate Move, acting as both, Players #1 and #2
        else {

            Set<String> myOpponentMovesInLastGameStageRound =
                    getMyOpponentMovesInLastGameStageRound
                            (
                                    listOfOpponentLastMovesAsPlayer1,
                                    listOfOpponentLastMovesAsPlayer2
                            );

            for( String myOpponentMoveInLastGameStageRound : myOpponentMovesInLastGameStageRound ) {

                String[] myOpponentMoveInLastGameStageRoundParts =
                        myOpponentMoveInLastGameStageRound.split(":");

                // Analysing the Last Move Played by My Opponent, as Player #1
                if(Integer.parseInt(myOpponentMoveInLastGameStageRoundParts[0]) == 1) {

                    System.out.println("Analysing the Last Move Played by My Opponent, as Player #1...\n\n");

                    if( ( this.myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtils
                            .getMyOpponentMovePlayedInLastGameStageRoundAsPlayer1()
                            .equalsIgnoreCase("") )
                            &&
                            ( this.myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtils.getNumOfGameStagesRoundsCurrentlyPlayed() == 1 ) )
                    {

                        this.myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtils.setMyOpponentMovePlayedInLastGameStageRoundAsPlayer1
                                (
                                        myOpponentMoveInLastGameStageRoundParts[2].toUpperCase()
                                );

                    }
                    else {

                        if( ( !this.myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtils
                                .getMyOpponentMovePlayedInLastGameStageRoundAsPlayer1()
                                .equalsIgnoreCase("") )
                                &&
                                ( !this.myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtils
                                        .getMyOpponentMovePlayedInLastGameStageRoundAsPlayer1()
                                        .equalsIgnoreCase( myOpponentMoveInLastGameStageRoundParts[2].toUpperCase() ) ) )
                        {

                            System.out.println("My Opponent changed his behaviour (and the previous last Move played), " +
                                    "as Player #1 and I will memorise that!!!\n\n");

                            this.myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtils.setMyOpponentMovePlayedInLastGameStageRoundAsPlayer1
                                    (
                                            myOpponentMoveInLastGameStageRoundParts[2].toUpperCase()
                                    );

                            this.myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtils.setTotalNumOfTimesMyOpponentMovesChangedAsPlayer1
                                    (
                                            this.myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtils
                                                    .getTotalNumOfTimesMyOpponentMovesChangedAsPlayer1() + 1
                                    );

                        }

                    }

                    this.myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtils.checkIfMyOpponentIsBehavingRandomAsPayer1();

                    // It seems that My Opponent behaved randomly as Player #1, until now,
                    // so I will act severely and I am gonna punish him always from now,
                    // with an All Defect Strategy
                    if( this.myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtils.isDefectingAsPunishmentToMyOpponentAsPlayer1() ) {

                        System.out.println("My Opponent played Moves, behaving randomly, as Player #1, " +
                                "in the last Game Stages/Rounds...\n\n");

                        // As My Opponent, behaved randomly, as Player #1, in the previous Game Stages/Rounds,
                        // I am gonna also play a DEFECT move in this Game Stage/Round and from now,
                        // as a response to that, as Player #2
                        System.out.println("Setting the Probability of " +
                                COOPERATE_AS_PLAYER_2 +
                                " as " + 0.0 + "!!!");
                        System.out.println("Setting the Probability of " +
                                DEFECT_AS_PLAYER_2 +
                                " as " + 1.0 + "!!!");

                        System.out.println("\n\n");

                        // Setting My Strategy, accordingly to the previously mentioned description
                        myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategy.put(COOPERATE_AS_PLAYER_2, 0.0);
                        myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategy.put(DEFECT_AS_PLAYER_2, 1.0);

                    }

                    // It seems that My Opponent DO NOT behaved randomly as Player #1, until now,
                    // so I will act nicely and I am gonna mimic him,
                    // with a general and well known, Tit For Tat Strategy
                    else {

                        // As Player #2, I will play and act, following the general and well known,
                        // Tit For Tat Strategy and mimic My Opponent's last Move played as Player #1,
                        // as response to that

                        // My Opponent played a COOPERATE move, in the last Game Stage/Round
                        if ( myOpponentMoveInLastGameStageRoundParts[2].equalsIgnoreCase(COOPERATE) ) {

                            System.out.println("My Opponent played " + COOPERATE +
                                    " Move, as Player #1, in the last Game Stage/Round...\n\n");

                            // As My Opponent, played a COOPERATE move, in the last Game Stage/Round,
                            // I am gonna also play a COOPERATE move in this Game Stage/Round,
                            // as a response to that
                            System.out.println("Setting the Probability of " + COOPERATE_AS_PLAYER_2 + " as " + 1.0 + "!!!");
                            System.out.println("Setting the Probability of " + DEFECT_AS_PLAYER_2 + " as " + 0.0 + "!!!");

                            System.out.println("\n\n");

                            // Setting My Strategy, accordingly to the previously mentioned description
                            myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategy.put(COOPERATE_AS_PLAYER_2, 1.0);
                            myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategy.put(DEFECT_AS_PLAYER_2, 0.0);

                        }

                        // My Opponent played a DEFECT move, in the last Game Stage/Round
                        else if
                        (
                                myOpponentMoveInLastGameStageRoundParts[2]
                                        .equalsIgnoreCase(DEFECT)
                        )
                        {

                            System.out.println("My Opponent played " + DEFECT +
                                    " Move, as Player #1, in the last Game Stage/Round...\n\n");

                            // As My Opponent, played a DEFECT move, in the last Game Stage/Round,
                            // I am gonna also play DEFECT move in this Game Stage/Round,
                            // as a response to that
                            System.out.println("Setting the Probability of " +
                                    COOPERATE_AS_PLAYER_2 +
                                    " as " + 0.0 + "!!!");
                            System.out.println("Setting the Probability of " +
                                    DEFECT_AS_PLAYER_2 +
                                    " as " + 1.0 + "!!!");

                            System.out.println("\n\n");

                            // Setting My Strategy, accordingly to the previously mentioned description
                            myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategy.put(COOPERATE_AS_PLAYER_2, 0.0);
                            myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategy.put(DEFECT_AS_PLAYER_2, 1.0);

                        }

                        // My Opponent played an UNKNOWN move, in the last Game Stage/Round
                        // NOTE:
                        // - If My Opponent followed the rules, this is never happening;
                        else {

                            System.out.println("My Opponent played " + UNKNOWN +
                                    " Move, as Player #1, in the last Game Stage/Round...\n\n");

                            // As My Opponent, played a DEFECT move, in the last Game Stage/Round,
                            // I am gonna also play DEFECT move in this Game Stage/Round,
                            // as a response to that, to punish him for not following the rules
                            System.out.println("Setting the Probability of " + COOPERATE_AS_PLAYER_2 + " as " + 0.0 + "!!!");
                            System.out.println("Setting the Probability of " + DEFECT_AS_PLAYER_2 + " as " + 1.0 + "!!!");

                            System.out.println("\n\n");

                            // Setting My Strategy, accordingly to the previously mentioned description
                            myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategy.put(COOPERATE_AS_PLAYER_2, 0.0);
                            myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategy.put(DEFECT_AS_PLAYER_2, 1.0);

                        }

                    }

                }


                // Analysing the Last Move Played by My Opponent, as Player #2
                if(Integer.parseInt(myOpponentMoveInLastGameStageRoundParts[0]) == 2) {

                    System.out.println("Analysing the Last Move Played by My Opponent, as Player #2...\n\n");

                    if( ( this.myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtils
                            .getMyOpponentMovePlayedInLastGameStageRoundAsPlayer2().equalsIgnoreCase("") )
                            &&
                            ( this.myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtils.getNumOfGameStagesRoundsCurrentlyPlayed() == 1 ) )
                    {

                        this.myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtils
                                .setMyOpponentMovePlayedInLastGameStageRoundAsPlayer2(
                                        myOpponentMoveInLastGameStageRoundParts[2].toUpperCase()
                                );

                    }
                    else {

                        if( ( !this.myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtils
                                .getMyOpponentMovePlayedInLastGameStageRoundAsPlayer2()
                                .equalsIgnoreCase("") )
                                &&
                                ( !this.myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtils
                                        .getMyOpponentMovePlayedInLastGameStageRoundAsPlayer2()
                                        .equalsIgnoreCase( myOpponentMoveInLastGameStageRoundParts[2].toUpperCase() ) ) )
                        {

                            System.out.println("My Opponent changed his behaviour (and the previous last Move played), " +
                                    "as Player #2 and I will memorise that!!!\n\n");

                            this.myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtils
                                    .setMyOpponentMovePlayedInLastGameStageRoundAsPlayer2
                                            (
                                                    myOpponentMoveInLastGameStageRoundParts[2].toUpperCase()
                                            );

                            this.myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtils.setTotalNumOfTimesMyOpponentMovesChangedAsPlayer2
                                    (
                                            this.myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtils
                                                    .getTotalNumOfTimesMyOpponentMovesChangedAsPlayer2() + 1
                                    );

                        }

                    }

                    this.myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtils.checkIfMyOpponentIsBehavingRandomAsPayer2();

                    // It seems that My Opponent behaved randomly as Player #2, until now,
                    // so I will act severely and I am gonna punish him always from now,
                    // with an All Defect Strategy, as Player #1
                    if( this.myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategyCommonUtils.isDefectingAsPunishmentToMyOpponentAsPlayer2() ) {

                        System.out.println("My Opponent played Moves, behaving randomly, as Player #2, " +
                                "in the last Game Stages/Rounds...\n\n");

                        // As My Opponent, behaved randomly, as Player #2, in the previous Game Stages/Rounds,
                        // I am gonna also play a DEFECT move in this Game Stage/Round and from now,
                        // as a response to that, as Player #1
                        System.out.println("Setting the Probability of " + COOPERATE_AS_PLAYER_1 + " as " + 0.0 + "!!!");
                        System.out.println("Setting the Probability of " + DEFECT_AS_PLAYER_1 + " as " + 1.0 + "!!!");

                        System.out.println("\n\n");

                        // Setting My Strategy, accordingly to the previously mentioned description
                        myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategy.put(COOPERATE_AS_PLAYER_1, 0.0);
                        myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategy.put(DEFECT_AS_PLAYER_1, 1.0);

                    }

                    // It seems that My Opponent DO NOT behaved randomly as Player #2, until now,
                    // so I will act nicely and I am gonna mimic him,
                    // with a general and well known, Tit For Tat Strategy, as Player #1
                    else {

                        // As Player #1, I will play and act, following the general and well known,
                        // Tit For Tat Strategy and mimic My Opponent's last Move played as Player #2,
                        // as response to that

                        // My Opponent played a COOPERATE move, as Player #2, in the last Game Stage/Round
                        if( myOpponentMoveInLastGameStageRoundParts[2].equalsIgnoreCase(COOPERATE) ) {

                            System.out.println("My Opponent played " + COOPERATE +
                                    " Move, as Player #2, in the last Game Stage/Round...\n\n");

                            // As My Opponent, played a COOPERATE move, in the last Game Stage/Round,
                            // I am gonna also play a COOPERATE move in this Game Stage/Round,
                            // as a response to that
                            System.out.println("Setting the Probability of " + COOPERATE_AS_PLAYER_1 +
                                    " as " + 1.0 + "!!!");
                            System.out.println("Setting the Probability of " + DEFECT_AS_PLAYER_1 +
                                    " as " + 0.0 + "!!!");

                            System.out.println("\n\n");

                            // Setting My Strategy, accordingly to the previously mentioned description
                            myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategy.put(COOPERATE_AS_PLAYER_1, 1.0);
                            myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategy.put(DEFECT_AS_PLAYER_1, 0.0);

                        }

                        // My Opponent played a DEFECT move, in the last Game Stage/Round
                        else if( myOpponentMoveInLastGameStageRoundParts[2].equalsIgnoreCase(DEFECT) ) {

                            System.out.println("My Opponent played " + DEFECT +
                                    " Move, as Player #2, in the last Game Stage/Round...\n\n");

                            // As My Opponent, played a DEFECT move, in the last Game Stage/Round,
                            // I am gonna also play DEFECT move in this Game Stage/Round,
                            // as a response to that
                            System.out.println("Setting the Probability of " +
                                    COOPERATE_AS_PLAYER_1 +
                                    " as " + 0.0 + "!!!");
                            System.out.println("Setting the Probability of " +
                                    DEFECT_AS_PLAYER_1 +
                                    " as " + 1.0 + "!!!");

                            System.out.println("\n\n");

                            // Setting My Strategy, accordingly to the previously mentioned description
                            myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategy.put(COOPERATE_AS_PLAYER_1, 0.0);
                            myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategy.put(DEFECT_AS_PLAYER_1, 1.0);

                        }

                        // My Opponent played an UNKNOWN move, in the last Game Stage/Round
                        // NOTE:
                        // - If My Opponent followed the rules, this is never happening;
                        else {

                            System.out.println("My Opponent played " + UNKNOWN +
                                    " Move, as Player #2, in the last Game Stage/Round...\n\n");

                            // As My Opponent, played a DEFECT move, in the last Game Stage/Round,
                            // I am gonna also play DEFECT move in this Game Stage/Round,
                            // as a response to that, to punish him for not following the rules
                            System.out.println("Setting the Probability of " + COOPERATE_AS_PLAYER_1 +
                                    " as " + 0.0 + "!!!");
                            System.out.println("Setting the Probability of " + DEFECT_AS_PLAYER_1 +
                                    " as " + 1.0 + "!!!");

                            System.out.println("\n\n");

                            // Setting My Strategy, accordingly to the previously mentioned description
                            myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategy.put(COOPERATE_AS_PLAYER_1, 0.0);
                            myOmegaTitForTatForUnknownNumRoundsWithProbabilityStrategy.put(DEFECT_AS_PLAYER_1, 1.0);

                        }

                    }

                }

            }

        }

    }

}
