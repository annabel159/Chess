package game.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import game.Alliance;
import game.pieces.Bishop;
import game.pieces.King;
import game.pieces.Knight;
import game.pieces.Pawn;
import game.pieces.Piece;
import game.pieces.Queen;
import game.pieces.Rook;
import game.player.BlackPlayer;
import game.player.Player;
import game.player.WhitePlayer;

/**
 * Class defining the board on which chess is being played.
 */
public class Board {

	private final List<Tile> gameBoard;
	// keep track on pieces on the board
	private final Collection<Piece> whitePieces;
	private final Collection<Piece> blackPieces;

	private final WhitePlayer whitePlayer;
	private final BlackPlayer blackPlayer;
	private final Player currentPlayer;
	
	private final Pawn enPassantPawn;

	private Board(final Builder builder) {
		this.gameBoard = createGameBoard(builder);
		this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
		this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);
		this.enPassantPawn = builder.enPassantPawn;
		
		final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
		final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);

		this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
		this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
		this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
			final String tileText = this.gameBoard.get(i).toString();
			builder.append(String.format("%3s", tileText));
			if ((i + 1) % BoardUtils.NUM_TILES_PER_ROW == 0) {
				builder.append("\n");
			}
		}
		return builder.toString();
	}

	/**
	 * Returns the white Player.
	 */
	public Player whitePlayer() {
		return this.whitePlayer;
	}

	/**
	 * Returns the black Player.
	 */
	public Player blackPlayer() {
		return this.blackPlayer;
	}

	/**
	 * Returns the current Player.
	 */
	public Player currentPlayer() {
		return this.currentPlayer;
	}

	/**
	 * Returns a collection of the black pieces.
	 */
	public Collection<Piece> getBlackPieces() {
		return this.blackPieces;
	}

	/**
	 * Returns a collection of the white pieces.
	 */
	public Collection<Piece> getWhitePieces() {
		return this.whitePieces;
	}
	
	/**
	 * Returns the pawn that is doing the en passant move.
	 */
	public Pawn getEnPassantPawn() {
		return this.enPassantPawn;
	}

	/**
	 * Returns a collection of legal moves.
	 */
	private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {

		final List<Move> legalMoves = new ArrayList<>();

		for (final Piece piece : pieces) {
			legalMoves.addAll(piece.calculateLegalMoves(this)); // calculate legalMoves gives collection and this
																// collection will be added to our container
		}
		return ImmutableList.copyOf(legalMoves);
	}

	/**
	 * Returns a collection of the active pieces.
	 */
	private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Alliance alliance) {
		final List<Piece> activePieces = new ArrayList<>();
		for (final Tile tile : gameBoard) {
			if (tile.isTileOccupied()) {
				final Piece piece = tile.getPiece();
				if (piece.getPieceAlliance() == alliance) {
					activePieces.add(piece);
				}
			}
		}
		return ImmutableList.copyOf(activePieces);
	}

	/**
	 * Returns the tile on the given tileCoordinate.
	 */
	public Tile getTile(final int tileCoordinate) {
		return gameBoard.get(tileCoordinate);
	}

	/**
	 * Returns a list of tiles to create a new GameBoard.
	 */
	private static List<Tile> createGameBoard(final Builder builder) {
		final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
		for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
			tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
		}
		return ImmutableList.copyOf(tiles);
	}

	/**
	 * Creates a standard board.
	 */
	public static Board createStandardBoard() {
		final Builder builder = new Builder();
		// Black Layout
		builder.setPiece(new Rook(Alliance.BLACK, 0));
		builder.setPiece(new Knight(Alliance.BLACK, 1));
		builder.setPiece(new Bishop(Alliance.BLACK, 2));
		builder.setPiece(new Queen(Alliance.BLACK, 3));
		builder.setPiece(new King(Alliance.BLACK, 4, true, true));
		builder.setPiece(new Bishop(Alliance.BLACK, 5));
		builder.setPiece(new Knight(Alliance.BLACK, 6));
		builder.setPiece(new Rook(Alliance.BLACK, 7));
		builder.setPiece(new Pawn(Alliance.BLACK, 8));
		builder.setPiece(new Pawn(Alliance.BLACK, 9));
		builder.setPiece(new Pawn(Alliance.BLACK, 10));
		builder.setPiece(new Pawn(Alliance.BLACK, 11));
		builder.setPiece(new Pawn(Alliance.BLACK, 12));
		builder.setPiece(new Pawn(Alliance.BLACK, 13));
		builder.setPiece(new Pawn(Alliance.BLACK, 14));
		builder.setPiece(new Pawn(Alliance.BLACK, 15));
		// White Layout
		builder.setPiece(new Rook(Alliance.WHITE, 63));
		builder.setPiece(new Knight(Alliance.WHITE, 62));
		builder.setPiece(new Bishop(Alliance.WHITE, 61));
		builder.setPiece(new Queen(Alliance.WHITE, 59));
		builder.setPiece(new King(Alliance.WHITE, 60, true, true));
		builder.setPiece(new Bishop(Alliance.WHITE, 58));
		builder.setPiece(new Knight(Alliance.WHITE, 57));
		builder.setPiece(new Rook(Alliance.WHITE, 56));
		builder.setPiece(new Pawn(Alliance.WHITE, 55));
		builder.setPiece(new Pawn(Alliance.WHITE, 54));
		builder.setPiece(new Pawn(Alliance.WHITE, 53));
		builder.setPiece(new Pawn(Alliance.WHITE, 52));
		builder.setPiece(new Pawn(Alliance.WHITE, 51));
		builder.setPiece(new Pawn(Alliance.WHITE, 50));
		builder.setPiece(new Pawn(Alliance.WHITE, 49));
		builder.setPiece(new Pawn(Alliance.WHITE, 48));
		// white to move
		builder.setMoveMaker(Alliance.WHITE);

		return builder.build();
	}

	/**
	 * Inner class that builds a board.
	 */
	public static class Builder {

		Map<Integer, Piece> boardConfig;
		Alliance nextMoveMaker;
		Pawn enPassantPawn;

		public Builder() {
			this.boardConfig = new HashMap<>();
		}

		public Builder setPiece(final Piece piece) {
			this.boardConfig.put(piece.getPiecePosition(), piece);
			return this;
		}

		public Builder setMoveMaker(final Alliance nextMoveMaker) {
			this.nextMoveMaker = nextMoveMaker;
			return this;
		}

		// immutable board based on builder
		public Board build() {
			return new Board(this);
		}

		public void setEnPassantPawn(Pawn enPassantPawn) {
			this.enPassantPawn = enPassantPawn;
		}
	}

	public Iterable<Move> getAllLegalMoves() {
		return Iterables.unmodifiableIterable(
				Iterables.concat(this.whitePlayer.getLegalMoves(), this.blackPlayer.getLegalMoves()));
	}

}
