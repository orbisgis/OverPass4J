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

import java.math.BigDecimal;

/**
 * Specify the bounding box coordinate for the request
 *
 * @author Sylvain Palominos (UBS 2018)
 * @author Erwan Bocher (CNRS)
 */
public class Bbox {

    /** South coordinate */
    private double s;
    /** West coordinate */
    private double w;
    /** North coordinate */
    private double n;
    /** East coordinate */
    private double e;

    private String area;

    /**
     * Main constructor.
     *
     * @param s South coordinate
     * @param w West coordinate
     * @param n North coordinate
     * @param e East coordinate
     */
    public Bbox(double s, double w, double n, double e){
        this.s = s;
        this.w = w;
        this.n = n;
        this.e = e;
    }

    public Bbox(String area){
        this.area = area;
    }

    @Override
    public String toString(){
        if(area != null){
            return area;
        }
        return +s+","+w+","+n+","+e;
    }

    /**
     * This method is used in the groovy script, it overrides the () operator and it calls the main constructor.
     *
     * @param s South coordinate
     * @param w West coordinate
     * @param n North coordinate
     * @param e East coordinate
     *
     * @return The bounding box object
     */
    public Bbox call(double s, double w, double n, double e){
        return new Bbox(s, w, n, e);
    }

    public Bbox call(BigDecimal s, BigDecimal w, BigDecimal n, BigDecimal e){
        return new Bbox(s.doubleValue(), w.doubleValue(), n.doubleValue(), e.doubleValue());
    }

    public Bbox call(String str){
        return new Bbox(str);
    }

    public boolean isArea(){
        return area !=null;
    }
}
