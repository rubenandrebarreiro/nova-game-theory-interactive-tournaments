package play;

import gametree.GameNode;

/**
 *
 * Computational Game Theory - 2019/2020
 * Faculty of Sciences and Technology of
 * New University of Lisbon (FCT NOVA)
 *
 * 1st Tournament - Prisoner's Dilemma,
 * using NOVA GTI (Game Theory Interactive) Platform
 *
 * Strategy's Common Utils
 *
 * Authors:
 * - Pedro Lamarao Pais (Student no. 48247)
 *   - pgp@campus.fct.unl.pt
 * - Ruben Andre Barreiro (Student no. 42648)
 *   - r.barreiro@campus.fct.unl.pt
 *
 */
public class StrategyCommonUtils {

    static void isATerminalGameNode(PlayStrategy myStrategy, GameNode gameNodePlayer1, GameNode gameNodePlayer2) {

        System.out.println("Checking if was reached a Terminal Game Node in " +
                           "the last Game Stage/Round for any of the Players...\n\n");

        if(myStrategy.getFinalP1Node() != -1) {

            if (gameNodePlayer1 != null) {

                System.out.println("Terminal Game Node in last round as P1 (Player #1): " + gameNodePlayer1);

            }

        }

        if(myStrategy.getFinalP2Node() != -1) {

            if (gameNodePlayer2 != null) {

                System.out.println("Terminal Game Node in last round as P2 (Player #2): " + gameNodePlayer2);

            }

        }

    }

}
