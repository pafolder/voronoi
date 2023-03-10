Simply-Connected Domains Discovery Application
=============================================

### Domains are searched on the Voronoi Diagram and checked if they are Simply-Connected
A brief description of the implementation of the algorithm is as follows: 

- Voronoi Diagram is represented by `VoronoiDiagram` class and is based on a list of Cells.

- Each Cell has a Seed, surrounding Polytope points and a list of the adjacent Cells, represented by `CellWithAdjacents`
  class.
- `VoronoiDiagram#computeDomains()` method provides computation of Domains.
After the domains found for the first time, each Domain is examined for being a Simply-Connected. For this, a `cloned` 
Voronoi Diagram is created with examined Domain cells coloured White. Domains computation is repeated for the `cloned`
diagram following by comparing the number of White Domains. A decrease of the number means that there are 
"White bubbles" in the examined Domain, and it is Non-Simply Connected.
- Application execution results in getting a list of domains with the status of `SIMPLY_CONNECTED` or 
`NON_SIMPLY_CONNECTED`.
Each Domain Cells' indices can be found in `cellIndices` property of the `Domain` class.
- The `seed` and `polytopePoints` properties of `CellWithAdjacents` class are not used by the 
`VoronoiDiagram#computeDomains()`
and provided just for further tasks only.
- There are 4 test cases implemented in the Junit section. A Graphical explanation of the initialization values in
`Data` class are:&ensp;[1](png/example1.png)
  &ensp;[2](png/example2.png)
  &ensp;[3](png/example3.png)
  &ensp;[4](png/example4.png)
