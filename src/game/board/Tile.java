package game.board;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import game.pieces.Piece;

/**
 * Class representing a chess tile. Class is made immutable by protecting the
 * tileCoordinate and making arguments to methods final.
 */
public abstract class Tile {

	protected final int tileCoordinate;

	private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

	private Tile(final int tileCoordinate) {
		this.tileCoordinate = tileCoordinate;
	}

	private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
		final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
		for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
			emptyTileMap.put(i, new EmptyTile(i));
		}

		return ImmutableMap.copyOf(emptyTileMap); // I want an immutable map. After I constructed the emptyTileMap,
													// nobody can change it.
		// use guavaLibrary of google - can be found on the Internet
		// can also use Collections.unmodifiableMap(emptyTileMap);
	}

	public static Tile createTile(final int tileCoordinate, final Piece piece) {
		return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
	}

	/**
	 * Returns whether or not there is a piece on the tile.
	 */
	public abstract boolean isTileOccupied();

	/**
	 * Returns piece on tile.
	 */
	public abstract Piece getPiece(); 

	/**
	 * Returns coordinate on tile.
	 */
	public int getTileCoordinate() {
		return this.tileCoordinate;
	}

	/**
	 * Class defining an empty tile.
	 */
	public static final class EmptyTile extends Tile { // can also be put in different file
		private EmptyTile(final int coordinate) {
			super(coordinate);
		}

		@Override
		public String toString() {
			return "-";
		}

		@Override
		public boolean isTileOccupied() {
			return false; // because by definition it is empty
		}

		@Override
		public Piece getPiece() {
			return null; // since there is no piece on an empty tile
		}
	}

	/**
	 * Class defining an occupied tile.
	 */
	public static final class OccupiedTile extends Tile {

		private final Piece pieceOnTile;

		private OccupiedTile(int tileCoordinate, final Piece pieceOnTile) {
			super(tileCoordinate);
			this.pieceOnTile = pieceOnTile;
		}

		@Override
		public String toString() {
			return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase()
					: getPiece().toString();
		} // Black lower case, white upper case

		@Override
		public boolean isTileOccupied() {
			return true;
		}

		@Override
		public Piece getPiece() {
			return this.pieceOnTile;
		}
	}
}
