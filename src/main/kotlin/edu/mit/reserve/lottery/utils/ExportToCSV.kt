package edu.mit.reserve.lottery.utils

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import edu.mit.reserve.lottery.models.Category
import edu.mit.reserve.lottery.models.Patient

fun export(filePath: String, rows: List<Patient>, header: List<List<String>>, categories: List<Category>) {

	val writer = csvWriter {
		charset = "ISO_8859_1"
		delimiter = ','
		nullCode = "NULL"
		lineTerminator = "\n"
	}

	writer.open(filePath) {
		writeAll(header)
		val columnNameRow = mutableListOf("id", "name", "date")
		categories.forEach { columnNameRow.add(it.name) }
		columnNameRow.addAll(listOf("first_lottery_number",
			"first_lottery_result", "second_lottery_number", "second_lottery_result", "accepted"))
		writeRow(columnNameRow)
		writeAll(rows.map{ getPatientRow(it, categories) })
	}
}

private fun getPatientRow(patient: Patient, categories: List<Category>): List<String> {
	val row = mutableListOf(patient.id, patient.name, patient.date.toString())
	categories.forEach { row.add(yesOrNo(patient.populationGroup.categories.contains(it))) }
	row.addAll(listOf(
		formatFirstLottery(patient, patient.firstLotteryNumber.toString()),
		formatFirstLottery(patient, winOrLoss(patient.firstLotteryResult)),
		formatSecondLottery(patient, patient.secondLotteryNumber.toString()),
		formatSecondLottery(patient, winOrLoss(patient.secondLotteryResult)),
		formatAccepted(patient)))
	return row
}

fun winOrLoss(result: Boolean) = if (result) "Win" else "Loss"
fun yesOrNo(value: Boolean) = if (value) "Yes" else "No"
fun formatAccepted(patient: Patient): String {
	return if (!patient.hasBeenGivenTheOpportunityToAcceptOrNot) "-"
	else if (patient.accepted) "Yes"
	else "No"
}

fun formatFirstLottery(patient: Patient, value: String): String {
	return if (patient.hasRunFirstLottery) value else "-"
}

fun formatSecondLottery(patient: Patient, value: String): String {
	return if (patient.hasRunSecondLottery) value else "-"
}