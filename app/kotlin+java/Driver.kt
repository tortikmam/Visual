class Driver(dName: String, dAge: Int, dSpeed: Int, dX: Int = 0, dY: Int = 0): Human(dName, dAge, dSpeed, dX, dY){

    // Переопределенное движение для Driver (только по X)
    override fun move() {
        // Движение только влево/вправо
        val directions = listOf(Pair(-1, 0), Pair(1, 0))
        val (dx, dy) = directions.random()
        x += dx * speed
        y += dy * speed
    }
}