# AGENTS.md

## Spec
- Start every spec by copying specs/TEMPLATE.spec.md to specs/<File>.spec.md. Fill it in; never restructure it.
- Write one section for every target function named in the request.
- Post-conditions are the test oracle: write them from intent, not from the code.
- Write exact input values and exact expected values (dates, strings) in every Tip.
- For every boundary, add Edge cases at the boundary and right next to it on both sides.
- End every post-condition with "Tested by <test>" or "Tip: Given … / When … / Then …".
- Before you finish, check the spec against these rules.

## Tests
- Write one test for every Tip: [Unit] in app/src/test, [Integration] in app/src/androidTest.
- Name tests subject_actionOrInput_result and use Given/When/Then.