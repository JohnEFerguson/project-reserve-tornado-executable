package edu.mit.reserve.ui.views

import edu.mit.reserve.ui.controllers.LotteryController
import tornadofx.*


class LotteryFooter : View() {

	private val lotteryController: LotteryController by inject()

	override val root = hbox(8) {

		style {
			padding = box(10.px)
		}

		button("Return To Config").action {
			find<PatientPage>().close()
			find<ConfigurationPage>().openWindow()
		}


		vbox {
			label {
				text = "    "
			}
		}


		button("Clear Patient List").action {
			lotteryController.clearPatients()
			// reset supply here
		}

	}
}