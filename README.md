# Laboratory 02 – Algorithm Benchmarking: Finding Duplicates

**Algorithms · Week 2 · Information Systems Engineering – S04-002**

**Team members:** Merino Miguel – Narváez Camila

## Problem

Determine whether a list of integers contains repeated values, comparing two correct solutions that produce the same answer.

| Algorithm | Strategy | Class |
|---|---|---|
| A | **Double loop:** compares each element with all the following ones | `Algorithm_A.java` |
| B | **Auxiliary structure:** stores the values already seen in an array and searches each new value in it | `Algorithm_B.java` |

## How to run

Each class is independent and has its own `main` method:

1. Open the project in IntelliJ IDEA.
2. Right-click `Algorithm_A` → **Run**.
3. Right-click `Algorithm_B` → **Run**.

Each program prints its results tables in the console. The case of n = 1,000,000 without duplicates takes several minutes; to skip it, set `RUN_WORST_CASE_ONE_MILLION` to `false`.

## Methodology (8 steps)

| Step | Implementation |
|---|---|
| 01 Generate datasets | `generateNoDuplicates()` and `generateEarlyDuplicate()`: numbers from 1 to n shuffled with *Fisher-Yates* and a fixed seed (`42 + n`), so that A and B receive exactly the same data |
| 02 Run algorithm A | `Algorithm_A.hasDuplicates()` |
| 03 Run algorithm B | `Algorithm_B.hasDuplicates()` |
| 04 Count operations | `countOperations()`: adds 1 for every comparison between two values |
| 05 Measure time | `System.nanoTime()` before and after each execution of `hasDuplicates()`; result in milliseconds |
| 06 Repeat tests | Each measurement is repeated 3 times and the average is reported |
| 07 Record table | Tables in the console and in `Lab02_Comparativo.xlsx` |
| 08 Technical conclusions | **Conclusions** section of this README |

Two scenarios were evaluated, based on the aspects to compare in the guide case:

- **Early duplicate (best case):** the second element repeats the first one.
- **No duplicates (worst case):** all values are different, so the algorithm must check the entire list.

Sizes: `n = [100, 1_000, 10_000, 100_000, 1_000_000]`

## Results

> Times depend on the computer where the program runs. The number of operations is exact and does not change between computers. The full table, with the 3 tests of each measurement and charts, is in `Lab02_Comparativo.xlsx`.

### Scenario 1: early duplicate (best case)

| Algorithm | n | Operations | Average time (ms) |
|---|---|---|---|
| A | 100 | 1 | 0.0004 |
| A | 1,000 | 1 | 0.0004 |
| A | 10,000 | 1 | 0.0003 |
| A | 100,000 | 1 | 0.0002 |
| A | 1,000,000 | 1 | 0.0004 |
| B | 100 | 1 | 0.0008 |
| B | 1,000 | 1 | 0.0024 |
| B | 10,000 | 1 | 0.0085 |
| B | 100,000 | 1 | 0.4811 |
| B | 1,000,000 | 1 | 1.0755 |

### Scenario 2: no duplicates (worst case)

| Algorithm | n | Operations | Average time (ms) |
|---|---|---|---|
| A | 100 | 4,950 | 0.0868 |
| A | 1,000 | 499,500 | 1.4054 |
| A | 10,000 | 49,995,000 | 15.8128 |
| A | 100,000 | 4,999,950,000 | 740.6575 |
| A | 1,000,000 | 499,999,500,000 | 75,682.6468 |
| B | 100 | 4,950 | 0.0364 |
| B | 1,000 | 499,500 | 0.6763 |
| B | 10,000 | 49,995,000 | 7.7944 |
| B | 100,000 | 4,999,950,000 | 764.1391 |
| B | 1,000,000 | 499,999,500,000 | 86,776.3323 |

## Conclusions

1. **Both algorithms are correct:** with the same data, they return the same answer (`true` with an early duplicate and `false` without duplicates).

2. **In the worst case, both have O(n²) complexity.** The operations follow exactly the formula n(n−1)/2: when n is multiplied by 10, the operations are multiplied by 100 (4,950 → 499,500 → 49,995,000 → 4,999,950,000 → 499,999,500,000). Time grows in the same proportion for large inputs: from n = 100,000 to n = 1,000,000, A went from ≈741 ms to ≈75,683 ms (≈102 times more) and B from ≈764 ms to ≈86,776 ms (≈114 times more). For small n the ratio is less regular because the measured times are very short and more affected by the computer's activity.

3. **A and B perform exactly the same number of comparisons.** B's auxiliary structure is an array that is traversed sequentially, so checking whether a value "has already been seen" requires comparing it with all the previous ones, just like A. B does not reduce the work; it only reorganizes it. With large inputs, B is even slightly slower (≈86.8 s vs. ≈75.7 s with n = 1,000,000) because of the extra cost of writing each value into the array. For small n, the differences (for example, B faster with n = 10,000) are due to variations of the computer.

4. **In the best case (early duplicate), both perform only 1 comparison,** that is, O(1). Even so, B's time grows with n (from 0.0008 ms to 1.08 ms), because before comparing it must create the `seen` array of size n, and allocating and initializing that memory has a cost proportional to n. A stays practically at 0 ms for every size.

5. **Memory used:** A needs no additional memory, only control variables (O(1)). B allocates an array of n integers (O(n)): about 4 MB with n = 1,000,000 (4 bytes × n).

6. **Measurement variability:** some isolated tests were much higher or lower than the others (for example, B with n = 100,000 in the best case: 1.3966 ms in test 1 versus ≈0.02 ms in the others; A with n = 1,000 in the worst case: 0.4264 ms in test 2 versus ≈1.89 ms in the others), due to Java's automatic optimization (JIT) and other processes running on the computer. That is why each measurement was repeated 3 times and the average is reported.

7. **Answer to the guide case question (n = 1,000,000):** neither of them scales well. In the worst case, both need about 500,000 million comparisons: each run took between ≈73 and ≈81 seconds with A and between ≈83 and ≈89 seconds with B. Between the two, **A is preferable**: it performs the same comparisons, was faster with large inputs, uses no extra memory, and does not pay the cost of creating the auxiliary array. For an auxiliary structure to actually improve performance, it must be able to answer "have I seen this value already?" without traversing itself completely (for example, a hash-based set or a marker array indexed by value), which would reduce the algorithm to O(n).
