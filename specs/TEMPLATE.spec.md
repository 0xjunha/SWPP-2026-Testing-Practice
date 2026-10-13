# Spec: `<File>.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `<function signature>`

**Intent**: <What the function is supposed to do, in one or two sentences.>

**Pre-conditions**
- <What must hold before the call.>

**Post-conditions**
- Success cases
  - <Result of a direct call for one kind of valid input. Add one per kind, including optional values left empty.> `[Unit | Integration]`
    - Tested by `<TestClass.subject_actionOrInput_result>` | Tip: Given … / When … / Then …
- Error cases
  - <Result of a direct call for input the function must reject. Write "None." if every input is valid.> `[Unit | Integration]`
    - Tested by `<TestClass.subject_actionOrInput_result>` | Tip: Given … / When … / Then …
- Edge cases
  - <Result of a direct call at, and right next to, each boundary between the Success cases.> `[Unit | Integration]`
    - Tested by `<TestClass.subject_actionOrInput_result>` | Tip: Given … / When … / Then …

**Undefined behavior** (optional)
- <Input left undefined, and why.>
