package edu.mit.reserve.ui.controllers

import edu.mit.reserve.ui.views.Category
import edu.mit.reserve.ui.views.GlobalLotteryView
import edu.mit.reserve.ui.views.GlobalValueConfigValueModel
import edu.mit.reserve.ui.views.PopulationGroup
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleSetProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableSet
import tornadofx.*
import kotlin.math.min
import kotlin.math.pow

class ConfigurationController : Controller() {

	val lotteryController: LotteryController by inject()
	val model: GlobalValueConfigValueModel by inject()
	val inputModel: GlobalLotteryView by inject()


	var categories = SimpleListProperty<Category>(observableListOf())
	var populationGroups = SimpleListProperty<PopulationGroup>(observableListOf())


	init {

		model.numCategories.onChange {

			val limit = min(it!!.toInt(), categories.value.size)
			val categoryList = categories.value.subList(0, limit)

			for (i in categoryList.size until it.toInt())
				categoryList.add(Category("", "0", false))

			categories.value = observableListOf(categoryList)
		}


	}

	fun updateIsFirstCategories(name: String) {

		categories.forEach {
			if (it.name != name) it.isFirst = false
		}
	}

	fun updatePopulationGroups() {

		populationGroups.value = observableListOf(generatePopulationGroups(categories.value))
	}

	fun submit(numPatients: Int, numCoursesAvailable: Int, numCategories: Int) {

		lotteryController.setGlobalDemand(numPatients)
		lotteryController.setGlobalSupply(numCoursesAvailable)
		lotteryController.setNumCategories(numCategories)

		lotteryController.clearCategoriesAndGroups()

		categories.value.forEach { lotteryController.addCategory(it.name, it.odds.toInt()) }

		lotteryController.setFirstCategory(categories.value.filter { it.isFirst }.get(0).name)

		populationGroups.forEach { lotteryController.addPopulationGroup(it.categoryNames, it.expectedDemand.toInt()) }

		lotteryController.finishConfiguringProbabilities()

		lotteryController.globalSupply.value = numCoursesAvailable

		inputModel.numCoursesAvailable.value = "Number of courses available: ${lotteryController.globalSupply.value}"
		inputModel.numPatients.value = "Number of patients who have accepted: 0"
	}
}

fun generatePopulationGroups(categories: List<Category>): List<PopulationGroup> {

	val populationGroups = mutableListOf<PopulationGroup>()

	val numberOfPermutations = 2.0.pow(categories.size.toDouble()) - 1

	for (i in 1..numberOfPermutations.toInt()) {
		val categorySet = mutableSetOf<String>()
		var placeholder = i
		for (element in categories) {

			if ((placeholder and 1) == 1) {
				categorySet.add(element.name)
			}
			placeholder = placeholder.shr(1)
		}

		println(categorySet)
		populationGroups.add(PopulationGroup(categorySet.asObservable(), "0"))
	}
	return populationGroups
}

