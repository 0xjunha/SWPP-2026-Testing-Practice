# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `setDueDateLabel(textView: TextView, task: Task?)`

**Intent**: 할 일의 현재 D-day 레이블을 TextView에 반영한다. 완료 여부를 바꾸면 새 상태에 맞는 레이블을 표시한다.

**Pre-conditions**
- Android 메인 스레드의 유효한 TextView에 호출한다. task는 Task 또는 null이다.
- 각 직접 호출은 text=""인 새 TextView로 시작한다. 날짜 예시는 기기의 오늘이 2026-10-20인 경우이다.
- 날짜 관련 테스트는 메인 스레드에서 기기 오늘 T를 한 번 읽고 같은 호출에서 T에 대한 상대 날짜를 사용한다. 예시의 절대 날짜와 동일한 날짜 관계를 유지한다.
- Task의 나머지 값은 title="Task", description="Description", id="task-1"이다.

**Post-conditions**
- Success cases
  - 완료된 항목은 마감일이 있어도 "CLEAR"를 표시한다. `[Integration]`
    - Tip: Given 기기 오늘=2026-10-20, dueDate=2026-10-22, isCompleted=true / When setDueDateLabel 호출 / Then text="CLEAR", visibility=View.VISIBLE(0).
  - 마감일 없는 완료 항목도 "CLEAR"를 표시한다. `[Integration]`
    - Tip: Given dueDate=null, isCompleted=true / When setDueDateLabel 호출 / Then text="CLEAR", visibility=View.VISIBLE(0).
  - 진행 중인 미래 마감 항목의 레이블을 표시한다. `[Integration]`
    - Tip: Given 기기 오늘=2026-10-20, dueDate=2026-10-22, isCompleted=false / When setDueDateLabel 호출 / Then text="D-2", visibility=View.VISIBLE(0).
  - 진행 중인 지난 마감 항목의 레이블을 표시한다. `[Integration]`
    - Tip: Given 기기 오늘=2026-10-20, dueDate=2026-10-18, isCompleted=false / When setDueDateLabel 호출 / Then text="D+2", visibility=View.VISIBLE(0).
  - 진행 중이며 마감일이 없으면 빈 레이블이다. `[Integration]`
    - Tip: Given dueDate=null, isCompleted=false, 초기 text="" / When setDueDateLabel 호출 / Then text="".
  - task가 null이면 빈 레이블이다. `[Integration]`
    - Tip: Given task=null, 초기 text="" / When setDueDateLabel 호출 / Then text="".
- Error cases
  - None. 명시된 타입과 사전조건을 만족하는 입력에 별도의 오류 결과를 요구하지 않는다.
- Edge cases
  - 오늘 마감 레이블은 "D-Day"이다. `[Integration]`
    - Tip: Given 기기 오늘=2026-10-20, dueDate=2026-10-20, isCompleted=false / When setDueDateLabel 호출 / Then text="D-Day", visibility=View.VISIBLE(0).
  - 바로 전날 마감 레이블은 "D+1"이다. `[Integration]`
    - Tip: Given 기기 오늘=2026-10-20, dueDate=2026-10-19, isCompleted=false / When setDueDateLabel 호출 / Then text="D+1", visibility=View.VISIBLE(0).
  - 바로 다음 날 마감 레이블은 "D-1"이다. `[Integration]`
    - Tip: Given 기기 오늘=2026-10-20, dueDate=2026-10-21, isCompleted=false / When setDueDateLabel 호출 / Then text="D-1", visibility=View.VISIBLE(0).

**Undefined behavior** (optional)
- null TextView나 메인 스레드 밖의 View 접근은 호출 계약 밖이다.
