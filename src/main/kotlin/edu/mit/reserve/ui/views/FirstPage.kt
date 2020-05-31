package edu.mit.reserve.ui.views

import edu.mit.reserve.ui.controllers.LotteryController
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import tornadofx.*

class FirstPage : View() {

	val controller: LotteryController by inject()
	val numberTotalPatientsNeedingRemdesivir = SimpleIntegerProperty()
	val numberCoursesToBeDistributed = SimpleIntegerProperty()
	val numberReserveCategories = SimpleIntegerProperty()

	override val root = form {

		setPrefSize(900.0, 220.0)

		label {
			text = "Hello and welcome to the Project Reserve starting page. Please enter the relevant inputs below to begin your session.\n\n\n"
			font = Font.font("Cambria", FontWeight.BOLD, 16.0)
		}

		fieldset {

			text = "Overall Supply and Demand:"

			field("Estimated total number of patients needing remdesivir:") {
				textfield(numberTotalPatientsNeedingRemdesivir)
			}

			field("Number of courses to be distributed:") {
				textfield(numberCoursesToBeDistributed)
			}

			field( "Number of reserve categories:") {
				textfield(numberReserveCategories)
			}

			button("Submit") {
				action {

					controller.logValue("Number of Total Patients Needing Remdesivir = ${numberTotalPatientsNeedingRemdesivir.value}")
					controller.logValue("Number of Courses to be Distributed = ${numberCoursesToBeDistributed.value}")
					controller.logValue("Number of Reserve Categories = ${numberReserveCategories.value}")

					controller.setGlobalDemand(numberTotalPatientsNeedingRemdesivir.value)
					controller.setGlobalSupply(numberCoursesToBeDistributed.value)
					controller.setNumCategories(numberReserveCategories.value)

					println( "User pressed submit.")
					replaceWith(find<ReserveCategory>())
				}
			}
		}
	}

}