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
        return "$name (возраст: $age) → позиция: ($x, $y), скорость: $speed"
    }
}

fun simulateTime(humans: List<Human>, seconds: Int) {
    repeat(seconds) {
        humans.forEach { human ->
            human.move()
            println(human)
        }
        Thread.sleep(1000)
    }
}

fun main() {
    val humans = listOf(
        Human("Иван Иванов", 30, 1),
        Human("Мария Петрова", 25, 2),
        Human("Жаркий Илья", 25, 5),
        Human("Савенок Анатол", 30, 3),
        Human("Клещ Владимр", 15, 4),
        Human("Сибирь Егорик", 18, 2),
        Human("Стрекоза Татьяна Викторовна", 50, 1),
        Human("Кузаюр", 11, 1),
        Human("Добри Андрей", 26, 2),
        Human("Алексей Сидоров", 19, 1)
    )

    simulateTime(humans, 10)
}
