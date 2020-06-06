package edu.mit.reserve.ui.views

import tornadofx.*

class PatientPage : View() {
	override val root = hbox {
		add(PatientList::class)
		add(PatientInput::class)
	}
}