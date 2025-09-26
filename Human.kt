open class Human(
    var name: String,
    var age: Int,
    override var speed: Int, // Реализация свойства из Movable
    override var x: Int = 0, // Реализация свойства из Movable
    override var y: Int = 0  // Реализация свойства из Movable
) : Movable {

    // Случайное движение (во все стороны)
    override fun move() {
        val directions = listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))
        val (dx, dy) = directions.random()
        x += dx * speed
        y += dy * speed
    }

    override fun toString(): String {
        return "$name (возраст: $age) → позиция: ($x, $y), скорость: $speed"
    }
}