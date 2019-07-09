package com.capgemini.chess.algorithms.implementation.universal_rules.filtering.move_faking;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.BoardManager;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;

/**
 * Class created for faking moves
 * Used for validation if move would cause a check on own king
 * It is available fake only one move in one instance!
 * @author MACIEWRO
 *
 */
public final class BoardManagerAdapter extends BoardManager {
	
	private Move moveToFake;

	BoardManagerAdapter(Board copyOfBoardToFake, Move moveToFake){
		super(copyOfBoardToFake);
		this.moveToFake = moveToFake;
	}
	
	boolean isFakeMoveCauseOwnCheck(){
		//perform a fake move
		try {
			this.performMove(moveToFake.getFrom(), moveToFake.getTo());
		} catch (InvalidMoveException e) {
			System.out.println("Fake move has thrown an exception! It should never happen!");
			//This should never happen - its just a fake move and its firstly filtered
			//think about logging it (it should never happen but not sure it will stay impossible during project developing
		}
		//give information if that state caused own king check
		return this.isKingInCheck(moveToFake.getMovedPiece().getColor());
	}
	
	@Override
	protected Move validateMove(Coordinate from, Coordinate to){
		//assume that move is valid
		return moveToFake;
	}


	

}
