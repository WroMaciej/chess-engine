package com.capgemini.chess.algorithms.implementation.universal_rules.filtering;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.piece_validation_strategy.PieceValidationStrategy;
import com.capgemini.chess.algorithms.implementation.universal_rules.PieceValidationFactory;
import com.capgemini.chess.algorithms.implementation.universal_rules.filtering.move_faking.MoveFakerService;

public class CoordinatesFiltering {

	private CoordinatesFilteringHelper coordinatesFilteringHelper; // decorator
	private CastlingValidatorService castlingValidator; // decorator
	private MoveFakerService moveFaker; // used for checking if move will cause
										// own check

	public CoordinatesFiltering() {
		coordinatesFilteringHelper = new CoordinatesFilteringHelper();
		castlingValidator = new CastlingValidatorService(coordinatesFilteringHelper);
		moveFaker = new MoveFakerService();
	}

	public boolean isEverMoved(Coordinate initialPiecePosition, List<Move> moveHistory) {
		return coordinatesFilteringHelper.isEverMoved(initialPiecePosition, moveHistory);
	}

	/**
	 * Removes taken coordinates from collection
	 * 
	 * @param coordinates
	 * @param board
	 * @return
	 */
	public Collection<Coordinate> onlyNotTakenCoordinates(Collection<Coordinate> coordinates, Board board) {
		return coordinates.stream()
				.filter(coordinate -> !coordinatesFilteringHelper.isCoordinateTaken(coordinate, board))
				.collect(Collectors.toList());
	}

	/**
	 * Gives collection of coordinates which are taken by opponent`s piece
	 * 
	 * @param piecePosition
	 * @param coordinates
	 * @param board
	 * @return
	 */
	public Collection<Coordinate> onlyTakenByOpponentCoordinates(Coordinate piecePosition,
			Collection<Coordinate> coordinates, Board board) {
		Color pieceColor = board.getPieceAt(piecePosition).getColor();
		return coordinates.stream()
				.filter(coordinate -> coordinatesFilteringHelper.isOpponentOnCoordinate(pieceColor, coordinate, board))
				.collect(Collectors.toList());

	}

	/**
	 * Gives collection of coordinates which are taken by opponent`s piece
	 * 
	 * @param piecePosition
	 * @param coordinates
	 * @param board
	 * @return
	 */
	public Collection<Coordinate> onlyTakenByOpponentCoordinates(Color pieceColor, Collection<Coordinate> coordinates,
			Board board) {
		return coordinates.stream()
				.filter(coordinate -> coordinatesFilteringHelper.isOpponentOnCoordinate(pieceColor, coordinate, board))
				.collect(Collectors.toList());
	}

	/**
	 * Removes coordinates which lays outside board
	 * 
	 * @param coordinates
	 * @return
	 */
	public Collection<Coordinate> onlyOnBoardCoordinates(Collection<Coordinate> coordinates) {
		return coordinates.stream().filter(coordinate -> coordinatesFilteringHelper.isCoordinateOnBoard(coordinate))
				.collect(Collectors.toList());
	}

	/**
	 * Finding all coordinates on path which reach next piece with it
	 * 
	 * @param piecePosition
	 * @param coordinatesPathStream
	 * @param board
	 * @return
	 */
	public Collection<Coordinate> onlyNotFurtherThanNextPiece(Coordinate piecePosition,
			Collection<Coordinate> coordinatesPathStream, Board board) {
		int nearestPieceDistance = coordinatesFilteringHelper.nearestPieceDistance(piecePosition, coordinatesPathStream,
				board);
		if (nearestPieceDistance == 0)
			return coordinatesPathStream;
		Predicate<Coordinate> filter = checkingCoordinate -> coordinatesFilteringHelper.getDistanceCalculator()
				.distance(piecePosition, checkingCoordinate) <= nearestPieceDistance;
		return coordinatesPathStream.stream().filter(filter).collect(Collectors.toList());
	}

	/**
	 * Finding all coordinates on path which reach next piece without it
	 * 
	 * @param piecePosition
	 * @param coordinatesPathStream
	 * @param board
	 * @return
	 */
	public Collection<Coordinate> onlyCloserThanNextPiece(Coordinate piecePosition,
			Collection<Coordinate> coordinatesPathStream, Board board) {
		int nearestPieceDistance = coordinatesFilteringHelper.nearestPieceDistance(piecePosition, coordinatesPathStream,
				board);
		if (nearestPieceDistance == 0)
			return coordinatesPathStream;
		Predicate<Coordinate> filter = checkingCoordinate -> coordinatesFilteringHelper.getDistanceCalculator()
				.distance(piecePosition, checkingCoordinate) < nearestPieceDistance;
		return coordinatesPathStream.stream().filter(filter).collect(Collectors.toList());
	}

	/**
	 * Removes coordinates leading to checking own king
	 * 
	 * @param piecePosition
	 * @param canHaveKingInCheckCoordinates
	 * @param board
	 * @return
	 */
	public Collection<Coordinate> onlyFreeOfOwnCheckCoordinates(Coordinate piecePosition,
			Collection<Coordinate> canHaveKingInCheckCoordinates, MoveType moveType, Board board) {
		// if its a fake board (to simulate move) - it is permitted to check
		// these conditions
		if (board.isFakeBoard()){
			return canHaveKingInCheckCoordinates;
		}
		Collection<Coordinate> freeOfOwnCheckCoordinates = new ArrayList<>();
		// iterate over every coordinate and do a fake move and
		Move fakeMove;
		Piece piece = board.getPieceAt(piecePosition);
		for (Coordinate moveTo : canHaveKingInCheckCoordinates) {
			// in case of castling - king should be imaginary teleported to all positions
			if (moveType == MoveType.CASTLING) {
				if (isCoordinateInCheck(piece.getColor(), moveTo, board)){
					return new ArrayList<>(); //it doesnt matter which coordinates are checked
				} else{
					freeOfOwnCheckCoordinates.add(moveTo);
				}
			}
			else{
				// get a board after fake move
				fakeMove = new Move(piecePosition, moveTo, moveType, piece);
				// if the move will not cause an own check - add it to filtered
				// collection
				if (!moveFaker.isFakeMoveCauseOwnCheck(fakeMove, board)) {
					freeOfOwnCheckCoordinates.add(moveTo);
				}
			}
		}
		return freeOfOwnCheckCoordinates;

	}

