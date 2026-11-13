# Laboratorio 02 – Benchmarking de algoritmos: buscar duplicados

**Algoritmos · Semana 2 · Ingeniería en Sistemas de Información – Cuarto semestre (UCE)**

## Problema

Determinar si una lista de enteros contiene valores repetidos, comparando dos soluciones correctas que producen la misma respuesta.

| Algoritmo | Estrategia | Clase |
|---|---|---|
| A | **Doble recorrido:** compara cada elemento con todos los siguientes | `Algorithm_A.java` |
| B | **Estructura auxiliar:** guarda en un arreglo los valores ya vistos y busca cada nuevo valor en él | `Algorithm_B.java` |

## Cómo ejecutar

Cada clase es independiente y tiene su propio `main`:

1. Abrir el proyecto en IntelliJ IDEA.
2. Clic derecho en `Algorithm_A` → **Run**.
3. Clic derecho en `Algorithm_B` → **Run**.

Cada programa imprime sus tablas de resultados en la consola. El caso de n = 1.000.000 sin duplicados tarda varios minutos; para omitirlo, cambiar `RUN_WORST_CASE_ONE_MILLION` a `false`.

## Metodología (8 pasos)

| Paso | Implementación |
|---|---|
| 01 Generar datasets | `generateNoDuplicates()` y `generateEarlyDuplicate()`: números del 1 al n mezclados con *Fisher-Yates* y semilla fija (`42 + n`), para que A y B reciban exactamente los mismos datos |
| 02 Ejecutar algoritmo A | `Algorithm_A.hasDuplicates()` |
| 03 Ejecutar algoritmo B | `Algorithm_B.hasDuplicates()` |
| 04 Contar operaciones | `countOperations()`: suma 1 por cada comparación entre dos valores |
| 05 Medir tiempo | `System.nanoTime()` antes y después de cada ejecución de `hasDuplicates()`; resultado en milisegundos |
| 06 Repetir pruebas | Cada medición se repite 3 veces y se reporta el promedio |
| 07 Registrar tabla | Tablas en consola y en `Lab02_Comparativo.xlsx` |
| 08 Concluir técnicamente | Sección **Conclusiones** de este README |

Se evaluaron dos escenarios, según los aspectos a comparar del caso guía:

- **Duplicado temprano (mejor caso):** el segundo elemento repite al primero.
- **Sin duplicados (peor caso):** todos los valores son distintos, así que el algoritmo debe revisar toda la lista.

Tamaños: `n = [100, 1_000, 10_000, 100_000, 1_000_000]`

## Resultados

> Los tiempos dependen del equipo donde se ejecuta. El número de operaciones es exacto y no cambia entre equipos. La tabla completa, con las 3 pruebas de cada medición y gráficos, está en `Lab02_Comparativo.xlsx`.

### Escenario 1: duplicado temprano (mejor caso)

| Algoritmo | n | Operaciones | Tiempo promedio (ms) |
|---|---|---|---|
| A | 100 | 1 | 0,0002 |
| A | 1.000 | 1 | 0,0001 |
| A | 10.000 | 1 | 0,0001 |
| A | 100.000 | 1 | 0,0002 |
| A | 1.000.000 | 1 | 0,0003 |
| B | 100 | 1 | 0,0003 |
| B | 1.000 | 1 | 0,0019 |
| B | 10.000 | 1 | 0,0079 |
| B | 100.000 | 1 | 0,4901 |
| B | 1.000.000 | 1 | 0,8352 |

### Escenario 2: sin duplicados (peor caso)

| Algoritmo | n | Operaciones | Tiempo promedio (ms) |
|---|---|---|---|
| A | 100 | 4.950 | 0,0342 |
| A | 1.000 | 499.500 | 1,4607 |
| A | 10.000 | 49.995.000 | 6,5928 |
| A | 100.000 | 4.999.950.000 | 662,3929 |
| A | 1.000.000 | 499.999.500.000 | COMPLETAR |
| B | 100 | 4.950 | 0,0347 |
| B | 1.000 | 499.500 | 0,6294 |
| B | 10.000 | 49.995.000 | 7,1151 |
| B | 100.000 | 4.999.950.000 | 834,6961 |
| B | 1.000.000 | 499.999.500.000 | COMPLETAR |

## Conclusiones

1. **Ambos algoritmos son correctos:** con los mismos datos devuelven la misma respuesta (`true` con duplicado temprano y `false` sin duplicados).

2. **En el peor caso, ambos tienen complejidad O(n²).** Las operaciones siguen exactamente la fórmula n(n−1)/2: cuando n se multiplica por 10, las operaciones se multiplican por 100 (4.950 → 499.500 → 49.995.000 → 4.999.950.000 → 499.999.500.000). El tiempo crece en la misma proporción: A pasó de ≈6,6 ms con n = 10.000 a ≈662 ms con n = 100.000 (≈100 veces más) y a ≈COMPLETAR ms con n = 1.000.000.

3. **A y B hacen exactamente la misma cantidad de comparaciones.** La estructura auxiliar de B es un arreglo que se recorre de forma secuencial, así que comprobar si un valor "ya fue visto" exige compararlo con todos los anteriores, igual que A. B no reduce el trabajo; solo lo reorganiza. Por eso sus tiempos en el peor caso son similares; las diferencias se deben al costo adicional de escribir en el arreglo y a variaciones del equipo.

4. **En el mejor caso (duplicado temprano), ambos hacen 1 sola comparación,** es decir O(1). Aun así, el tiempo de B crece con n (de 0,0003 ms a 0,84 ms), porque antes de comparar debe crear el arreglo `seen` de tamaño n, y reservar e inicializar esa memoria tiene un costo proporcional a n. A se mantiene prácticamente en 0 ms.

5. **Memoria usada:** A no necesita memoria adicional, solo variables de control (O(1)). B reserva un arreglo de n enteros (O(n)): unos 4 MB con n = 1.000.000 (4 bytes × n).

6. **Variabilidad de las mediciones:** algunas pruebas aisladas salieron mucho más altas que las demás (por ejemplo, B con n = 100.000 en el mejor caso: 1,42 ms en la prueba 1 frente a ≈0,02 ms en las otras), por la optimización automática de Java (JIT) y otros procesos del equipo. Por eso cada medición se repitió 3 veces y se reporta el promedio.

7. **Respuesta a la pregunta del caso (n = 1.000.000):** ninguna de las dos escala bien. En el peor caso, ambas necesitan unas 500.000 millones de comparaciones y cada ejecución tardó alrededor de COMPLETAR segundos. Entre las dos, **A es preferible**: hace las mismas comparaciones, no usa memoria extra y no paga el costo de crear el arreglo auxiliar. Para que una estructura auxiliar realmente mejore el rendimiento, debe poder responder "¿ya vi este valor?" sin recorrerse completa (por ejemplo, un conjunto basado en *hashing* o un arreglo de marcas indexado por valor), lo que reduciría el algoritmo a O(n).
