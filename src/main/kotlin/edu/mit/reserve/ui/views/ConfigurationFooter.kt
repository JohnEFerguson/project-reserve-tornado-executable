package edu.mit.reserve.ui.views

import edu.mit.reserve.ui.controllers.ConfigurationController
import tornadofx.*

class ConfigurationFooter: View() {

	private val configurationController: ConfigurationController by inject()
	private val model: GlobalValueConfigValueModel by inject()

	override val root = vbox {

		button("Submit") {

			tooltip("Confirm configuration")

			useMaxWidth = true

			enableWhen(model.valid.and(configurationController.selectedFirstCategory))

			action {
				configurationController.submit(model.numPatients.value.toInt(),
					model.numCoursesAvailable.value.toInt(), model.numCategories.value.toInt())
				find<ConfigurationPage>().close()
				find<LotteryPage>().openWindow()
			}
		}
	}
}