# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `setDueDateLabel(textView: TextView, task: Task?)`

**Intent**: PDF 53쪽의 현재 할 일 상태를 TextView에 표시한다. 호출 이전 View의 내용에 의존하지 않고 매번 text와 visibility를 갱신하며, 빈 레이블은 이전 텍스트를 지운다. 빈 문자열일 때의 특정 visibility 값은 기능 의도가 정하지 않았으므로 강제하지 않는다.

**Pre-conditions**
- Android 메인 스레드의 유효한 TextView에 호출한다. task는 Task 또는 null이다.
- 새 View뿐 아니라 이전 바인딩 결과가 남은 동일 TextView에도 호출할 수 있다.
- 날짜 관련 테스트는 메인 스레드에서 기기 오늘 T를 한 번 읽고 같은 호출에서 T에 대한 상대 날짜를 사용한다. 예시의 절대 날짜와 동일한 날짜 관계를 유지한다.
- Task의 나머지 값은 title="Task", description="Description", id="task-1"이다.

**Post-conditions**
- Success cases
  - 완료된 항목은 마감일이 있어도 "CLEAR"를 표시한다. `[Integration]`
    - Tip: Given 기기 오늘=2026-10-20, dueDate=2026-10-22, isCompleted=true / When setDueDateLabel 호출 / Then text="CLEAR", visibility=View.VISIBLE(0). Tested by `TasksListBindingsTest.setDueDateLabel_completedWithDueDate_showsClear`.
  - 마감일 없는 완료 항목도 "CLEAR"를 표시한다. `[Integration]`
    - Tip: Given dueDate=null, isCompleted=true / When setDueDateLabel 호출 / Then text="CLEAR", visibility=View.VISIBLE(0). Tested by `TasksListBindingsTest.setDueDateLabel_completedWithoutDueDate_showsClear`.
  - 진행 중인 미래 마감 항목의 레이블을 표시한다. `[Integration]`
    - Tip: Given 기기 오늘=2026-10-20, dueDate=2026-10-22, isCompleted=false / When setDueDateLabel 호출 / Then text="D-2", visibility=View.VISIBLE(0). Tested by `TasksListBindingsTest.setDueDateLabel_activeDueInTwoDays_showsDMinus2`.
  - 진행 중인 지난 마감 항목의 레이블을 표시한다. `[Integration]`
    - Tip: Given 기기 오늘=2026-10-20, dueDate=2026-10-18, isCompleted=false / When setDueDateLabel 호출 / Then text="D+2", visibility=View.VISIBLE(0). Tested by `TasksListBindingsTest.setDueDateLabel_activeOverdueByTwoDays_showsDPlus2`.
  - 미완료이며 마감일이 없으면 text=""로 초기화하여 표시할 레이블을 없앤다. `[Integration]`
    - Tip: Given dueDate=null, isCompleted=false, 초기 text="", visibility=View.VISIBLE(0) / When 호출 / Then text=""; 빈 레이블의 특정 visibility 값은 요구하지 않는다. Tested by `TasksListBindingsTest.setDueDateLabel_activeWithoutDueDate_returnsEmptyLabel`.
  - task가 null이면 text=""로 초기화하여 표시할 레이블을 없앤다. `[Integration]`
    - Tip: Given task=null, 초기 text="", visibility=View.VISIBLE(0) / When 호출 / Then text=""; 빈 레이블의 특정 visibility 값은 요구하지 않는다. Tested by `TasksListBindingsTest.setDueDateLabel_nullTask_returnsEmptyLabel`.
- Error cases
  - None. 명시된 타입과 사전조건을 만족하는 입력에 별도의 오류 결과를 요구하지 않는다.
