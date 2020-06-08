package edu.mit.reserve.ui.views

import javafx.scene.text.FontWeight
import javafx.scene.text.TextAlignment
import tornadofx.*
import java.awt.Font.BOLD

class Styles : Stylesheet() {
	companion object {
		val center by cssclass()
		val firstPage by cssclass()
		val categoryDefinition by cssclass()
		val heading by cssclass()
	}

	init {

		center {
			textAlignment = TextAlignment.CENTER
		}

		heading {
			fontSize = 20.px
			fontWeight = FontWeight.BOLD
		}

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