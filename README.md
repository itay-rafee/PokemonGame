
# EX2
Object Oriented Course Assignment - Creating and Implementing Directed Weighted Graph Theory.


In this project we were asked to implement an directed weighted graph with the algorithms of Graph Theory.
We will briefly explain the algorithms, the data structures we used and the complexities of the operations.


<table align="center">
<td><h3>Final implementation of the methods:</h3>  
<p align="center"><img src="https://github.com/itay-rafee/Ex2/raw/main/images/pokJar.jpg" width="650"/></p>
</td>
</table>
<table align="center">
<tr><td>

**Edge weight examples**  
from D to E: 1  
from B to C: 0.3  
from A to F: 0.9  

**Shortest Path examples:**  
**(lowest weight)**  
from B to A:  
B > A > E > F  
from A to D:  
A > E > F > D  

</td>

<td> 
	

```jsonc
Directed Weighted Graph illustration:
```
<p align="center"><img src="https://github.com/itay-rafee/Ex2/raw/main/images/dwgraph.png"/></p>

<!--
<p align="center">
<img src="https://github.com/AlmogJakov/AlmogJakov/blob/main/welcome-back-small.gif"/>
</p>
-->

</td></tr>
</table>

* "images" folder should be 'src' type

<h2> DWGraph_DS Class (Represents a graph): </h2>

In this class we were asked to implement a class representing directed weighted graph
This class represents an directed weighted graph that can contain a very large amount of vertices and for that it is necessary to maintain efficient running time.
To do so there is a need for a data structure where all vertices can be accessed efficiently.
Therefore, the appropriate data structure fo performing operations on a very large list of vertex is HashMap. 
(such as returning a vertex value, checking neighbors between vertices etc.)

```diff 
@@ HashMap<Integer,node_data> _graph; @@ (for receiving any vertex by key).
``` 
```diff 
@@ HashMap<Integer, HashMap<Integer, edge_data>> _ni; @@ (for receiving any vertex neighbors as keys while value = edge).
``` 
```diff 
@@ HashMap<Integer, HashSet<Integer>> _niRevers; @@ (for receiving any vertex reversed edges).
``` 

The following functions can be performed:
- Return vertex by key - easily done by accessing the '_graph' HashMap in complexity O(1)
- Return edge between 2 vertices - performed by checking the existence of one vertex in the list of neighbors (HashMap) of the other.
- Adding a vertex to a graph - In HashMap Adding (to each HashMap) is in complexity O(1).
- Adding an edge to a graph between 2 vertices - done by adding a vertex to the list of neighbors of the other (two directions) with the edge.
- Returns a collection of all vertices of the graph - done by returning '_graph' values using values() function.
- Returns a collection of all the edges of specific vertex - done by a returning collection of the vertex neighbors keys using values() function.
- Deleting a vertex from the graph - removing the vertex (after deleting all the edges connected to it) In HashMap deletion is in complexity O(1).
- Deleting an edge from the graph - done by deleting a vertex from the list of neighbors of the other (two directions).
- Obtaining a numeric variable representing the number of vertices - easily done by the size() function built in '_graph' HashMap object in complexity O(1).
- Receiving a numeric variable that represents the number of edges - easily done by a direct variable (stored in the class) updated with each operation of adding\deleting 
  an edge to the graph.
- Receiving a numeric variable that represents the number of MC (changes) in the class - easily done by a direct variable (stored in the class) updated with each action 
  of adding\deleting an vertex\edge in the graph or updating edge weight.

<h2></h2>
<h3> Edge Location Class (Represents the location of edge) [Internal Class of DWGraph_DS Class]: </h3>  
In this class we were asked to implement a class that represents the location of edge.  
The following functions can be performed:

- Return the edge - easily performed by a direct variable stored in class in complexity O(1).  
- Return the ratio - easily performed by a direct variable stored in class in complexity O(1).  

<h2></h2>
<h3> Edge Class (Represents a edge) [Internal Class of DWGraph_DS Class]: </h3>  
In this class we were asked to implement a class that represents an edge, in this class basic storage and operations are performed on the edge.  
The following functions can be performed:

- Return source vertex key - easily performed by a direct variable stored in class in complexity O(1).  
- Return destination vertex key - easily performed by a direct variable stored in class in complexity O(1).  
- Return the weight of the edge - easily performed by a direct variable stored in class in complexity O(1).  
- Return the info of the edge - easily performed by a direct variable stored in class in complexity O(1).  
- Assigning a value to a variable that contains information (meta data) - easily done by changing the direct variable stored in class in complexity O(1).  
- Receiving a variable used for temporal marking - easily done by a direct variable stored in class in complexity O(1).  
- Assigning a value used for temporal marking - easily done by changing the direct variable stored in class in complexity O(1).

