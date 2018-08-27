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
 * @author Sylvain PALOMINOS (UBS 2018)
 * @author Erwan Bocher (CNRS)
 */
public class Rel extends NRWSet {

    public Rel(String ... filters){
        objectType = ObjectType.REL;
        operator = Operator.UNION;
        for(String str : filters){
            filterList.add(new Filter(str));
        }
        this.subSetList.add(this);
    }

    public Rel(Bbox bbox, String ... filters){
        objectType = ObjectType.REL;
        operator = Operator.UNION;
        this.bbox = bbox;
        for(String str : filters){
            filterList.add(new Filter(str));
        }
        this.subSetList.add(this);
    }

    public Rel(Operator operator, String ... filters){
        this.objectType = ObjectType.REL;
        this.operator =operator;
        for(String str : filters){
            filterList.add(new Filter(str));
        }
        this.subSetList.add(this);
    }

    public Rel(Operator operator, Bbox bbox, String ... filters){
        this.objectType = ObjectType.REL;
        this.operator =operator;
        this.bbox = bbox;
        for(String str : filters){
            filterList.add(new Filter(str));
        }
        this.subSetList.add(this);
    }

    public Rel(Operator operator, Bbox bbox, List<Filter> filterList){
        this.objectType = ObjectType.REL;
        this.operator =operator;
        this.bbox = bbox;
        for(Filter filter : filterList){
            this.filterList.add(filter.copy());
        }
        this.subSetList.add(this);
    }

    public Rel call(String ... filters){
        return new Rel(filters);
    }

    public Rel call(String filter){
        return new Rel(filter);
    }

    public Rel call(Bbox bbox, String ... filters){
        return new Rel(bbox, filters);
    }

    public Rel call(Operator operator, String ... filters){
        return new Rel(operator, filters);
    }

    public Rel call(Operator operator, Bbox bbox, String ... filters){
        return new Rel(operator, bbox, filters);
    }
}
