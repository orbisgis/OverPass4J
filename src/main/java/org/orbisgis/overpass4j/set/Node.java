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
package org.orbisgis.overpass4j.set;

import org.orbisgis.overpass4j.Bbox;
import org.orbisgis.overpass4j.Filter;

import java.util.List;

/**
 * A node consists of a single point in space defined by its latitude, longitude and node id.
 *
 * @author Sylvain PALOMINOS (UBS 2018)
 * @author Erwan Bocher (CNRS)
 */
public class Node extends NRWSet {

    public Node(int identifier){
        objectType = ObjectType.NODE;
        operator = Operator.UNION;
        this.bbox = null;
        identifiers = new int[]{identifier};
        this.subSetList.add(this);
    }

    public Node(String ... filters){
        this((Bbox)null, filters);
    }

    public Node(Bbox bbox, String ... filters){
        this(Operator.UNION, bbox, filters);
    }

    public Node(Operator operator, String ... filters){
        this(operator, null, filters);
    }

    public Node(Operator operator, Bbox bbox, String ... filters){
        this.objectType = ObjectType.NODE;
        this.operator =operator;
        this.bbox = bbox;
        for(String str : filters){
            filterList.add(new Filter(str));
        }
        this.subSetList.add(this);
    }

    public Node(Operator operator, Bbox bbox, List<Filter> filterList){
        this.objectType = ObjectType.NODE;
        this.operator =operator;
        this.bbox = bbox;
        for(Filter filter : filterList){
            this.filterList.add(filter.copy());
        }
        this.subSetList.add(this);
    }

    public Node call(int identifier){
        return new Node(identifier);
    }

    public Node call(String filter){
        return new Node(filter);
    }

    public Node call(String ... filters){
        return new Node(filters);
    }

    public Node call(Bbox bbox, String ... filters){
        return new Node(bbox, filters);
    }

    public Node call(Operator operator, String ... filters){
        return new Node(operator, filters);
    }

    public Node call(Operator operator, Bbox bbox, String ... filters){
        return new Node(operator, bbox, filters);
    }
}
