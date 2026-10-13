# Spec: `<File>.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> End each one with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `<function signature>`

**Intent**: <What the function is supposed to do, in one or two sentences.>

**Pre-conditions**
- <What must hold before the call.>

**Post-conditions**
- Success cases
  - <Result for valid input.> `[Unit]`
    - Tested by `<TestClass.subject_actionOrInput_result>`
- Error cases
  - <Result for invalid or missing input.> `[Unit]`
    - Tip: Given … / When … / Then …
- Edge cases
  - <Result at boundary values.> `[Unit]`
    - Tip: Given … / When … / Then …
- State cases
  - <What saved data or the screen must look like after the call.> `[Integration]`
    - Tip: Given … / When … / Then …

**Undefined behavior** (optional)
- <Input left undefined, and why.>
