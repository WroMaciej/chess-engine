package com.capgemini.chess.algorithms.implementation.universal_rules.filtering.move_faking;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;

/**
 * Faking moves on imaginary board
 * Used for testing if move would lead to checking own king
 * @author MACIEWRO
 *
 */
public class MoveFakerService {
	
	Piece pieceDeepCopy(Piece pieceToCopy){
		return pieceToCopy;
	}
	
	Coordinate coordinateDeepCopy(Coordinate coordinateToCopy){
		return new Coordinate(coordinateToCopy.getX(), coordinateToCopy.getY());
	}
	
	Piece[][] piecesSetupDeepCopy(Board boardToFake){
		Piece [][] piecesDeepCopy = new Piece[Board.SIZE][Board.SIZE];
		for (int x = 0; x < Board.SIZE; x++){
			for (int y = 0; y < Board.SIZE; y++){
				piecesDeepCopy[x][y] = pieceDeepCopy(boardToFake.getPieceAt(new Coordinate(x, y)));
			}
		}
		return piecesDeepCopy;
	}
	
	Move moveDeepCopy(Move moveToCopy){
		Coordinate fromCopy = coordinateDeepCopy(moveToCopy.getFrom());
		Coordinate toCopy = coordinateDeepCopy(moveToCopy.getTo());
		Piece pieceCopy = pieceDeepCopy(moveToCopy.getMovedPiece());
		MoveType moveTypeCopy = moveToCopy.getType();
		return new Move(fromCopy, toCopy, moveTypeCopy, pieceCopy);
	}
	
	List<Move> moveHistoryDeepCopy(Board boardToFake){
		List<Move> listDeepCopy = new ArrayList<>();
		for (Move moveToCopy: boardToFake.getMoveHistory()){
			listDeepCopy.add(moveDeepCopy(moveToCopy));
		}
		return listDeepCopy;
	}
	
	public boolean isFakeMoveCauseOwnCheck(Move moveToFake, Board boardToFake){
		if (boardToFake.isFakeBoard()) return false; //dont fake a fake board -> it could lead to recurentional stack overflow
		Board copyOfBoard = new Board(piecesSetupDeepCopy(boardToFake), moveHistoryDeepCopy(boardToFake));
		copyOfBoard.setFakeBoard(true); //its a fake board so do not check own king in check rules inside it!
		BoardManagerAdapter fakeBoardManager = new BoardManagerAdapter(copyOfBoard, moveToFake);
		//perform a fake move
		return fakeBoardManager.isFakeMoveCauseOwnCheck();	
	}

}