	public Coordinate kingCoordinate(Color kingColor, Board board) {
		for (int x = 0; x < Board.SIZE; x++) {
			for (int y = 0; y < Board.SIZE; y++) {
				Coordinate coordinate = new Coordinate(x, y);
				Piece pieceAtCoordinate = board.getPieceAt(coordinate);
				if (pieceAtCoordinate != null && pieceAtCoordinate.getType() == PieceType.KING
						&& pieceAtCoordinate.getColor() == kingColor) {
					return coordinate;
				}
			}
		}
		return null; // if no king on board
	}

	/**
	 * Gives information if given king is in check
	 * 
	 * @param kingColor
	 * @param board
	 * @return true if check exists
	 */
	public boolean isKingInCheck(Color kingColor, Board board) {
		Coordinate kingCoordinate = kingCoordinate(kingColor, board);
		// if there is no king on board (tests)
		if (kingCoordinate == null)
			return false;
		// iterate over every opponents piece and check capture moves pinned to
		// given king
		Collection<Coordinate> allOpponents = opponentsCoordinates(kingColor, board);
		PieceValidationFactory pieceValidationFactory = new PieceValidationFactory(); // TODO it should be injected
		PieceValidationStrategy strategy;
		Map<Coordinate, Move> opponentMoves;
		Move singleMove;
		// for each opponent
		for (Coordinate opponent : allOpponents) {
			strategy = pieceValidationFactory.moveStrategyByPieceCoordinate(opponent, board);
			opponentMoves = strategy.allLegalMoves(opponent, board);
			// for each legal TO position
			for (Coordinate toPosition : opponentMoves.keySet()) {
				singleMove = opponentMoves.get(toPosition);
				// if its a capture for king or en passant -> IT IS A CHECK
				if ((singleMove.getType() == MoveType.CAPTURE || singleMove.getType() == MoveType.EN_PASSANT)
						&& singleMove.getTo().equals(kingCoordinate)) {
					return true;
				}
			}
		}
		return false;

	}

	private Collection<Coordinate> opponentsCoordinates(Color ownKingColor, Board board) {
		// collection of all pieces on board
		Collection<Coordinate> allOpponentsCoordinates = coordinatesFilteringHelper.allTakenCoordinates(board);
		// taken only by opponent
		allOpponentsCoordinates = onlyTakenByOpponentCoordinates(ownKingColor, allOpponentsCoordinates, board);
		return allOpponentsCoordinates;
	}
	
	private Collection<Coordinate> ownPiecesCoordinates(Color ownKingColor, Board board) {
		// collection of all pieces on board
		Collection<Coordinate> allTakenCoordinates = coordinatesFilteringHelper.allTakenCoordinates(board);
		// NOT taken by opponent
		Collection<Coordinate> friends = onlyTakenByOpponentCoordinates(ownKingColor.getOppositeColor(), allTakenCoordinates, board);
		return friends;
	}

	/**
	 * Check if it would be a check if king was on given position
	 * 
	 * @param kingColor
	 * @param checkingCoordinate
	 * @param board
	 * @return
	 */
	private boolean isCoordinateInCheck(Color kingColor, Coordinate checkingCoordinate, Board board) {
		// fake move king attack to given position position and check if it would be a check
		Coordinate kingPosition = kingCoordinate(kingColor, board);
		if (kingPosition == null)
			return false;
		if (kingPosition.equals(checkingCoordinate))
			return isKingInCheck(kingColor, board);

		Piece king;
		if (kingColor == Color.WHITE) {
			king = Piece.WHITE_KING;
		} else {
			king = Piece.BLACK_KING;
		}
		Move fakeMove = new Move(kingPosition, checkingCoordinate, MoveType.ATTACK, king);
		return moveFaker.isFakeMoveCauseOwnCheck(fakeMove, board);
	}
	
	/**
	 * Checks if there is any legal move for given color
	 * @param board situation on board
	 * @return true if given color has any legal move
	 */
	public boolean isAnyMoveValid(Color colorToMove, Board board) {
		//iterate over every piece in given color and break if 1st legal move appear
		Collection<Coordinate> friends = ownPiecesCoordinates(colorToMove, board);
		PieceValidationStrategy strategy;
		PieceValidationFactory pieceValidationFactory = new PieceValidationFactory(); // TODO it should be injected
		Map<Coordinate, Move> legalMoves;
		for (Coordinate friend : friends){
			strategy = pieceValidationFactory.moveStrategyByPieceCoordinate(friend, board);
			legalMoves = strategy.allLegalMoves(friend, board);
			//if there is any legal move -> break
			if (!legalMoves.keySet().isEmpty()){
				return true;
			}
		}
		return false;
	}

	public CoordinatesFilteringHelper getCoordinatesFilteringHelper() {
		return coordinatesFilteringHelper;
	}

	public CastlingValidatorService getCastlingValidator() {
		return castlingValidator;
	}



}
