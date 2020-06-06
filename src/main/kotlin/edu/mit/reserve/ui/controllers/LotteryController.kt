package edu.mit.reserve.ui.controllers

import edu.mit.reserve.lottery.Lottery
import edu.mit.reserve.lottery.models.Category
import edu.mit.reserve.lottery.models.Patient
import edu.mit.reserve.lottery.utils.roundDouble
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import tornadofx.*
import java.time.LocalDate

class LotteryController: Controller() {

	private val lottery: Lottery = Lottery()

	fun clearCategoriesAndGroups() { lottery.clearCategoriesAndGroups() }

	fun setGlobalDemand(globalDemand: Int) { lottery.setGlobalDemand(globalDemand) }
	fun setGlobalSupply(globalSupply: Int) { lottery.setGlobalSupply(globalSupply) }
	fun setNumCategories(numCategories: Int) { lottery.setNumCategories(numCategories) }
	fun setFirstCategory(name: String) {
		lottery.setFirstCategoryByName(name)
	}

	fun getFirstCategory() = lottery.getFirstCategory()

	fun getPopulationGroups() = lottery.getPopulationGroups()
	fun getNumCategories() = lottery.getNumCategories()
	fun getCategories() = lottery.getCategories()

	fun addCategory(name: String, oddsPercentage: Int) { lottery.addCategory(name, oddsPercentage) }
	fun addPopulationGroup(categoryNames: Set<String>, demand: Int) { lottery.addPopulationGroup(categoryNames, demand)}

	fun shouldRunSecondLottery(patient: Patient): Boolean {
		return !patient.firstLotteryResult && (patient.populationGroup.categories.size > 1 || !patient.populationGroup.categories.contains(this.getFirstCategory()))
	}


	fun addPatient(id: String, name: String, categories: Set<Category>, date: LocalDate) {

		val newPatient = Patient(id, name, edu.mit.reserve.lottery.models.PopulationGroup(categories), date)
		newPatient.firstLotteryResult = lottery.firstLottery(newPatient)
		newPatient.secondLotteryResult = this.shouldRunSecondLottery(newPatient) && lottery.secondLottery(newPatient)
		val curPatients = patients.toMutableList()
		curPatients.add(newPatient)
		patients.value = curPatients.toObservable()
	}

	fun finishConfiguringProbabilities() {
		lottery.calculatePg()
		lottery.calculateCutoffs()
		firstLotteryCutoffRows.clear()
		firstLotteryCutoffRows.add(FirstLotteryCutoffRow(lottery.getFirstCategory().toString(), roundDouble(lottery.getFirstLotteryCategoryCutoff()).toString()))
		firstLotteryCutoffRows.add(FirstLotteryCutoffRow("non-${lottery.getFirstCategory()}", roundDouble(lottery.getNonFirstLotteryCategoryCutoff()).toString()))
	}

	val firstLotteryCutoffRows = mutableListOf<FirstLotteryCutoffRow>().asObservable()

	val patients = SimpleListProperty<Patient>()

	fun clearPatients() {
		patients.value = FXCollections.observableArrayList()
	}

	val globalSupply = SimpleIntegerProperty(lottery.getGlobalSupply())
	val coursesUsed = SimpleIntegerProperty(0)
}

class FirstLotteryCutoffRow(val name: String, val cutOff: String)
