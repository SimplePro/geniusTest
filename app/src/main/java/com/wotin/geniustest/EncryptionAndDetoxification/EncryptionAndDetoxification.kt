package com.wotin.geniustest.EncryptionAndDetoxification

class EncryptionAndDetoxification {
    fun encryptionAndDetoxification(str : String) : String {
        val encryptionDictionary = mapOf<String, String>("a" to "8", "b" to "e", "c" to "l", "d" to "n", "e" to "b", "f" to "i",
            "g" to "h", "h" to "g", "i" to "f", "j" to "k", "k" to "j", "l" to "c", "m" to "q", "n" to "d", "o" to "r", "p" to "v", "q" to "m",
            "r" to "o", "s" to "6", "t" to "y", "u" to "x", "v" to "p", "w" to "z", "x" to "u", "y" to "t", "z" to "w", "1" to "5", "2" to "7",
            "3" to "4", "4" to "3", "5" to "1", "6" to "s", "7" to "2", "8" to "a", "9" to "@", "0" to ".", "@" to "9", "." to "0")

        var result = ""

        for (char in str) {
            result = result + encryptionDictionary[char.toString()]
        }

        return result
    }
}