package com.capgemini.chess.algorithms.implementation.piece_validation_strategy.pieces;

import java.util.Collection;
import java.util.Map;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.PieceType;

public class BishopValidationService extends BishopRookValidationService {

	/**
	 * Strategy of move validation for bishop
	 */
	public BishopValidationService() {
		super();
	}

	@Override
	protected Map<StraightMoveDirection, Collection<Coordinate>> straightPathsCoordinates(Coordinate piecePosition) {
		return straightPathForPieceCoordinates(PieceType.BISHOP, piecePosition);
	}	


}
