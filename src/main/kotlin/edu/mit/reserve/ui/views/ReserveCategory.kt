package edu.mit.reserve.ui.views

import edu.mit.reserve.ui.controllers.LotteryController
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class ReserveCategory : View() {

	private val controller: LotteryController by inject()
	private val mapReserveCategoryIndexToName = HashMap<Int, SimpleStringProperty>()
	private val mapReserveCategoryIndexToOdds = HashMap<Int, SimpleIntegerProperty>()

	override val root = form()
	init {
		title = "Reserve category definition"

		with(root) {
			setPrefSize( 900.0, 900.0 )
			for (reserveCategoryNumber in 1..controller.getNumCategories()) {

				fieldset {
					text = "Category $reserveCategoryNumber"
					field( "Category Name:") {
						val categoryName = SimpleStringProperty()
						textfield(categoryName)
						mapReserveCategoryIndexToName[reserveCategoryNumber] = categoryName
					}
					field( "Adjusted Category Odds:") {
						val adjustedCategoryOdds = SimpleIntegerProperty()
						textfield(adjustedCategoryOdds)
						mapReserveCategoryIndexToOdds[reserveCategoryNumber] = adjustedCategoryOdds
					}
				}
			}

			button("Submit") {
				action {
					for (reserveCategoryNumber in 1..controller.getNumCategories()) {

						controller.logValue("Creating Category $reserveCategoryNumber Name: ${mapReserveCategoryIndexToName[reserveCategoryNumber]}" +
							" Odds: ${mapReserveCategoryIndexToOdds[reserveCategoryNumber]}")

						controller.addCategory(mapReserveCategoryIndexToName[reserveCategoryNumber]!!.value, mapReserveCategoryIndexToOdds[reserveCategoryNumber]!!.value)
					}

					controller.createPopulationGroups()

					println("User pressed Submit.")

					replaceWith(find<PermutationDemandInput>())
				}
			}
		}
	}
}