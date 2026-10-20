# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: PDF 53쪽의 D-day 규칙에 따라 완료 상태를 우선하고, 미완료 항목은 전체 달력 일수의 부호와 크기로 레이블을 반환한다. 월/연/윤일의 날짜 성분이나 나머지를 일수로 사용하지 않는다.

**Pre-conditions**
- today와 dueDate는 유효한 LocalDate이며 dueDate만 null을 허용한다.
- 모든 날짜 기대값은 명시된 today에 대한 독립적인 달력 계산 결과이다.

**Post-conditions**
- Success cases
  - 완료된 할 일은 마감일 유무와 무관하게 "CLEAR"를 반환한다. `[Unit]`
    - Tip: Given today=2026-10-20, dueDate=2026-10-22, isCompleted=true / When dueDateLabel 호출 / Then "CLEAR". Tested by `DueDateUtilsTest.dueDateLabel_completedWithDueDate_returnsClear`.
  - 완료된 할 일의 마감일이 비어 있어도 "CLEAR"를 반환한다. `[Unit]`
    - Tip: Given today=2026-10-20, dueDate=null, isCompleted=true / When dueDateLabel 호출 / Then "CLEAR". Tested by `DueDateUtilsTest.dueDateLabel_completedWithoutDueDate_returnsClear`.
  - 진행 중이며 이틀 뒤 마감이면 "D-2"를 반환한다. `[Unit]`
    - Tip: Given today=2026-10-20, dueDate=2026-10-22, isCompleted=false / When dueDateLabel 호출 / Then "D-2". Tested by `DueDateUtilsTest.dueDateLabel_activeDueInTwoDays_returnsDMinus2`.
  - 진행 중이며 이틀 지났으면 "D+2"를 반환한다. `[Unit]`
    - Tip: Given today=2026-10-20, dueDate=2026-10-18, isCompleted=false / When dueDateLabel 호출 / Then "D+2". Tested by `DueDateUtilsTest.dueDateLabel_activeOverdueByTwoDays_returnsDPlus2`.
  - 진행 중이며 마감일이 없으면 빈 문자열을 반환한다. `[Unit]`
    - Tip: Given today=2026-10-20, dueDate=null, isCompleted=false / When dueDateLabel 호출 / Then "". Tested by `DueDateUtilsTest.dueDateLabel_activeWithoutDueDate_returnsEmpty`.
  - 완료 상태는 마감일이 과거/오늘/미래/null인 모든 경우보다 우선한다. `[Unit]`
    - Tip: Given today=2026-10-20, dueDate=[2026-09-20, 2026-10-20, 2026-11-20, null], isCompleted=true / When 각 값으로 호출 / Then 모두 "CLEAR". Tested by `DueDateUtilsTest.dueDateLabel_completedDateVariants_alwaysReturnsClear`.
- Error cases
  - None. 명시된 타입과 사전조건을 만족하는 입력에 별도의 오류 결과를 요구하지 않는다.
- Edge cases
  - 오늘 마감이면 정확히 "D-Day"를 반환한다. `[Unit]`
    - Tip: Given today=2026-10-20, dueDate=2026-10-20, isCompleted=false / When dueDateLabel 호출 / Then "D-Day". Tested by `DueDateUtilsTest.dueDateLabel_dueToday_returnsDDay`.
  - 오늘의 바로 이전 날짜는 "D+1"이다. `[Unit]`
    - Tip: Given today=2026-10-20, dueDate=2026-10-19, isCompleted=false / When dueDateLabel 호출 / Then "D+1". Tested by `DueDateUtilsTest.dueDateLabel_dueYesterday_returnsDPlus1`.
  - 오늘의 바로 다음 날짜는 "D-1"이다. `[Unit]`
    - Tip: Given today=2026-10-20, dueDate=2026-10-21, isCompleted=false / When dueDateLabel 호출 / Then "D-1". Tested by `DueDateUtilsTest.dueDateLabel_dueTomorrow_returnsDMinus1`.
  - 월말 경계의 전날, 당일, 다음 날을 구분한다. `[Unit]`
    - Tip: Given today=2026-10-31, dueDate=[2026-10-30, 2026-10-31, 2026-11-01], isCompleted=false / When 각 날짜로 dueDateLabel 호출 / Then 각각 ["D+1", "D-Day", "D-1"]. Tested by `DueDateUtilsTest.dueDateLabel_monthEnd_returnsAdjacentCalendarLabels`.
  - 연말 경계의 전날, 당일, 다음 날을 구분한다. `[Unit]`
    - Tip: Given today=2026-12-31, dueDate=[2026-12-30, 2026-12-31, 2027-01-01], isCompleted=false / When 각 날짜로 dueDateLabel 호출 / Then 각각 ["D+1", "D-Day", "D-1"]. Tested by `DueDateUtilsTest.dueDateLabel_yearEnd_returnsAdjacentCalendarLabels`.
  - 윤일 경계의 전날, 당일, 다음 날을 구분한다. `[Unit]`
    - Tip: Given today=2028-02-29, dueDate=[2028-02-28, 2028-02-29, 2028-03-01], isCompleted=false / When 각 날짜로 dueDateLabel 호출 / Then 각각 ["D+1", "D-Day", "D-1"]. Tested by `DueDateUtilsTest.dueDateLabel_leapDay_returnsAdjacentCalendarLabels`.
  - 완전한 한 달을 포함한 미래 일수는 날짜 성분의 나머지가 아니라 전체 일수이다. `[Unit]`
    - Tip: Given today=2026-10-20, dueDate=[2026-11-19, 2026-11-20, 2026-11-21], isCompleted=false / When 각 날짜로 호출 / Then 각각 ["D-30", "D-31", "D-32"]. Tested by `DueDateUtilsTest.dueDateLabel_fullMonthBoundary_returnsTotalCalendarDays`.
  - 한 달 전의 경계 양쪽에서도 전체 경과 일수를 사용한다. `[Unit]`
    - Tip: Given today=2026-10-20, dueDate=[2026-09-19, 2026-09-20, 2026-09-21], isCompleted=false / When 각 날짜로 호출 / Then 각각 ["D+31", "D+30", "D+29"]. Tested by `DueDateUtilsTest.dueDateLabel_previousMonthBoundary_returnsTotalCalendarDays`.
  - 완전한 일 년과 바로 이전/다음 날짜도 전체 일수로 계산한다. `[Unit]`
    - Tip: Given today=2026-10-20, dueDate=[2027-10-19, 2027-10-20, 2027-10-21], isCompleted=false / When 각 날짜로 호출 / Then 각각 ["D-364", "D-365", "D-366"]. Tested by `DueDateUtilsTest.dueDateLabel_fullYearBoundary_returnsTotalCalendarDays`.
  - 윤년 2월의 마지막 날과 3월 시작도 누적 일수로 계산한다. `[Unit]`
    - Tip: Given today=2028-02-01, dueDate=[2028-02-28, 2028-02-29, 2028-03-01], isCompleted=false / When 각 날짜로 호출 / Then 각각 ["D-27", "D-28", "D-29"]. Tested by `DueDateUtilsTest.dueDateLabel_leapMonthBoundary_returnsTotalCalendarDays`.
  - 윤일을 포함한 일 년은 366일이다. `[Unit]`
    - Tip: Given today=2024-01-01, dueDate=2025-01-01, isCompleted=false / When 호출 / Then "D-366". Tested by `DueDateUtilsTest.dueDateLabel_leapYearSpan_returnsDMinus366`.

**Undefined behavior** (optional)
- 유효하지 않은 날짜의 LocalDate 생성 실패와 null today의 강제 주입은 호출 계약 밖이다.
