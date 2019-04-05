import roadmap.parseRoadmap
import typemaps.datefns.format
import typemaps.peg.parse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

class ParserKtTest {

    @Test fun simpleParser() {
        val result = parse("""
            Project 1, Task 1
            2019-01-01, 2019-02-02
            Team1

        """.trimIndent())[0]

        assertEquals(result.title.project, "Project 1")
        assertEquals(result.title.task, "Task 1")
    }

    @Test fun fullParsingExample() {
        val result = parse("""
            Project A, Task A
            2019-01-10 + 9w, 2019-01-23
            Team A
            @ important-milestone, planned
            ^ google | https://google.com
            > A critical milestone
            > More important than Milestone B
            """.trimIndent())[0]

        assertEquals("Project A", result.title.project)
        assertEquals("Task A", result.title.task)
        assertEquals("2019-01-10", result.start.date)
        assertEquals("9w", result.start.delta)
        assertEquals("2019-01-23", result.end?.date)
        assertEquals("google", result.meta.links[0].name)
        assertEquals("https://google.com", result.meta.links[0].url)
        assertEquals("A critical milestone\nMore important than Milestone B", result.meta.desc)
    }

    @Test fun parseError() {
        assertFails {
            parse("""
            Project 1, Task 1
            2019-01-01, 2019-02-02
        """.trimIndent())[0]
        }
    }

    @Test fun parseErrorWithLineNumber() {
        val error = parseRoadmap("""
            Project 1, Task 1
            Some random date
        """.trimIndent()).error!!

        console.log(error)
        assertTrue { error.message.contains("Some random date") }
    }

    @Test fun parseRoadmapWithDates() {
        val result = parseRoadmap("""
            Project A, Task A
            2019-01-10 + 3d, 3d
            Team A
            """.trimIndent()).value!!

        assertEquals("13", format(result.items[0].start, "D"))
        assertEquals("16", format(result.items[0].end!!, "D"))
    }
}