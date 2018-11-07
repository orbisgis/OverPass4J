# OverPass4J
The library reproduce the OverPass API in JAVA/Groovy. This library is specially designed to improve the OverPass 
request writing in Groovy.

### Statements
OverPass request consist into several statements, all ending with a `;`. A statement starts with the object to 
request, then  zero or more tag filter and to finish an optional bounding box. The statement will request all the 
object with the defined type, with the tag specified as filter in the given bounding box.<br><br>
There is three object which can be requested : `node`, `way`, `rel`.<br><br>
The tag filter are composed of a `key` with an optional value `=value` written inside `[]`. The `key` and the `value` 
can be surrounded with `"`. For example `[building=yes][height]["roof"="asphalt"]`<br><br>
The bounding box is defined with the coordinate of the South, West, North and East border : `(50.746,7.154,50.748,7.157)`
###### OverPass
```java
node[name="Vannes"][population](47.0,-3.0,48.0,-2.0);
way[name="Vannes"][website](47.0,-3.0,48.0,-2.0);
rel[name="Vannes"][capital](47.0,-3.0,48.0,-2.0);
area[name="Vannes"][historic](47.0,-3.0,48.0,-2.0);
```
###### Java
```java
new Node(new Bbox(47.0,-3.0,48.0,-2.0),"name=Vannes", "population");
new Way(new Bbox(47.0,-3.0,48.0,-2.0),"name=Vannes", "website");
new Rel(new Bbox(47.0,-3.0,48.0,-2.0),"name=Vannes", "capital");
new Area(new Bbox(47.0,-3.0,48.0,-2.0),"name=Vannes", "historic");
```
###### Groovy
```groovy
node(bbox(47.0,-3.0,48.0,-2.0),"name=Vannes", "population")
way(bbox(47.0,-3.0,48.0,-2.0),"name=Vannes", "website")
rel(bbox(47.0,-3.0,48.0,-2.0),"name=Vannes", "capital")
area(bbox(47.0,-3.0,48.0,-2.0),"name=Vannes", "historic")
```

### Statement union and sets
Statement can be merge into a set in order to request different kind of object in the same queries. All the satement 
should be placed inside `()` ending with a `;`
###### OverPass
```java
(
    node[name="Vannes"][population](47.0,-3.0,48.0,-2.0);
    way[name="Vannes"][website](47.0,-3.0,48.0,-2.0);
    rel[name="Vannes"][capital](47.0,-3.0,48.0,-2.0);
    area[name="Vannes"][historic](47.0,-3.0,48.0,-2.0);
);
```
###### Java
```java
new Set(
    new Node(new Bbox(47.0,-3.0,48.0,-2.0),"name=Vannes", "population");
    new Way(new Bbox(47.0,-3.0,48.0,-2.0),"name=Vannes", "website");
    new Rel(new Bbox(47.0,-3.0,48.0,-2.0),"name=Vannes", "capital");
    new Area(new Bbox(47.0,-3.0,48.0,-2.0),"name=Vannes", "historic");
);
```
###### Groovy
```groovy
node(bbox(47.0,-3.0,48.0,-2.0),"name=Vannes", "population") +
way(bbox(47.0,-3.0,48.0,-2.0),"name=Vannes", "website") +
rel(bbox(47.0,-3.0,48.0,-2.0),"name=Vannes", "capital") +
area(bbox(47.0,-3.0,48.0,-2.0),"name=Vannes", "historic")
```

### Input set

An input set represents all the data represented by a set which can be used as input of operations. A specific name 
can be used always starting wit a `.` like : `.yoursetname`. By default, if not name is given, a set is store as the 
default one `._;`.<br>
Then the name can be reused as an alias for other statement like for-each.<br>
A set can be store as the default just by calling it.

###### OverPass
```java
(node[name="Vannes"][population](47.0,-3.0,48.0,-2.0);)->.a;
//Both following set are store as the default input set.
(node[name="Vannes"][population](47.0,-3.0,48.0,-2.0);)->._;
(node[name="Vannes"][population](47.0,-3.0,48.0,-2.0););

//Store the set named 'a' as the default set
(node[name="Vannes"][population](47.0,-3.0,48.0,-2.0);)->.a;
.a->._; /** or **/ .a;
```
###### Java
```java
new Set(new Node(new Bbox(47.0,-3.0,48.0,-2.0),"name=Vannes", "population")).setAlias("a");
//Both following set are store as the default input set.
new Set(new Node(new Bbox(47.0,-3.0,48.0,-2.0),"name=Vannes", "population")).setAlias("_");
new Set(new Node(new Bbox(47.0,-3.0,48.0,-2.0),"name=Vannes", "population"));

//Store the set named 'a' as the default set
Set set = new Set(new Node(new Bbox(47.0,-3.0,48.0,-2.0),"name=Vannes", "population")).setAlias("a");
new DefaultSet(set); /** or **/ new DefaultSet("a");
```
###### Groovy
```groovy
node(bbox(47.0,-3.0,48.0,-2.0),"name=Vannes", "population")>>"a"
//Both following set are store as the default input set.
node(bbox(47.0,-3.0,48.0,-2.0),"name=Vannes", "population")>>"_"
node(bbox(47.0,-3.0,48.0,-2.0),"name=Vannes", "population")

//Store the set named 'a' as the default set
set = node(bbox(47.0,-3.0,48.0,-2.0),"name=Vannes", "population")>>"a"
defaultSet(set); /** or **/defaultSet("a")
```

