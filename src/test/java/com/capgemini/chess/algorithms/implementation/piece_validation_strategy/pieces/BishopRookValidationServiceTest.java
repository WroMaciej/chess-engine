package com.capgemini.chess.algorithms.implementation.piece_validation_strategy.pieces;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.piece_validation_strategy.pieces.BishopRookValidationService;
import com.capgemini.chess.algorithms.implementation.piece_validation_strategy.pieces.BishopValidationService;
import com.capgemini.chess.algorithms.implementation.piece_validation_strategy.pieces.BishopRookValidationService.StraightMoveDirection;

public class BishopRookValidationServiceTest {

	
	@Test 
	public void shouldReturnUpPath(){
		//given
		BishopRookValidationService bishopMove = new BishopValidationService();
		Coordinate coordinate = new Coordinate(0, 0);
		//when
		Collection<Coordinate> path = bishopMove.singleStraightPathCoordinates(StraightMoveDirection.UP, coordinate);
		//then
		assertEquals(7, path.size());
		assertTrue(path.contains(new Coordinate(0, 4)));
		assertFalse(path.contains(new Coordinate(1, 1)));
	}
	
	@Test 
	public void shouldReturnUpRightPath(){
		//given
		BishopRookValidationService bishopMove = new BishopValidationService();
		Coordinate coordinate = new Coordinate(0, 0);
		//when
		Collection<Coordinate> path = bishopMove.singleStraightPathCoordinates(StraightMoveDirection.UP_RIGHT, coordinate);
		//then
		assertEquals(7, path.size());
		assertTrue(path.contains(new Coordinate(3, 3)));
		assertFalse(path.contains(new Coordinate(1, 0)));
	}
	
	@Test
	public void shouldReturnLegalMovesWhenBishopInEdge(){
		//given
		BishopRookValidationService bishopMove = new BishopValidationService();
		Board board = new Board();
		Coordinate bishopCoordinate = new Coordinate(0, 0);
		board.setPieceAt(Piece.WHITE_BISHOP, bishopCoordinate);
		//when
		Collection<Coordinate> legalCoordinates = bishopMove.allLegalMoves(bishopCoordinate, board).keySet();
		//then
		assertEquals(7 , legalCoordinates.size());
	}
	
	@Test
	public void shouldReturn1StraightPathWhenBishopInEdge(){
		//given
		BishopRookValidationService bishopMove = new BishopValidationService();
		Coordinate bishopCoordinate = new Coordinate(0, 0);
		//when
		Collection<Coordinate> upRightPath = bishopMove.straightPathForPieceCoordinates(PieceType.BISHOP, bishopCoordinate).get(StraightMoveDirection.UP_RIGHT);
		//then
		assertEquals(7 , upRightPath.size());
		assertTrue(upRightPath.contains(new Coordinate(1, 1)));
	}
	
	@Test
	public void shouldReturnPathFromMapWhenBishopInEdge(){
		//given
		BishopRookValidationService bishopMove = new BishopValidationService();
		Coordinate bishopCoordinate = new Coordinate(0, 0);
		//board.setPieceAt(Piece.WHITE_BISHOP, bishopCoordinate);
		//when
		Collection<Coordinate> upRightPath = bishopMove.straightPathsCoordinates(bishopCoordinate).get(StraightMoveDirection.UP_RIGHT);
		//then
		assertEquals(7 , upRightPath.size());
		assertTrue(upRightPath.contains(new Coordinate(1, 1)));
	}
	
}
