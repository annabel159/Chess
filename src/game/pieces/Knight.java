package game.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import game.Alliance;
import game.board.Board;
import game.board.BoardUtils;
import game.board.Move;
import game.board.Move.MajorAttackMove;
import game.board.Move.MajorMove;
import game.board.Tile;

/**
 * Class the knight piece.
 */
public class Knight extends Piece {

	// List of candidate destination with respect to current position
	private final static int[] CANDIDATE_MOVE_COORDINATES = { -17, -15, -10, -6, 6, 10, 15, 17 };

	public Knight(final Alliance pieceAlliance, final int piecePosition) {
		super(PieceType.KNIGHT, pieceAlliance, piecePosition, true);
	}
	
	public Knight(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
		super(PieceType.KNIGHT, pieceAlliance, piecePosition, isFirstMove);
	}

	/**
	 * Returns a collection of legal moves of the knight on the board.
	 */
	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		// loop trough all candidate locations
		final List<Move> legalMoves = new ArrayList<>();

		for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {

			final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

			// if it is a valid Tile then check if occupied
			if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {

				if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset)
						|| isSecondColumnExclusion(this.piecePosition, currentCandidateOffset)
						|| isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset)
						|| isEightColumnExclusion(this.piecePosition, currentCandidateOffset)) {
					continue;
				}

				final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

				if (!candidateDestinationTile.isTileOccupied()) {
					legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
				} else {

					final Piece pieceAtDestination = candidateDestinationTile.getPiece();
					final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

					if (this.pieceAlliance != pieceAlliance) {
						legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination)); // enemy
																															// piece
					}
				}
			}
		}

		return ImmutableList.copyOf(legalMoves);
	}

	@Override
	public String toString() {
		return PieceType.KNIGHT.toString();
	}

	/**
	 * Returns the special case of being in the first column.
	 */
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		// if current position is in first column of the chess board
		return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidateOffset == -17) || (candidateOffset == -10)
				|| (candidateOffset == 6) || (candidateOffset == 15));
	}

	/**
	 * Returns the special case of being in the second column.
	 */
	private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.SECOND_COLUMN[currentPosition] && ((candidateOffset == -10) || (candidateOffset == 6));
	}

	/**
	 * Returns the special case of being in the seventh column.
	 */
	private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.SEVENTH_COLUMN[currentPosition] && ((candidateOffset == -6) || (candidateOffset == 10));
	}

	/**
	 * Returns the special case of being in the eigth column.
	 */
	private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.EIGHT_COLUMN[currentPosition] && ((candidateOffset == -15) || (candidateOffset == -6)
				|| (candidateOffset == 10) || (candidateOffset == 17));
	}

	/**
	 * Returns a knight that has made a move.
	 */
	@Override
	public Knight movePiece(final Move move) {
		return new Knight(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
	}

}
