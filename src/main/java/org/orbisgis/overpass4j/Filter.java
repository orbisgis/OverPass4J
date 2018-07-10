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

/**
 * @author Sylvain PALOMINOS (UBS 2018)
 * @author Erwan Bocher (CNRS)
 */
public class Filter {

    private String key;
    private String value;
    private Operator operator;

    public enum Operator{EQUALS, NOT_EQUALS, EXISTS, NOT_EXISTS, REGEX, NOT_REGEX}

    public Filter(String filterStr){
        if(filterStr.contains("!=")){
            key = filterStr.split("!=")[0].replaceAll("\"", "");
            value = filterStr.split("!=")[1].replaceAll("\"", "");
            operator = Operator.NOT_EQUALS;
        }
        else if(filterStr.contains("=")){
            key = filterStr.split("=")[0].replaceAll("\"", "");
            value = filterStr.split("=")[1].replaceAll("\"", "");
            operator = Operator.EQUALS;
        }
        else if(filterStr.contains("!~")){
            key = filterStr.split("!~")[0].replaceAll("\"", "");
            value = filterStr.split("!~")[1].replaceAll("\"", "");
            operator = Operator.NOT_REGEX;
        }
        else if(filterStr.contains("~")){
            key = filterStr.split("~")[0].replaceAll("\"", "");
            value = filterStr.split("~")[1].replaceAll("\"", "");
            operator = Operator.REGEX;
        }
        else if(filterStr.contains("!")){
            key = filterStr.replaceAll("[\"!]", "");
            value = null;
            operator = Operator.NOT_EXISTS;
        }
        else {
            key = filterStr.replaceAll("\"", "");
            operator = Operator.EXISTS;
        }
    }

    public String getKey(){
        return key;
    }

    @Override
    public String toString(){
        String str = "";
        switch(operator){
            case NOT_EQUALS:
                str+="\""+key+"\"!=\""+value+"\"";
                break;
            case EQUALS:
                str+="\""+key+"\"=\""+value+"\"";
                break;
            case NOT_REGEX:
                str+="\""+key+"\"!~\""+value+"\"";
                break;
            case REGEX:
                str+="\""+key+"\"~\""+value+"\"";
                break;
            case NOT_EXISTS:
                str+="!"+"\""+key+"\"";
                break;
            case EXISTS:
                str+="\""+key+"\"";
                break;
        }
        return str;
    }

    public Filter copy(){
        return new Filter(this.toString().replaceAll("[\\[\\]]", ""));
    }
}
