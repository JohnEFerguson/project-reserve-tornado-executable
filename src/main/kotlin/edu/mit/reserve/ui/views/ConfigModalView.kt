package edu.mit.reserve.ui.views

import edu.mit.reserve.ui.controllers.GlobalConfigRow
import edu.mit.reserve.ui.controllers.LotteryController
import edu.mit.reserve.ui.controllers.PopulationGroupConfigRow
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import tornadofx.*

class ConfigModalView : Fragment("Lottery Configuratione") {

	val lotteryController: LotteryController by inject()

	override val root = vbox {

		style {
			padding = box(10.px)
		}

		vbox(20.0) {


			vbox(10.0) {

				label {
					font = Font.font("Cambria", FontWeight.EXTRA_BOLD, 16.0)
					text = "Global Configuration Values"
				}

				tableview<GlobalConfigRow> {

					prefWidth = 800.0
					prefHeight = 55.0

					items = lotteryController.globalConfigRow


					readonlyColumn("Expected Number of Patients", GlobalConfigRow::demand).remainingWidth()
					readonlyColumn("Initial Number of Courses Available", GlobalConfigRow::supply)
					readonlyColumn("Number of Reserve Categories", GlobalConfigRow::numCategories)
					readonlyColumn("First Lottery Category", GlobalConfigRow::firstCategory)

					columnResizePolicy = SmartResize.POLICY

				}
			}

			hbox(10.0) {
				vbox(10.0) {

					label {
						font = Font.font("Cambria", FontWeight.EXTRA_BOLD, 16.0)
						text = "Reserve Category Configuration"
					}

					tableview<Category> {

						prefWidth = 400.0
						prefHeight = 250.0

						items = lotteryController.getCategories()
							.map { Category(it.name, it.odds.toString(), true) }.asObservable()

						readonlyColumn("Name", Category::name).remainingWidth()
						readonlyColumn("Adjusted Odds", Category::odds)

						columnResizePolicy = SmartResize.POLICY

					}
				}

				vbox(10.0) {

					label {
						font = Font.font("Cambria", FontWeight.EXTRA_BOLD, 16.0)
						text = "Population Group Demand Configuration"
					}

					tableview<PopulationGroupConfigRow> {

						prefWidth = 400.0
						prefHeight = 250.0

						items = lotteryController.getPopulationGroups().map {
							PopulationGroupConfigRow(it.toString(), it.demand.toString()) }.asObservable()

						readonlyColumn("Population Group", PopulationGroupConfigRow::name).remainingWidth()
						readonlyColumn("Expected Demand", PopulationGroupConfigRow::demand)

						columnResizePolicy = SmartResize.POLICY

					}
				}
			}
		}
	}
}
