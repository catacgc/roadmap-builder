import roadmap.parseRoadmap
import kotlin.test.Test
import kotlin.test.assertEquals

class ParserKtTest {

    @Test fun testParsingItem() {
        val result = parseRoadmap("""
            Project 1, Task 1
            2019-01-01, 2019-02-02
            Team1

        """.trimIndent()).value!!

        assertEquals(result.items.size, 1)
        assertEquals(result.items[0].group, "Project 1")
        assertEquals(result.items[0].title, "Task 1")
    }
}