package utils

object DisplayUtils {

    fun displayWelcomeMessage() {
        val message = """
            *************************************************
            *                                               *
            *           Добро пожаловать в Морской бой!     *
            *                                               *
            *************************************************
        """.trimIndent()
        println("${AnsiColors.ANSI_PURPLE}$message${AnsiColors.ANSI_RESET}")
    }

    fun displayGameOptions() {
        val options = """
            *************************************************
            *            Выберите действие                  *
            *************************************************
            * 1. Начать игру                                *
            * 2. Показать правила                           *
            *************************************************
        """.trimIndent()
        println("${AnsiColors.ANSI_GREEN}$options${AnsiColors.ANSI_RESET}")
    }

    fun displayRules() {
        val rules = """
            *************************************************
            *               Правила игры                    *
            *************************************************
            *   Для игрока:                                 *
            * 1. Расставьте свои корабли на доске вручную   *
            *   или автоматически.                          *
            * 2. Корабли расставляются по размеру,          *
            *   начальным координатам и ориентации.         *
            * 3. Стреляйте по координатам                   *
            *   (например, A 0, B 1 и т.д.).                *
            * 4. Игра заканчивается, когда все корабли      *
            *   одного из игроков уничтожены.               *
            * 5. Удачи и получайте удовольствие!            *
            *                                               *
            *   О противнике:                               *
            * 1. Бот расставляет корабли автоматически.     *
            * 2. Бот стреляет случайно, но если попадает,   *
            *   продолжает стрелять по соседним клеткам,    *
            *   пока не разрушит корабль.                   *
            * 3. Каждый третий ход бота гарантированно      *
            *   успешен.                                    *
            *************************************************
        """.trimIndent()
        println("${AnsiColors.ANSI_CYAN}$rules${AnsiColors.ANSI_RESET}")
    }
}
