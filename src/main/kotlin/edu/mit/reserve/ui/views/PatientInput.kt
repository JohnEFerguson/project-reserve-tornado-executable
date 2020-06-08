package edu.mit.reserve.ui.views

import edu.mit.reserve.lottery.models.Category
import edu.mit.reserve.ui.controllers.LotteryController
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
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
	val patientValuesModel: PatientInputFieldsModel by inject()

	override val root = form()

	init {


		title = "Patient input"

		with(root) {


			fieldset {
				text = "Input a patient"

				field("Patient Id:") {
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

					useMaxWidth = true


					action {

						val categories = mutableSetOf<Category>()
						categoryToStatus.keys.forEach {
							if (categoryToStatus[it]!!.value) categories.add(it)
						}

						println(patientValuesModel.patientId.value + " " + patientValuesModel.patientName.value)
						controller.addPatient(patientValuesModel.patientId.value, patientValuesModel.patientName.value, categories, patientValuesModel.date.value)
					}
				}

				vbox(5.0) {

					label {
						textProperty().bind(model.numCoursesAvailable)
					}

					label {
						textProperty().bind(model.numPatients)
					}
				}

				vbox {

					alignment = Pos.BOTTOM_CENTER

					hbox(spacing = 20.0) {


						button("Return To Config") {

							useMaxWidth = true

							tooltip("OHH BABY")

							action {
								find<PatientPage>().close()
								find<ConfigurationPage>().openWindow()
							}
						}


						button("View Cutoffs") {

							useMaxWidth = true

							action {
								find<CutoffModalView>().openModal(stageStyle = StageStyle.UTILITY)
							}
						}



						button("Clear Patient List") {

							useMaxWidth = true

							action {
								controller.clearPatients()
								// reset supply here
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
}

class PatientInputFieldsModel : ItemViewModel<PatientInputFields>() {
	val patientId = bind(PatientInputFields::patientIdProperty)
	val patientName = bind(PatientInputFields::patientNameProperty)
	val date = bind(PatientInputFields::dateProperty)
}
