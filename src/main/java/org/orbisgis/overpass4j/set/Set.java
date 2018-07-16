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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Sylvain PALOMINOS (UBS 2018)
 * @author Erwan Bocher (CNRS)
 */
public class Set {

    protected List<SubSet> subSetList = new ArrayList<>();
    protected String alias;
    private String subSetAlias;

    public Set(String subSetAlias){
        this.subSetAlias = subSetAlias;
    }

    public Set(SubSet subSet){
        subSetList.add(subSet);
    }

    private Set(SubSet... subSets){
        subSetList.addAll(Arrays.asList(subSets));
    }

    protected Set(){ }

    public Set plus(Set set){
        List<SubSet> subList = new ArrayList<>();
        for(SubSet subSet : subSetList){
            subList.add(subSet.copy());
        }
        for(SubSet subSet : set.getSubSetList()){
            subList.add(subSet.copy());
        }
        return new Set(subList.toArray(new SubSet[]{}));
    }

    public Set minus(Set set){
        List<SubSet> subList = new ArrayList<>();
        for(SubSet subSet : subSetList){
            subList.add(subSet.copy());
        }
        for(SubSet subSet : set.subSetList){
            SubSet sub = subSet.copy();
            if(sub.getOperator().equals(SubSet.Operator.UNION)){
                sub.setOperator(SubSet.Operator.DIFFERENCE);
            }
            else if(sub.getOperator().equals(SubSet.Operator.DIFFERENCE)){
                sub.setOperator(SubSet.Operator.UNION);
            }
            subList.add(sub);
        }
        return new Set(subList.toArray(new SubSet[]{}));
    }

    public List<SubSet> getSubSetList(){
        return subSetList;
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder("(");
        if(subSetAlias != null && !subSetAlias.isEmpty()){
            str.append("\"").append(subSetAlias).append("\"");
        }
        else if(subSetList != null){
            for (SubSet subSet : subSetList) {
                if(subSet != null) {
                    str.append(subSet.toString());
                }
            }
        }
        str.append(")");
        if(alias != null && !alias.isEmpty()){
            str.append("->.");
            str.append(alias);
        }
        str.append(";");
        return str.toString();
    }

    public Set rightShift(String alias){
        return setAlias(alias);
    }

    public Set setAlias(String alias){
        this.alias = alias;
        return this;
    }

    public Set call(SubSet... subSets){
        return new Set(subSets);
    }
}
