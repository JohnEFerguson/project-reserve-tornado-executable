package edu.mit.reserve

import edu.mit.reserve.models.Category
import edu.mit.reserve.models.Patient
import edu.mit.reserve.models.PopulationGroup
import kotlin.random.Random

class Lottery(
	private val globalSupply: Int,
	private val globalDemand: Int,
	private val populationGroupDemands: HashMap<PopulationGroup, Int> = HashMap(),
	private val firstLotteryCategory: Category
) {

	private var pg = 0.0
	private val firstBiteLotteryScale = 1000
	private val secondBiteLotteryScale = 1000
	private var firstLotteryCategoryCutoff = 0.0
	private var nonFirstLotteryCategoryCutoff = 0.0
	private val secondLotteryCutoffs = HashMap<PopulationGroup, Double>()

	fun addPopulationGroupDemand(populationGroup: PopulationGroup, demand: Int) {
		populationGroupDemands[populationGroup] = demand
	}

	fun calculatePg() {

		var quotient = 0.0

		var sumPopulationGroupDemand = 0

		for ((populationGroup, demand) in populationGroupDemands) {

			var quotientTerm = 1.0
			for (category in populationGroup.categories) {
				quotientTerm += category.odds
			}

			sumPopulationGroupDemand += demand

			quotient += quotientTerm * demand
		}

		quotient += (globalDemand - sumPopulationGroupDemand)

		pg = globalSupply / quotient
	}

	fun calculateCutoffs() {

		firstLotteryCategoryCutoff = (1.0 + firstLotteryCategory.odds) * pg * firstBiteLotteryScale
		nonFirstLotteryCategoryCutoff = pg * firstBiteLotteryScale

		for (populationGroup in populationGroupDemands.keys) {

			var firstLotteryCutoff = if (populationGroup.categories.contains(firstLotteryCategory)) firstLotteryCategoryCutoff else nonFirstLotteryCategoryCutoff

			var cutoff = firstLotteryCutoff

			cutoff /= firstBiteLotteryScale

			var categoryOddsSum = 1.0

			populationGroup.categories.forEach { categoryOddsSum += it.odds }

			cutoff = ((categoryOddsSum * pg) - cutoff) / (1.0 - firstLotteryCutoff / firstBiteLotteryScale) * secondBiteLotteryScale

			secondLotteryCutoffs[populationGroup] = cutoff
		}


	}

	fun processPatient(patient: Patient): Boolean {

		val firstLotteryNumber = Random.nextDouble(until = firstBiteLotteryScale.toDouble())

		var firstCutoff = if (patient.populationGroup.categories.contains(firstLotteryCategory)) firstLotteryCategoryCutoff else nonFirstLotteryCategoryCutoff

		if (firstLotteryNumber < firstCutoff) return true

		val secondLotteryNumber = Random.nextDouble(until = secondBiteLotteryScale.toDouble())

		return secondLotteryNumber < secondLotteryCutoffs[patient.populationGroup]!! // should throw exception here
	}


	fun getFirstLotteryCategoryCutoff(): Double = this.firstLotteryCategoryCutoff
	fun getNonFirstLotteryCategoryCutoff(): Double = this.nonFirstLotteryCategoryCutoff
	fun getPg(): Double = this.pg
	fun getPopulationGroupCutoff(group: PopulationGroup): Double = this.secondLotteryCutoffs[group]!! // Todo: should throw exception here
}

