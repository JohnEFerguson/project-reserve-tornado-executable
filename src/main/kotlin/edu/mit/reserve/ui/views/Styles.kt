package edu.mit.reserve.ui.views

import tornadofx.*

class Styles : Stylesheet() {
	companion object {
		val firstPage by cssclass()
		val categoryDefinition by cssclass()
	}

	init {

		firstPage {
			prefWidth = 900.px
			prefHeight = 220.px
		}


		categoryDefinition {
			prefWidth = 300.px
			prefHeight = 400.px
			scrollBar
		}


	}
}