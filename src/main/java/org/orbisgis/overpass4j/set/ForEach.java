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

import org.orbisgis.overpass4j.Query;

/**
 * @author Sylvain PALOMINOS (UBS 2018)
 * @author Erwan Bocher (CNRS)
 */
public class ForEach extends SubSet {

    private Query[] queries;
    private String inputSetAlias;

    public ForEach(Query... queries){
        this.queries = queries;
    }

    public ForEach(String inputSetAlias, Query... queries){
        this.queries = queries;
        this.inputSetAlias = inputSetAlias;
    }

    public ForEach call(Query... queries){
        return new ForEach(queries);
    }

    public ForEach call(String inputSetAlias, Query... queries){
        return new ForEach(inputSetAlias, queries);
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder("foreach");
        if(inputSetAlias != null && !inputSetAlias.isEmpty()){
            str.append(".");
            str.append(inputSetAlias);
        }
        str.append("{");
        for(Query query : queries) {
            query.format(null);
            str.append(query.toString());
        }
        str.append("}");
        if(alias != null && !alias.isEmpty()){
            str.append("->.");
            str.append(alias);
        }
        str.append(";");
        return str.toString();
    }
}
