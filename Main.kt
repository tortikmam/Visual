open class Human(
    var name: String,
    var age: Int,
    var speed: Int,
    var x: Int = 0, // начальная позиция по X
    var y: Int = 0  // начальная позиция по Y
) {
    // Случайное движение
    open fun move() {
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

class Driver(dName: String, dAge: Int, dSpeed: Int, dX: Int = 0, dY: Int = 0): Human(dName, dAge, dSpeed, dX, dY){

    override fun move() {
        val directions = listOf(Pair(-1, 0), Pair(1, 0))
        val (dx, dy) = directions.random()
        x += dx * speed
        y += dy * speed

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
        Human("Стрекоза Татьяна Викторовна", 50, 1),
        Human("Кузаюр", 11, 1),
        Human("Добри Андрей", 26, 2),
        Human("Кукурузин Вася", 19, 1)
    )
    val drivers = Driver("Да не умер он в конце драйва", 19, 1)

    val allElements = humans + drivers

    simulateTime(allElements, 10)
}
