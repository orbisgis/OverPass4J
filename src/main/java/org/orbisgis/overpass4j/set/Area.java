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
 * The area SubSet selects all elements of the chosen type that are inside the given area.
 *
 * @author Sylvain PALOMINOS (UBS 2018)
 * @author Erwan Bocher (CNRS)
 */
public class Area extends SubSet {

    public Area(String ... filters){
        objectType = ObjectType.AREA;
        operator = Operator.UNION;
        for(String str : filters){
            filterList.add(new Filter(str));
        }
    }

    public Area(Bbox bbox, String ... filters){
        objectType = ObjectType.AREA;
        operator = Operator.UNION;
        this.bbox = bbox;
        for(String str : filters){
            filterList.add(new Filter(str));
        }
    }

    public Area(Operator operator, String ... filters){
        this.objectType = ObjectType.AREA;
        this.operator =operator;
        for(String str : filters){
            filterList.add(new Filter(str));
        }
    }

    public Area(Operator operator, Bbox bbox, String ... filters){
        this.objectType = ObjectType.AREA;
        this.operator =operator;
        this.bbox = bbox;
        for(String str : filters){
            filterList.add(new Filter(str));
        }
    }

    public Area(Operator operator, Bbox bbox, List<Filter> filterList){
        this.objectType = ObjectType.AREA;
        this.operator =operator;
        this.bbox = bbox;
        for(Filter filter : filterList){
            this.filterList.add(filter.copy());
        }
    }

    public Area call(String ... filters){
        return new Area(filters);
    }

    public Area call(Bbox bbox, String ... filters){
        return new Area(bbox, filters);
    }

    public Area call(Operator operator, String ... filters){
        return new Area(operator, filters);
    }

    public Area call(Operator operator, Bbox bbox, String ... filters){
        return new Area(operator, bbox, filters);
    }
}
