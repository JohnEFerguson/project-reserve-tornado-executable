package edu.mit.reserve.ui.views

import edu.mit.reserve.lottery.models.Patient
import edu.mit.reserve.ui.controllers.LotteryController
import javafx.scene.image.Image
import tornadofx.*
import java.lang.Math.round

class PatientList : View() {

	private val controller: LotteryController by inject()
	private var populationGroupTableEditModel: TableViewEditModel<Patient> by singleAssign()
	val model: GlobalLotteryView by inject()

	fun roundDouble(x: Double) = round(x * 100.0) / 100.0

	override val root = form()

	init {

		title = "Patient input"

		with(root) {

			tableview<Patient> {

				populationGroupTableEditModel = editModel

				items = controller.patients

				prefWidth = 800.0

				columnResizePolicy = SmartResize.POLICY

				readonlyColumn("Id", Patient::id).contentWidth(30.0, true, false)
				readonlyColumn("Name", Patient::name).contentWidth(70.0, true, false)
				readonlyColumn("Categories", Patient::populationGroup).contentWidth(30.0, true, false)
				readonlyColumn("Date", Patient::date).contentWidth(30.0, true, true)

				column("1st Lottery", Patient::firstLotteryResult).contentWidth(20.0).cellFormat {

					text = ""

					if (!rowItem.hasRunFirstLottery) {

						graphic = hbox(spacing = 5) {

							button("Run").action {
								rowItem.shouldRunSecondLottery = controller.shouldRunSecondLottery(rowItem)
								rowItem.hasRunFirstLottery = true
								text = it.toString()
								this.removeFromParent()
								refresh()
							}
						}
					} else if (rowItem.firstLotteryResult) {

						graphic = hbox(spacing = 5) {

							label {
								textFill = c("green")
								text = roundDouble(rowItem.firstLotteryNumber).toString()

							}


						}

					} else {

						graphic = hbox(spacing = 5) {

							label {
								textFill = c("red")
								text = roundDouble(rowItem.firstLotteryNumber).toString()

							}

						}

					}
				}

				column("2nd Lottery", Patient::secondLotteryResult).contentWidth(20.0).cellFormat {
					text = ""
					if (rowItem.shouldRunSecondLottery) {
						graphic = hbox(spacing = 5) {
							button("Run").action {
								rowItem.hasRunSecondLottery = true
								rowItem.shouldRunSecondLottery = false
								text = it.toString()
								this.removeFromParent()
								refresh()
							}
						}
					} else if (rowItem.hasRunSecondLottery && rowItem.secondLotteryResult) {

						graphic = hbox(spacing = 5) {

							label {
								textFill = c("green")
								text = roundDouble(rowItem.secondLotteryNumber).toString()
							}
						}

					} else if (rowItem.hasRunSecondLottery) {

						graphic = hbox(spacing = 5) {

							label {
								textFill = c("red")
								text = roundDouble(rowItem.secondLotteryNumber).toString()
							}
						}
					} else {

						graphic = hbox(spacing = 5) {
							text = ""
						}
					}
				}


				column("Accept", Patient::accepted).contentWidth(20.0).cellFormat {
					text = ""
					if (controller.globalSupply > 0 && !rowItem.chosenToAcceptOrNot && ((rowItem.firstLotteryResult && rowItem.hasRunFirstLottery) || (rowItem.secondLotteryResult && rowItem.hasRunSecondLottery))) {
						graphic = hbox(spacing = 5) {
							button("Accept").action {
								rowItem.accepted = true
								text = rowItem.accepted.toString()
								rowItem.chosenToAcceptOrNot = true

								controller.globalSupply -= 1
								controller.coursesUsed += 1

								model.numCoursesAvailable.value = "Number of courses available: ${controller.globalSupply.value}"
								model.numPatients.value = "Number of patients accepted: ${controller.coursesUsed.value}"
								this.removeFromParent()
								refresh()
							}
						}
					} else if (rowItem.chosenToAcceptOrNot && rowItem.accepted) {

						graphic = hbox(spacing = 5) {

							imageview {
								image = Image("/check.png", 20.0, 20.0, false, true)
							}
						}

					} else if (rowItem.chosenToAcceptOrNot) {

						graphic = hbox(spacing = 5) {

							imageview {
								image = Image("/ex.png", 20.0, 20.0, false, true)
							}
						}

					} else {

						graphic = hbox(spacing = 5) {
							text = ""
						}
					}
				}
			}

		}


	}

}