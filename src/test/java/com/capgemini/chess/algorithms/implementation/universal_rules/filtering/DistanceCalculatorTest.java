package com.capgemini.chess.algorithms.implementation.universal_rules.filtering;

import static org.junit.Assert.*;

import org.junit.Test;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.implementation.universal_rules.filtering.DistanceCalculator;

public class DistanceCalculatorTest {
	
	@Test
	public void shouldReturnDistance7FromSideToSide(){
		//given
		DistanceCalculator distanceCalculator = new DistanceCalculator();		
		//when
		int distance =  distanceCalculator.distance(new Coordinate(3, 0), new Coordinate(3, 7));
		//then
		assertEquals(7, distance);
	}
	
	@Test
	public void shouldReturnX0Y1VectorFromSideToSide(){
		//given
		DistanceCalculator distanceCalculator = new DistanceCalculator();		
		//when
		Coordinate vector = distanceCalculator.coordinatesVector(new Coordinate(3, 0), new Coordinate(3, 7));
		//then
		assertEquals(0, vector.getX());
		assertEquals(1, vector.getY());
	}
	
	@Test
	public void shouldReturnDistance7FromCornerToCorner(){
		//given
		DistanceCalculator distanceCalculator = new DistanceCalculator();		
		//when
		int distance =  distanceCalculator.distance(new Coordinate(0, 0), new Coordinate(7, 7));
		//then
		assertEquals(7, distance);
	}
	
	@Test
	public void shouldReturnDistance0ForNoMove(){
		//given
		DistanceCalculator distanceCalculator = new DistanceCalculator();		
		//when
		int distance =  distanceCalculator.distance(new Coordinate(1, 1), new Coordinate(1, 1));
		//then
		assertEquals(0, distance);
	}
	
	@Test
	public void shouldReturnDistance2ForKnightMove(){
		//given
		DistanceCalculator distanceCalculator = new DistanceCalculator();		
		//when
		int distance =  distanceCalculator.distance(new Coordinate(5, 2), new Coordinate(4, 0));
		//then
		assertEquals(2, distance);
	}

}
