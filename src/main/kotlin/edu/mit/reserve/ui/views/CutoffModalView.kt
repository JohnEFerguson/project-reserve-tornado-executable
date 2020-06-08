package edu.mit.reserve.ui.views

import edu.mit.reserve.lottery.models.PopulationGroup
import edu.mit.reserve.ui.controllers.FirstLotteryCutoffRow
import edu.mit.reserve.ui.controllers.LotteryController
import javafx.scene.text.FontWeight
import tornadofx.*

class CutoffModalView : Fragment("Lottery Cutoffs") {

	val lotteryController: LotteryController by inject()

	override val root = vbox {


		vbox(20.0) {

			vbox(10.0) {

				label {
					addClass(heading)
					text = "First Lottery Cutoffs"
				}

				tableview<FirstLotteryCutoffRow> {

					prefWidth = 400.0
					prefHeight = 250.0

					items = lotteryController.firstLotteryCutoffRows


					readonlyColumn("Classification", FirstLotteryCutoffRow::name).remainingWidth()
					readonlyColumn("Cutoff", FirstLotteryCutoffRow::cutOff)

					columnResizePolicy = SmartResize.POLICY

				}
			}


			vbox(10.0) {

				label {
					addClass(heading)
					text = "Second Lottery Cutoffs"
				}

				tableview<PopulationGroup> {

					prefWidth = 400.0
					prefHeight = 250.0

					items = lotteryController.getPopulationGroups().filter { it.involvedInSecondLottery }.asObservable()


					readonlyColumn("Population Group", PopulationGroup::categories).remainingWidth().cellFormat { it ->
						var formatted = ""
						it.forEach { formatted += "$it, " }

						text = formatted.substring(0, formatted.length - 2)
					}
					readonlyColumn("Cutoff", PopulationGroup::secondCutoff)

					columnResizePolicy = SmartResize.POLICY

				}
			}
		}
	}
}
