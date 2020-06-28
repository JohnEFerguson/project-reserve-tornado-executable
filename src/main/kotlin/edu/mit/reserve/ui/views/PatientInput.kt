package edu.mit.reserve.ui.views

import edu.mit.reserve.lottery.models.Category
import edu.mit.reserve.ui.controllers.LotteryController
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleSetProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.stage.StageStyle
import tornadofx.*
import java.time.LocalDate
import kotlin.collections.HashMap


class PatientInput : Fragment() {

	private val controller: LotteryController by inject()
	private val categoryToStatus = HashMap<Category, SimpleBooleanProperty>()
	val model: GlobalLotteryView by inject()
	val patientValuesModel: PatientInputFieldsModel by inject()

	override val root = form()

	init {


		title = "Patient input"

		with(root) {


			fieldset {
				text = "Input a patient"

				field("Patient ID:") {
					textfield(patientValuesModel.patientId) {
						validator {
							if (it.isNullOrBlank()) error("This field cannot be blank.")
							else null
						}
					}

				}

				field("Patient Name:") {
					textfield(patientValuesModel.patientName) {
						validator {
							if (it.isNullOrBlank()) error("This field cannot be blank.")
							else null
						}
					}
				}


				field("Date:") {
					datepicker(patientValuesModel.date) {
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

			vbox(50.0) {
				button("Submit") {

					enableWhen(patientValuesModel.valid)

					tooltip("Enter patient into database")

					useMaxWidth = true


					action {

						categoryToStatus.keys.forEach {
							if (categoryToStatus[it]!!.value) patientValuesModel.categories.value.add(it)
						}

						controller.addPatient(patientValuesModel.patientId.value, patientValuesModel.patientName.value, patientValuesModel.categories.value, patientValuesModel.date.value)

						categoryToStatus.keys.forEach {
							categoryToStatus[it]!!.value = false
						}

						controller.clearPatientInputFields()
					}
				}

				vbox(35.0) {

					vbox(5.0) {


						label {

							text = "Number of courses available:"

							font = Font.font("Cambria", FontWeight.BOLD, 16.0)

						}


						label {

							textProperty().bind(model.numCoursesAvailable)

							font = Font.font("Cambria", FontWeight.EXTRA_BOLD, 30.0)

						}
					}


					vbox(5.0) {

						label {

							text = "Number of patients accepting drugs:"

							font = Font.font("Cambria", FontWeight.BOLD, 16.0)

						}

						label {
							textProperty().bind(model.numPatients)

							font = Font.font("Cambria", FontWeight.EXTRA_BOLD, 30.0)
						}
					}
				}

				vbox {

					alignment = Pos.BOTTOM_CENTER

					hbox(spacing = 7.0) {


						button("View Config") {

							tooltip("View configuration for lottery.")

							useMaxWidth = true

							action {
								find<ConfigModalView>().openModal(stageStyle = StageStyle.UTILITY)
							}
						}


						button("View Cutoffs") {

							tooltip("View cutoffs for both lotteries.")

							useMaxWidth = true

							action {
								find<CutoffModalView>().openModal(stageStyle = StageStyle.UTILITY)
							}
						}



						button("Clear Patient List") {

							tooltip("Warning: patient list permanently deleted.")

							useMaxWidth = true

							action {
								controller.clearPatients()
								// reset supply here
							}
						}

						button("Export To CSV") {

							tooltip("Export patient data as a .csv in current directory.")

							useMaxWidth = true

							action {
								controller.exportToCSV()
							}
						}
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


class PatientInputFields(patientId: String, patientName: String, date: LocalDate) {
	val patientIdProperty by lazy { SimpleStringProperty(patientId) }
	var patientId by patientIdProperty

	val patientNameProperty by lazy { SimpleStringProperty(patientName) }
	var patientName by patientNameProperty

	val dateProperty by lazy { SimpleObjectProperty<LocalDate>(date) }
	var date by dateProperty

	val categoriesProperty by lazy { SimpleSetProperty<Category>() }
	var categories by categoriesProperty
}

class PatientInputFieldsModel : ItemViewModel<PatientInputFields>() {
	val patientId = bind(PatientInputFields::patientIdProperty)
	val patientName = bind(PatientInputFields::patientNameProperty)
	val date = bind(PatientInputFields::dateProperty)
	val categories = bind(PatientInputFields::categoriesProperty)
}
