package edu.mit.reserve.lottery

import edu.mit.reserve.lottery.models.Category
import edu.mit.reserve.lottery.models.PopulationGroup
import org.junit.Assert
import org.junit.Before
import org.junit.Test

private const val DOUBLE_DELTA = 1e-15

class LotteryTest {

	private lateinit var lottery: Lottery
	private lateinit var populationGroup1: PopulationGroup
	private lateinit var populationGroup2: PopulationGroup
	private lateinit var populationGroup3: PopulationGroup
	private lateinit var populationGroup4: PopulationGroup
	private lateinit var populationGroup5: PopulationGroup
	private lateinit var populationGroup6: PopulationGroup
	private lateinit var populationGroup7: PopulationGroup

	@Before
	fun setup() {

		val category1 = Category(name = "EOL", odds = -.5)
		val category2 = Category(name = "EW", odds = .25)
		val category3 = Category(name = "HA", odds = .25)

		// Todo: Ask Parag about 8th case (non EOL, non EW, non HA)
		// EW only
		populationGroup1 = PopulationGroup(setOf(category2))
		// HA only
		populationGroup2 = PopulationGroup(setOf(category3))
		// EW | HA only
		populationGroup3 = PopulationGroup(setOf(category2, category3))
		// EOL only
		populationGroup4 = PopulationGroup(setOf(category1))
		// EOL | EW only
		populationGroup5 = PopulationGroup(setOf(category1, category2))
		// EOL | HA only
		populationGroup6 = PopulationGroup(setOf(category1, category3))
		// EOL | EW | HA only
		populationGroup7 = PopulationGroup(setOf(category1, category2, category3))

		lottery = Lottery(globalSupply = 15, globalDemand = 60, firstLotteryCategory = category1)

		lottery.addPopulationGroupDemand(populationGroup1, 12)
		lottery.addPopulationGroupDemand(populationGroup2, 24)
		lottery.addPopulationGroupDemand(populationGroup3, 0)
		lottery.addPopulationGroupDemand(populationGroup4, 0)
		lottery.addPopulationGroupDemand(populationGroup5, 0)
		lottery.addPopulationGroupDemand(populationGroup6, 0)
		lottery.addPopulationGroupDemand(populationGroup7, 0)

		lottery.calculatePg()

		lottery.calculateCutoffs()
	}

	@Test
	fun `Check that quotient and cutoffs were calculated correctly`() {
		// Test general community probability of win
		Assert.assertEquals(0.217391304347826, lottery.getPg(), DOUBLE_DELTA)
		// Test EOL win - first lottery
		Assert.assertEquals(108.69565217391305, lottery.getFirstLotteryCategoryCutoff(), DOUBLE_DELTA)
		// Test non-EOL win - first lottery
		Assert.assertEquals(217.3913043478261, lottery.getNonFirstLotteryCategoryCutoff(), DOUBLE_DELTA)
		// Test EOL and EW win - second lottery
		Assert.assertEquals( 60.97560975609757, lottery.getPopulationGroupCutoff(populationGroup5), DOUBLE_DELTA)
		// Test EW only win - second lottery
		Assert.assertEquals( 69.44444444444443, lottery.getPopulationGroupCutoff(populationGroup1), DOUBLE_DELTA)
		// Test EOL and HA win - second lottery
		Assert.assertEquals( 60.97560975609757, lottery.getPopulationGroupCutoff(populationGroup6), DOUBLE_DELTA)
		// Test HA only win - second lottery
		Assert.assertEquals( 69.44444444444443, lottery.getPopulationGroupCutoff(populationGroup2), DOUBLE_DELTA)
		// Test EOL, EW, HA win - second lottery
		Assert.assertEquals( 121.95121951219512, lottery.getPopulationGroupCutoff(populationGroup7), DOUBLE_DELTA)
		// Test EW and HA only win - second lottery
		Assert.assertEquals( 138.88888888888889, lottery.getPopulationGroupCutoff(populationGroup3), DOUBLE_DELTA)
	}
}