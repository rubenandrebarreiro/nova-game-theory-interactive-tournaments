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
 * Tit For Tat Strategy's Common Utils, extending the Strategy's Common Utils
 *
 * Authors:
 * - Pedro Lamarao Pais (Student no. 48247)
 *   - pgp@campus.fct.unl.pt
 * - Ruben Andre Barreiro (Student no. 42648)
 *   - r.barreiro@campus.fct.unl.pt
 *
 */

public class TitForTatStrategyCommonUtils extends PrisonersDilemmaStrategyCommonUtils {


    // Global Instance Variables:

    /**
     * The Play Strategy object, representing the Tit For Tat Strategy.
     */
    private final PlayStrategy myTitForTatStrategy;

    /**
     * The number of times that My Opponent's Moves changed, every time he played, as Player #1.
     */
    private int totalNumOfTimesMyOpponentMovesChangedAsPlayer1;

    /**
     * The number of times that My Opponent's Moves changed, every time he played, as Player #2.
     */
    private int totalNumOfTimesMyOpponentMovesChangedAsPlayer2;


    // Constructors:

    /**
     * Constructor #1:
     * - Constructor of the Tit For Tat Strategy's Common Utils, extending the Strategy's Common Utils,
     *   given the Play Strategy object, representing the Omega Tit For Tat Strategy.
     *
     * @param myTitForTatStrategy the Play Strategy object, representing the Tit For Tat Strategy
     */
    public TitForTatStrategyCommonUtils(PlayStrategy myTitForTatStrategy) {

        super();

        this.myTitForTatStrategy = myTitForTatStrategy;

    }


    // Public Methods/Procedures:

    /**
     * Performs the Setup of the Parameters' Configuration for the Tit For Tat Strategy.
     */
    public void setupTitForTatStrategyParametersConfiguration() {

        setTotalNumOfGameStagesRounds( this.getMyTitForTatStrategy().getMaximumNumberOfIterations() + 1 );

        setNumOfGameStagesRoundsCurrentlyPlayed(0);

    }

    /**
     * Prints the Setup of the Parameters' Configuration for the Tit For Tat Strategy.
     */
    public void printTitForTatStrategyParametersConfiguration() {

        System.out.println("--- INFORMATION ABOUT THE PARAMETERS' CONFIGURATION FOR " +
                           "THE TIT FOR TAT STRATEGY ---");

        System.out.println("\n");

        System.out.println("    Total Number of Game Stages/Rounds:");
        System.out.println("    - " + TOTAL_NUM_OF_GAME_STAGES_ROUNDS);

        System.out.println("\n\n");

    }

    /**
     * Returns the Play Strategy object, representing the Tit For Tat Strategy.
     *
     * @return the Play Strategy object, representing the Tit For Tat Strategy
     */
    public PlayStrategy getMyTitForTatStrategy() {

        return this.myTitForTatStrategy;

    }

}
