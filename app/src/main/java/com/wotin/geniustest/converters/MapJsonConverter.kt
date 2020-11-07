package com.wotin.geniustest.converters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MapJsonConverter {

    fun MapToJsonConverter(json: String): Map<String, Any> {

        val gson = Gson()

        var map: Map<String, Any> =
            gson.fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
        for(i in map.keys) {
            if(map[i] is Float) {
            }
        }
        map.forEach { println(it) }

        return map
    }



}
