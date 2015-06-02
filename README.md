# Asynchronous-Distance-Vector-Routing

This program finds the minimum distance to all nodes in a graph for each node.
Each node hold the minimum distance to every other node as well as the minimum

The nodes respond to change and will update minimum paths if a new one is found.
In case of an update, a node will inform all of its direct neighbors about it.
In case of a link changing value (ie becomes trafficed) the nodes will update
to reflect the change.

The initial graph is below 





![initialGraph](https://github.com/reymargera/Asynchronous-Distance-Vector-Routing/blob/master/initialGraphImage.gif)
