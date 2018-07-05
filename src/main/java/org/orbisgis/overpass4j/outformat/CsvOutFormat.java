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
package org.orbisgis.overpass4j.outformat;

/**
 * @author Sylvain PALOMINOS (UBS 2018)
 * @author Erwan Bocher (CNRS)
 */
public class CsvOutFormat implements OutFormat {

    private Boolean headerLine;
    private Character separator;
    private String[] fields;

    public CsvOutFormat(boolean headerLine, char separator, String... fields){
        this.headerLine = headerLine;
        this.separator = separator;
        this.fields = fields;
    }

    public CsvOutFormat(boolean headerLine, String... fields){
        this.headerLine = headerLine;
        this.fields = fields;
    }

    public CsvOutFormat(String... fields){
        this.fields = fields;
    }

    @Override
    public String toString(){
        String str = "csv(";
        StringBuilder fieldStr = new StringBuilder();
        for(String fieldName : fields){
            if(fieldStr.length() > 0){
                fieldStr.append(",");
            }
            if(fieldName.contains("::")){
                fieldStr.append(fieldName.replace("::", "::\""));
                fieldStr.append("\"");
            }
            else {
                fieldStr.append(fieldName);
            }
        }
        str+=fieldStr;
        if(headerLine != null){
            str+=";"+headerLine;
        }
        if(separator != null){
            str+=";"+separator;
        }
        str+=")";
        return str;
    }

    public CsvOutFormat call(boolean headerLine, char separator, String... fields){
        return new CsvOutFormat(headerLine, separator, fields);
    }

    public CsvOutFormat call(boolean headerLine, String... fields){
        return new CsvOutFormat(headerLine, fields);
    }

    public CsvOutFormat call(String... fields){
        return new CsvOutFormat(fields);
    }
}
