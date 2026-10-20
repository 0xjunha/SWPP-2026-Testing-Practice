package com.example.swpp.tododapp.tasks

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.swpp.tododapp.data.Task
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicReference
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class TasksListBindingsTest {
    private fun freshView(): TextView = TextView(ApplicationProvider.getApplicationContext<Context>())

    private fun task(due: LocalDate?, completed: Boolean = false): Task =
        Task("Task", "Description", completed, "task-1", due)

    private fun assertLabel(view: TextView, text: String, visibility: Int) {
        assertEquals("TextView.text", text, view.text.toString())
        assertEquals("TextView.visibility", visibility, view.visibility)
    }

    private fun onMain(block: () -> Unit) {
        // Propagate assertions to JUnit instead of crashing the instrumentation main thread.
        val failure = AtomicReference<Throwable?>()
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            try {
                block()
            } catch (error: Throwable) {
                failure.set(error)
            }
        }
        failure.get()?.let { throw it }
    }

    @Test
    fun setDueDateLabel_completedWithDueDate_showsClear() = onMain {
        // GIVEN - Real Android TextView; reference date captured on the same UI thread.
        val today = LocalDate.now()
        val view = freshView()
        // WHEN - Bind a completed task.
        setDueDateLabel(view, task(today.plusDays(2), true))
        // THEN - Completion takes precedence over the due date.
        assertLabel(view, "CLEAR", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_completedWithoutDueDate_showsClear() = onMain {
        // GIVEN - Real Android TextView; reference date captured on the same UI thread.
        val view = freshView()
        // WHEN
        setDueDateLabel(view, task(null, true))
        // THEN
        assertLabel(view, "CLEAR", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_activeDueInTwoDays_showsDMinus2() = onMain {
        // GIVEN - Real Android TextView; reference date captured on the same UI thread.
        val today = LocalDate.now()
        val view = freshView()
        // WHEN
        setDueDateLabel(view, task(today.plusDays(2)))
        // THEN
        assertLabel(view, "D-2", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_activeOverdueByTwoDays_showsDPlus2() = onMain {
        // GIVEN - Real Android TextView; reference date captured on the same UI thread.
        val today = LocalDate.now()
        val view = freshView()
        // WHEN
        setDueDateLabel(view, task(today.minusDays(2)))
        // THEN
        assertLabel(view, "D+2", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_activeWithoutDueDate_clearsAndHides() = onMain {
        // GIVEN - Real Android TextView; reference date captured on the same UI thread.
        val view = freshView()
        // WHEN
        setDueDateLabel(view, task(null))
        // THEN
        assertLabel(view, "", View.GONE)
    }

    @Test
    fun setDueDateLabel_nullTask_clearsAndHides() = onMain {
        // GIVEN - Real Android TextView; reference date captured on the same UI thread.
        val view = freshView()
        // WHEN
        setDueDateLabel(view, null)
        // THEN
        assertLabel(view, "", View.GONE)
    }

    @Test
    fun setDueDateLabel_dueToday_showsDDay() = onMain {
        // GIVEN - Real Android TextView; reference date captured on the same UI thread.
        val today = LocalDate.now()
        val view = freshView()
        // WHEN
        setDueDateLabel(view, task(today.plusDays(0)))
        // THEN
        assertLabel(view, "D-Day", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_dueYesterday_showsDPlus1() = onMain {
        // GIVEN - Real Android TextView; reference date captured on the same UI thread.
        val today = LocalDate.now()
        val view = freshView()
        // WHEN
        setDueDateLabel(view, task(today.plusDays(-1)))
        // THEN
        assertLabel(view, "D+1", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_dueTomorrow_showsDMinus1() = onMain {
        // GIVEN - Real Android TextView; reference date captured on the same UI thread.
        val today = LocalDate.now()
        val view = freshView()
        // WHEN
        setDueDateLabel(view, task(today.plusDays(1)))
        // THEN
        assertLabel(view, "D-1", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_completedWithoutDueDateThenActive_clearsLabel() = onMain {
        // GIVEN - Real Android TextView; reference date captured on the same UI thread.
        val view = freshView()
        val completed = task(null, true)
        // WHEN / THEN - Rebind the same view across both completion boundaries.
        setDueDateLabel(view, completed)
        assertLabel(view, "CLEAR", View.VISIBLE)
        setDueDateLabel(view, completed.copy(isCompleted = false))
        assertLabel(view, "", View.GONE)
        setDueDateLabel(view, completed)
        assertLabel(view, "CLEAR", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_recycledViewBoundToNull_clearsOldLabel() = onMain {
        // GIVEN - Real Android TextView; reference date captured on the same UI thread.
        val view = freshView()
        // WHEN / THEN - Recycling must not retain the prior task's label.
        setDueDateLabel(view, task(null, true))
        assertLabel(view, "CLEAR", View.VISIBLE)
        setDueDateLabel(view, null)
        assertLabel(view, "", View.GONE)
    }

    @Test
    fun setDueDateLabel_recycledViewBoundToActiveWithoutDueDate_clearsOldLabel() = onMain {
        // GIVEN - Real Android TextView; reference date captured on the same UI thread.
        val today = LocalDate.now()
        val view = freshView()
        // WHEN / THEN
        setDueDateLabel(view, task(today.plusDays(2)))
        assertLabel(view, "D-2", View.VISIBLE)
        setDueDateLabel(view, task(null))
        assertLabel(view, "", View.GONE)
    }

    @Test
    fun setDueDateLabel_hiddenViewBoundToCompleted_showsClear() = onMain {
        // GIVEN - Real Android TextView; reference date captured on the same UI thread.
        val view = freshView().apply { text = "OLD"; visibility = View.GONE }
        // WHEN
        setDueDateLabel(view, task(null, true))
        // THEN
        assertLabel(view, "CLEAR", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_datedTaskCompletionRoundTrip_updatesLabel() = onMain {
        // GIVEN - Real Android TextView; reference date captured on the same UI thread.
        val today = LocalDate.now()
        val view = freshView()
        val active = task(today.plusDays(1))
        // WHEN / THEN
        setDueDateLabel(view, active)
        assertLabel(view, "D-1", View.VISIBLE)
        setDueDateLabel(view, active.copy(isCompleted = true))
        assertLabel(view, "CLEAR", View.VISIBLE)
        setDueDateLabel(view, active)
        assertLabel(view, "D-1", View.VISIBLE)
    }
}
