package edu.mit.reserve.ui.views

import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import tornadofx.*

class GlobalValueConfig : View() {

	val model: GlobalValueConfigValueModel by inject()


	override val root = form {

		style {
			prefWidth = 400.px
			prefHeight = 200.px
		}

		label {
			text = "Project Reserve | Configuration Page\n\n\n"
			font = Font.font("Cambria", FontWeight.BOLD, 16.0)
		}

		fieldset {

			field("Estimated total number of patients needing remdesivir:") {
				textfield(model.numPatients) {
					validator {
						if (it.isNullOrBlank() || !it.isInt() || it.toInt() < 0) error("Add a whole number > 0")
						else null
					}
				}
			}

			field("Number of courses to be distributed:") {
				textfield(model.numCoursesAvailable) {
					validator {
						if (it.isNullOrBlank() || !it.isInt() || it.toInt() < 0) error("Add a whole number > 0")
						else null
					}
				}
			}

			field("Number of reserve categories:") {
				combobox(model.numCategories) {
					items = observableListOf((1..5).toList())
				}
			}
		}
	}

}


class GlobalValueConfigValue(numPatients: Int, numCoursesAvailable: Int, numCategories: Int) {
	val numPatientsProperty by lazy { SimpleIntegerProperty(numPatients) }
	var numPatients by numPatientsProperty

	val numCoursesAvailableProperty by lazy { SimpleIntegerProperty(numCoursesAvailable) }
	var numCoursesAvailable by numCoursesAvailableProperty

	val numCategoriesProperty by lazy { SimpleIntegerProperty(numCategories)}
	var numCategories by numCategoriesProperty
}

class GlobalValueConfigValueModel : ItemViewModel<GlobalValueConfigValue>() {
	val numPatients = bind(GlobalValueConfigValue::numPatientsProperty)
	val numCoursesAvailable = bind(GlobalValueConfigValue::numCoursesAvailableProperty)
	val numCategories = bind(GlobalValueConfigValue::numCategoriesProperty)
}
