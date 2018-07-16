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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sylvain PALOMINOS (UBS 2018)
 * @author Erwan Bocher (CNRS)
 */
public abstract class SubSet extends Set{

    protected ObjectType objectType;
    protected Operator operator;
    protected List<Filter> filterList = new ArrayList<>();
    protected Bbox bbox;
    protected String alias;
    private static String[] RECURSE_TAGS = new String[]{"n", "w", "r", "bn", "bw", "br"};

    public Operator getOperator(){
        return operator;
    }

    protected enum ObjectType {
        NODE,WAY,REL,AREA,NWR,DERIVED
    }

    protected enum Operator{
        UNION, DIFFERENCE, INTERSECT;
        public static Operator getOperator(String str){
            if(str.equals("+")){
                return UNION;
            }
            else if(str.equals("-")){
                return DIFFERENCE;
            }
            else if(str.equals("^")){
                return INTERSECT;
            }
            else{
                return null;
            }
        }
    }

    private String operatorToString(){
        switch(operator){
            case DIFFERENCE:
                return "-";
            case INTERSECT:
                return ".";
            default :
                return "";
        }
    }

    private String filterToString(){
        StringBuilder str = new StringBuilder();
        for(Filter filter : filterList){
            boolean tagFound = false;
            for(String tag : RECURSE_TAGS){
                if(filter.getKey().startsWith(tag+".") || filter.getKey().equals(tag)){
                    tagFound = true;
                }
            }
            if(tagFound){
                str.append(filter.toString());
            }
            else {
                str.append(filter.toString());
            }
        }
        return str.toString();
    }

    private String bboxToString(){
        if(bbox != null){
            return "("+bbox.toString()+")";
        }
        return "";
    }

    private String aliasToString(){
        if(alias != null){
            return "->."+alias;
        }
        return "";
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(operatorToString());
        str.append(objectType.name().toLowerCase());
        str.append(filterToString());
        if(bbox != null){
            str.append(bboxToString());
        }
        str.append(aliasToString());
        str.append(";");
        return str.toString();
    }

    public void setOperator(Operator op){
        this.operator = op;
    }

    //Groovy overloading
    public SubSet rightShift(String alias){
        return setAlias(alias);
    }

    public SubSet setAlias(String alias){
        this.alias = alias;
        return this;
    }

    public SubSet copy(){
        switch(this.objectType){
            case REL:
                return new Rel(this.operator, this.bbox, this.filterList).setAlias(this.alias);
            case WAY:
                return new Way(this.operator, this.bbox, this.filterList).setAlias(this.alias);
            case NODE:
                return new Node(this.operator, this.bbox, this.filterList).setAlias(this.alias);
            case AREA:
                return new Area(this.operator, this.bbox, this.filterList).setAlias(this.alias);
        }
        return null;
    }
}
