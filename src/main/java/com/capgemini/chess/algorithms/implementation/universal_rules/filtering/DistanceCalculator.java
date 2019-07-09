package com.capgemini.chess.algorithms.implementation.universal_rules.filtering;

import com.capgemini.chess.algorithms.data.Coordinate;


/**
 * Calculating distance between two coordinates
 * @author MACIEWRO
 *
 */
public class DistanceCalculator {
	
	/**
	 * Difference between two given positions on board
	 * 
	 * @param position1
	 *            starting coordinates
	 * @param position2
	 *            final coordinates
	 * @return Difference of coordinates gives as point (x=dX, y=dY)
	 */
	private Coordinate coordinatesDifference(Coordinate position1, Coordinate position2) {
		int deltaX = position2.getX() - position1.getX();
		int deltaY = position2.getY() - position1.getY();
		return new Coordinate(deltaX, deltaY);
	}

	/**
	 * Vector of move from one coordinate to another to perform shortest move
	 * (as King)
	 * 
	 * @param position1
	 *            starting coordinates
	 * @param position2
	 *            final coordinates
	 * @return
	 */
	Coordinate coordinatesVector(Coordinate position1, Coordinate position2) {
		Coordinate coordinatesDifference = coordinatesDifference(position1, position2);
		int vectorX = Integer.signum(coordinatesDifference.getX());
		int vectorY = Integer.signum(coordinatesDifference.getY());
		return new Coordinate(vectorX, vectorY);
	}

	/**
	 * Calculates distance from one position to another Distance diagonally
	 * (from 1,1 to 2,2) is also counted as 1! It is counted as number of king
	 * moves needed to move from position1 to position2 on empty board
	 * 
	 * @param position1
	 * @param position2
	 * @return number number of king moves needed to move from position1 to
	 *         position2 on empty board
	 */
	public int distance(Coordinate position1, Coordinate position2) {
		if (position1.equals(position2))
			return 0;
		else {
			int kingMovesNumber = 0;
			int kingPositionX = position1.getX();
			int kingPositionY = position1.getY();
			// move diagonally from position 1 as long as it is possible and
			// then move horizontal/vertical
			while (kingPositionX != position2.getX() || kingPositionY != position2.getY()) {
				Coordinate actualKingPosition = new Coordinate(kingPositionX, kingPositionY);
				Coordinate moveVector = coordinatesVector(actualKingPosition, position2);
				kingPositionX += moveVector.getX();
				kingPositionY += moveVector.getY();
				kingMovesNumber++;
			}
			return kingMovesNumber;
		}

	}

}