### Special operations

Operations can be done on input set like looping on element or recuse up and down to select other linked elements.

##### Recurse up

It takes an input set and returns a a set containing all upper osm core elements contained by the elements of the
input set : 
 - all ways that have a node which appears in the input set, plus
 - all relations that have a node or way which appears in the input set

Like all the sets you can specified the input set and a name to store the set.

###### OverPass
```java
//Recurse up on the default input set and store it in the default input set.
<;
//Recurse up on the default input set and store it in the input set named 'a'.
<->.a;
//Recurse up on the input set named 'a' and store it in the input set named 'a'.
.a<;
//Recurse up on the input set named 'a' and store it in the input set named 'b'.
.a<->.b;
```
###### Java
```java
//Recurse up on the default input set and store it in the default input set.
new RecurseUp();
//Recurse up on the default input set and store it in the input set named 'a'.
new RecurseUp().setAlias("a");
//Recurse up on the input set named 'a' and store it in the input set named 'a'.
new RecurseUp("a");
//Recurse up on the input set named 'a' and store it in the input set named 'b'.
new RecurseUp("a").setAlias("b");
```
###### Groovy
```groovy
//Recurse up on the default input set and store it in the default input set.
recurseUp()
//Recurse up on the default input set and store it in the input set named 'a'.
recurseUp()>>"a"
//Recurse up on the input set named 'a' and store it in the input set named 'a'.
recurseUp("a")
//Recurse up on the input set named 'a' and store it in the input set named 'b'.
recurseUp("a")>>"b"
```

##### Recurse down

It takes an input set and returns a a set containing all lower osm core elements contained by the elements of the
input set : 
 - all nodes that are part of a way which appears in the input set, plus
 - all nodes and ways that are members of a relation which appears in the input set<br>
Like all the sets you can specified the input set and a name to store the set.

###### OverPass
```java
//Recurse down on the default input set and store it in the default input set.
>;
//Recurse down on the default input set and store it in the input set named 'a'.
>->.a;
//Recurse down on the input set named 'a' and store it in the input set named 'a'.
.a>;
//Recurse down on the input set named 'a' and store it in the input set named 'b'.
.a>->.b;
```
###### Java
```java
//Recurse down on the default input set and store it in the default input set.
new RecurseDown();
//Recurse down on the default input set and store it in the input set named 'a'.
new RecurseDown().setAlias("a");
//Recurse down on the input set named 'a' and store it in the input set named 'a'.
new RecurseDown("a");
//Recurse down on the input set named 'a' and store it in the input set named 'b'.
new RecurseDown("a").setAlias("b");
```
###### Groovy
```groovy
//Recurse down on the default input set and store it in the default input set.
recurseDown()
//Recurse down on the default input set and store it in the input set named 'a'.
recurseDown()>>"a"
//Recurse down on the input set named 'a' and store it in the input set named 'a'.
recurseDown("a")
//Recurse down on the input set named 'a' and store it in the input set named 'b'.
recurseDown("a")>>"b"
```
 

##### For-each loop

This statement loop on all the element of the input set and execute the sub query.<br>
Like all the sets you can specified the input set and a name to store the set.

###### OverPass
```java
foreach{
    ...
};

foreach.a{
    ...
}->.b;

//A full example
way[name="Vannes"][website](47.0,-3.0,48.0,-2.0)->.a;
foreach.a{
    out;
}->.b;
```
###### Java
```java
new ForEach(/** you query here **/);
//A full example
new Way(new Bbox(47.0,-3.0,48.0,-2.0),"name=Vannes", "website").setAlias("a");
new ForEach("a", new Query()).setAlias("b");
```
###### Groovy
```groovy
foreach(/** you query here **/)
//A full example
way(bbox(47.0,-3.0,48.0,-2.0),"name=Vannes", "website")>>"a"
foreach("a", query())>>"b"
```
 

##### Map way/relation to area

Map the relation and ways of the input set to their Overpass API area id counterpart. 
Like all the sets you can specified the input set and a name to store the set.

