# AGENTS.md

## Spec
- Write specs in specs/<File>.spec.md, following specs/TEMPLATE.spec.md.
- Post-conditions are the test oracle: write them from intent, not from the code.
- End every post-condition with "Tested by <test>" or "Tip: Given … / When … / Then …".

## Tests
- Write one test for every Tip: [Unit] in app/src/test, [Integration] in app/src/androidTest.
- Name tests subject_actionOrInput_result and use Given/When/Then.