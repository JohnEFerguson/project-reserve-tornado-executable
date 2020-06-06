package edu.mit.reserve.ui.views

import edu.mit.reserve.ui.controllers.ConfigurationController
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleSetProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableSet
import tornadofx.*

class PopulationGroupTable : View() {

	private val configurationController: ConfigurationController by inject()
	private var populationGroupTableEditModel: TableViewEditModel<PopulationGroup> by singleAssign()


	override val root = hbox {

		tableview(configurationController.populationGroups) {

			prefHeight = 400.0
			prefWidth = 400.0

			isEditable = true

			column("Name", PopulationGroup::categoryNames).remainingWidth().cellFormat {
				it ->
				var formatted = ""
				it.forEach { formatted += "$it, "}

				text = formatted.substring(0, formatted.length - 2)
			}

			column("Expected Demand", PopulationGroup::expectedDemand).contentWidth(50.0, true, false).cellFragment(PopulationGroupNumberEditor::class)

			columnResizePolicy = SmartResize.POLICY

			enableCellEditing()
			enableDirtyTracking()

			populationGroupTableEditModel = editModel
		}
	}
}


class PopulationGroup(categoryNames: ObservableSet<String>, expectedDemand: String) {
	private val categoryNamesProperty = SimpleSetProperty<String>(this, "name", categoryNames)
	var categoryNames by categoryNamesProperty
	private val expectedDemandProperty = SimpleStringProperty(this, "expectedDemand", expectedDemand)
	var expectedDemand by expectedDemandProperty
}

class PopulationGroupModel : ItemViewModel<PopulationGroup>() {
	val categoryNames = bind(PopulationGroup::categoryNames)
	val expectedDemand = bind(PopulationGroup::expectedDemand)
}

class PopulationGroupNumberEditor: TableCellFragment<PopulationGroup, String>() {
	// Bind our ItemModel to the rowItemProperty, which points to the current Item
	val model = PopulationGroupModel().bindToRowItem(this)

	override val root = stackpane {
		textfield(model.expectedDemand) {
			removeWhen(editingProperty.not())
			validator {

				var expectedDemandNumberValue = 0

				var canParse = true
				try {
					expectedDemandNumberValue = model.expectedDemand.value.toInt()
				} catch (e: Exception) {
					canParse = false
				}

				if (!canParse || (expectedDemandNumberValue < 0)) error("Please enter a postive whole number") else null
			}
			// Call cell.commitEdit() only if validation passes
			action {
				if (model.commit()) {
					cell?.commitEdit(model.expectedDemand.value)
				}
			}
		}
		// Label is visible when not in edit mode, and always shows committed value (itemProperty)
		label(itemProperty) {
			removeWhen(editingProperty)
		}
	}

	// Make sure we rollback our model to avoid showing the last failed edit
	override fun startEdit() {
		model.rollback()
	}

}