- Edge cases
  - 오늘 마감 레이블은 "D-Day"이다. `[Integration]`
    - Tip: Given 기기 오늘=2026-10-20, dueDate=2026-10-20, isCompleted=false / When setDueDateLabel 호출 / Then text="D-Day", visibility=View.VISIBLE(0). Tested by `TasksListBindingsTest.setDueDateLabel_dueToday_showsDDay`.
  - 바로 전날 마감 레이블은 "D+1"이다. `[Integration]`
    - Tip: Given 기기 오늘=2026-10-20, dueDate=2026-10-19, isCompleted=false / When setDueDateLabel 호출 / Then text="D+1", visibility=View.VISIBLE(0). Tested by `TasksListBindingsTest.setDueDateLabel_dueYesterday_showsDPlus1`.
  - 바로 다음 날 마감 레이블은 "D-1"이다. `[Integration]`
    - Tip: Given 기기 오늘=2026-10-20, dueDate=2026-10-21, isCompleted=false / When setDueDateLabel 호출 / Then text="D-1", visibility=View.VISIBLE(0). Tested by `TasksListBindingsTest.setDueDateLabel_dueTomorrow_showsDMinus1`.
  - 같은 View에서 마감일 없는 항목을 완료→미완료→완료로 바인딩하면 CLEAR→빈 레이블→CLEAR로 갱신한다. `[Integration]`
    - Tip: Given 같은 TextView와 dueDate=null, isCompleted 순서=[true,false,true] / When 차례대로 호출 / Then text=["CLEAR","","CLEAR"], 비어 있지 않은 두 결과는 visibility=View.VISIBLE(0). Tested by `TasksListBindingsTest.setDueDateLabel_completedWithoutDueDateThenActive_clearsLabel`.
  - 이전 레이블이 있던 View에 null을 바인딩해도 이전 텍스트를 제거한다. `[Integration]`
    - Tip: Given 같은 TextView, 첫 task=(dueDate=null,isCompleted=true), 다음 task=null / When 차례로 호출 / Then "CLEAR"/View.VISIBLE(0) 후 text="". Tested by `TasksListBindingsTest.setDueDateLabel_recycledViewBoundToNull_clearsOldLabel`.
  - 날짜가 있던 항목에서 마감일 없는 미완료 항목으로 재사용할 때 이전 레이블을 제거한다. `[Integration]`
    - Tip: Given 기기 오늘=2026-10-20, 같은 TextView, 첫 task=(dueDate=2026-10-22,isCompleted=false), 다음 task=(dueDate=null,isCompleted=false) / When 차례로 호출 / Then "D-2"/View.VISIBLE(0) 후 text="". Tested by `TasksListBindingsTest.setDueDateLabel_recycledViewBoundToActiveWithoutDueDate_clearsOldLabel`.
  - 숨겨져 있던 View도 표시할 레이블이 생기면 다시 보인다. `[Integration]`
    - Tip: Given 초기 text="OLD", visibility=View.GONE(8), task=(dueDate=null,isCompleted=true) / When 호출 / Then text="CLEAR", visibility=View.VISIBLE(0). Tested by `TasksListBindingsTest.setDueDateLabel_hiddenViewBoundToCompleted_showsClear`.
  - 날짜가 있는 항목은 같은 View에서 미완료→완료→미완료로 갱신한다. `[Integration]`
    - Tip: Given 기기 오늘=2026-10-20, dueDate=2026-10-21, isCompleted 순서=[false,true,false] / When 같은 View에 호출 / Then text=["D-1","CLEAR","D-1"], visibility는 모두 View.VISIBLE(0). Tested by `TasksListBindingsTest.setDueDateLabel_datedTaskCompletionRoundTrip_updatesLabel`.

**Undefined behavior** (optional)
- null TextView와 메인 스레드 밖의 View 조작은 호출 계약 밖이다.
- 날짜 예시의 기기 오늘은 2026-10-20이다. 실제 테스트에서는 같은 UI 스레드 블록에서 T=LocalDate.now()를 읽고 상대 날짜를 만들어 실행 날짜가 바뀌어도 기대 문자열이 유지되게 한다.
