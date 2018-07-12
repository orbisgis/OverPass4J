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
 *
 *
 * Copyright (C) 2018 CNRS (Lab-STICC UMR CNRS 6285)
 */
package org.orbisgis.overpass4j.outformat;

/**
 * CSV output format returns OSM data as csv document.
 * It requires String parameters to define a list of fields to display and two optional parameters for adding/removing
 * the CSV header line and changing the column separator.
 *
 * @author Sylvain PALOMINOS (UBS 2018)
 * @author Erwan Bocher (CNRS)
 */
public class CsvOutFormat implements OutFormat {

    /** If not set or set to true display the header line in the CSV output. If se to false, hide the header line */
    private Boolean headerLine;
    /** Sets the column separator. If not set the character ' ' (whitespace) will be used */
    private Character separator;
    /** The list of the fields to display */
    private String[] fields;

    /**
     * Full constructor.
     *
     * @param headerLine If true display the header line, if false hide it.
     * @param separator Column separator.
     * @param fields List of fields to display.
     */
    public CsvOutFormat(boolean headerLine, char separator, String... fields){
        this.headerLine = headerLine;
        this.separator = separator;
        this.fields = fields;
    }


    /**
     * Constructor.
     *
     * @param headerLine If true display the header line, if false hide it.
     * @param fields List of fields to display.
     */
    public CsvOutFormat(boolean headerLine, String... fields){
        this.headerLine = headerLine;
        this.fields = fields;
    }


    /**
     * Minimal constructor.
     *
     * @param fields List of fields to display.
     */
    public CsvOutFormat(String... fields){
        this.fields = fields;
    }

    @Override
    public String toString(){
        StringBuilder fieldStr = new StringBuilder();
        fieldStr.append("csv(");
        fieldStr.append(fieldNameToString());
        fieldStr.append(headerLineToString());
        fieldStr.append(separatorToString());
        fieldStr.append(")");
        return fieldStr.toString();
    }

    /**
     * Generates the string use for the OverPass query with the field list.
     *
     * @return A string with the field.
     */
    private String fieldNameToString(){
        StringBuilder str = new StringBuilder();
        for(String fieldName : fields){
            if(str.length() > 0){
                str.append(",");
            }
            if(fieldName.contains("::")){
                str.append(fieldName.replace("::", "::\"")).append("\"");
            }
            else {
                str.append("\"").append(fieldName).append("\"");
            }
        }
        return str.toString();
    }

    /**
     * Generates the string use for the OverPass query with the header line.
     *
     * @return A string with the header line.
     */
    private String headerLineToString() {
        StringBuilder str = new StringBuilder();
        if(headerLine != null){
            str.append(";");
            str.append(headerLine);
        }
        return str.toString();
    }

    /**
     * Generates the string use for the OverPass query with the separator.
     *
     * @return A string with the separator.
     */
    private String separatorToString() {
        StringBuilder str = new StringBuilder();
        if(separator != null){
            str.append(";");
            str.append(separator);
        }
        return str.toString();
    }

    /**
     * Methods used to override the groovy operator '()' which call the corresponding constructor and return the
     * CsvOutFormat object.
     *
     * @param headerLine If true display the header line, if false hide it.
     * @param separator Column separator.
     * @param fields List of fields to display.
     *
     * @return CsvOutFormat object
     */
    public CsvOutFormat call(boolean headerLine, char separator, String... fields){
        return new CsvOutFormat(headerLine, separator, fields);
    }


    /**
     * Methods used to override the groovy operator '()' which call the corresponding constructor and return the
     * CsvOutFormat object.
     *
     * @param headerLine If true display the header line, if false hide it.
     * @param fields List of fields to display.
     *
     * @return CsvOutFormat object
     */
    public CsvOutFormat call(boolean headerLine, String... fields){
        return new CsvOutFormat(headerLine, fields);
    }


    /**
     * Methods used to override the groovy operator '()' which call the corresponding constructor and return the
     * CsvOutFormat object.
     *
     * @param fields List of fields to display.
     *
     * @return CsvOutFormat object
     */
    public CsvOutFormat call(String... fields){
        return new CsvOutFormat(fields);
    }
}
