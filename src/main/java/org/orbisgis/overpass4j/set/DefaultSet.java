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
public class DefaultSet extends SubSet {

    public DefaultSet(){
        this.alias = "_";
    }

    public DefaultSet(String alias){
        this.alias = alias;
    }

    public DefaultSet(SubSet set){
        this.alias = set.alias;
    }

    @Override
    public String toString(){
        return "."+alias+";";
    }

    public DefaultSet call(){
        return new DefaultSet();
    }

    public DefaultSet call(String alias){
        return new DefaultSet(alias);
    }

    public DefaultSet call(SubSet set){
        return new DefaultSet(set);
    }
}
