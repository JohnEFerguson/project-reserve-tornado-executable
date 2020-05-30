package edu.mit.reserve

import edu.mit.reserve.models.Category
import edu.mit.reserve.models.PopulationGroup
import org.junit.Before
import org.junit.Test

class LotteryTest {

	private lateinit var lottery: Lottery

	@Before
	fun setup() {

		val category1 = Category(name = "EOL", odds = -.5F)
		val category2 = Category(name = "EW", odds = .25F)
		val category3 = Category(name = "HA", odds = .25F)

		val populationGroup1 = PopulationGroup(setOf(category2))
		val populationGroup2 = PopulationGroup(setOf(category3))
		val populationGroup3 = PopulationGroup(setOf(category2, category3))
		val populationGroup4 = PopulationGroup(setOf(category1))
		val populationGroup5 = PopulationGroup(setOf(category1, category2))
		val populationGroup6 = PopulationGroup(setOf(category1, category3))
		val populationGroup7 = PopulationGroup(setOf(category1, category2, category3))

		lottery = Lottery(globalSupply = 60, globalDemand = 15, firstLotteryCategory = category1)

		lottery.addPopulationGroupDemand(populationGroup1, 12)
		lottery.addPopulationGroupDemand(populationGroup2, 24)
		lottery.addPopulationGroupDemand(populationGroup3, 0)
		lottery.addPopulationGroupDemand(populationGroup4, 0)
		lottery.addPopulationGroupDemand(populationGroup5, 0)
		lottery.addPopulationGroupDemand(populationGroup6, 0)
		lottery.addPopulationGroupDemand(populationGroup7, 0)
	}

	@Test
	fun runLottery() {

	}
}