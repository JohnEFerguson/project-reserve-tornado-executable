package edu.mit.reserve.lottery.models

import java.time.LocalDate


class Patient(
	val primaryId: Int,
	val id: String,
	val name: String,
	var populationGroup: PopulationGroup,
	val date: LocalDate
) {

	var firstLotteryNumber = -1.0
	var secondLotteryNumber = -1.0

	var firstLotteryResult = false
	var secondLotteryResult = false
	var shouldRunSecondLottery = false
	var hasRunFirstLottery = false
	var hasRunSecondLottery = false
	var accepted = false
	var chosenToAcceptOrNot = false
	var hasBeenGivenTheOpportunityToAcceptOrNot = false
}