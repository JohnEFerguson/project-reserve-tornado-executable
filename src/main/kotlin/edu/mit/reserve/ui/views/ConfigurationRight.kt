package edu.mit.reserve.ui.views

import edu.mit.reserve.ui.controllers.ConfigurationController
import tornadofx.*

class ConfigurationRight : View() {

	private val configurationController: ConfigurationController by inject()
	private val model: GlobalValueConfigValueModel by inject()
	private val categoryListModel: CategoryModel by inject()
	private val populationGroupListModel: PopulationGroupModel by inject()

	override val root = vbox {

		style {
			padding = box(5.px)
		}

		add(PopulationGroupTable::class)
		borderpane {

			center {

				style {
					padding = box(5.px)
				}

				button("Submit") {

					enableWhen(model.valid.and(categoryListModel.valid).and(populationGroupListModel.valid))

					action {
						configurationController.submit(model.numPatients.value.toInt(),
							model.numCoursesAvailable.value.toInt(), model.numCategories.value.toInt())
						find<ConfigurationPage>().close()
						find<LotteryPage>().openWindow()
					}
				}
			}


		}
	}
}