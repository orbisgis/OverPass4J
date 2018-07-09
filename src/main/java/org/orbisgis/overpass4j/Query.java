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

import org.orbisgis.overpass4j.outformat.OutFormat;
import org.orbisgis.overpass4j.set.Set;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author Sylvain PALOMINOS (UBS 2018)
 * @author Erwan Bocher (CNRS)
 */
public class Query {

    private String API_URL = "http://overpass-api.de/api/interpreter?data=";

    private OutFormat format;
    private int timeout = -1;
    private int maxsize = -1;
    private Set[] dataSet;
    private String[] outs;
    private Bbox bbox;

    public Query format(OutFormat format) {
        this.format = format;
        return this;
    }

    public Query call(OutFormat format) {
        this.format = format;
        return this;
    }

    public Query call() {
        return new Query();
    }

    public Query timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public Query maxsize(int maxsize) {
        this.maxsize = maxsize;
        return this;
    }

    public Query dataSet(Set... set) {
        this.dataSet = set;
        return this;
    }

    public Query out(String... outs){
        this.outs = outs;
        return this;
    }

    public Query out(){
        this.outs = new String[]{};
        return this;
    }

    public Query out(Out... outs){
        this.outs = new String[outs.length];
        for(int i=0; i<outs.length; i++){
            this.outs[i] = outs[i].name();
        }
        return this;
    }

    public Query bbox(double s, double w, double n, double e){
        this.bbox = new Bbox(s, w, n, e);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        if (format != null) {
            str.append("[out:").append(format).append("]");
        }
        if (bbox != null) {
            str.append("[bbox:").append(bbox).append("]");
        }
        if (timeout != -1) {
            str.append("[timeout:").append(timeout).append("]");
        }
        if (maxsize != -1) {
            str.append("[maxsize:").append(maxsize).append("]");
        }
        if (str.length() > 0) {
            str.append(";");
        }
        if(dataSet != null) {
            for (Set set : dataSet) {
                str.append(set.toString());
            }
        }

        str.append("out");
        if(outs != null) {
            for (String out : outs) {
                str.append(" ").append(out);
            }
        }
        str.append(";");
        return str.toString();
    }
    
    
    /**
     * Execute the query and save the result into a file.
     * If the file exists it will be removed
     * @param filePath
     * @return
     * @throws IOException 
     */
     public boolean execute(String filePath) throws IOException {
         return execute(filePath, true);
     }

    /**
     * Execute the query and save the result into a file
     * @param filePath the path of the file
     * @param removeFile true to remove the file, false 
     * to append in an existing file.
     * @return
     * @throws IOException 
     */
    public boolean execute(String filePath, boolean removeFile) throws IOException {        
        File file = new File(filePath);        
        if(removeFile && file.exists()){
            file.delete();
        }
        URL url = new URL(API_URL + URLEncoder.encode(this.toString()));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            OutputStream outStream = new FileOutputStream(file);
            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            while ((bytesRead = connection.getInputStream().read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            connection.getInputStream().close();
            outStream.close();
            return true;
        }
        return false;
    }

    /**
     * Execute the query and return an InputStream that contains the result
     * @return
     * @throws IOException 
     */    
    public InputStream execute() throws IOException {
        URL url = new URL(API_URL + URLEncoder.encode(this.toString()));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return connection.getInputStream();
        }
        return null;
    }
}
