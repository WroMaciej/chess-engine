package com.capgemini.chess.algorithms.implementation.piece_validation_strategy.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;

public abstract class BishopRookValidationService extends AnyPieceValidationService {
	
	/**
	 * Direction of move with its moving vectors
	 * @author MACIEWRO
	 *
	 */
	protected enum StraightMoveDirection{
		UP(PieceType.ROOK, 0, 1),
		DOWN(PieceType.ROOK, 0, -1),
		LEFT(PieceType.ROOK, -1, 0),
		RIGHT(PieceType.ROOK, 1, 0),
		UP_LEFT(PieceType.BISHOP, -1, 1),
		UP_RIGHT(PieceType.BISHOP, 1, 1),
		DOWN_LEFT(PieceType.BISHOP, -1, -1),
		DOWN_RIGHT(PieceType.BISHOP, 1, -1);
		
		PieceType allowedPieceType;
		int moveVectorX;
		int moveVectorY;
		
		private StraightMoveDirection(PieceType allowedPieceType, int moveVectorX, int moveVectorY) {
			this.allowedPieceType = allowedPieceType;
			this.moveVectorX = moveVectorX;
			this.moveVectorY = moveVectorY;
		}
		
	}
	

	/**
	 * Gives all straight path coordinates for bishop and rook
	 * @param direction move direction
	 * @param piecePosition
	 * @return List of coordinates for single path (ex UP)
	 */
	protected Collection<Coordinate> singleStraightPathCoordinates(StraightMoveDirection direction, Coordinate piecePosition) {
		int moveVectorX = direction.moveVectorX;
		int moveVectorY = direction.moveVectorY;
		
		Collection<Coordinate> coordinatesList = new ArrayList<>();
		int positionX = piecePosition.getX() + moveVectorX;
		int positionY = piecePosition.getY() + moveVectorY;
		while (coordinatesFiltering.getCoordinatesFilteringHelper().isCoordinateOnBoard(positionX, positionY)) {
			coordinatesList.add(new Coordinate(positionX, positionY));
			positionX+=moveVectorX;
			positionY+=moveVectorY;
		}
		return coordinatesList;
	}
	
	/**
	 * Abstract for getting all 4 moving paths
	 * Implementation depends if its Bishop or Rook
	 */
	protected abstract Map<StraightMoveDirection, Collection<Coordinate>> straightPathsCoordinates(Coordinate piecePosition);
	
	/**
	 * Gives map with coordinates map for given piece
	 * @param pieceType
	 * @param piecePosition
	 * @return
	 */
	protected Map<StraightMoveDirection, Collection<Coordinate>> straightPathForPieceCoordinates(PieceType pieceType, Coordinate piecePosition){
		Map<StraightMoveDirection, Collection<Coordinate>> pathMapForPiece = new HashMap<>();
		Collection<Coordinate> sinlePathCoordinates;
		for (StraightMoveDirection direction: StraightMoveDirection.values()){
			if (direction.allowedPieceType == pieceType){
				//for every correct direction
				sinlePathCoordinates = singleStraightPathCoordinates(direction, piecePosition);
				pathMapForPiece.put(direction, sinlePathCoordinates);
			}
		}
		return pathMapForPiece;
	}
	
	
	
	

	public BishopRookValidationService() {
		super();
	}
	
	/**
	 * Converting all coordinates from all 4 paths to list
	 * @param straightMovesMap
	 * @return
	 */
	private Collection<Coordinate> straightMovesMapToList(Map<StraightMoveDirection, Collection<Coordinate>> straightMovesMap){
		Collection<Coordinate> coordinatesList = new ArrayList<>();
		for (Map.Entry<StraightMoveDirection, Collection<Coordinate>> singlePath : straightMovesMap.entrySet()){
			coordinatesList.addAll(singlePath.getValue());
		}
		return coordinatesList;
	}

	//TODO use stream to filter map
	@Override
	protected Collection<Coordinate> legalAttackWithKingInCheckCoordinates(Coordinate piecePosition, Board board) {
		//resulting map for filtered coordinates
		Map<StraightMoveDirection, Collection<Coordinate>> filteredMap = new HashMap<>();
		//get all 4 straight moves paths
		Map<StraightMoveDirection, Collection<Coordinate>> all4StraightPaths = straightPathsCoordinates(piecePosition);
		//iterate over every direction (path) (4 times)
		
		for (StraightMoveDirection direction : all4StraightPaths.keySet()){
			Collection<Coordinate> singlePath = new ArrayList<>();
			singlePath = all4StraightPaths.get(direction);
			//leave only coordinates nearer to next piece
			singlePath = coordinatesFiltering.onlyCloserThanNextPiece(piecePosition, singlePath, board);
			filteredMap.put(direction, singlePath);
		}
		//giving all map values to list
		return straightMovesMapToList(filteredMap);
	}

	//TODO use stream to filter map
	@Override
	protected Collection<Coordinate> legalCaptureWithKingInCheckCoordinates(Coordinate piecePosition, Board board) {
		//resulting map for filtered coordinates
		Map<StraightMoveDirection, Collection<Coordinate>> filteredMap = new HashMap<>();
		//get all 4 straight moves paths
		Map<StraightMoveDirection, Collection<Coordinate>> all4StraightPaths = straightPathsCoordinates(piecePosition);
		//iterate over every direction (path)
		for (StraightMoveDirection direction : all4StraightPaths.keySet()){
			Collection<Coordinate> singlePath = new ArrayList<>();
			singlePath = all4StraightPaths.get(direction);
			//remove coordinates further than nearest piece (nearest piece and closer coordinates stays)
			singlePath = coordinatesFiltering.onlyNotFurtherThanNextPiece(piecePosition, singlePath, board);
			//remove coordinates other than taken by opponent`s piece
			singlePath = coordinatesFiltering.onlyTakenByOpponentCoordinates(piecePosition, singlePath, board);
			filteredMap.put(direction, singlePath);
		}
		//giving all map values to list
		return straightMovesMapToList(filteredMap);
	}
}
