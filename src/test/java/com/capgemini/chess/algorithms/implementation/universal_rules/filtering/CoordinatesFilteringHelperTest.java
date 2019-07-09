package com.capgemini.chess.algorithms.implementation.universal_rules.filtering;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.universal_rules.filtering.CoordinatesFilteringHelper;

public class CoordinatesFilteringHelperTest {
	
	@Test
	public void shouldReturnWhenBishopInCornerAndQuenn(){
		//given
		Board board = new Board();
		Coordinate bishopCoordinate = new Coordinate(0, 0);
		Coordinate queenCoordinate = new Coordinate(3, 3);
		Coordinate rookBehindQueen = new Coordinate(4, 4);
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
		
		CoordinatesFilteringHelper coordinatesFilteringHelper = new CoordinatesFilteringHelper();
		//when
		int distanceToNearestPiece = coordinatesFilteringHelper.nearestPieceDistance(bishopCoordinate, coordinatesPathStream, board);
		Piece nearestPiece = coordinatesFilteringHelper.nearestPieceOnPath(bishopCoordinate, coordinatesPathStream, board);
		//then
		assertEquals(3, distanceToNearestPiece);
		assertEquals(Piece.WHITE_QUEEN, nearestPiece);
	}

}
