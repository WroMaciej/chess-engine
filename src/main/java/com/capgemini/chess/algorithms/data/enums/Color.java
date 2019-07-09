package com.capgemini.chess.algorithms.data.enums;

/**
 * Chess piece color
 * 
 * @author Michal Bejm
 *
 */
public enum Color {
	WHITE (1) {
		@Override
		public Color getOppositeColor() {
			return Color.BLACK;
		}
	}, 
	BLACK (-1) {
		@Override
		public Color getOppositeColor() {
			return WHITE;
		}
	};
	
	/**
	 * Represents vector of pawn move (+1 for white and -1 for black)
	 */
	public final int moveVector;
	//getter
	
	
	

	private Color(int moveVector) {
		this.moveVector = moveVector;
	}

	/**
	 * Gets opposite color
	 * @return
	 */
	public abstract Color getOppositeColor();
}
