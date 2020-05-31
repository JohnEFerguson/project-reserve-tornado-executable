package edu.mit.reserve.ui.controllers

import edu.mit.reserve.lottery.Lottery
import edu.mit.reserve.lottery.models.Category
import edu.mit.reserve.lottery.models.PopulationGroup
import tornadofx.*
import java.time.LocalDate

class LotteryController: Controller() {

	private val lottery: Lottery = Lottery()

	fun setGlobalDemand(globalDemand: Int) { lottery.setGlobalDemand(globalDemand) }
	fun setGlobalSupply(globalSupply: Int) { lottery.setGlobalSupply(globalSupply) }
	fun setNumCategories(numCategories: Int) { lottery.setNumCategories(numCategories) }
	fun setFirstCategory(name: String) { lottery.setFirstCategoryByName(name) }

	fun createPopulationGroups() { lottery.createPopulationGroups() }

	fun getPopulationGroups() = lottery.getPopulationGroups()
	fun getNumCategories() = lottery.getNumCategories()
	fun getCategories() = lottery.getCategories()

	fun addCategory(name: String, oddsPercentage: Int) { lottery.addCategory(name, oddsPercentage) }
	fun addPopulationGroupDemand(populationGroup: PopulationGroup, demand: Int) { lottery.addPopulationGroupDemand(populationGroup, demand)}
	fun addPatient(id: String, name: String, categories: Set<Category>, date: LocalDate) { lottery.addPatient(id, name, categories, date) }

	fun logValue(value: Any?) { println(value.toString()) }
}