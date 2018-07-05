/*
 * This file is part of the OverPass4J library.
 *
 * OverPass4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OverPass4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OverPass4J. If not, see <http://www.gnu.org/licenses/>.
 */
package org.orbisgis.overpass4j;

import org.orbisgis.overpass4j.outformat.CsvOutFormat;
import org.orbisgis.overpass4j.outformat.JsonOutFormat;
import org.orbisgis.overpass4j.outformat.XmlOutFormat;
import org.orbisgis.overpass4j.set.*;

/**
 * @author Sylvain PALOMINOS (UBS 2018)
 * @author Erwan Bocher (CNRS)
 */
public class OP4JObjects {

    public static Bbox bbox = new Bbox(0,0,0,0);


    public static Node node = new Node();
    public static Way way = new Way();
    public static Rel rel = new Rel();
    public static Area area = new Area();

    public static DefaultSet defaultSet = new DefaultSet();
    public static RecurseUp recurseUp = new RecurseUp();
    public static RecurseDown recurseDown = new RecurseDown();

    public static MapToArea map_to_area = new MapToArea();

    public static Set set = new Set();
    public static ForEach foreach = new ForEach();

    public static Query query = new Query();

    public static XmlOutFormat xml = new XmlOutFormat();
    public static JsonOutFormat json = new JsonOutFormat();
    public static CsvOutFormat csv = new CsvOutFormat();

}
