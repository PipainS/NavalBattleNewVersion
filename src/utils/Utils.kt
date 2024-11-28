package utils

object Utils {
    // преобразованный метод PadeEnd для работы с ansi
    fun String.padEndAnsi(totalLength: Int, padChar: Char = ' '): String {
        val strippedLength = this.replace(Regex("\u001B\\[[;\\d]*m"), "").length
        val padLength = totalLength - strippedLength

        return this + padChar.toString().repeat(padLength.coerceAtLeast(0))
    }

    fun readValidatedInput(prompt: String = "", checkForGodMode: Boolean = false): Pair<String, Boolean> {
        var isGodMode = false
        while (true) {
            try {
                if (prompt.isNotEmpty()) {
                    print(prompt)
                }
                val input = readlnOrNull()?.trim()?.lowercase() ?: throw IllegalArgumentException("Ввод не может быть пустым")

                if (checkForGodMode && input.endsWith("godmode")) {
                    isGodMode = true
                    println("${AnsiColors.ANSI_RED}godmode включен!${AnsiColors.ANSI_RESET}")
                    return Pair(input.removeSuffix("godmode").trim(), isGodMode)
                }

                if (input != "да" && input != "нет" && input != "yes" && input != "no") {
                    throw IllegalArgumentException("Пожалуйста, введите 'да' или 'нет' (или 'yes'/'no')")
                }
                return Pair(input, isGodMode)
            } catch (e: IllegalArgumentException) {
                println("${AnsiColors.ANSI_RED}${e.message}${AnsiColors.ANSI_RESET}")
            }
        }
    }


}