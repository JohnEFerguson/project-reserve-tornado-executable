package edu.mit.reserve.ui.views

import edu.mit.reserve.lottery.models.Category
import edu.mit.reserve.ui.controllers.LotteryController
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.stage.StageStyle
import tornadofx.*
import java.time.LocalDate
import kotlin.collections.HashMap


class PatientInput : View() {

	private val controller: LotteryController by inject()
	private val patientId = SimpleStringProperty()
	private val patientName = SimpleStringProperty()
	private val date = SimpleObjectProperty<LocalDate>()
	private val categoryToStatus = HashMap<Category, SimpleBooleanProperty>()
	val model: GlobalLotteryView by inject()

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


			label {
				textProperty().bind(model.numCoursesAvailable)
			}

			label {
				textProperty().bind(model.numPatients)
			}


			borderpane {

				style {
					padding = box(10.px)
				}

				left {

					button("Return To Config").action {
						find<PatientPage>().close()
						find<ConfigurationPage>().openWindow()
					}

				}

				center {
					button("View Cutoffs").action {
						find<CutoffModalView>().openModal(stageStyle = StageStyle.UTILITY)
					}
				}

				right {


					button("Clear Patient List").action {
						controller.clearPatients()
						// reset supply here
					}
				}


			}
		}

	}
}

class GlobalLottery(numPatients: String, numCoursesAvailable: String) {
	val numPatientsProperty = SimpleStringProperty(numPatients)
	var numPatients by numPatientsProperty

	val numCoursesAvailableProperty = SimpleStringProperty(numCoursesAvailable)
	var numCoursesAvailable by numCoursesAvailableProperty
}

class GlobalLotteryView : ItemViewModel<GlobalLottery>() {
	val numPatients = bind(GlobalLottery::numPatientsProperty)
	val numCoursesAvailable = bind(GlobalLottery::numCoursesAvailableProperty)
}
