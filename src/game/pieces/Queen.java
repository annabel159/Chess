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
 * Class determining queen piece.
 */
public class Queen extends Piece {

	private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = { -9, -8, -7, -1, 1, 7, 8, 9 };

	public Queen(final Alliance pieceAlliance, final int piecePosition) {
		super(PieceType.QUEEN, pieceAlliance, piecePosition, true);
	}
	
	public Queen(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
		super(PieceType.QUEEN, pieceAlliance, piecePosition, isFirstMove);
	}

	/**
	 * Returns a collection of the legal moves of queen on board.
	 */
	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {

		final List<Move> legalMoves = new ArrayList<>();

		for (final int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) {
			int candidateDestinationCoordinate = this.piecePosition;

			while (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {

				if (isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)
						|| isEightColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)) {
					break;
				}

				candidateDestinationCoordinate += candidateCoordinateOffset;

				if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
					final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

					if (!candidateDestinationTile.isTileOccupied()) {
						legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
					} else {

						final Piece pieceAtDestination = candidateDestinationTile.getPiece();
						final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

						if (this.pieceAlliance != pieceAlliance) { // occupied by enemy, then attack
							legalMoves.add(
									new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination)); // enemy
																														// piece
						}
						break;
					}
				}
			}
		}

		return ImmutableList.copyOf(legalMoves);
	}

	@Override
	public String toString() {
		return PieceType.QUEEN.toString();
	}

	/**
	 * Returns the special case of being in the first column.
	 */
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition]
				&& (candidateOffset == -9 || candidateOffset == 7 || candidateOffset == -1);
	}

	/**
	 * Returns the special case of being in the eigth column.
	 */
	private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.EIGHT_COLUMN[currentPosition]
				&& (candidateOffset == -7 || candidateOffset == 9 || candidateOffset == 1);
	}

	/**
	 * Returns a queen that has done a move.
	 */
	@Override
	public Queen movePiece(final Move move) {
		return new Queen(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
	}

}
