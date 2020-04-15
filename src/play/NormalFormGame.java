package play;

import java.util.ArrayList;
import java.util.List;

public class NormalFormGame {


	// Global Instance Variables:

	/**
	 * The List of Action Moves for Player #1 (Rows' Player)
	 */
	public List<String> player1RowActionMoves;

	/**
	 * The List of Action Moves for Player #1 (Rows' Player)
	 */
	public List<String> player2ColumnActionMoves;

	/**
	 * The number of Rows of the Normal-Form Game.
	 */
	private int numOfRows;

	/**
	 * The number of Columns of the Normal-Form Game.
	 */
	private int numOfColumns;

	/**
	 * The Boolean Flags, that keep the information about
	 * which the Normal-Form Game's Rows (Action Moves for Player #1)
	 * are being considered or not.
	 */
	private boolean[] normalFormGameRowsPlayer1Considered;

	/**
	 * The Boolean Flags, that keep the information about
	 * which the Normal-Form Game's Columns (Action Moves for Player #2)
	 * are being considered or not.
	 */
	private boolean[] normalFormGameColumnsPlayer2Considered;

	/**
	 * The Payoff Utilities for the Normal-Form Game's Rows (Action Moves for Player #1).
	 */
	public int[][] utilitiesPayoffsNormalFormMatrixPlayer1;

	/**
	 * The Payoff Utilities for the Normal-Form Game's Rows (Action Moves for Player #2).
	 */
	public int[][] utilitiesPayoffsNormalFormMatrixPlayer2;


	// Constructors:

	/**
	 * Constructor #1:
	 * - An empty constructor for a Normal-Form Game.
	 */
	public NormalFormGame() {

		// Empty Constructor

	}

	/**
	 *
	 *
	 * @param utilitiesPayoffsNormalFormMatrixPlayer1
	 * @param utilitiesPayoffsNormalFormMatrixPlayer2
	 * @param labelsMovesPlayer1
	 * @param labelsMovesPlayer2
	 */
	public NormalFormGame(int[][] utilitiesPayoffsNormalFormMatrixPlayer1,
						  int[][] utilitiesPayoffsNormalFormMatrixPlayer2,
						  String[] labelsMovesPlayer1,
						  String[] labelsMovesPlayer2) {

		// Configurations for the Rows' Player (Player #1)
		this.numOfRows = labelsMovesPlayer1.length;

		this.player1RowActionMoves = new ArrayList<>();

		this.normalFormGameRowsPlayer1Considered = new boolean[this.numOfRows];

		for (int currentRow = 0; currentRow < this.numOfRows; currentRow++) {

			this.player1RowActionMoves.add( labelsMovesPlayer1[currentRow]
					                  .substring( labelsMovesPlayer1[currentRow].lastIndexOf(':') + 1 ) );

			this.normalFormGameRowsPlayer1Considered[currentRow] = true;

		}

		// Configurations for the Columns' Player (Player #2)
		this.numOfColumns = labelsMovesPlayer2.length;

		this.player2ColumnActionMoves = new ArrayList<>();

		this.normalFormGameColumnsPlayer2Considered = new boolean[this.numOfColumns];

		for (int currentColumn = 0; currentColumn < this.numOfColumns; currentColumn++) {

			this.player2ColumnActionMoves.add( labelsMovesPlayer2[currentColumn]
										 .substring( labelsMovesPlayer2[currentColumn].lastIndexOf(':') + 1 ) );

			this.normalFormGameColumnsPlayer2Considered[currentColumn] = true;

		}

		// The Payoffs' Utilities for this Normal-Form Game
		this.utilitiesPayoffsNormalFormMatrixPlayer1 = new int[this.numOfRows][this.numOfColumns];
		this.utilitiesPayoffsNormalFormMatrixPlayer2 = new int[this.numOfRows][this.numOfColumns];

		for (int currentRow = 0; currentRow < this.numOfRows; currentRow++) {

			for (int currentColumn = 0; currentColumn < this.numOfColumns; currentColumn++) {

				this.utilitiesPayoffsNormalFormMatrixPlayer1[currentRow][currentColumn] =
						utilitiesPayoffsNormalFormMatrixPlayer1[currentRow][currentColumn];
				this.utilitiesPayoffsNormalFormMatrixPlayer2[currentRow][currentColumn] =
						utilitiesPayoffsNormalFormMatrixPlayer2[currentRow][currentColumn];

			}

		}

	}

	public boolean getNormalFormGameRowPlayer1Considered(int numRow) {

		return this.normalFormGameRowsPlayer1Considered[numRow];

	}

	public void setNormalFormGameRowPlayer1AsNotConsidered(int numRow) {

		this.normalFormGameRowsPlayer1Considered[numRow] = false;

	}

	public boolean getNormalFormGameColumnPlayer2Considered(int numColumn) {

		return this.normalFormGameColumnsPlayer2Considered[numColumn];

	}

	public void setNormalFormGameColumnPlayer2AsNotConsidered(int numColumn) {

		this.normalFormGameColumnsPlayer2Considered[numColumn] = false;

	}



	/**
	 * Prints the this Normal-Form Game in a Matrix Form.
	 *
	 * NOTE:
	 * - The names of the actions are shortened to the first letter;
	 */
	public void showGame() {

		System.out.print("****");
		for (int j = 0; j<this.numOfColumns; j++)  if (this.normalFormGameColumnsPlayer2Considered[j])
			System.out.print("***********");
		System.out.println();
		System.out.print("    ");
		for (int j = 0; j<this.numOfColumns; j++)  if (this.normalFormGameColumnsPlayer2Considered[j]) {
				if (this.player2ColumnActionMoves.size()>0) {
					System.out.print("   ");
					System.out.print(this.player2ColumnActionMoves.get(j).substring(0,1));
					System.out.print("   ");
				}
				else {
					System.out.print("\t");
					System.out.print("Col " +j);
				}
		}
		System.out.println();
		for (int i = 0; i<this.numOfRows; i++) if (this.normalFormGameRowsPlayer1Considered[i]) {
			if (this.player1RowActionMoves.size()>0) System.out.print(this.player1RowActionMoves.get(i).substring(0,1)+ ": ");
			else System.out.print("Row " +i+ ": ");
			for (int j = 0; j<this.numOfColumns; j++)  if (this.normalFormGameColumnsPlayer2Considered[j]) {
				String fs = String.format("| %d,%d", this.utilitiesPayoffsNormalFormMatrixPlayer1[i][j], this.utilitiesPayoffsNormalFormMatrixPlayer2[i][j]);
				System.out.print(fs+"  ");
			}
			System.out.println("|");
		}
		System.out.print("****");
		for (int j = 0; j<this.numOfColumns; j++)  if (this.normalFormGameColumnsPlayer2Considered[j])
			System.out.print("***********");
		System.out.println();
	}
	
}
