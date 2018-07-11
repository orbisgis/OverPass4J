package org.orbisgis.overpass4j;

import groovy.lang.GroovyShell;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.orbisgis.overpass4j.outformat.CsvOutFormat;
import org.orbisgis.overpass4j.outformat.JsonOutFormat;
import org.orbisgis.overpass4j.set.*;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class QueryTest {

    private GroovyShell shell;

    @Before
    public void init(){
        shell = new GroovyShell(this.getClass().getClassLoader());
        shell.setProperty("node", new Node());
        shell.setProperty("way", new Way());
        shell.setProperty("area", new Area());
        shell.setProperty("rel", new Rel());
        shell.setProperty("bbox", new Bbox(0, 0, 0, 0));
        shell.setProperty("set", new Set());
        shell.setProperty("query", new Query());
        shell.setProperty("json", new JsonOutFormat());
        shell.setProperty("csv", new CsvOutFormat());
        shell.setProperty("skel", Out.skel);
        shell.setProperty("recurseUp", new RecurseUp());
        shell.setProperty("recurseDown", new RecurseDown());
        shell.setProperty("map_to_area", new MapToArea());
        shell.setProperty("foreach", new ForEach());
    }

    @Test
    public void javaQueryTest() {
        //Test subsets
        Node node = new Node(new Bbox(47, -3, 48, -2), "name=Vannes", "population");
        assertEquals("node[\"name\"=\"Vannes\"][\"population\"](47.0,-3.0,48.0,-2.0);", node.toString());
        Node nodeNoBBox = new Node("name=Vannes", "population");
        assertEquals("node[\"name\"=\"Vannes\"][\"population\"];", nodeNoBBox.toString());
        Way way = new Way(new Bbox(47.0, -3.0, 48.0, -2.0), "name=Vannes", "website");
        assertEquals("way[\"name\"=\"Vannes\"][\"website\"](47.0,-3.0,48.0,-2.0);", way.toString());
        Rel rel = new Rel(new Bbox(47.0, -3.0, 48.0, -2.0), "name=Vannes", "capital");
        assertEquals("rel[\"name\"=\"Vannes\"][\"capital\"](47.0,-3.0,48.0,-2.0);", rel.toString());
        Area area = new Area(new Bbox(47.0, -3.0, 48.0, -2.0), "name=Vannes", "historic");
        assertEquals("area[\"name\"=\"Vannes\"][\"historic\"](47.0,-3.0,48.0,-2.0);", area.toString());

        //Test comparison filter
        Node compFilter = new Node(new Bbox(47, -3, 48, -2), "building:levels>=3", "building:levels<5");
        assertEquals("node(if:t[\"building:levels\"]>=3)(if:t[\"building:levels\"]<5)(47.0,-3.0,48.0,-2.0);", compFilter.toString());
        compFilter = new Node(new Bbox(47, -3, 48, -2), "building:levels>3", "building:levels<=5");
        assertEquals("node(if:t[\"building:levels\"]>3)(if:t[\"building:levels\"]<=5)(47.0,-3.0,48.0,-2.0);", compFilter.toString());

        //Test set
        Set set = new Set(node, way, rel, area);
        assertEquals("(node[\"name\"=\"Vannes\"][\"population\"](47.0,-3.0,48.0,-2.0);" +
                "way[\"name\"=\"Vannes\"][\"website\"](47.0,-3.0,48.0,-2.0);" +
                "rel[\"name\"=\"Vannes\"][\"capital\"](47.0,-3.0,48.0,-2.0);" +
                "area[\"name\"=\"Vannes\"][\"historic\"](47.0,-3.0,48.0,-2.0););", set.toString());

        //Test request format
        assertEquals("[out:json];out;", new Query().format(new JsonOutFormat()).toString());
        assertEquals("[out:csv(::\"id\",amenity,contact:website)];out;",
                new Query().format(new CsvOutFormat("::id", "amenity", "contact:website")).toString());
        assertEquals("[out:json][timeout:900][maxsize:1073741824];();out skel;",
                new Query().format(new JsonOutFormat()).timeout(900).maxsize(1073741824)
                        .dataSet(new Set()).out(Out.skel).toString());

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
        assertEquals("foreach.a{out;}->.b;", new ForEach("a", new Query()).setAlias("b").toString());

        //For Each tests
        assertEquals(".a map_to_area->.b;", new MapToArea("a").setAlias("b").toString());
    }

    @Test
    public void groovyQueryTest() {

        //Test subsets
        String node = "node(bbox(47.0,-3.0,48.0,-2.0),\"name=Vannes\", \"population\")";
        assertEquals("node[\"name\"=\"Vannes\"][\"population\"](47.0,-3.0,48.0,-2.0);", shell.evaluate(node).toString());
        String way = "way(bbox(47.0,-3.0,48.0,-2.0),\"name=Vannes\", \"website\")";
        assertEquals("way[\"name\"=\"Vannes\"][\"website\"](47.0,-3.0,48.0,-2.0);", shell.evaluate(way).toString());
        String rel = "rel(bbox(47.0,-3.0,48.0,-2.0),\"name=Vannes\", \"capital\")";
        assertEquals("rel[\"name\"=\"Vannes\"][\"capital\"](47.0,-3.0,48.0,-2.0);", shell.evaluate(rel).toString());
        String area = "area(bbox(47.0,-3.0,48.0,-2.0),\"name=Vannes\", \"historic\")";
        assertEquals("area[\"name\"=\"Vannes\"][\"historic\"](47.0,-3.0,48.0,-2.0);", shell.evaluate(area).toString());

        //Test set
        String set = "set("+node+","+way+","+rel+","+area+");";
        assertEquals("(node[\"name\"=\"Vannes\"][\"population\"](47.0,-3.0,48.0,-2.0);" +
                "way[\"name\"=\"Vannes\"][\"website\"](47.0,-3.0,48.0,-2.0);" +
                "rel[\"name\"=\"Vannes\"][\"capital\"](47.0,-3.0,48.0,-2.0);" +
                "area[\"name\"=\"Vannes\"][\"historic\"](47.0,-3.0,48.0,-2.0););", shell.evaluate(set).toString());

        //Test request format
        assertEquals("[out:json];out;", shell.evaluate("query() format json").toString());
        assertEquals("[out:csv(::\"id\",amenity,contact:website)];out;",
                shell.evaluate("query() format csv(\"::id\", \"amenity\", \"contact:website\")").toString());
        assertEquals("[out:json][timeout:900][maxsize:1073741824];();out skel;",
                shell.evaluate("query() format json timeout 900 maxsize 1073741824 dataSet set() out skel").toString());

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
        assertEquals("foreach.a{out;}->.b;",shell.evaluate("foreach(\"a\", query())>>\"b\"").toString());

        //For Each tests
        assertEquals(".a map_to_area->.b;", shell.evaluate("map_to_area(\"a\")>>\"b\"").toString());
    }

    @Test
    public void groovyQueryExecuteFileTest() {
        String filePath = "target/osm_data.json";
        String query = "query() format json timeout 900 maxsize 1073741824 dataSet set() out skel execute ";
        //Create the file
        shell.evaluate(query + "\"" +filePath + "\"");
        Assert.assertTrue(new File(filePath).exists());
        //Append in an existing file
        shell.evaluate(query + "(\"" + filePath+ "\", true)");

        filePath = "target/osm_data2.json";
        //Create the file
        shell.evaluate(query + "(\"" + filePath+ "\", false)");
        Assert.assertTrue(new File(filePath).exists());

    }
    
}
