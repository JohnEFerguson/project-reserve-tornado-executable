package edu.mit.reserve.ui.views

import tornadofx.*

class ConfigurationLeft : View() {

	override val root = vbox {


		style {
			padding = box(5.px)
		}

		add(GlobalValueConfig::class)
		add(CateogoryTable::class)
	}
}