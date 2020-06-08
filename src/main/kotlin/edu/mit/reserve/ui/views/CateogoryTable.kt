package edu.mit.reserve.ui.views

import edu.mit.reserve.ui.controllers.ConfigurationController
import edu.mit.reserve.ui.views.Styles.Companion.center
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.text.TextAlignment
import tornadofx.*

class CateogoryTable: View() {

	private val configurationController: ConfigurationController by inject()
	private var categoryTableEditModel: TableViewEditModel<Category> by singleAssign()


	override val root = hbox {

		tableview<Category> {

			prefWidth = 400.0
			prefHeight = 250.0

			items = configurationController.categories

			isEditable = true

			style {
				textAlignment = TextAlignment.CENTER
			}

			column("Name", Category::name).makeEditable().remainingWidth()
			column("Odds", Category::odds).contentWidth(40.0, true, false).cellFragment(CategoryTableNumberEditor::class)
			column("First", Category::isFirst).makeEditable().contentWidth(40.0, true, false)

			enableCellEditing()
			enableDirtyTracking()

			columnResizePolicy = SmartResize.POLICY

			onEditCommit {
				configurationController.updatePopulationGroups()

				if (it.isFirst) {
					configurationController.updateIsFirstCategories(it.name)
				}

				refresh()
			}


			categoryTableEditModel = editModel
		}
	}
}


class Category(name: String, odds: String, isFirst: Boolean) {
	val nameProperty = SimpleStringProperty(this, "name", name)
	var name by nameProperty
	val oddsProperty = SimpleStringProperty(this, "odds", odds)
	var odds by oddsProperty
	val isFirstProperty = SimpleBooleanProperty(this, "isFirst", isFirst)
	var isFirst by isFirstProperty
}

class CategoryModel : ItemViewModel<Category>() {
	val name = bind(Category::nameProperty)
	val odds = bind(Category::oddsProperty)
	val isFirst = bind(Category::isFirstProperty)
}


class CategoryTableNumberEditor : TableCellFragment<Category, String>() {
	// Bind our ItemModel to the rowItemProperty, which points to the current Item
	val model = CategoryModel().bindToRowItem(this)

	override val root = stackpane {
		textfield(model.odds) {
			removeWhen(editingProperty.not())
			validator {

				var oddsNumberValue = 0

				var canParse = true
				try {
					oddsNumberValue = model.odds.value.toInt()
				} catch (e: Exception) {
					canParse = false
				}

				if (!canParse || (oddsNumberValue > 100 || oddsNumberValue < -100)) error("Please pick a number between -100 and 100") else null
			}
			// Call cell.commitEdit() only if validation passes
			action {
				if (model.commit()) {
					cell?.commitEdit(model.odds.value)
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