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

import static org.orbisgis.overpass4j.Filter.Operator.*;

/**
 * @author Sylvain PALOMINOS (UBS 2018)
 * @author Erwan Bocher (CNRS)
 */
public class Filter {

    private String key;
    private String value;
    private Operator operator;

    public enum Operator{EQUALS, NOT_EQUALS, EXISTS, NOT_EXISTS, REGEX, NOT_REGEX, SUP, SUP_EQ, INF, INF_EQ}

    private Filter(Operator op, String key, String value){
        this.operator = op;
        this.key = key;
        this.value = value;
    }

    public Filter(String filterStr){
        if(filterStr.contains("!=")){
            key = filterStr.split("!=")[0].replaceAll("\"", "");
            value = filterStr.split("!=")[1].replaceAll("\"", "");
            operator = Operator.NOT_EQUALS;
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
        else if(filterStr.contains(">=")){
            key = filterStr.split(">=")[0].replaceAll("\"", "");
            value = filterStr.split(">=")[1].replaceAll("\"", "");
            operator = SUP_EQ;
        }
        else if(filterStr.contains(">")){
            key = filterStr.split(">")[0].replaceAll("\"", "");
            value = filterStr.split(">")[1].replaceAll("\"", "");
            operator = SUP;
        }
        else if(filterStr.contains("<=")){
            key = filterStr.split("<=")[0].replaceAll("\"", "");
            value = filterStr.split("<=")[1].replaceAll("\"", "");
            operator = INF_EQ;
        }
        else if(filterStr.contains("<")){
            key = filterStr.split("<")[0].replaceAll("\"", "");
            value = filterStr.split("<")[1].replaceAll("\"", "");
            operator = INF;
        }
        else if(filterStr.contains("=")){
            key = filterStr.split("=")[0].replaceAll("\"", "");
            value = filterStr.split("=")[1].replaceAll("\"", "");
            operator = Operator.EQUALS;
        }
        else {
            key = filterStr.replaceAll("\"", "");
            operator = Operator.EXISTS;
        }
        key = key.trim();
        if(value != null) {
            value = value.trim();
        }
    }

    public String getKey(){
        return key;
    }

    @Override
    public String toString(){
        switch(operator){
            case NOT_EQUALS:    return"[\""+key+"\"!=\""+value+"\"]";
            case EQUALS:        return"[\""+key+"\"=\""+value+"\"]";
            case NOT_REGEX:     return"[\""+key+"\"!~\""+value+"\"]";
            case REGEX:         return"[\""+key+"\"~\""+value+"\"]";
            case NOT_EXISTS:    return"[!"+"\""+key+"\"]";
            case EXISTS:        return"[\""+key+"\"]";
            case SUP:           return "(if:t[\""+key+"\"]>"+value+")";
            case SUP_EQ:        return "(if:t[\""+key+"\"]>="+value+")";
            case INF:           return "(if:t[\""+key+"\"]<"+value+")";
            case INF_EQ:        return "(if:t[\""+key+"\"]<="+value+")";
            default:            return "";
        }
    }

    public Filter copy(){
        return new Filter(operator, key, value);
    }
}
