package edu.mit.reserve.lottery.utils

import edu.mit.reserve.lottery.models.Category
import edu.mit.reserve.lottery.models.PopulationGroup
import kotlin.math.pow


fun generatePopulationGroups(categories: List<Category>): MutableList<PopulationGroup> {

    val populationGroups = mutableListOf<PopulationGroup>()

    val numberOfPermutations = 2.0.pow(categories.size.toDouble()) - 1

    for (i in 1..numberOfPermutations.toInt() ) {
        val categorySet = mutableSetOf<Category>()
        var placeholder = i
        for (element in categories) {

            if ( (placeholder and 1) == 1 ) {
                categorySet.add(element)
            }
            placeholder = placeholder.shr(1)
        }

        populationGroups.add(PopulationGroup(categorySet))
    }
    return populationGroups
}

