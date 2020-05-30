package edu.mit.reserve

import edu.mit.reserve.models.Category
import edu.mit.reserve.models.PopulationGroup
import org.junit.Assert
import org.junit.Before
import org.junit.Test

private const val DOUBLE_DELTA = 1e-15

class LotteryTest {

	private lateinit var lottery: Lottery

	@Before
	fun setup() {

		val category1 = Category(name = "EOL", odds = -.5)
		val category2 = Category(name = "EW", odds = .25)
		val category3 = Category(name = "HA", odds = .25)

		val populationGroup1 = PopulationGroup(setOf(category2))
		val populationGroup2 = PopulationGroup(setOf(category3))
		val populationGroup3 = PopulationGroup(setOf(category2, category3))
		val populationGroup4 = PopulationGroup(setOf(category1))
		val populationGroup5 = PopulationGroup(setOf(category1, category2))
		val populationGroup6 = PopulationGroup(setOf(category1, category3))
		val populationGroup7 = PopulationGroup(setOf(category1, category2, category3))

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

		Assert.assertEquals(0.217391304347826, lottery.getPg(), DOUBLE_DELTA)
		Assert.assertEquals(108.69565217391305, lottery.getfirstLotteryCategoryCutoff(), DOUBLE_DELTA)
		Assert.assertEquals(217.3913043478261, lottery.getNonFirstLotteryCategoryCutoff(), DOUBLE_DELTA)
	}
}