package play.prisonersdilemma.titfortat.utils;

import play.PlayStrategy;
import play.prisonersdilemma.utils.PrisonersDilemmaStrategyCommonUtils;

/**
 *
 * Computational Game Theory - 2019/2020
 * Faculty of Sciences and Technology of
 * New University of Lisbon (FCT NOVA | FCT/UNL)
 *
 * 1st Tournament - Prisoner's Dilemma,
 * using NOVA GTI (Game Theory Interactive) Platform
 *
 * Omega Tit For Tat Strategy's Common Utils, extending the Strategy's Common Utils
 *
 * Authors:
 * - Pedro Lamarao Pais (Student no. 48247)
 *   - pgp@campus.fct.unl.pt
 * - Ruben Andre Barreiro (Student no. 42648)
 *   - r.barreiro@campus.fct.unl.pt
 *
 */

public class OmegaTitForTatStrategyCommonUtils extends PrisonersDilemmaStrategyCommonUtils {


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
     * The maximum number of times for allowing My Opponent's Moves changes,
     * meaning after that, I will start to play Always Defect from then.
     */
    private static int MAX_NUM_OF_TIMES_FOR_ALLOWING_MY_OPPONENT_MOVES_CHANGES;


    // Global Instance Variables:

    /**
     * The Play Strategy object, representing the Omega Tit For Tat Strategy.
     */
    private final PlayStrategy myOmegaTitForTatStrategy;

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


    // Constructors:

    /**
     * Constructor #1:
     * - Constructor of the Omega Tit For Tat Strategy's Common Utils, extending the Strategy's Common Utils,
     *   given the Play Strategy object, representing the Omega Tit For Tat Strategy.
     *
     * @param myOmegaTitForTatStrategy the Play Strategy object, representing the Omega Tit For Tat Strategy
     */
    public OmegaTitForTatStrategyCommonUtils(PlayStrategy myOmegaTitForTatStrategy) {

        super();

        this.myOmegaTitForTatStrategy = myOmegaTitForTatStrategy;

    }


    // Public Methods/Procedures:

    /**
     * Performs the Setup of the Parameters' Configuration for the Omega Tit For Tat Strategy.
     */
    public void setupOmegaTitForTatStrategyParametersConfiguration() {

        setTotalNumOfGameStagesRounds( this.getMyOmegaTitForTatStrategy().getMaximumNumberOfIterations() + 1 );

        setMaxNumOfTimesForAllowingMyOpponentMovesChanges();

        setNumOfGameStagesRoundsCurrentlyPlayed(0);

        this.totalNumOfTimesMyOpponentMovesChangedAsPlayer1 = 0;
        this.totalNumOfTimesMyOpponentMovesChangedAsPlayer2 = 0;

        this.defectingAsPunishmentToMyOpponentAsPlayer1 = false;
        this.defectingAsPunishmentToMyOpponentAsPlayer2 = false;

    }

