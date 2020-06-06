package edu.mit.reserve.ui.views

import edu.mit.reserve.lottery.models.PopulationGroup
import edu.mit.reserve.ui.controllers.FirstLotteryCutoffRow
import edu.mit.reserve.ui.controllers.LotteryController
import tornadofx.*

class CutoffModalView: Fragment("Lottery Cutoffs") {

	val lotteryController: LotteryController by inject()

	override val root = vbox{


		label {
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

		label {
			text = "Second Lottery Cutoffs"
		}

		tableview<PopulationGroup> {

			prefWidth = 400.0
			prefHeight = 250.0

			items = lotteryController.getPopulationGroups().filter { it.involvedInSecondLottery }.asObservable()


			readonlyColumn("Population Group", PopulationGroup::categories).remainingWidth()
			readonlyColumn("Cutoff", PopulationGroup::secondCutoff)

			columnResizePolicy = SmartResize.POLICY

		}

	}
}