###### OverPass
```java
map_to_area;
.a map_to_area->.b;
```
###### Java
```java
new MapToArea();
new MapToArea("a").setAlias("b");
```
###### Groovy
```groovy
map_to_area()
map_to_area("a")>>"b"
```

### Request format

A request is composed of a file format, optionals request parameters, a set of statements and a print parameter

#### File format
A file format can be specified to set how the output should be formatted. The available formats are : `json`, `xml`, `csv`.

##### CSV
The `csv` format can be set with different parameters : 
 - `boolean headerLine` (optional) : If set to true, the CSV output will contains the column headers.
 - `char separator` (optional) : If set, the given character will be used as separator
 - `String[] fields` : Array of the field to add tothe output csv.

###### Special field names
The Overpass API can recognize some special field names which represent scpecific OSM information :
 - `::id` :  OSM Object ID 
 - `::type` :  OSM Object type: node, way, relation 
 - `::otype` :  OSM Object as numeric value 
 - `::lat` :  Latitude (available for nodes, or in out center mode) 
 - `::lon` :  Longitude (available for nodes, or in out center mode)
 The following meta information fields are only available, if out meta; is used to output OSM elements.
 - `::version` : OSM object's version number
 - `::timestamp` : Last changed timestamp of an OSM object
 - `::changeset` : Changeset in which the object was changed
 - `::uid` : OSM User id
 - `::user` : OSM User name 
 
 
#### Request parameter
Some parameter of the request can be set :
 - `timeout` : maximum time for teh request execution. If this time is reached, the request ins cancelled.
 - `maxisize` : the maximum size of the request output.

#### Print parameter
The output of the request is printed by the keyword `out` which may be followed by a word describing the verbosity mode, 
ending with a `;`
The verbosity mode keywords are : 
 - `body` : which is the default mode: ids, child elements, and tags are printed. If you want the mode body, you can
  omit the mode attribute. 
 - `skel` : The skeleton print mode is somewhat shorter: No tags are printed in this mode, only ids, child elements,
  and coordinates of nodes.
 - `ids` : Which is the shortest print mode; only ids are printed.
 - `meta` meta is the most complete mode. Beside child elements and tags, also the meta data (timestamp, version, 
 changeset, user name and id) is printed 
