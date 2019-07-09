package com.capgemini.chess.algorithms.implementation.universal_rules;

import java.util.HashMap;
import java.util.Map;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.piece_validation_strategy.PieceValidationStrategy;
import com.capgemini.chess.algorithms.implementation.piece_validation_strategy.pieces.BishopValidationService;
import com.capgemini.chess.algorithms.implementation.piece_validation_strategy.pieces.KingValidationService;
import com.capgemini.chess.algorithms.implementation.piece_validation_strategy.pieces.KnightValidationService;
import com.capgemini.chess.algorithms.implementation.piece_validation_strategy.pieces.PawnValidationService;
import com.capgemini.chess.algorithms.implementation.piece_validation_strategy.pieces.QueenValidationService;
import com.capgemini.chess.algorithms.implementation.piece_validation_strategy.pieces.RookValidationService;
import com.capgemini.chess.algorithms.implementation.universal_rules.filtering.CoordinatesFiltering;

/**
 * Checking what if the move is valid and what type of move is it
 * @author MACIEWRO
 *
 */
public class PieceValidationFactory {
	
	private final Map<PieceType, PieceValidationStrategy> moveStrategyByPieceType;
	private final CoordinatesFiltering coordinatesFiltering; //not in that way
	
	
	public PieceValidationFactory() {
		moveStrategyByPieceType = new HashMap<>();
		moveStrategyByPieceType.put(PieceType.PAWN, new PawnValidationService());
		moveStrategyByPieceType.put(PieceType.KING, new KingValidationService());
		moveStrategyByPieceType.put(PieceType.KNIGHT, new KnightValidationService());
		moveStrategyByPieceType.put(PieceType.BISHOP, new BishopValidationService());
		moveStrategyByPieceType.put(PieceType.ROOK, new RookValidationService());
		moveStrategyByPieceType.put(PieceType.QUEEN, new QueenValidationService());
		
		coordinatesFiltering = new CoordinatesFiltering();
	}
	
	public PieceValidationStrategy moveStrategyByPieceType(PieceType pieceType){
		return moveStrategyByPieceType.get(pieceType);
	}
	
	public PieceValidationStrategy moveStrategyByPieceCoordinate(Coordinate piecePosition, Board board){
		return moveStrategyByPieceType.get(board.getPieceAt(piecePosition).getType());
	}

	public CoordinatesFiltering getCoordinatesFiltering() {
		return coordinatesFiltering;
	}
	
	

}
