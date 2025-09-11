data class Human(
    var name: String,
    var age: Int,
    var speed: Int,
    var x: Int = 0, // начальная позиция по X
    var y: Int = 0  // начальная позиция по Y
) {
    // Случайное движение
    fun move() {
        val directions = listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))
        val (dx, dy) = directions.random()
        x += dx * speed
        y += dy * speed
    }

    // Красивый вывод информации о человеке
    override fun toString(): String {
        return "$name (возраст: $age) -> позиция: ($x, $y), скорость: $speed"
    }
}

// Симуляция движения
fun simulateTime(humans: List<Human>, seconds: Int) {
    repeat(seconds) {
        humans.forEach { human ->
            human.move()
            println(human)
        }
        Thread.sleep(1000) // задержка 1 секунда
    }
}

// Точка входа
fun main() {
    val humans = listOf(

        Human("Иван Иванов", 30, 1),
        Human("Мария Петрова", 25, 2),
        Human("Савенок Анатолий", 18, 5),
        Human("Жаркий Илья", 18, 3),
        Human("Клещ Владимр", 18, 2),
        Human("Стрекоза Татьяна Викторовна", 40, 3),
        Human("Солод Паел", 40, 4),
        Human("Сибирь Егорик", 78, 1),
        Human("Павел Волк", 31, 6),
        Human("Кирилл Шмель", 15, 3)

    )

    simulateTime(humans, 10) // симуляция 10 секунд
}