    /**
     * Prints the Setup of the Parameters' Configuration for the Omega Tit For Tat Strategy.
     */
    public void printOmegaTitForTatStrategyParametersConfiguration() {

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
     * Returns the Play Strategy object, representing the Omega Tit For Tat Strategy.
     *
     * @return the Play Strategy object, representing the Omega Tit For Tat Strategy
     */
    public PlayStrategy getMyOmegaTitForTatStrategy() {

        return this.myOmegaTitForTatStrategy;

    }

    /**
     * Returns the Total Number of times that My Opponent's Moves changed, as Player #1.
     *
     * @return the Total Number of times that My Opponent's Moves changed, as Player #1
     */
    public int getTotalNumOfTimesMyOpponentMovesChangedAsPlayer1() {

        return this.totalNumOfTimesMyOpponentMovesChangedAsPlayer1;

    }

    /**
     * Sets the Total Number of times that My Opponent's Moves changed, as Player #1.
     *
     * @param totalNumOfTimesMyOpponentMovesChangedAsPlayer1 the Total Number of times that
     *        My Opponent's Moves changed, as Player #1
     */
    public void setTotalNumOfTimesMyOpponentMovesChangedAsPlayer1
            (
                    int totalNumOfTimesMyOpponentMovesChangedAsPlayer1
            )
    {

        this.totalNumOfTimesMyOpponentMovesChangedAsPlayer1 = totalNumOfTimesMyOpponentMovesChangedAsPlayer1;

    }

    /**
     * Returns the Total Number of times that My Opponent's Moves changed, as Player #2.
     *
     * @return the Total Number of times that My Opponent's Moves changed, as Player #2
     */
    public int getTotalNumOfTimesMyOpponentMovesChangedAsPlayer2() {

        return this.totalNumOfTimesMyOpponentMovesChangedAsPlayer2;

    }

    /**
     * Sets the Total Number of times that My Opponent's Moves changed, as Player #2.
     *
     * @param totalNumOfTimesMyOpponentMovesChangedAsPlayer2 the Total Number of times that
     *        My Opponent's Moves changed, as Player #2
     */
    public void setTotalNumOfTimesMyOpponentMovesChangedAsPlayer2
            (
                    int totalNumOfTimesMyOpponentMovesChangedAsPlayer2
            )
    {

        this.totalNumOfTimesMyOpponentMovesChangedAsPlayer2 = totalNumOfTimesMyOpponentMovesChangedAsPlayer2;

    }

    /**
     * Returns the boolean value that keeps the information about I'm currently playing a Defect Move, as Player #2,
     * as punishment, in response to, a possible random behaviour from My Opponent playing as Player #1.
     *
     * @return the boolean value that keeps the information about I'm currently playing a Defect Move, as Player #2,
     *         as punishment, in response to, a possible random behaviour from My Opponent playing as Player #1
     */
    public boolean isDefectingAsPunishmentToMyOpponentAsPlayer1() {

        return this.defectingAsPunishmentToMyOpponentAsPlayer1;

    }

    /**
     * Sets the boolean value that keeps the information about I'm currently playing a Defect Move, as Player #2,
     * as punishment, in response to, a possible random behaviour from My Opponent playing as Player #1.
     *
     * @param defectingAsPunishmentToMyOpponentAsPlayer1 the boolean value that keeps the information about
     *        I'm currently playing a Defect Move, as Player #2, as punishment, in response to,
     *        a possible random behaviour from My Opponent playing as Player #1
     */
    public void setDefectingAsPunishmentToMyOpponentAsPlayer1(boolean defectingAsPunishmentToMyOpponentAsPlayer1) {

        this.defectingAsPunishmentToMyOpponentAsPlayer1 = defectingAsPunishmentToMyOpponentAsPlayer1;

    }

    /**
     * Returns the boolean value that keeps the information about I'm currently playing a Defect Move, as Player #1,
     * as punishment, in response to, a possible random behaviour from My Opponent playing as Player #2.
     *
     * @return the boolean value that keeps the information about I'm currently playing a Defect Move, as Player #1,
     *         as punishment, in response to, a possible random behaviour from My Opponent playing as Player #2
     */
    public boolean isDefectingAsPunishmentToMyOpponentAsPlayer2() {

        return this.defectingAsPunishmentToMyOpponentAsPlayer2;

    }

    /**
     * Sets the boolean value that keeps the information about I'm currently playing a Defect Move, as Player #1,
     * as punishment, in response to, a possible random behaviour from My Opponent playing as Player #2.
     *
     * @param defectingAsPunishmentToMyOpponentAsPlayer2 the boolean value that keeps the information about
     *        I'm currently playing a Defect Move, as Player #1, as punishment, in response to,
     *        a possible random behaviour from My Opponent playing as Player #2
     */
    public void setDefectingAsPunishmentToMyOpponentAsPlayer2(boolean defectingAsPunishmentToMyOpponentAsPlayer2) {

        this.defectingAsPunishmentToMyOpponentAsPlayer2 = defectingAsPunishmentToMyOpponentAsPlayer2;

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

            this.setDefectingAsPunishmentToMyOpponentAsPlayer1(true);

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

            this.setDefectingAsPunishmentToMyOpponentAsPlayer2(true);

        }

    }

}
