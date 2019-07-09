package com.capgemini.chess.algorithms.implementation.universal_rules.filtering;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;


public class CoordinatesFilteringHelper {

	
	private DistanceCalculator distanceCalculator = new DistanceCalculator();

	/**
	 * Checks if coordinate is in bound of board
	 * 
	 * @param coordinate
	 *            position
	 * @return true if position belongs to board
	 */
	public boolean isCoordinateOnBoard(Coordinate coordinate) {
		return isCoordinateOnBoard(coordinate.getX(), coordinate.getY());
	}
	
	/**
	 * Checks if coordinate is in bound of board
	 * 
	 * @param coordinateX
	 * @param coordinateY
	 * @return true if position belongs to board
	 */
	public boolean isCoordinateOnBoard(int coordinateX, int coordinateY) {
		boolean isColumnOnBoard = coordinateX >= 0 && coordinateX < Board.SIZE;
		boolean isRowOnBoard = coordinateY >= 0 && coordinateY < Board.SIZE;
		return isColumnOnBoard && isRowOnBoard;
	}
	
	/**
	 * Checks if coordinate is taken
	 * 
	 * @param coordinate
	 *            position on board
	 * @param board
	 * @return true if piece stays on given position
	 */
	boolean isCoordinateTaken(Coordinate coordinate, Board board) {
		return board.getPieceAt(coordinate) != null;
	}


	/**
	 * Checks if coordinate is belongs to given list
	 * 
	 * @param coordinate
	 * @param coordinateList
	 * @return true if is present
	 */
	boolean isCoordinatePresent(Coordinate coordinate, Collection<Coordinate> coordinates) {
		return coordinates.stream().anyMatch(anyCoordinate -> anyCoordinate.equals(coordinate));
	}


	/**
	 * Checks if opponent`s piece is placed on given coordinates
	 * 
	 * @param actualPlayer
	 *            actual player color
	 * @param coordinate
	 *            checking coordinate
	 * @param board
	 *            actual board
	 * @return true if opponent stays on given coordinates
	 */
	boolean isOpponentOnCoordinate(Color actualPlayer, Coordinate coordinate, Board board) {
		return board.getPieceAt(coordinate) == null ? false : board.getPieceAt(coordinate).getColor() == actualPlayer.getOppositeColor();
	}
	
	
	/**
	 * Checks if piece has ever moved during game basing on its initial position
	 * @param initialPiecePosition initial position of piece 
	 * @param moveHistory history of all moves
	 * @return true if piece has moved at least once
	 */
	boolean isEverMoved(Coordinate initialPiecePosition, List<Move> moveHistory){
		for (Move moveDone: moveHistory){
			//move could be performed both FROM and (for sure in case of position different than initial) TO this position
			if (moveDone.getFrom().equals(initialPiecePosition) || moveDone.getTo().equals(initialPiecePosition)) return true;
		}
		return false;
	}
	
	
	
	/**
	 * Gives position of nearest piece on path given by coordinates stream
	 * @param piecePosition
	 * @param coordinatesPathStream
	 * @param board
	 * @return coordinates of piece or null if there is no piece on path
	 */
	private Coordinate nearestPieceCoordinates(Coordinate piecePosition, Collection<Coordinate> coordinatesPathStream,
			Board board) {
		Coordinate nearestPieceCoordinates = null;
		int actualNearestDistance = 0;
		for (Coordinate checkingCoordinate : coordinatesPathStream) {
			if (isCoordinateTaken(checkingCoordinate, board)){
				if (nearestPieceCoordinates == null 
						|| distanceCalculator.distance(piecePosition, checkingCoordinate) < actualNearestDistance){
					nearestPieceCoordinates = checkingCoordinate;
					actualNearestDistance = distanceCalculator.distance(piecePosition, checkingCoordinate);
				}			
			}
		}
		return nearestPieceCoordinates;
	}
	
	/**
	 * Gives position of nearest piece
	 * @param piecePosition actual piece position
	 * @param coordinatesPathStream
	 * @param board
	 * @return Piece or null of whole path is empty
	 */
	Piece nearestPieceOnPath(Coordinate piecePosition, Collection<Coordinate> coordinatesPathStream, Board board){
		Coordinate nearestPieceCoordinates = nearestPieceCoordinates(piecePosition, coordinatesPathStream, board);
		return nearestPieceCoordinates == null ? null : board.getPieceAt(nearestPieceCoordinates);
	}
	
	/**
	 * Gives distance between given piece position and its nearest piece
	 * @param piecePosition
	 * @param coordinatesPathStream
	 * @param board
	 * @return distance between pieces or 0 if there is no piece at path
	 */
	int nearestPieceDistance(Coordinate piecePosition, Collection<Coordinate> coordinatesPathStream,
			Board board){
		Coordinate nearestPieceCoordinates = nearestPieceCoordinates(piecePosition, coordinatesPathStream, board);
		if (nearestPieceCoordinates == null) return 0;
		else return distanceCalculator.distance(piecePosition, nearestPieceCoordinates);
	}

	
	
	/**
	 * Checks if piece from given position has opponents King on its capture coordinates
	 * @param piecePosition
	 * @param capturePositions
	 * @param board
	 * @return true if piece has king on capture position
	 */
	boolean isOpponentKingOnCapturePosition(Coordinate piecePosition, Collection<Coordinate> capturePositions, Board board){
		Color pieceColor = board.getPieceAt(piecePosition).getColor();
		Color opponentColor = pieceColor.getOppositeColor();
		Piece opponentsKing = Piece.pieceByTypeAndColor(PieceType.KING, opponentColor);
		return capturePositions.stream().anyMatch(coordinate -> board.getPieceAt(coordinate).equals(opponentsKing));
	}
	
	/**
	 * Collection of all coordinates taken by any piece
	 * @param actualColor
	 * @param board
	 * @return
	 */
	Collection<Coordinate> allTakenCoordinates(Board board){
		Collection<Coordinate> alltakenCoordinates = new ArrayList<>();
		for (int coordinateX = 0; coordinateX < Board.SIZE; coordinateX++){
			for (int coordinateY = 0; coordinateY < Board.SIZE; coordinateY++){
				Coordinate checkingCoordinate = new Coordinate(coordinateX, coordinateY);
				if (isCoordinateTaken(checkingCoordinate, board)) {
					alltakenCoordinates.add(checkingCoordinate);
				}
			}
		}
		return alltakenCoordinates;
	}

	DistanceCalculator getDistanceCalculator() {
		return distanceCalculator;
	}
	
	
}