The output containg all the elements selected by all the query sets (the default and all the other set with aliases).
 ###### OverPass
 ```java
[out:json][timeout:900][maxsize:1073741824];
...
out skel;

[out:json];
...
out;

[out:csv(::"id", amenity, "contact:website")];
...
out
```
###### Java
```java
new Query(...).format(new JsonOutFormat()).timeout(900).maxsize(1073741824).out(Out.skel);
new Query(...).format(new JsonOutFormat())
new Query(...).format(new CsvOutFormat("::id", "amenity", "contact:website"))
```
###### Groovy
```groovy
query(...) format json timeout 900 maxsize 1073741824 out skel
query(...) format json
query(...) format csv("::id", "amenity", "contact:website")
```
 
 ### Execution
 
 The request can be executed by calling the method `execute(String filePath)` of the `Query` object, with the path of 
 the file where the data should be written.

 Note : execute() will return an InputStream
 
 ###### Java
 ```java
 new Query(...). ... .execute("path/of/the/file")
 ```
 ###### Groovy
 ```groovy
 query(...) ... execute "path/of/the/file"
 ```
 
 ### Full request example
 ###### OverPass
 ```java
[out:json][timeout:900][maxsize:1073741824];
(
    node(51.15,7.0,51.35,7.3);
    rel["boundary"="postal_code"][postal_code=65343];
);
out meta;
 ```
 ###### Java
 ```java
 new Query(
         new Node(new Bbox(51.15,7.0,51.35,7.3))
         .plus(new Rel("boundary=postal_code", "postal_code=65343")))
    .format(new JsonOutFormat())
    .timeout(900)
    .maxsize(1073741824)
    .out(Out.meta)
    .execute("path/of/the/file")
 ```
 ###### Groovy
 ```groovy
 query(node(bbox(51.15,7.0,51.35,7.3)) + ("boundary=postal_code", "postal_code=65343"))
    format json 
    timeout 900 
    maxsize 1073741824 
    out meta 
    execute "path/of/the/file"
 ```
 
 
 ### Groovy scripts
 
 This example shows how to save in a json file all the ways with the tag highway.
 ```groovy
@GrabResolver(name='orbisgis', root='http://repo.orbisgis.org/')
@Grab(group='org.orbisgis', module='overpass4j', version='1.0-SNAPSHOT')

import static org.orbisgis.overpass4j.Out.*
import static  org.orbisgis.overpass4j.OP4JObjects.*

def bbox_osm= bbox(47.65799702121347,-2.756415009498596,47.65963017480206,-2.7535852789878845)

//Get road features
query(way(bbox_osm,"highway")) format json timeout 900 out meta execute "/tmp/fileOSM.json"
println(new File("/tmp/fileOSM.json").text);
 ```
 
  This example shows how to count all node, way and relation buildings and save the result in a CSV file.
  
  ```groovy
@GrabResolver(name='orbisgis', root='http://repo.orbisgis.org/')
@Grab(group='org.orbisgis', module='overpass4j', version='1.0-SNAPSHOT')

import static org.orbisgis.overpass4j.Out.*
import static  org.orbisgis.overpass4j.OP4JObjects.*

def bbox_osm= bbox(47.65799702121347,-2.756415009498596,47.65963017480206,-2.7535852789878845)

//Count the number of buildings for a specific area 
request = query(node(bbox_osm,"building=yes")+way(bbox_osm,"building=yes")+rel(bbox_osm,"building=yes")) format csv("::count", "::count:nodes", "::count:ways", "::count:relations") timeout 900 out count execute "/tmp/fileOSM.csv"
println(new File("/tmp/fileOSM.csv").text);
 ```
  This example shows how to count all node, way and relation buildings with a level tag greater than 0.
  
  ```groovy
@GrabResolver(name='orbisgis', root='http://repo.orbisgis.org/')
@Grab(group='org.orbisgis', module='overpass4j', version='1.0-SNAPSHOT')

import static org.orbisgis.overpass4j.Out.*
import static  org.orbisgis.overpass4j.OP4JObjects.*

def bbox_osm = bbox(47.627338817222,-2.7989387512207,47.679605072474,-2.706241607666)

request = query() format csv("::count", "::count:nodes", "::count:ways", "::count:relations") timeout 300 dataSet set(node(bbox_osm,"building=yes", "building:levels>0" ),way(bbox_osm,"building=yes", "building:levels>0"),rel(bbox_osm,"building=yes","building:levels>0")) out count execute "/tmp/fileOSM.csv"
println(new File("/tmp/fileOSM.csv").text);  
  ```
  
 This example shows how to return all French external reference (key ref:INSEE), used in France to hold the municipality's number as defined by the Institut national de la statistique et des études économiques (INSEE). 
  
  ```groovy
@GrabResolver(name='orbisgis', root='http://repo.orbisgis.org/')
@Grab(group='org.orbisgis', module='overpass4j', version='1.0-SNAPSHOT')

import static org.orbisgis.overpass4j.Out.*
import static  org.orbisgis.overpass4j.OP4JObjects.*

def bbox_osm = bbox(41.1290213474951,-7.492675781249999,51.358061573190916,11.8212890625)

request = query() format csv("ref:INSEE") timeout 300 dataSet set(node(bbox_osm,"ref:INSEE"),) out body execute "/tmp/fileOSM.csv"

println(new File("/tmp/fileOSM.csv").text);  
  ```
  
 This example shows a complex query to count the number of buildings equal to yes and with a building:levels greater than 0. The count operation is applied for each French external reference (key ref:INSEE and admin_level=8) at a specified area.
  
  ```groovy
@GrabResolver(name='orbisgis', root='http://repo.orbisgis.org/')
@Grab(group='org.orbisgis', module='overpass4j', version='1.0-SNAPSHOT')

import static org.orbisgis.overpass4j.Out.*
import static  org.orbisgis.overpass4j.OP4JObjects.*

def bbox_osm = bbox(46.717268685073954,-5.240478515625,49.2032427441791,-0.4119873046875)

request = query() format csv("ref:INSEE","name","::count","::count:nodes","::count:ways","::count:rels") dataSet rel(bbox_osm, "ref:INSEE", "admin_level=8"), map_to_area(), foreach(query(set(defaultSet("d"))).out(), query(set(rel(bbox("area"),"building=yes", "building:levels>0"), way(bbox("area"),"building=yes", "building:levels>0"), node(bbox("area"),"building=yes", "building:levels>0"))).out(count))>>"d" execute "/tmp/fileOSM.csv" 

println(new File("/tmp/fileOSM.csv").text);  
  ```
# Funding

Overpass4J is developed within the following frameworks :


* PAENDORA (Planification, Adaptation et Energie des DOnnées territoriales et Accompagnement, 2017-2020), ADEME Programme: MODEVAL-URBA 2017

* URCLIM  (URban CLIMate services, 2017-2020), JPI Climate Programme (http://www.jpi-climate.eu/nl/25223460-URCLIM.html)

* ANR CENSE (Caractérisation des environnements sonores urbains : vers une approche globale associant données libres, mesures et modélisations, 2016 -2020) (http://www.agence-nationale-recherche.fr/Projet-ANR-16-CE22-0012)
