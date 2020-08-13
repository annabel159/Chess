package game.pieces;

import java.util.Collection;

import game.Alliance;
import game.board.Board;
import game.board.Move;

/**
 * Class defining a piece.
 */
public abstract class Piece {

	protected final PieceType pieceType;
	protected final int piecePosition;
	protected final Alliance pieceAlliance; // every piece is black or white, declared as an enum
	protected final boolean isFirstMove;
	private final int cachedHashCode;

	public Piece(final PieceType pieceType, final Alliance pieceAlliance, final int piecePosition,
			final boolean isFirstMove) {
		this.pieceType = pieceType;
		this.piecePosition = piecePosition;
		this.pieceAlliance = pieceAlliance;
		this.isFirstMove = isFirstMove;
		this.cachedHashCode = computedHashCode();
	}

	private int computedHashCode() {
		int result = pieceType.hashCode();
		result = 31 * result + pieceAlliance.hashCode();
		result = 31 * result + piecePosition;
		result = 31 * result + (isFirstMove ? 1 : 0);
		return result;
	}

	// following method does reference equality so we override method because we want object equality
	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Piece)) {
			return false;
		}
		final Piece otherPiece = (Piece) other;
		return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType()
				&& pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove();
	}

	@Override
	public int hashCode() {
		return this.cachedHashCode;
	}

	/**
	 * Returns the color of the piece (white or black).
	 */
	public Alliance getPieceAlliance() {
		return this.pieceAlliance;
	}

	/**
	 * Returns true if it is the first move a piece makes during this game.
	 */
	public boolean isFirstMove() {
		return this.isFirstMove;
	}

	/**
	 * Returns the pieceType.
	 */
	public PieceType getPieceType() {
		return this.pieceType;
	}

	/**
	 * Returns the value of a piece.
	 */
	public int getPieceValue() {
		return this.pieceType.getPieceValue();
	}

	public abstract Piece movePiece(Move move);

	// set cannot have duplicates and is unordered
	// list is ordered
	// Collection is just a collection, it's unspecified
	public abstract Collection<Move> calculateLegalMoves(final Board board);

	/**
	 * Returns the position of a piece.
	 */
	public int getPiecePosition() {
		return this.piecePosition;
	}

	/**
	 * Enumeration of the different types of pieces.
	 */
	public enum PieceType {

		PAWN(100, "P") {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		KNIGHT(300, "N") {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		BISHOP(300, "B") {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		ROOK(500, "R") {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return true;
			}
		},
		QUEEN(900, "Q") {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		KING(10000, "K") {
			@Override
			public boolean isKing() {
				return true;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		};

		private String pieceName;
		private int pieceValue;

		PieceType(final int pieceValue, final String pieceName) {
			this.pieceName = pieceName;
			this.pieceValue = pieceValue;
		}

		@Override
		public String toString() {
			return this.pieceName;
		}

		/**
		 * Returns the value of a piece.
		 */
		public int getPieceValue() {
			return this.pieceValue;
		}

		/**
		 * Returns true if piece is a king.
		 */
		public abstract boolean isKing();

		/**
		 * Returns true if piece is a rook.
		 */
		public abstract boolean isRook();
	}
}
