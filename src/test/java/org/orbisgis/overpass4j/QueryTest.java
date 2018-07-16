package org.orbisgis.overpass4j;

import groovy.lang.GroovyShell;
import org.junit.Before;
import org.junit.Test;
import org.orbisgis.overpass4j.outformat.CsvOutFormat;
import org.orbisgis.overpass4j.outformat.JsonOutFormat;
import org.orbisgis.overpass4j.outformat.XmlOutFormat;
import org.orbisgis.overpass4j.set.*;

import java.io.File;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class QueryTest {

    private GroovyShell shell;

    @Before
    public void init(){
        shell = new GroovyShell(this.getClass().getClassLoader());

        shell.setProperty("area", OP4JObjects.area);
        shell.setProperty("bbox", OP4JObjects.bbox);
        shell.setProperty("csv", OP4JObjects.csv);
        shell.setProperty("defaultSet", OP4JObjects.defaultSet);
        shell.setProperty("foreach", OP4JObjects.foreach);
        shell.setProperty("json", OP4JObjects.json);
        shell.setProperty("map_to_area", OP4JObjects.map_to_area);
        shell.setProperty("node", OP4JObjects.node);
        shell.setProperty("query", OP4JObjects.query);
        shell.setProperty("recurseDown", OP4JObjects.recurseDown);
        shell.setProperty("recurseUp", OP4JObjects.recurseUp);
        shell.setProperty("rel", OP4JObjects.rel);
        shell.setProperty("set", OP4JObjects.set);
        shell.setProperty("way", OP4JObjects.way);
        shell.setProperty("xml", OP4JObjects.xml);

        shell.setProperty("asc", Out.asc);
        shell.setProperty("bb", Out.bb);
        shell.setProperty("body", Out.body);
        shell.setProperty("center", Out.center);
        shell.setProperty("count", Out.count);
        shell.setProperty("geom", Out.geom);
        shell.setProperty("ids", Out.ids);
        shell.setProperty("meta", Out.meta);
        shell.setProperty("qt", Out.qt);
        shell.setProperty("skel", Out.skel);
        shell.setProperty("tags", Out.tags);
    }

    @Test
    public void javaQueryTest() {
        //Test subsets
        //Test Node
        Node node = new Node(new Bbox(47, -3, 48, -2), "name=Vannes", "population");
        assertEquals("node[\"name\"=\"Vannes\"][\"population\"](47.0,-3.0,48.0,-2.0);", node.toString());
        Node nodeNoBBox = new Node("name=Vannes", "population");
        assertEquals("node[\"name\"=\"Vannes\"][\"population\"];", nodeNoBBox.toString());

        //Test Way
        Way way = new Way(new Bbox(47.0, -3.0, 48.0, -2.0), "name=Vannes", "website");
        assertEquals("way[\"name\"=\"Vannes\"][\"website\"](47.0,-3.0,48.0,-2.0);", way.toString());

        //Test Rel
        Rel rel = new Rel(new Bbox(47.0, -3.0, 48.0, -2.0), "name=Vannes", "capital");
        assertEquals("rel[\"name\"=\"Vannes\"][\"capital\"](47.0,-3.0,48.0,-2.0);", rel.toString());

        //Test Area
        Area area = new Area(new Bbox(47.0, -3.0, 48.0, -2.0), "name=Vannes", "historic");
        assertEquals("area[\"name\"=\"Vannes\"][\"historic\"](47.0,-3.0,48.0,-2.0);", area.toString());

        //Test set add and remove
        Set set1 = new Node("name=Vannes", "population").plus(new Way("name=Vannes", "population")).plus(
                new Rel("name=Vannes", "population"));
        Set set2 = new Node("name=Vannes", "historic").plus(new Way("name=Vannes", "historic")).plus(
                new Rel("name=Vannes", "historic"));
        assertEquals("(node[\"name\"=\"Vannes\"][\"population\"];way[\"name\"=\"Vannes\"][\"population\"];" +
                        "rel[\"name\"=\"Vannes\"][\"population\"];-node[\"name\"=\"Vannes\"][\"historic\"];" +
                        "-way[\"name\"=\"Vannes\"][\"historic\"];-rel[\"name\"=\"Vannes\"][\"historic\"];);",
                set1.minus(set2).toString());

        //Test operator
        Node nodeOp = new Node("name=Vannes", "name!=Vannes", "name!~Vannes", "name~Vannes", "!name");
        assertEquals("node[\"name\"=\"Vannes\"][\"name\"!=\"Vannes\"][\"name\"!~\"Vannes\"][\"name\"~\"Vannes\"]" +
                "[!\"name\"];", nodeOp.toString());

        //Test comparison filter
        Node compFilter = new Node(new Bbox(47, -3, 48, -2), "building:levels>=3", "building:levels<5");
        assertEquals("node(if:t[\"building:levels\"]>=3)(if:t[\"building:levels\"]<5)(47.0,-3.0,48.0,-2.0);", compFilter.toString());
        compFilter = new Node(new Bbox(47, -3, 48, -2), "building:levels > 3", "building:levels<=5");
        assertEquals("node(if:t[\"building:levels\"]>3)(if:t[\"building:levels\"]<=5)(47.0,-3.0,48.0,-2.0);", compFilter.toString());

        //Test set
        Set set = node.plus(way).plus(rel).plus(area);
        assertEquals("(node[\"name\"=\"Vannes\"][\"population\"](47.0,-3.0,48.0,-2.0);" +
                "way[\"name\"=\"Vannes\"][\"website\"](47.0,-3.0,48.0,-2.0);" +
                "rel[\"name\"=\"Vannes\"][\"capital\"](47.0,-3.0,48.0,-2.0);" +
                "area[\"name\"=\"Vannes\"][\"historic\"](47.0,-3.0,48.0,-2.0););", set.toString());

        //Test request format
        assertEquals("[out:json];", new Query().format(new JsonOutFormat()).toString());
        assertEquals("[out:csv(::\"id\",\"amenity\",\"contact:website\")];out;",
                new Query().format(new CsvOutFormat("::id", "amenity", "contact:website")).out().toString());
        assertEquals("[out:json][timeout:900][maxsize:1073741824];();out skel;",
                new Query(new Set("")).format(new JsonOutFormat()).timeout(900).maxsize(1073741824).out(Out.skel).toString());
        assertEquals("[out:xml][bbox:25.0,6.02,28.68,7.85];();out;",  new Query(new Set("")).format(new XmlOutFormat())
                .bbox(25.0, 6.02, 28.68, 7.85).out().toString());

        //Recurse tests
        assertEquals("<;", new RecurseUp().toString());
        assertEquals("<->.a;", new RecurseUp().setAlias("a").toString());
        assertEquals(".a<;", new RecurseUp("a").toString());
        assertEquals(".a<->.b;", new RecurseUp("a").setAlias("b").toString());

        assertEquals(">;", new RecurseDown().toString());
        assertEquals(">->.a;", new RecurseDown().setAlias("a").toString());
        assertEquals(".a>;", new RecurseDown("a").toString());
        assertEquals(".a>->.b;", new RecurseDown("a").setAlias("b").toString());

        //For Each tests
        assertEquals("foreach.a->.b();", new ForEach("a", new Query()).setAlias("b").toString());

        //For Each tests
        assertEquals(".a map_to_area->.b;", new MapToArea("a").setAlias("b").toString());
    }

    @Test
    public void groovyQueryTest() {
        //Test Node
        String node = "node(bbox(47.0,-3.0,48.0,-2.0),\"name=Vannes\", \"population\")";
        assertEquals("node[\"name\"=\"Vannes\"][\"population\"](47.0,-3.0,48.0,-2.0);", shell.evaluate(node).toString());
        String nodeNoBBox = "node(\"name=Vannes\", \"population\")";
        assertEquals("node[\"name\"=\"Vannes\"][\"population\"];", shell.evaluate(nodeNoBBox).toString());

        //Test Way
        String way = "way(bbox(47.0,-3.0,48.0,-2.0),\"name=Vannes\", \"website\")";
        assertEquals("way[\"name\"=\"Vannes\"][\"website\"](47.0,-3.0,48.0,-2.0);", shell.evaluate(way).toString());

        //Test Rel
        String rel = "rel(bbox(47.0,-3.0,48.0,-2.0),\"name=Vannes\", \"capital\")";
        assertEquals("rel[\"name\"=\"Vannes\"][\"capital\"](47.0,-3.0,48.0,-2.0);", shell.evaluate(rel).toString());

        //Test Area
        String area = "area(bbox(47.0,-3.0,48.0,-2.0),\"name=Vannes\", \"historic\")";
        assertEquals("area[\"name\"=\"Vannes\"][\"historic\"](47.0,-3.0,48.0,-2.0);", shell.evaluate(area).toString());

        //Test set add and remove
        String set1 = "node(\"name=Vannes\", \"population\")+way(\"name=Vannes\", \"population\")+" +
                "rel(\"name=Vannes\", \"population\")";
        String set2 = "node(\"name=Vannes\", \"historic\")+way(\"name=Vannes\", \"historic\")+" +
                "rel(\"name=Vannes\", \"historic\")";
        assertEquals("(node[\"name\"=\"Vannes\"][\"population\"];way[\"name\"=\"Vannes\"][\"population\"];" +
                        "rel[\"name\"=\"Vannes\"][\"population\"];node[\"name\"=\"Vannes\"][\"historic\"];" +
                        "way[\"name\"=\"Vannes\"][\"historic\"];rel[\"name\"=\"Vannes\"][\"historic\"];);",
                shell.evaluate(set1+"+"+set2).toString());
        assertEquals("(node[\"name\"=\"Vannes\"][\"population\"];way[\"name\"=\"Vannes\"][\"population\"];" +
                        "rel[\"name\"=\"Vannes\"][\"population\"];-node[\"name\"=\"Vannes\"][\"historic\"];" +
                        "way[\"name\"=\"Vannes\"][\"historic\"];rel[\"name\"=\"Vannes\"][\"historic\"];);",
                shell.evaluate(set1+"-"+set2).toString());

        //Test operator
        String nodeOp = "node(\"name=Vannes\", \"name!=Vannes\", \"name!~Vannes\", \"name~Vannes\", \"!name\")";
        assertEquals("node[\"name\"=\"Vannes\"][\"name\"!=\"Vannes\"][\"name\"!~\"Vannes\"][\"name\"~\"Vannes\"]" +
                "[!\"name\"];", shell.evaluate(nodeOp).toString());

        //Test comparison filter
        String compFilter = "node(bbox(47, -3, 48, -2), \"building:levels>=3\", \"building:levels<5\")";
        assertEquals("node(if:t[\"building:levels\"]>=3)(if:t[\"building:levels\"]<5)(47.0,-3.0,48.0,-2.0);",
                shell.evaluate(compFilter).toString());
        compFilter = "node(bbox(47, -3, 48, -2), \"building:levels > 3\", \"building:levels<=5\")";
        assertEquals("node(if:t[\"building:levels\"]>3)(if:t[\"building:levels\"]<=5)(47.0,-3.0,48.0,-2.0);",
                shell.evaluate(compFilter).toString());

        //Test set
        String set = "set("+node+","+way+","+rel+","+area+");";
        assertEquals("(node[\"name\"=\"Vannes\"][\"population\"](47.0,-3.0,48.0,-2.0);" +
                "way[\"name\"=\"Vannes\"][\"website\"](47.0,-3.0,48.0,-2.0);" +
                "rel[\"name\"=\"Vannes\"][\"capital\"](47.0,-3.0,48.0,-2.0);" +
                "area[\"name\"=\"Vannes\"][\"historic\"](47.0,-3.0,48.0,-2.0););", shell.evaluate(set).toString());

        //Test request format
        assertEquals("[out:json];", shell.evaluate("query() format json").toString());
        assertEquals("[out:csv(::\"id\",\"amenity\",\"contact:website\")];out;",
                shell.evaluate("query() format csv(\"::id\", \"amenity\", \"contact:website\") out()").toString());
        assertEquals("[out:json][timeout:900][maxsize:1073741824];();out skel;",
                shell.evaluate("query(set()) format json timeout 900 maxsize 1073741824 out skel").toString());
        assertEquals("[out:xml][bbox:25.0,6.02,28.68,7.85];();out;",
                shell.evaluate("query(set()).format(xml).bbox(25.0, 6.02, 28.68, 7.85).out()").toString());

        //Recurse tests
        assertEquals("<;",shell.evaluate("recurseUp()").toString());
        assertEquals("<->.a;", shell.evaluate("recurseUp()>>\"a\"").toString());
        assertEquals(".a<;", shell.evaluate("recurseUp(\"a\")").toString());
        assertEquals(".a<->.b;", shell.evaluate("recurseUp(\"a\")>>\"b\"").toString());

        assertEquals(">;", shell.evaluate("recurseDown()").toString());
        assertEquals(">->.a;",shell.evaluate("recurseDown()>>\"a\"").toString());
        assertEquals(".a>;", shell.evaluate("recurseDown(\"a\")").toString());
        assertEquals(".a>->.b;", shell.evaluate("recurseDown(\"a\")>>\"b\"").toString());

        //For Each tests
        assertEquals("foreach.a->.b();",shell.evaluate("foreach(\"a\", query())>>\"b\"").toString());

        //For Each tests
        assertEquals(".a map_to_area->.b;", shell.evaluate("map_to_area(\"a\")>>\"b\"").toString());
    }

    @Test
    public void groovyQueryExecuteFileTest() {
        String filePath = "target/osm_data.json";
        String badQuery = "query(set(node(\"\\\"\"))) format json timeout 900 maxsize 1073741824 out skel execute ";
        String query = "query(set()) format json timeout 900 maxsize 1073741824 out skel execute ";
        //Create the file
        assertEquals(true, shell.evaluate(query + "\"" +filePath + "\""));
        assertTrue(new File(filePath).exists());
        //Append in an existing file
        assertEquals(true, shell.evaluate(query + "(\"" + filePath+ "\", true)"));

        filePath = "target/osm_data2.json";
        //Create the file
        assertEquals(true, shell.evaluate(query + "(\"" + filePath+ "\", false)"));
        assertTrue(new File(filePath).exists());

        //Test bad request
        filePath = "target/osm_data2.json";
        //Create the file
        assertEquals(false, shell.evaluate(badQuery + "(\"" + filePath+ "\", false)"));
        assertTrue(new File(filePath).exists());

        //Test the stream
        Object o = shell.evaluate(query + "()");
        assertTrue(o instanceof InputStream);
        o = shell.evaluate(badQuery + "()");
        assertTrue(o instanceof InputStream);
    }
}
