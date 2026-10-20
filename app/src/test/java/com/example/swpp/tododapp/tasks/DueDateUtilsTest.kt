package com.example.swpp.tododapp.tasks

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class DueDateUtilsTest {
    private data class Case(
        val dueDate: LocalDate?,
        val today: LocalDate,
        val completed: Boolean,
        val expected: String
    )

    @Test
    fun dueDateLabel_completedWithDueDate_returnsClear() {
        // GIVEN - Concrete calendar inputs and independent literal expectations.
        val cases = listOf(
            Case(LocalDate.parse("2026-10-22"), LocalDate.parse("2026-10-20"), true, "CLEAR")
        )
        for (case in cases) {
            // WHEN - Call the production function with an explicit reference date.
            val result = dueDateLabel(case.dueDate, case.today, case.completed)
            // THEN - The oracle does not call the production code or repeat Period.days.
            assertEquals("today=${case.today}, dueDate=${case.dueDate}, completed=${case.completed}", case.expected, result)
        }
    }

    @Test
    fun dueDateLabel_completedWithoutDueDate_returnsClear() {
        // GIVEN - Concrete calendar inputs and independent literal expectations.
        val cases = listOf(
            Case(null, LocalDate.parse("2026-10-20"), true, "CLEAR")
        )
        for (case in cases) {
            // WHEN - Call the production function with an explicit reference date.
            val result = dueDateLabel(case.dueDate, case.today, case.completed)
            // THEN - The oracle does not call the production code or repeat Period.days.
            assertEquals("today=${case.today}, dueDate=${case.dueDate}, completed=${case.completed}", case.expected, result)
        }
    }

    @Test
    fun dueDateLabel_activeDueInTwoDays_returnsDMinus2() {
        // GIVEN - Concrete calendar inputs and independent literal expectations.
        val cases = listOf(
            Case(LocalDate.parse("2026-10-22"), LocalDate.parse("2026-10-20"), false, "D-2")
        )
        for (case in cases) {
            // WHEN - Call the production function with an explicit reference date.
            val result = dueDateLabel(case.dueDate, case.today, case.completed)
            // THEN - The oracle does not call the production code or repeat Period.days.
            assertEquals("today=${case.today}, dueDate=${case.dueDate}, completed=${case.completed}", case.expected, result)
        }
    }

    @Test
    fun dueDateLabel_activeOverdueByTwoDays_returnsDPlus2() {
        // GIVEN - Concrete calendar inputs and independent literal expectations.
        val cases = listOf(
            Case(LocalDate.parse("2026-10-18"), LocalDate.parse("2026-10-20"), false, "D+2")
        )
        for (case in cases) {
            // WHEN - Call the production function with an explicit reference date.
            val result = dueDateLabel(case.dueDate, case.today, case.completed)
            // THEN - The oracle does not call the production code or repeat Period.days.
            assertEquals("today=${case.today}, dueDate=${case.dueDate}, completed=${case.completed}", case.expected, result)
        }
    }

    @Test
    fun dueDateLabel_activeWithoutDueDate_returnsEmpty() {
        // GIVEN - Concrete calendar inputs and independent literal expectations.
        val cases = listOf(
            Case(null, LocalDate.parse("2026-10-20"), false, "")
        )
        for (case in cases) {
            // WHEN - Call the production function with an explicit reference date.
            val result = dueDateLabel(case.dueDate, case.today, case.completed)
            // THEN - The oracle does not call the production code or repeat Period.days.
            assertEquals("today=${case.today}, dueDate=${case.dueDate}, completed=${case.completed}", case.expected, result)
        }
    }

    @Test
    fun dueDateLabel_dueToday_returnsDDay() {
        // GIVEN - Concrete calendar inputs and independent literal expectations.
        val cases = listOf(
            Case(LocalDate.parse("2026-10-20"), LocalDate.parse("2026-10-20"), false, "D-Day")
        )
        for (case in cases) {
            // WHEN - Call the production function with an explicit reference date.
            val result = dueDateLabel(case.dueDate, case.today, case.completed)
            // THEN - The oracle does not call the production code or repeat Period.days.
            assertEquals("today=${case.today}, dueDate=${case.dueDate}, completed=${case.completed}", case.expected, result)
        }
    }

    @Test
    fun dueDateLabel_dueYesterday_returnsDPlus1() {
        // GIVEN - Concrete calendar inputs and independent literal expectations.
        val cases = listOf(
            Case(LocalDate.parse("2026-10-19"), LocalDate.parse("2026-10-20"), false, "D+1")
        )
        for (case in cases) {
            // WHEN - Call the production function with an explicit reference date.
            val result = dueDateLabel(case.dueDate, case.today, case.completed)
            // THEN - The oracle does not call the production code or repeat Period.days.
            assertEquals("today=${case.today}, dueDate=${case.dueDate}, completed=${case.completed}", case.expected, result)
        }
    }

    @Test
    fun dueDateLabel_dueTomorrow_returnsDMinus1() {
        // GIVEN - Concrete calendar inputs and independent literal expectations.
        val cases = listOf(
            Case(LocalDate.parse("2026-10-21"), LocalDate.parse("2026-10-20"), false, "D-1")
        )
        for (case in cases) {
            // WHEN - Call the production function with an explicit reference date.
            val result = dueDateLabel(case.dueDate, case.today, case.completed)
            // THEN - The oracle does not call the production code or repeat Period.days.
            assertEquals("today=${case.today}, dueDate=${case.dueDate}, completed=${case.completed}", case.expected, result)
        }
    }

    @Test
    fun dueDateLabel_monthEnd_returnsAdjacentCalendarLabels() {
        // GIVEN - Concrete calendar inputs and independent literal expectations.
        val cases = listOf(
            Case(LocalDate.parse("2026-10-30"), LocalDate.parse("2026-10-31"), false, "D+1"),
            Case(LocalDate.parse("2026-10-31"), LocalDate.parse("2026-10-31"), false, "D-Day"),
            Case(LocalDate.parse("2026-11-01"), LocalDate.parse("2026-10-31"), false, "D-1")
        )
        for (case in cases) {
            // WHEN - Call the production function with an explicit reference date.
            val result = dueDateLabel(case.dueDate, case.today, case.completed)
            // THEN - The oracle does not call the production code or repeat Period.days.
            assertEquals("today=${case.today}, dueDate=${case.dueDate}, completed=${case.completed}", case.expected, result)
        }
    }

    @Test
    fun dueDateLabel_yearEnd_returnsAdjacentCalendarLabels() {
        // GIVEN - Concrete calendar inputs and independent literal expectations.
        val cases = listOf(
            Case(LocalDate.parse("2026-12-30"), LocalDate.parse("2026-12-31"), false, "D+1"),
            Case(LocalDate.parse("2026-12-31"), LocalDate.parse("2026-12-31"), false, "D-Day"),
            Case(LocalDate.parse("2027-01-01"), LocalDate.parse("2026-12-31"), false, "D-1")
        )
        for (case in cases) {
            // WHEN - Call the production function with an explicit reference date.
            val result = dueDateLabel(case.dueDate, case.today, case.completed)
            // THEN - The oracle does not call the production code or repeat Period.days.
            assertEquals("today=${case.today}, dueDate=${case.dueDate}, completed=${case.completed}", case.expected, result)
        }
    }

    @Test
    fun dueDateLabel_leapDay_returnsAdjacentCalendarLabels() {
        // GIVEN - Concrete calendar inputs and independent literal expectations.
        val cases = listOf(
            Case(LocalDate.parse("2028-02-28"), LocalDate.parse("2028-02-29"), false, "D+1"),
            Case(LocalDate.parse("2028-02-29"), LocalDate.parse("2028-02-29"), false, "D-Day"),
            Case(LocalDate.parse("2028-03-01"), LocalDate.parse("2028-02-29"), false, "D-1")
        )
        for (case in cases) {
            // WHEN - Call the production function with an explicit reference date.
            val result = dueDateLabel(case.dueDate, case.today, case.completed)
            // THEN - The oracle does not call the production code or repeat Period.days.
            assertEquals("today=${case.today}, dueDate=${case.dueDate}, completed=${case.completed}", case.expected, result)
        }
    }

    @Test
    fun dueDateLabel_fullMonthBoundary_returnsTotalCalendarDays() {
        // GIVEN - Concrete calendar inputs and independent literal expectations.
        val cases = listOf(
            Case(LocalDate.parse("2026-11-19"), LocalDate.parse("2026-10-20"), false, "D-30"),
            Case(LocalDate.parse("2026-11-20"), LocalDate.parse("2026-10-20"), false, "D-31"),
            Case(LocalDate.parse("2026-11-21"), LocalDate.parse("2026-10-20"), false, "D-32")
        )
        for (case in cases) {
            // WHEN - Call the production function with an explicit reference date.
            val result = dueDateLabel(case.dueDate, case.today, case.completed)
            // THEN - The oracle does not call the production code or repeat Period.days.
            assertEquals("today=${case.today}, dueDate=${case.dueDate}, completed=${case.completed}", case.expected, result)
        }
    }

    @Test
    fun dueDateLabel_previousMonthBoundary_returnsTotalCalendarDays() {
        // GIVEN - Concrete calendar inputs and independent literal expectations.
        val cases = listOf(
            Case(LocalDate.parse("2026-09-19"), LocalDate.parse("2026-10-20"), false, "D+31"),
            Case(LocalDate.parse("2026-09-20"), LocalDate.parse("2026-10-20"), false, "D+30"),
            Case(LocalDate.parse("2026-09-21"), LocalDate.parse("2026-10-20"), false, "D+29")
        )
        for (case in cases) {
            // WHEN - Call the production function with an explicit reference date.
            val result = dueDateLabel(case.dueDate, case.today, case.completed)
            // THEN - The oracle does not call the production code or repeat Period.days.
            assertEquals("today=${case.today}, dueDate=${case.dueDate}, completed=${case.completed}", case.expected, result)
        }
    }

    @Test
    fun dueDateLabel_fullYearBoundary_returnsTotalCalendarDays() {
        // GIVEN - Concrete calendar inputs and independent literal expectations.
        val cases = listOf(
            Case(LocalDate.parse("2027-10-19"), LocalDate.parse("2026-10-20"), false, "D-364"),
            Case(LocalDate.parse("2027-10-20"), LocalDate.parse("2026-10-20"), false, "D-365"),
            Case(LocalDate.parse("2027-10-21"), LocalDate.parse("2026-10-20"), false, "D-366")
        )
        for (case in cases) {
            // WHEN - Call the production function with an explicit reference date.
            val result = dueDateLabel(case.dueDate, case.today, case.completed)
            // THEN - The oracle does not call the production code or repeat Period.days.
            assertEquals("today=${case.today}, dueDate=${case.dueDate}, completed=${case.completed}", case.expected, result)
        }
    }

    @Test
    fun dueDateLabel_leapMonthBoundary_returnsTotalCalendarDays() {
        // GIVEN - Concrete calendar inputs and independent literal expectations.
        val cases = listOf(
            Case(LocalDate.parse("2028-02-28"), LocalDate.parse("2028-02-01"), false, "D-27"),
            Case(LocalDate.parse("2028-02-29"), LocalDate.parse("2028-02-01"), false, "D-28"),
            Case(LocalDate.parse("2028-03-01"), LocalDate.parse("2028-02-01"), false, "D-29")
        )
        for (case in cases) {
            // WHEN - Call the production function with an explicit reference date.
            val result = dueDateLabel(case.dueDate, case.today, case.completed)
            // THEN - The oracle does not call the production code or repeat Period.days.
            assertEquals("today=${case.today}, dueDate=${case.dueDate}, completed=${case.completed}", case.expected, result)
        }
    }

    @Test
    fun dueDateLabel_leapYearSpan_returnsDMinus366() {
        // GIVEN - Concrete calendar inputs and independent literal expectations.
        val cases = listOf(
            Case(LocalDate.parse("2025-01-01"), LocalDate.parse("2024-01-01"), false, "D-366")
        )
        for (case in cases) {
            // WHEN - Call the production function with an explicit reference date.
            val result = dueDateLabel(case.dueDate, case.today, case.completed)
            // THEN - The oracle does not call the production code or repeat Period.days.
            assertEquals("today=${case.today}, dueDate=${case.dueDate}, completed=${case.completed}", case.expected, result)
        }
    }

    @Test
    fun dueDateLabel_completedDateVariants_alwaysReturnsClear() {
        // GIVEN - Concrete calendar inputs and independent literal expectations.
        val cases = listOf(
            Case(LocalDate.parse("2026-09-20"), LocalDate.parse("2026-10-20"), true, "CLEAR"),
            Case(LocalDate.parse("2026-10-20"), LocalDate.parse("2026-10-20"), true, "CLEAR"),
            Case(LocalDate.parse("2026-11-20"), LocalDate.parse("2026-10-20"), true, "CLEAR"),
            Case(null, LocalDate.parse("2026-10-20"), true, "CLEAR")
        )
        for (case in cases) {
            // WHEN - Call the production function with an explicit reference date.
            val result = dueDateLabel(case.dueDate, case.today, case.completed)
            // THEN - The oracle does not call the production code or repeat Period.days.
            assertEquals("today=${case.today}, dueDate=${case.dueDate}, completed=${case.completed}", case.expected, result)
        }
    }
}
