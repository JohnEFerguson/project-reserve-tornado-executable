package edu.mit.reserve.ui.controllers

import edu.mit.reserve.lottery.Lottery
import edu.mit.reserve.lottery.models.Category
import edu.mit.reserve.lottery.models.Patient
import edu.mit.reserve.lottery.models.PopulationGroup
import edu.mit.reserve.lottery.utils.export
import edu.mit.reserve.lottery.utils.roundDouble
import edu.mit.reserve.ui.views.GlobalLotteryView
import edu.mit.reserve.ui.views.PatientInputFieldsModel
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import tornadofx.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar.getInstance

class LotteryController: Controller() {

	private val patientValuesModel: PatientInputFieldsModel by inject()
	private val model: GlobalLotteryView by inject()

	private var newPatientId = 0

	private val lottery: Lottery = Lottery()

	fun clearCategoriesAndGroups() { lottery.clearCategoriesAndGroups() }

	fun setGlobalDemand(globalDemand: Int) { lottery.setGlobalDemand(globalDemand) }
	fun setGlobalSupply(globalSupply: Int) { lottery.setGlobalSupply(globalSupply) }
	fun setNumCategories(numCategories: Int) { lottery.setNumCategories(numCategories) }
	fun setFirstCategory(name: String) {
		lottery.setFirstCategoryByName(name)
	}

	fun getPopulationGroups() = lottery.getPopulationGroups()
	fun getCategories() = lottery.getCategories()

	fun addCategory(name: String, oddsPercentage: Int) { lottery.addCategory(name, oddsPercentage) }
	fun addPopulationGroup(categoryNames: Set<String>, demand: Int) { lottery.addPopulationGroup(categoryNames, demand)}

	fun addPatient(id: String, name: String, categories: Set<Category>, date: LocalDate) {

		var populationGroup = PopulationGroup(categories, 0)
		lottery.getPopulationGroups().forEach {
			if (categories.toString() == it.categories.toString()) populationGroup = it
		}

		val newPatient = Patient(newPatientId, id, name, populationGroup, date)
		newPatient.firstLotteryResult = lottery.firstLottery(newPatient)
		newPatient.secondLotteryResult = newPatient.populationGroup.categories.isNotEmpty() && newPatient.populationGroup.involvedInSecondLottery && lottery.secondLottery(newPatient)
		val curPatients = patients.toMutableList()
		curPatients.add(newPatient)
		patients.value = curPatients.toObservable()
		newPatientId += 1
	}

	fun deletePatient(primaryIdToDelete: Int) {
		patients.value = patients.toMutableList().filter { it.primaryId != primaryIdToDelete }.toObservable()
	}

	fun finishConfiguringProbabilities() {
		lottery.calculatePg()
		lottery.calculateCutoffs()
		firstLotteryCutoffRows.clear()
		firstLotteryCutoffRows.add(FirstLotteryCutoffRow(lottery.getFirstCategory().toString(), roundDouble(lottery.getFirstLotteryCategoryCutoff()).toString()))
		firstLotteryCutoffRows.add(FirstLotteryCutoffRow("non-${lottery.getFirstCategory()}", roundDouble(lottery.getNonFirstLotteryCategoryCutoff()).toString()))
		globalConfigRow.add(GlobalConfigRow(lottery.getGlobalSupply().toString(), lottery.getGlobalDemand().toString(), getCategories().size.toString(), lottery.getFirstCategory()))
	}


	val firstLotteryCutoffRows = mutableListOf<FirstLotteryCutoffRow>().asObservable()
	val globalConfigRow = mutableListOf<GlobalConfigRow>().asObservable()

	val patients = SimpleListProperty<Patient>()

	fun clearPatients() {
		patients.value = FXCollections.observableArrayList()
		globalSupply.value += coursesUsed.value
		coursesUsed.value = 0
		model.numCoursesAvailable.value = "${globalSupply.value}"
		model.numPatients.value = "${coursesUsed.value}"
	}


	val globalSupply = SimpleIntegerProperty(lottery.getGlobalSupply())
	val coursesUsed = SimpleIntegerProperty(0)

	fun clearPatientInputFields() {

		patientValuesModel.patientId.value = ""
		patientValuesModel.patientName.value = ""
		patientValuesModel.date.value = LocalDate.now()
		patientValuesModel.categories.value.clear()
	}

	private fun getCSVHeader(): List<List<String>> {

		val header = mutableListOf(listOf("Project Reserve Export"))
		header.add(listOf(""))
		header.add(listOf("Global Config Values:"))
		header.add(listOf("Expected Number of Patients", "Initial Number of Courses Available",
			"Number of Categories", "First Lottery Category"))
		header.add(listOf(lottery.getGlobalDemand().toString(),
			lottery.getGlobalSupply().toString(),
			lottery.getCategories().size.toString(),
			lottery.getFirstCategory().toString()))

		header.add(listOf(""))
		header.add(listOf("Reserve Category Configuration:"))
		header.add(listOf("Name", "Adjusted Odds"))
		lottery.getCategories().forEach {
			header.add(listOf(it.name, "${it.odds * 100}%"))
		}

		header.add(listOf(""))
		header.add(listOf("Population Group Configuration:"))
		header.add(listOf("Population Group", "Expected Demand"))
		lottery.getPopulationGroups().forEach {
			header.add(listOf(it.toString(), it.demand.toString()))
		}

		header.add(listOf(""))
		header.add(listOf("First Lottery Cutoffs:"))
		header.add(listOf("Category", "Cutoff"))
		firstLotteryCutoffRows.forEach {
			header.add(listOf(it.name, it.cutOff))
		}
		header.add(listOf(""))
		header.add(listOf("Second Lottery Cutoffs:"))
		header.add(listOf("Population Group", "Cutoff"))
		getPopulationGroups().filter { it.involvedInSecondLottery }.forEach {
			header.add(listOf(it.toString(), it.secondCutoff))
		}
		header.add(listOf(""))
		header.add(listOf(""))
		return header
	}

	fun exportToCSV() {

		export(
			"${SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(getInstance().time)}_project_reserve_patient_export.csv",
			patients.toList(), getCSVHeader(), getCategories())
	}
}

class FirstLotteryCutoffRow(val name: String, val cutOff: String)
class GlobalConfigRow(val supply: String, val demand: String, val numCategories: String, val firstCategory: Category)
class PopulationGroupConfigRow(val name: String, val demand: String)