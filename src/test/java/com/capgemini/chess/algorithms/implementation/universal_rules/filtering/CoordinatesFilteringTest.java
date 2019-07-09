package com.capgemini.chess.algorithms.implementation.universal_rules.filtering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.universal_rules.filtering.CoordinatesFiltering;

public class CoordinatesFilteringTest {

	@Test
	public void shouldReturnEmptyCoordinatesWhenOnlyNotTaken() {
		// given
		CoordinatesFiltering coordinatesFiltering = new CoordinatesFiltering();
		Board board = new Board();
		Coordinate whitePawn = new Coordinate(1, 2);
		Coordinate blackPawn = new Coordinate(1, 3);
		Coordinate emptyCoordinate = new Coordinate(1, 1);
		board.setPieceAt(Piece.WHITE_PAWN, whitePawn);
		board.setPieceAt(Piece.BLACK_PAWN, blackPawn);
		
		Collection<Coordinate> toFilter = new ArrayList<>();
		toFilter.add(whitePawn);
		toFilter.add(blackPawn);
		toFilter.add(emptyCoordinate);
		
		// when
		toFilter = coordinatesFiltering.onlyNotTakenCoordinates(toFilter, board);

		// then
		assertEquals(1, toFilter.size());
		assertEquals(true, toFilter.contains(emptyCoordinate));
	}
	
	@Test
	public void shouldReturnEmptyPath(){
		//given
		CoordinatesFiltering coordinatesFiltering = new CoordinatesFiltering();
		Board board = new Board();
		Coordinate bishopCoordinate = new Coordinate(0, 0);
		Coordinate queenCoordinate = new Coordinate(3, 3);
		Coordinate rookBehindQueen = new Coordinate(5, 5);
		board.setPieceAt(Piece.BLACK_BISHOP, bishopCoordinate);
		board.setPieceAt(Piece.WHITE_QUEEN, queenCoordinate);
		board.setPieceAt(Piece.WHITE_ROOK, rookBehindQueen);
		Collection<Coordinate> coordinatesPathStream = new ArrayList<>();
		coordinatesPathStream.add(new Coordinate(1, 1));
		coordinatesPathStream.add(new Coordinate(2, 2));
		coordinatesPathStream.add(new Coordinate(3, 3));
		coordinatesPathStream.add(new Coordinate(4, 4));
		coordinatesPathStream.add(new Coordinate(5, 5));
		coordinatesPathStream.add(new Coordinate(6, 6));
		coordinatesPathStream.add(new Coordinate(7, 7));
		Collection<Coordinate> closer = new ArrayList<>();
		Collection<Coordinate> notFurter = new ArrayList<>();
		//when
		closer = coordinatesFiltering.onlyCloserThanNextPiece(bishopCoordinate, coordinatesPathStream, board);
		notFurter = coordinatesFiltering.onlyNotFurtherThanNextPiece(bishopCoordinate, coordinatesPathStream, board);
		//then
		assertEquals(2, closer.size());
		assertEquals(3, notFurter.size());
	}
	
	@Test
	public void shouldFindWhiteKingIsChecked(){
		//given
		CoordinatesFiltering coordinatesFiltering = new CoordinatesFiltering();
		Board board = new Board();
		board.setPieceAt(Piece.BLACK_BISHOP, new Coordinate(0, 0));
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(4, 4));
		board.setPieceAt(Piece.WHITE_QUEEN, new Coordinate(7, 1));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(2, 6));
		//when
		boolean whiteKingInCheck = coordinatesFiltering.isKingInCheck(Color.WHITE, board);
		boolean blackKingInCheck = coordinatesFiltering.isKingInCheck(Color.BLACK, board);
		
		//then
		assertTrue(whiteKingInCheck);
		assertFalse(blackKingInCheck);
	}
	
	@Test
	public void shouldReturnOnlyFreeOfOwnCheckCoordinatesForPawn(){
		//given
		CoordinatesFiltering coordinatesFiltering = new CoordinatesFiltering();
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(4, 0));
		Coordinate pawnPosition = new Coordinate(5, 1);
		board.setPieceAt(Piece.WHITE_PAWN, pawnPosition);
		board.setPieceAt(Piece.BLACK_BISHOP, new Coordinate(6, 2));
		Collection<Coordinate> nonOwnCheckAttack = new ArrayList<>();
		nonOwnCheckAttack.add(new Coordinate(5, 2));
		nonOwnCheckAttack.add(new Coordinate(5, 3));
		Coordinate onlyLegalCaptureMove = new Coordinate(6, 2);
		Collection<Coordinate> nonOwnCheckCapture = new ArrayList<>();
		nonOwnCheckCapture.add(onlyLegalCaptureMove);
		//when
		nonOwnCheckAttack = coordinatesFiltering.onlyFreeOfOwnCheckCoordinates(pawnPosition, nonOwnCheckAttack , MoveType.ATTACK, board);	
		nonOwnCheckCapture = coordinatesFiltering.onlyFreeOfOwnCheckCoordinates(pawnPosition, nonOwnCheckCapture , MoveType.CAPTURE, board);
		//then
		assertEquals(0, nonOwnCheckAttack.size());
		assertEquals(1, nonOwnCheckCapture.size());
		assertTrue(nonOwnCheckCapture.contains(onlyLegalCaptureMove));	
	}
	
	@Test
	public void shouldReturnCoordinatesTakenByOpponent(){
		//given
		CoordinatesFiltering coordinatesFiltering = new CoordinatesFiltering();
		Board board = new Board();
		Coordinate pawnPosition = new Coordinate(1, 5);
		board.setPieceAt(Piece.WHITE_PAWN, pawnPosition);
		board.setPieceAt(Piece.BLACK_KNIGHT, new Coordinate(2, 5)); //1
		board.setPieceAt(Piece.WHITE_KNIGHT, new Coordinate(3, 5));
		board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(1, 4));
		board.setPieceAt(Piece.BLACK_KNIGHT, new Coordinate(2, 4)); //2
		board.setPieceAt(Piece.BLACK_KNIGHT, new Coordinate(3, 4)); //3
		Collection<Coordinate> allCoordinatesToCheck = new ArrayList<>();
		allCoordinatesToCheck.add(new Coordinate(2, 5));
		allCoordinatesToCheck.add(new Coordinate(3, 5));
		allCoordinatesToCheck.add(new Coordinate(1, 4));
		allCoordinatesToCheck.add(new Coordinate(2, 4));
		allCoordinatesToCheck.add(new Coordinate(3, 4));
		allCoordinatesToCheck.add(new Coordinate(0, 5));
		allCoordinatesToCheck.add(new Coordinate(0, 4));
		//when
		Collection<Coordinate> takenCoordinates = coordinatesFiltering.onlyTakenByOpponentCoordinates(pawnPosition, allCoordinatesToCheck, board);
		//then
		assertEquals(3, takenCoordinates.size());
		
		
	}
	
	
		

}
