package edu.mit.reserve.lottery

import edu.mit.reserve.lottery.models.Category
import edu.mit.reserve.lottery.models.Patient
import edu.mit.reserve.lottery.models.PopulationGroup
import edu.mit.reserve.lottery.utils.generatePopulationGroups
import java.lang.IllegalArgumentException
import java.time.LocalDate
import kotlin.random.Random

class Lottery(
	private var globalSupply: Int = 0,
	private var globalDemand: Int = 0,
	private val populationGroupDemands: HashMap<PopulationGroup, Int> = HashMap(),
	private var firstLotteryCategory: Category = Category("foo", 0.0)
) {

	private val patients = mutableListOf<Patient>()
	private val categories = mutableListOf<Category>()
	private val nameCategoryMapping = HashMap<String, Category>()

	private var populationGroups = mutableListOf<PopulationGroup>()

	private var numCategories = 0

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

			val firstLotteryCutoff = if (populationGroup.categories.contains(firstLotteryCategory)) firstLotteryCategoryCutoff else nonFirstLotteryCategoryCutoff

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

		var firstCutoff = if (patient.getPopulationGroup().categories.contains(firstLotteryCategory)) firstLotteryCategoryCutoff else nonFirstLotteryCategoryCutoff

		if (firstLotteryNumber < firstCutoff) return true

		val secondLotteryNumber = Random.nextDouble(until = secondBiteLotteryScale.toDouble())

		return secondLotteryNumber < secondLotteryCutoffs[patient.getPopulationGroup()]!!
	}

	fun addCategory(name: String, oddsPercentage: Int) {
		val newCategory = Category(name = name, odds = oddsPercentage.toDouble() / 100.0)
		categories.add(newCategory)
		nameCategoryMapping.put(name, newCategory)

	}

	fun addPatient(id: String, name: String, categories: Set<Category>, date: LocalDate) {
		patients.add(Patient(id, name, PopulationGroup(categories), date))

	}

	fun createPopulationGroups() {
		this.populationGroups = generatePopulationGroups(this.categories)
	}

	fun setGlobalSupply(globalSupply: Int) { this.globalSupply = globalSupply}
	fun setGlobalDemand(globalDemand: Int) { this.globalDemand = globalDemand}
	fun setNumCategories(numCategories:Int) { this.numCategories = numCategories }
	fun setFirstCategoryByName(name: String) {
		this.firstLotteryCategory = nameCategoryMapping[name] ?: throw IllegalArgumentException("There must be a category with name $name")
	}



	fun getNumCategories() = this.numCategories
	fun getFirstLotteryCategoryCutoff(): Double = this.firstLotteryCategoryCutoff
	fun getNonFirstLotteryCategoryCutoff(): Double = this.nonFirstLotteryCategoryCutoff
	fun getPg(): Double = this.pg
	fun getPopulationGroupCutoff(group: PopulationGroup): Double = this.secondLotteryCutoffs[group]!! // Todo: should throw exception here
	fun getCategories() = this.categories
	fun getPopulationGroups() = this.populationGroups


}


