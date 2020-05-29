package edu.mit.reserve

import edu.mit.reserve.models.Category
import edu.mit.reserve.models.Patient
import edu.mit.reserve.models.PopulationGroup

class Lottery(
	private val globalSupply: Int,
	private val globalDemand: Int,
	private val populationGroupDemands: HashMap<PopulationGroup, Int> = HashMap(),
	private val firstLotteryCategory: Category
) {

	private var pg = 0.0F
	private val firstBiteLotteryScale = 1000
	private val secondBiteLotteryScale = 1000
	private var firstLotteryCategoryCutoff = 0.0F
	private var nonFirstLotteryCategoryCutoff = 0.0F
	private val secondLotteryCutoffs = HashMap<PopulationGroup, Float>()

	fun addPopulationGroupDemand(populationGroup: PopulationGroup, demand: Int) {
		populationGroupDemands[populationGroup] = demand
	}

	fun calculatePg() {

		var quotient = 1.0F

		for ((populationGroup, demand) in populationGroupDemands) {

			var quotientTerm = 1.0F
			for (category in populationGroup.categories) {
				quotientTerm += category.odds
			}

			quotient += quotientTerm * demand
		}

		pg = globalSupply / quotient
	}

	fun calculateCutoffs() {

		firstLotteryCategoryCutoff = 1.0F + firstLotteryCategory.odds * pg * firstBiteLotteryScale
		nonFirstLotteryCategoryCutoff = pg * firstBiteLotteryScale

		for (populationGroup in populationGroupDemands.keys) {

			var firstLotteryCutoff = if (populationGroup.categories.contains(firstLotteryCategory)) firstLotteryCategoryCutoff else nonFirstLotteryCategoryCutoff

			var cutoff = firstLotteryCutoff

			cutoff /= firstBiteLotteryScale

			var categoryOddsSum = 1.0F

			populationGroup.categories.forEach { categoryOddsSum += it.odds }

			cutoff = ((categoryOddsSum * pg) - cutoff) / (1.0F - firstLotteryCutoff / firstBiteLotteryScale) * secondBiteLotteryScale

			secondLotteryCutoffs[populationGroup] = cutoff
		}


	}

	fun processPatient(patient: Patient) {
	}

}