<h2> DWGraph_Algo Class (Represents the Graph Theory algorithms): </h2>

In this class we were asked to implement a class representing the algorithms of Graph Theory (directed weighted graph).

In this class some of the algorithms implemented using dijkstra algorithm.  
Intuitive explanation of the algorithm (source: Wikipedia.org):
```diff
 Algorithm loop:
   * As long as there are any unvisited vertices:
   * Mark the X vertex as visited. (current vertex. In the first iteration this is the vertex of the source S)
   * For each vertex Y which is a neighbor of X and we have not yet visited it:
        Y is updated so that its distance is equal to the minimum value between two values: between its current distance,
        and the weight of the edge connecting X and Y plus the distance between S and X.
   * Select a new vertex X as the vertex whose distance from the source S is the shortest (at this point) from all the
     vertices in the graph we have not yet visited.
 The algorithm ends when the new vertex X is the destination or (to find all the fastest paths) when we have visited all the vertices.
```
One of the algorithms implemented using BFS algorithm.  
Intuitive explanation of the algorithm (source: Wikipedia.org):
```diff
The algorithm uses a queue data structure to determine which is the next vertex it is going to visit.
	Each time we visits an vertex we marks it as being "visited", then inspects all the edged coming out of it.
	If an edge leads to an unvisited vertex, that node is added to the queue.
	This way it is ensured that the algorithm will scan the vertices in the order determined by their distance from the initial vertex
	(Because a vertex that enters the queue leaves it only after all the vertices that were in it before have left).
The complexity of the algorithm (in an almost completely linked graph) is in complexity O(v+e) where v=vertices, e=edges of the graph.
```
  
The following functions can be performed:
- Initializes the class object - easily done by referring this graph to the input graph  
- Deep copy to graph - In this method the implementation made using copy constructor on DWGraph_DS class.
- Check if the graph is strongly linked - in this function we used Kosaraju algorithm (using BFS).  
   Implementation is by the principle that the graph is strongly connected if the original graph is connected and also if after reversing the graph it still connected.  
- Return the shortest path between 2 vertices - There are 2 functions that we implemented in a similar way:  
  * A function that returns the shortest path length (by weight).  
  * A function that returns the vertices of the shortest path  
      In this function we used the dijkstra algorithm as explained above,  
      That's because the algorithm scans the vertices in the order determined by their distance (by weight) from the initial vertex, And since we can attribute to each vertex 	the parent corresponding to it (which is closer [at one level] to the initial vertex), So inevitably when we reach the destined vertex we can build the shortest path (the path that weighs the least) and this is done by attributing each vertex to its "parent".  
- Save the graph as an object to an json file.
- Load the graph from an json file and initialize it.

<h2> NodeData Class (Represents a vertex): </h2>

In this class we were asked to implement a class that represents a vertex, in this class basic storage and operations are performed on the vertex.  

The following functions can be performed:  
- Key return - easily performed by a direct variable stored in class in complexity O(1).  
- Location return - easily performed by a direct variable stored in class in complexity O(1).  
- Assigning the location of this vertex - easily done by changing the direct variable stored in class in complexity O(1).  
- Return the weight of the vertex - easily performed by a direct variable stored in class in complexity O(1).  
- Assigning the weight of the vertex - easily done by changing the direct variable stored in class in complexity O(1).  
- Receiving a variable that contains information (meta data) - easily done by a direct variable stored in class in complexity O(1).  
- Assigning a value to a variable that contains information (meta data) - easily done by changing the direct variable stored in class in complexity O(1).  
- Receiving a variable used for temporal marking - easily done by a direct variable stored in class in complexity O(1).  
- Assigning a value used for temporal marking - easily done by changing the direct variable stored in class in complexity O(1).

<h2> GeoLocation Class (Represents the GeoLocation of an object): </h2>

In this class we were asked to implement a class that represents the geo location of an object such as vertex or edge.

The following functions can be performed:  
- Return position along the x-axis - easily performed by a direct variable stored in class in complexity O(1).  
- Return position along the y-axis - easily performed by a direct variable stored in class in complexity O(1).  
- Return position along the z-axis - easily performed by a direct variable stored in class in complexity O(1).  
- Distance calculation between input location object to this location - performed by internal formula of distance.  
