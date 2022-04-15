package graphs;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DirectedGraph<V extends Identifiable, E> {

    private final Map<String, V> vertices = new HashMap<>();
    private final Map<V, Map<V, E>> edges = new HashMap<>();

    /**
     * representation invariants:
     * 1.  the vertices map stores all vertices by their identifying id (which prevents duplicates)
     * 2.  the edges map stores all directed outgoing edges by their from-vertex and then in the nested map by their to-vertex
     * 3.  there can only be two directed edges between any two given vertices v1 and v2:
     * one from v1 to v2 in edges.get(v1).get(v2)
     * one from v2 to v1 in edges.get(v2).get(v1)
     * 4.  every vertex instance in the key-sets of edges shall also occur in the vertices map and visa versa
     **/

    public DirectedGraph() {
    }

    public Collection<V> getVertices() {
        return vertices.values();
    }

    /**
     * finds the vertex in the graph identified by the given id
     *
     * @param id
     * @return the vertex that matches the given id
     * null if none of the vertices matches the id
     */
    public V getVertexById(String id) {
        return vertices.get(id);
    }

    /**
     * retrieves the collection of neighbour vertices that can be reached directly
     * via an out-going directed edge from 'fromVertex'
     *
     * @param fromVertex
     * @return null if fromVertex cannot be found in the graph
     * an empty collection if fromVertex has no neighbours
     */
    public Collection<V> getNeighbours(V fromVertex) {
        // we check if that vertex is null, because if it is it cannot have any neighbours and we return null.
        // Otherwise we get from the edges map the vertex. Now we have all values for that vertex.
        // But those values are in a different map. So we only call .keySet() on that map so we only get the neighbour Vertex’ and not the neighbour vertex’ with the edge.
        //This way we can return a collection of neighbour vertex’ for a particular vertex.
        if (fromVertex == null) return null;
        return this.edges.get(fromVertex).keySet();
    }

    public Collection<V> getNeighbours(String fromVertexId) {
        return this.getNeighbours(this.getVertexById(fromVertexId));
    }

    /**
     * retrieves the collection of edges
     * which connects the 'fromVertex' with its neighbours
     * (only the out-going edges directed from 'fromVertex' towards a neighbour shall be included
     *
     * @param fromVertex
     * @return null if fromVertex cannot be found in the graph
     * an empty collection if fromVertex has no out-going edges
     */
    public Collection<E> getEdges(V fromVertex) {
        //retrieve the collection of out-going edges which connect fromVertex with a neighbour in the edges data structure
        if (fromVertex == null) return null;
        return this.edges.get(fromVertex).values();

    }

    public Collection<E> getEdges(String fromId) {
        return this.getEdges(this.getVertexById(fromId));
    }

    /**
     * Adds newVertex to the graph, if not yet present and in a way that maintains the representation invariants.
     * If a duplicate of newVertex (with the same id) already exists in the graph,
     * nothing will be added, and the existing duplicate will be kept and returned.
     *
     * @param newVertex
     * @return the duplicate of newVertex with the same id that already exists in the graph,
     * or newVertex itself if it has been added.
     */
    public V addOrGetVertex(V newVertex) {
        //we check if the vertex is null, if it is we return null.
        // Afterwards we check in an if statement if the vertices map contains the vertex we are trying to add.
        // We do this by calling the containsKey() method and the containsValue() method.
        // If these are false it means the vertex does not yet exist in the vertices map,
        // which means we can add the vertex to the vertices map and return that vertex.
        //
        //If the vertices map does contain the new vertex we then simply get this vertex from the vertices map and return it.
        if(newVertex == null) return null;
        if (!this.vertices.containsKey(newVertex.getId()) && !this.vertices.containsValue(newVertex)) {
            this.vertices.put(newVertex.getId(), newVertex);
            return newVertex;
        }
        if (this.vertices.containsKey(newVertex.getId())) {
            return this.vertices.get(newVertex.getId());
        }

        return null;
    }


    /**
     * Adds a new, directed edge 'newEdge'
     * from vertex 'fromVertex' to vertex 'toVertex'
     * No change shall be made if a directed edge already exists between these vertices
     *
     * @param fromVertex the start vertex of the directed edge
     * @param toVertex   the target vertex of the directed edge
     * @param newEdge    the instance with edge information
     * @return whether the edge has been added successfully
     */
    public boolean addEdge(V fromVertex, V toVertex, E newEdge) {
        //  add (directed) newEdge to the graph between fromVertex and toVertex

        addOrGetVertex(fromVertex);
        addOrGetVertex(toVertex);

        return this.addEdge(fromVertex.getId(), toVertex.getId(), newEdge);
    }

    /**
     * Adds a new, directed edge 'newEdge'
     * from vertex with id=fromId to vertex with id=toId
     * No change shall be made if a directed edge already exists between these vertices
     *
     * @param fromId  the id of the start vertex of the outgoing edge
     * @param toId    the id of the target vertex of the directed edge
     * @param newEdge the instance with edge information
     * @return whether the edge has been added successfully
     */
    public boolean addEdge(String fromId, String toId, E newEdge) {
        //We check if the given vertices exist, if they don't exist we will return false, because we can't add it.
        if (!this.vertices.containsKey(fromId) || !this.vertices.containsKey(toId)) {
            return false;
        }
        //We will initialize a map, where we will add the edge.
        Map<V, E> map = new HashMap<>();
        //In this map we will add the given edge with the existing vertex.
        map.put(this.getVertexById(toId), newEdge);

        //We check if the given vertex is in the edges Map, if they don't exist we will add this to the edges.
        if (this.edges.containsKey(this.getVertexById(fromId))) {
            //If there is already an edge, we should not override it.
            if (this.edges.get(this.getVertexById(fromId)).containsKey(this.getVertexById(toId))) {
                return false;
            }
            //We will add all the existing edges to our map.
            map.putAll(edges.get(this.getVertexById(fromId)));
        }
        // If they don't exist we will add this to the edges
        this.edges.put(this.getVertexById(fromId), map);
        //returning true because we added the edge.
        return true;

    }
    /**
     * Adds two directed edges: one from v1 to v2 and one from v2 to v1
     * both with the same edge information
     *
     * @param v1
     * @param v2
     * @param newEdge
     * @return whether both edges have been added
     */
    public boolean addConnection(V v1, V v2, E newEdge) {
        return this.addEdge(v1, v2, newEdge) && this.addEdge(v2, v1, newEdge);
    }

    /**
     * Adds two directed edges: one from id1 to id2 and one from id2 to id1
     * both with the same edge information
     *
     * @param id1
     * @param id2
     * @param newEdge
     * @return whether both edges have been added
     */
    public boolean addConnection(String id1, String id2, E newEdge) {
        return this.addEdge(id1, id2, newEdge) && this.addEdge(id2, id1, newEdge);
    }

    /**
     * retrieves the directed edge between 'fromVertex' and 'toVertex' from the graph, if any
     *
     * @param fromVertex the start vertex of the designated edge
     * @param toVertex   the end vertex of the designated edge
     * @return the designated directed edge that has been registered in the graph
     * returns null if no connection has been set up between these vertices in the specified direction
     */
    public E getEdge(V fromVertex, V toVertex) {
        if (fromVertex == null || toVertex == null) return null;
        // retrieve the directed edge between vertices fromVertex and toVertex from the graph
        return this.edges.get(fromVertex).get(toVertex);
    }

    public E getEdge(String fromId, String toId) {
        return this.getEdge(this.vertices.get(fromId), this.vertices.get(toId));
    }

    /**
     * @return the total number of vertices in the graph
     */
    public int getNumVertices() {
        return vertices.size();
    }

    /**
     * calculates and returns the total number of directed edges in the graph data structure
     *
     * @return the total number of edges in the graph
     */
    public int getNumEdges() {
        return this.edges
                .values() // We will get the values inside the nested map in the edges.
                .stream() // We will simply stream to this data.
                .mapToInt(Map::size) //We will map it to Integers, because we need to return an Integer and we will map it by size,
                                    //because the sum of these sizes is the number of edges.
                .sum();
    }

    /**
     * Remove vertices without any connection from the graph
     */
    public void removeUnconnectedVertices() {
        this.edges.entrySet().removeIf(e -> e.getValue().size() == 0);
        this.vertices.entrySet().removeIf(e -> !this.edges.containsKey(e.getValue()));
    }

    /**
     * represents a path of connected vertices and edges in the graph
     */
    public class DGPath {
        private Deque<V> vertices = new LinkedList<>();
        private double totalWeight = 0.0;
        private Set<V> visited = new HashSet<>();

        /**
         * representation invariants:
         * 1. vertices contains a sequence of vertices that are connected in the graph by a directed edge,
         * i.e. FOR ALL i: 0 < i < vertices.length: this.getEdge(vertices[i-1],vertices[i]) will provide edge information of the connection
         * 2. a path with one vertex has no edges
         * 3. a path without vertices is empty
         * totalWeight is a helper attribute to capture additional info from searches, not a fundamental property of a path
         * visited is a helper set to be able to track visited vertices in searches, not a fundamental property of a path
         **/

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(
                    String.format("Weight=%f Length=%d visited=%d (",
                            this.totalWeight, this.vertices.size(), this.visited.size()));
            String separator = "";
            for (V v : this.vertices) {
                sb.append(separator + v.getId());
                separator = ", ";
            }
            sb.append(")");
            return sb.toString();
        }

        public Queue<V> getVertices() {
            return this.vertices;
        }

        public double getTotalWeight() {
            return this.totalWeight;
        }

        public Set<V> getVisited() {
            return this.visited;
        }
    }

    /**
     * Uses a depth-first search algorithm to find a path from the start vertex to the target vertex in the graph
     * All vertices that are being visited by the search should also be registered in path.visited
     *
     * @param startId
     * @param targetId
     * @return the path from start to target
     * returns null if either start or target cannot be matched with a vertex in the graph
     * or no path can be found from start to target
     */
    public DGPath depthFirstSearch(String startId, String targetId) {
        //We initialize the start and the target from the given ids.
        V start = getVertexById(startId);
        V target = getVertexById(targetId);
        //If the variables are empty, we can't do anything so we return null.
        if (start == null || target == null) return null;

        //Initialize a DGPath variable to keep track of the visited and vertices.
        DGPath path = new DGPath();

        //We will return the result of our function.
        return DFSsearch(start, target, path);
    }
    public DGPath DFSsearch(V start, V target, DGPath path){
        //If the path start path is already visited, we will return null.
        if(path.visited.contains(start)) return null;

        //We will add the start to the visited list, because we will start from here.
        path.visited.add(start);

        //If we are at the target we will go into this statement.
        if(start.equals(target)){
            //We will add this place to the end of the vertices, because it's our destination.
            path.vertices.addLast(start);
            //We return the path, because we found our target.
            return path;
        }
        //If we didn't found our target, we will loop through all the neighbours of the place where we are.
        for(V neighbour : this.getNeighbours(start)){
            //The next place we will go is the first neighbour in the loop, we will call the DFSsearch again
            // to go through this cycle again. We will go through all the child of this first neighbour, before we go
            // to the next neighbour of our start position.
            DGPath nextPath = DFSsearch(neighbour, target, path);
            //If the path doesn't return null, we found the target via the child, we will go into this if statement.
            if(nextPath != null) {
                // We will add this vertex to the vertices.
                path.vertices.addFirst(start);
                // We will return the path, because we found our target.
                return path;
            }
        }
        // Return null, if we didn't found the target.
        return null;
    }



    /**
     * Uses a breadth-first search algorithm to find a path from the start vertex to the target vertex in the graph
     * All vertices that are being visited by the search should also be registered in path.visited
     *
     * @param startId
     * @param targetId
     * @return the path from start to target
     * returns null if either start or target cannot be matched with a vertex in the graph
     * or no path can be found from start to target
     */
    public DGPath breadthFirstSearch(String startId, String targetId) {
        //We initialize the start and the target from the given ids.
        V start = getVertexById(startId);
        V target = getVertexById(targetId);
        //If the variables are empty, we can't do anything so we return null.
        if (start == null || target == null) return null;

        //Initialize a DGPath variable to keep track of the visited and vertices.
        DGPath path = new DGPath();
        path.visited.add(start);

        //If we are at the target we will go into this statement.
        if(start.equals(target)){
            //We will add this place to the end of the vertices, because it's our destination.
            path.vertices.add(start);
            //We return the path, because we found our target.
            return path;
        }
        //add our target at the last position in the path.vertices.
        // Afterwards we make a Queue and a map and insert the specified element into this queue.
        // We also put the visitedFrom key value to the start vertex.
        // Afterwards we use the queue.poll() so we can get the first element in the queue which
        // is the start vertex we offered earlier in the queue and we put this to the current vertex variable.
        // Now we will loop while the current variable is not null. Basically until there are no items more
        // left in the queue.
        path.vertices.addLast(target);
        Queue<V> fifoQueue = new LinkedList<>();
        Map<V, V> visitedFrom = new HashMap<>();

        fifoQueue.offer(start);
        visitedFrom.put(start, null);

        V current = fifoQueue.poll();
        while (current != null){
            //we loop through each neighbour of the current vertex.
            // If that neighbour is equal to our target we enter another while loop in where we make the path.
            // We add the current vertex at the start of path.vertices.
            // And update the ‘current’ variable so we know from which vertex we visited the current vertex.
            // We do this by using the map we made earlier in which we keep track from which
            // vertex we came to the next vertex.
            // By letting this loop run we will get the full path eventually and return it.
            for (V neighbour: getNeighbours(current)){
                if (neighbour.equals(target)){
                    while (current!= null){
                        path.vertices.addFirst(current);
                        current = visitedFrom.get(current);
                    }
                    return path;
                    //If it is not equal to the target we check if the neighbour is already defined as a key
                    // in the visitedFrom map. But this will always be false,
                    // because we never added the neighbour here yet.
                    // So we enter the if statement and add this neighbour to the path.visited vertex’.
                    // Now we will put the neighbour as the key and it’s value as the current vertex in the visitedFrom map.
                    // By doing this we know that we visited this neighbour from the current vertex.
                    // Which we need to construct the path once we found the target.
                    // Afterwards we insert the neighbour vertex into this queue and by using queue.poll()
                    // we can update the current vertex to the new neighbour that we added to the queue.
                } else if (!visitedFrom.containsKey(neighbour)){
                    path.visited.add(neighbour);
                    visitedFrom.put(neighbour, current);
                    fifoQueue.offer(neighbour);
                }
            }
            current = fifoQueue.poll();
        }
        return null;
    }

    // helper class to register the state of a vertex in dijkstra shortest path algorithm
