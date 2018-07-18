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
 */
import groovy.time.TimeCategory

@GrabResolver(name='orbisgis', root='http://repo.orbisgis.org/')
@Grab(group='org.orbisgis', module='overpass4j', version='1.0-SNAPSHOT')

import static org.orbisgis.overpass4j.Out.*
import static  org.orbisgis.overpass4j.OP4JObjects.*

/**
 * This script calculate in OSM for each city of France the number of node, way and relation which are building and
 * which building:level tag is > 0
 *
 * @author Sylvain PALOMINOS (UBS 2018)
 * @author Erwan Bocher (CNRS)
 */

/**
 * Copy the fileIn in the fileOut  without extra linebreak and tabulation.
 * @param fileIn Raw file
 * @param fileOut CSV file
 */
//Method formatting the txt into a valid CSV
def formatOutCsv(fileIn, fileOut){
	new File(fileOut).withWriter { w ->
		prevLine = ""
		first = true
		new File(fileIn).eachLine { line ->
			if(!first){
				if(!prevLine.empty){
					w << prevLine.replaceAll("\t\t\t\t", "")+line.replaceAll("\t\t", "\t")+"\n"
					prevLine=""
				}
				else{
					prevLine = line
				}
			}		
			else{
				w << line + "\n"
				first = false
			}
		}
	}
}

//Global var
start=1
end=99
Date startDate
Date stepDate
Date codeDate
def i
first = true

//Manage arguments with CliBuilder
def cli = new CliBuilder(usage:'osm [options] <pathToTheFile>', header:'Options')
cli.d(longOpt:'departments', args:1, argName:'departments', 'departments to analyze. Values are coma separated and range can be defined with a ":"; Example : 45,16,25:36')
cli._(longOpt:'debug', 'print debug messages')
cli.h(longOpt:'help', 'print this message')

def opt = cli.parse(args)
def errMsg = "Invalid arguments.\nusage: ${cli.usage}\n" +
		"Try `osm --help' for more information."

if (!opt) {
	println "error processing arguments\n"
	return
} else if (opt.h) {
	cli.usage()
	return
} else if(opt.arguments().size()!=1) {
	println errMsg
	return
}  else {
	file = opt.arguments()[0]
	if(new File(file+".txt").exists() && !new File(file+".txt").delete() ){
		println "Unable to delete the file ${file+".txt"}, content will be append."
	}
	if(new File(file+".csv").exists() && !new File(file+".csv").delete() ){
		println "Unable to delete the file ${file+".csv"}, content will be append."
	}
}

debug = opt.debug

//Build the department list
List<String> list = new ArrayList<>()
if(opt.d){
	List<String> tmp = new ArrayList<>()
	for(String s : opt.d.split(",")){
		str = s.trim()
		if(str.contains(":")){
			min = Integer.valueOf(str.split(":")[0])
			max = Integer.valueOf(str.split(":")[1])
			for(j=min; j<=max; j++){
				tmp.add(j<10 ? "0$j" : "$j")
			}
		}
		else {
			tmp.add(str.size() == 1 ? "0$str" : "$str")
		}
	}
	for(String str : tmp) {
		for (j = 0; j < 100; j++) {
			if(j<10) {
				list.add(str + "0" + j)
			}
			else{
				list.add(str + j)
			}
		}
	}
}

if(list.isEmpty()) {
	for (i = start; i <= end; i++) {
		dep = ""
		if (i == 2) {
			dep = "2A"
			for(j=0; j<100; j++){
				if(j<10) {
					list.add(dep + "0" + j)
				}
				else{
					list.add(dep + j)
				}
			}
			dep = "2B"
			for(j=0; j<100; j++){
				if(j<10) {
					list.add(dep + "0" + j)
				}
				else{
					list.add(dep + j)
				}
			}
		}
		dep = i < 10 ? "0$i" : "$i"
		for(j=0; j<100; j++){
			if(j<10) {
				list.add(dep + "0" + j)
			}
			else{
				list.add(dep + j)
			}
		}
	}
}

//Execute the osm requests
if(debug){
	startDate=new Date()
	use(TimeCategory) {
		def duration = startDate
		println "Start at ${String.format("%02d",duration.hours)}:${String.format("%02d",duration.minutes)}:${String.format("%02d",duration.seconds)}"
	}
}
//Iterate on all the code
i=1
retryMax = 10
List<String> error = new ArrayList<>()
for ( String str : list ) {
	if (debug) {
		stepDate = new Date()
	}

	println "INSEE:${str}X  $i/${list.size}"
	retryCount = 0
	success = false
	codeDate = new Date()
	while (!success && retryCount < retryMax) {
		success = query(
					rel("ref:INSEE~${str}.", "admin_level=8"),
					map_to_area(),
					foreach(
						query(set("d")).out(),
						query(
								way(bbox("area.d"), "building", "building:levels>0")+
								rel(bbox("area.d"), "building", "building:levels>0")
						).out(count)
					) >> "d"
				)
				.format(csv(first, "ref:INSEE", "name", "::count", "::count:ways", "::count:relations"))
				.execute(file + ".txt", true, false)

		if (!success) {
			retryCount++
			println "Error on code ${str}XX, retrying $retryCount/$retryMax"
			if(retryCount>=retryMax){
				error.add(str)
			}

		} else {
			first = false
		}
	}
	if(debug){
		use(TimeCategory) {
			def duration = new Date()-codeDate
			println "Last ${String.format("%02d",duration.hours)}:${String.format("%02d",duration.minutes)}:${String.format("%02d",duration.seconds)}"
		}
	}
	i++
}

if(debug){
	use(TimeCategory) {
		def duration = new Date()-startDate
		println "End, script last ${String.format("%02d",duration.hours)}:${String.format("%02d",duration.minutes)}:${String.format("%02d",duration.seconds)}"
	}
}

println "Error on codes : $error"
//End script and format output
println "Start reformat."
formatOutCsv(file+".txt", file+".csv")
new File(file+".txt").delete()
println "End reformat."
println "Result ready, formatted CSV in ${file+".csv"}"
