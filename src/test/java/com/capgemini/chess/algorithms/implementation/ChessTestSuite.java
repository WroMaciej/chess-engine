package com.capgemini.chess.algorithms.implementation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.capgemini.chess.algorithms.implementation.piece_validation_strategy.pieces.BishopRookValidationServiceTest;
import com.capgemini.chess.algorithms.implementation.piece_validation_strategy.pieces.KingValidationServiceTest;
import com.capgemini.chess.algorithms.implementation.piece_validation_strategy.pieces.PawnValidationServiceTest;
import com.capgemini.chess.algorithms.implementation.universal_rules.filtering.CoordinatesFilteringHelperTest;
import com.capgemini.chess.algorithms.implementation.universal_rules.filtering.CoordinatesFilteringTest;
import com.capgemini.chess.algorithms.implementation.universal_rules.filtering.DistanceCalculatorTest;
import com.capgemini.chess.algorithms.implementation.universal_rules.filtering.move_faking.MoveFakerServiceTest;

/**
 * Test suite containing all tests
 *
 * @author Michal Bejm
 * @author MACIEWRO Maciej Wróblewski
 *
 */
@RunWith(Suite.class)
@SuiteClasses({
	BoardManagerTest.class,
	BishopRookValidationServiceTest.class,
	KingValidationServiceTest.class,
	PawnValidationServiceTest.class,
	MoveFakerServiceTest.class,
	CoordinatesFilteringHelperTest.class,
	CoordinatesFilteringTest.class,
	DistanceCalculatorTest.class

})
public class ChessTestSuite {

}
