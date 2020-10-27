package com.wotin.geniustest.CustomClass

class RankCustomClass(
    val UniqueId : String = "",
    val id: String = "",
    val bestScore : String = "",
    val level : String = "",
    val ranking : String = "",
    val heart: String = ""
) {
    override fun toString(): String {
        return "UniqueId : $UniqueId, id : $id, bestScore : $bestScore, level : $level, ranking : $ranking, heart : $heart"
    }
}