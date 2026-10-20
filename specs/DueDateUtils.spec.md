# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: 할 일의 완료 여부와 마감일을 D-day 문자열로 표현한다. 날짜 차이는 월과 연도를 포함한 전체 달력 일수이며 기대값은 PDF 53쪽의 기능 의도에서 정한다.

**Pre-conditions**
- today는 유효한 LocalDate이며 dueDate는 유효한 LocalDate 또는 null이다.
- isCompleted는 Boolean이다. 함수는 전달받은 today를 기준으로 계산한다.

**Post-conditions**
- Success cases
  - 완료된 할 일은 마감일 유무와 무관하게 "CLEAR"를 반환한다. `[Unit]`
    - Tip: Given today=2026-10-20, dueDate=2026-10-22, isCompleted=true / When dueDateLabel 호출 / Then "CLEAR".
  - 완료된 할 일의 마감일이 비어 있어도 "CLEAR"를 반환한다. `[Unit]`
    - Tip: Given today=2026-10-20, dueDate=null, isCompleted=true / When dueDateLabel 호출 / Then "CLEAR".
  - 진행 중이며 이틀 뒤 마감이면 "D-2"를 반환한다. `[Unit]`
    - Tip: Given today=2026-10-20, dueDate=2026-10-22, isCompleted=false / When dueDateLabel 호출 / Then "D-2".
  - 진행 중이며 이틀 지났으면 "D+2"를 반환한다. `[Unit]`
    - Tip: Given today=2026-10-20, dueDate=2026-10-18, isCompleted=false / When dueDateLabel 호출 / Then "D+2".
  - 진행 중이며 마감일이 없으면 빈 문자열을 반환한다. `[Unit]`
    - Tip: Given today=2026-10-20, dueDate=null, isCompleted=false / When dueDateLabel 호출 / Then "".
- Error cases
  - None. 명시된 타입과 사전조건을 만족하는 입력에 별도의 오류 결과를 요구하지 않는다.
- Edge cases
  - 오늘 마감이면 정확히 "D-Day"를 반환한다. `[Unit]`
    - Tip: Given today=2026-10-20, dueDate=2026-10-20, isCompleted=false / When dueDateLabel 호출 / Then "D-Day".
  - 오늘의 바로 이전 날짜는 "D+1"이다. `[Unit]`
    - Tip: Given today=2026-10-20, dueDate=2026-10-19, isCompleted=false / When dueDateLabel 호출 / Then "D+1".
  - 오늘의 바로 다음 날짜는 "D-1"이다. `[Unit]`
    - Tip: Given today=2026-10-20, dueDate=2026-10-21, isCompleted=false / When dueDateLabel 호출 / Then "D-1".
  - 월말 경계의 전날, 당일, 다음 날을 구분한다. `[Unit]`
    - Tip: Given today=2026-10-31, dueDate=[2026-10-30, 2026-10-31, 2026-11-01], isCompleted=false / When 각 날짜로 dueDateLabel 호출 / Then 각각 ["D+1", "D-Day", "D-1"].
  - 연말 경계의 전날, 당일, 다음 날을 구분한다. `[Unit]`
    - Tip: Given today=2026-12-31, dueDate=[2026-12-30, 2026-12-31, 2027-01-01], isCompleted=false / When 각 날짜로 dueDateLabel 호출 / Then 각각 ["D+1", "D-Day", "D-1"].
  - 윤일 경계의 전날, 당일, 다음 날을 구분한다. `[Unit]`
    - Tip: Given today=2028-02-29, dueDate=[2028-02-28, 2028-02-29, 2028-03-01], isCompleted=false / When 각 날짜로 dueDateLabel 호출 / Then 각각 ["D+1", "D-Day", "D-1"].

**Undefined behavior** (optional)
- 존재하지 않는 날짜를 LocalDate로 생성하는 실패는 이 함수 호출 이전의 범위이다.
