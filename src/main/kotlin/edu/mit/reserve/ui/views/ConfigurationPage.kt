package edu.mit.reserve.ui.views

import tornadofx.*

class ConfigurationPage: View() {



	override val root = vbox {
		hbox {
			add(ConfigurationLeft::class)
			add(ConfigurationRight::class)
		}
		add(ConfigurationFooter::class)
	}
}