package edu.mit.reserve.ui.views

import tornadofx.*

class LotteryPage : View() {
	override val root = vbox {
		add(PatientPage::class)
	}
}