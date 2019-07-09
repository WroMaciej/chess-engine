package com.capgemini.chess.algorithms.implementation.piece_validation_strategy.pieces;

import java.util.Collection;
import java.util.Map;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.PieceType;

/**
 * Strategy of move validation for Rook
 */
public class RookValidationService extends BishopRookValidationService {

	public RookValidationService() {
		super();
	}
	
	@Override
	protected Map<StraightMoveDirection, Collection<Coordinate>> straightPathsCoordinates(Coordinate piecePosition) {
		return straightPathForPieceCoordinates(PieceType.ROOK, piecePosition);
	}	
	
}
