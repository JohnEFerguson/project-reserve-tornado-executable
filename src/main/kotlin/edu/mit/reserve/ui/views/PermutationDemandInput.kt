package edu.mit.reserve.ui.views

import edu.mit.reserve.lottery.models.PopulationGroup
import edu.mit.reserve.ui.controllers.LotteryController
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import tornadofx.*

class PermutationDemandInput : View() {

	private val controller: LotteryController by inject()
	private val populationGroupToDemand = HashMap<PopulationGroup, SimpleIntegerProperty>()

	override val root = form()

	init {
		title = "Estimate demand"

		with(root) {

			val selectedFirstLottery = SimpleStringProperty()

			fieldset {
				text = "Lottery 1 Category"
				field("Select first lottery category") {
					val categoryNames = FXCollections.observableArrayList(controller.getCategories().map { it.name })
					combobox(selectedFirstLottery, categoryNames)
				}
			}

			fieldset {
				text = "Define Population Group Demands"

				for (group in controller.getPopulationGroups()) {

					field("$group:") {
						val expectedGroupDemand = SimpleIntegerProperty()
						textfield(expectedGroupDemand)
						populationGroupToDemand[group] = expectedGroupDemand
					}
				}
			}

			button("Submit") {
				action {
					println("User pressed Submit.")
					controller.setFirstCategory(selectedFirstLottery.value)
					controller.logValue("Selected First Lottery: ${selectedFirstLottery.value}")
					for (group in controller.getPopulationGroups()) {
						controller.addPopulationGroupDemand(group, populationGroupToDemand[group]!!.value)
					}

					replaceWith(find<PatientInput>())
				}
			}
		}
	}
}