// your may change this class or delete it altogether follow a different approach in your implementation
    private class DSPNode implements Comparable<DSPNode> {
        protected V vertex;                // the graph vertex that is concerned with this DSPNode
        protected V fromVertex = null;     // the parent's node vertex that has an edge towards this node's vertex
        protected boolean marked = false;  // indicates DSP processing has been marked complete for this vertex
        protected double weightSumTo = Double.MAX_VALUE;   // sum of weights of current shortest path to this node's vertex

        private DSPNode(V vertex) {
            this.vertex = vertex;
        }

        // comparable interface helps to find a node with the shortest current path, sofar
        @Override
        public int compareTo(DSPNode dspv) {
            return Double.compare(weightSumTo, dspv.weightSumTo);
        }

        public void setWeightSumTo(double weightSumTo) {
            this.weightSumTo = weightSumTo;
        }
    }

    /**
     * Calculates the edge-weighted shortest path from start to target
     * according to Dijkstra's algorithm of a minimum spanning tree
     *
     * @param startId      id of the start vertex of the search
     * @param targetId     id of the target vertex of the search
     * @param weightMapper provides a function, by which the weight of an edge can be retrieved or calculated
     * @return the shortest path from start to target
     * returns null if either start or target cannot be matched with a vertex in the graph
     * or no path can be found from start to target
     */
    public DGPath dijkstraShortestPath(String startId, String targetId,
                                       Function<E, Double> weightMapper) {
        //We initialize the start and the target from the given ids.
        V start = getVertexById(startId);
        V target = getVertexById(targetId);
        //If the variables are empty, we can't do anything so we return null.
        if (start == null || target == null) return null;

        //Initialize a DGPath variable to keep track of the visited and vertices.
        DGPath path = new DGPath();
        path.visited.add(start);

        //If we are at the target we will go into this statement.
        if(start.equals(target)){
            //We will add this place to the end of the vertices, because it's our destination.
            path.vertices.add(start);
            //We return the path, because we found our target.
            return path;
        }
        //We will initialize a DSPNoce per vertex into a map.
        //In this node we will keep track of the sum of weight to this vertex and if they're marked.
        Map<V, DSPNode> progressData = new HashMap<>();

        //We will start by making a node of the position where we start.
        DSPNode firstNode = new DSPNode(start);
        //Because we haven't travelled we set the sum of our weight to 0.
        firstNode.weightSumTo = 0.0;

        //We will add this first vertex to our progressData.
        progressData.put(start, firstNode);

        // We will keep looping while this node is not null.
        while (firstNode != null) {
            //We will mark this node, if we begin the processing of the node.
            firstNode.marked = true;
            // For each neighbour of this node's vertex.
            for (V neighbour: getNeighbours(firstNode.vertex)){
                //We will initialize a node for this neighbour.
                DSPNode neighbourNode = new DSPNode(neighbour);
                //We will calculate the weight of the path to this node.
                neighbourNode.setWeightSumTo(firstNode.weightSumTo + weightMapper.apply(this.getEdge(firstNode.vertex, neighbour)));

                //We will link the two nodes, by setting the fromVertex of this neighbourNode.
                neighbourNode.fromVertex = firstNode.vertex;

                //If we haven't been to this neighbour yet, we will simply add this neighbour to the progressData.
                if(progressData.get(neighbour) == null){
                    progressData.put(neighbour, neighbourNode);
                    //If we have been to this neighbour, we will check if the sum of weight of this path is lower
                    //If it is lower we will update this node in the progressData, with the new sum of weight.
                } else if(progressData.get(neighbour).weightSumTo >= neighbourNode.weightSumTo){
                    progressData.put(neighbour, neighbourNode);
                }

                //We will add this neighbour to the visited Set
                path.visited.add(neighbour);
            }
            //If we found our target.
            if(firstNode.vertex.equals(target)){
                //We will set the totalWeight of the path to the weightSumTo of this node.
                path.totalWeight = firstNode.weightSumTo;
                //If the vertex of the target is not null.
                while(firstNode.vertex != null){
                    //We will add this node's vertex to our vertices path.
                    path.vertices.addFirst(firstNode.vertex);
                    //We will set the vertex of this node to the fromVertex of this node in the progressData.
                    firstNode.vertex = progressData.get(firstNode.vertex).fromVertex;
                }
                //We will return this path, because this is the path to the target.
                return path;
            }
            //We will go to the next available node, we do this by filtering it on all the nodes who aren't marked.
            firstNode = progressData
                    .values()
                    .stream()
                    .filter(v -> !v.marked)
                    .min(DSPNode::compareTo)
                    .orElse(null);
        }
        //If we don't find the target, we will return null.
        return null;
    }


    @Override
    public String toString() {
        return this.getVertices().stream()
                .map(v -> v.toString() + ": " +
                        this.edges.get(v).entrySet().stream()
                                .map(e -> e.getKey().toString() + "(" + e.getValue().toString() + ")")
                                .collect(Collectors.joining(",", "[", "]"))
                )
                .collect(Collectors.joining(",\n  ", "{ ", "\n}"));
    }
}
