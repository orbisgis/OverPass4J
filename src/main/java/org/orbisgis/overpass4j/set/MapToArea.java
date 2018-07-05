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

/**
 * @author Sylvain PALOMINOS (UBS 2018)
 * @author Erwan Bocher (CNRS)
 */
public class MapToArea extends SubSet {

    private String inputAlias;

    public MapToArea(){}

    public MapToArea(String inputAlias){
        this.inputAlias = inputAlias;
    }

    public MapToArea(SubSet set){
        this.inputAlias = set.alias;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        if(inputAlias != null){
            str.append(".").append(inputAlias).append(" ");
        }
        str.append("map_to_area");
        if(alias != null){
            str.append("->.").append(alias);
        }
        str.append(";");
        return str.toString();
    }

    public MapToArea call(){
        return new MapToArea();
    }

    public MapToArea call(String inputAlias){
        return new MapToArea(inputAlias);
    }

    public MapToArea call(SubSet set){
        return new MapToArea(set);
    }
}
