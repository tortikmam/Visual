import kotlin.concurrent.thread

fun simulateTimeParallel(movableElements: List<Movable>, seconds: Int) {
    val threads = mutableListOf<Thread>()

    // Создаем отдельный поток для КАЖДОГО движущегося объекта
    movableElements.forEach { element ->
        val thread = thread {
            repeat(seconds) { step ->
                element.move()
                println("$element - шаг ${step + 1}")
                Thread.sleep(1000) // Пауза между шагами
            }
        }
        threads.add(thread)
    }

    // Ждем завершения ВСЕХ потоков
    threads.forEach { it.join() }
}

fun main() {
    val humans = listOf(
        Human("Стрекоза Татьяна Викторовна", 50, 1),
        Human("Кузаюр", 11, 1),
        Human("Добри Андрей", 26, 2),
        Human("Кукурузин Вася", 19, 1)
    )
    val driver = Driver("Да не умер он в конце драйва", 19, 5)

    // Теперь в список можно добавлять любые объекты, реализующие Movable
    val allElements: List<Movable> = humans + driver

    simulateTimeParallel(allElements, 5)
}
