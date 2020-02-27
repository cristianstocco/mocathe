# MOCATHE (MOdels for CAncer THErapies)


A Java implementation for MOCATHE: a software to inference the tumor evolutions by applying therapy medicines and understanding how it is absorbing to the normal (healty) states. The previous version of this software is CIMICE (https://github.com/redsnic/tumorEvolutionWithMarkovChains).


## Application
Before using this software, you need to Run the CIMICE software as in the previous link which generates a dotGraph.
This output, the dotGraph, is used for the input in MOCATHE software and some therapy medicines can be found in LOBICO (http://lobico.nki.nl/browseByModels).


## Usage

In terminal:
```bash
java -cp bin main/Main -dotPathGraph -type -formula -pFormula
```

with:
| Param | Type | Description |
| --- | --- | --- |
| -dotPathGraph | String | the path of dot graph file |
| -type | String | type could be repair (-r) or damage&diverge (-d) |
| -formula | String | therapy formula in DNF mode |
| -pFormula | double | probability of the therapy formula |
