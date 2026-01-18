package com.example.myfotogramapp.application.utilis


fun normalizeText(text: String, isBio: Boolean = false): String {
    var result = text
        .trim() // rimuove spazi e newline ai bordi
        .replace("[ ]{2,}".toRegex(), " ") // comprime spazi multipli in uno

    result = if (isBio) {
        result.replace("\n{2,}".toRegex(), "\n")
    } else {
        result.replace("\n{3,}".toRegex(), "\n\n")
    }

    return result
}

fun isValidUsername(username: String): Boolean {
    val regex = "^[A-Za-z0-9_]+$".toRegex()
    return username.matches(regex)
}