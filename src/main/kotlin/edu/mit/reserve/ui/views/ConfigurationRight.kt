package edu.mit.reserve.ui.views

import tornadofx.*

class ConfigurationRight : View() {

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
			}
		}
	}
}