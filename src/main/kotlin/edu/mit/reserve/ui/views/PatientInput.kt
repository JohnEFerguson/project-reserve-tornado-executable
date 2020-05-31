package edu.mit.reserve.ui.views

import edu.mit.reserve.lottery.models.Category
import edu.mit.reserve.ui.controllers.LotteryController
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.time.LocalDate
import kotlin.collections.HashMap


class PatientInput : View() {

	private val controller: LotteryController by inject()
	private val patientId = SimpleStringProperty()
	private val patientName = SimpleStringProperty()
	private val date = SimpleObjectProperty<LocalDate>()
	private val categoryToStatus = HashMap<Category, SimpleBooleanProperty>()


	override val root = form()

	init {
		title = "Patient input"

		with(root) {


			fieldset {
				text = "Input a patient"

				field("Patient Id:") {
					textfield(patientId)
				}

				field("Patient Name:") {
					textfield(patientName)
				}


				field("Date:") {
					datepicker(date) {
						value = LocalDate.now()
					}
				}

				controller.getCategories().forEach {

					val status = SimpleBooleanProperty()

					field {
						checkbox(it.toString(), status)
						categoryToStatus[it] = status
					}
				}
			}

			button("Submit") {

				action {

					val categories = mutableSetOf<Category>()
					categoryToStatus.keys.forEach {
						if (categoryToStatus[it]!!.value) categories.add(it)
					}

					controller.addPatient(patientId.value, patientName.value, categories, date.value)
					println("Patient Submitted id: ${patientId.value} name: ${patientName.value} categories: $categories date: ${date.value}")
				}
			}
		}
	}
}