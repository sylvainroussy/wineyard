var data = [];
var baseMaps;
var map;
var controlLayer = {};
var baseMapLayer = [];
var overlayMaps=[];
var legend ;

var meteoResult;
var focusEnabled = true;

function initGIS() {
	
	
	
	var mazenayLat = 46.9033543;
	var mazenayLong = 4.5959406;

	var nolayLat = 46.9033543;
	var nolayLong = 4.5959406;

	var meursaultLat = 46.9033543;
	var meursaultLong = 4.5959406;

	// BASE LAYERS
	var osmUrl = 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
	var osmAttrib = 'Map data © <a href="https://openstreetmap.org">OpenStreetMap</a> contributors';
	var osm = new L.TileLayer(osmUrl, {
		maxZoom : 18,
		attribution : osmAttrib
	});
	
	var reliefLayer = new L.TileLayer("https://maps-for-free.com/../layer/relief/z{z}/row{y}/{z}_{x}-{y}.jpg", {
		maxZoom : 18,
		attribution : osmAttrib
	});
	
	var topoMap = new L.TileLayer("https://{s}.tile.opentopomap.org/{z}/{x}/{y}.png", {
		maxZoom : 18,
		attribution : osmAttrib
	});
	
	var landUrl = 'http://{s}.tile.thunderforest.com/landscape/{z}/{x}/{y}.png';
	var thunAttrib = '&copy; '+osmUrl+' Contributors & '+landUrl;
	var landMap = new L.TileLayer(landUrl, {
		maxZoom : 18,
		attribution : thunAttrib
	});
	
	baseMaps = {
		    "osm": osm,
		    //"relief": reliefLayer,
		    "topoMap":topoMap,
		    //"landMap":landMap
	};


	map = L.map('map',{center:[ nolayLat, nolayLong], zoom:10, zoomControl: false });	
	
	 
	
	map.on('contextmenu',(e) => {
		  L.popup({autoClose:true,closeOnClick:true, closeButton:false,className:'mapContextMenu'})
		  .setLatLng(e.latlng)
		  .setContent('<div><a href="#" onclick="getMeteoByCoords('+e.latlng.lat+','+e.latlng.lng+');">Demander la météo</a></div>')
		  .addTo(map)
		  .openOn(map);
		});
	
		
	map.on("click", function(event){
		getMeteo(event.latlng);
	});
	
	for (var key in baseMaps) {
		baseMaps[key].addTo(map);  
	}
	
	  /*legend = L.control({position: 'bottomright'});
	legend.onAdd = function (map) {

	  var div = L.DomUtil.create('div', 'info legend'),
	        grades = ["Car", "ball"],
	        labels = ["http://datentaeter.de/wp-content/uploads/2016/06/flag_de.png","http://datentaeter.de/wp-content/uploads/2016/06/flag_de.png"];

	    // loop through our density intervals and generate a label with a colored square for each interval
	    for (var i = 0; i < grades.length; i++) {
	        div.innerHTML +=
	            grades[i] + (" <img src="+ labels[i] +" height='50' width='50'>") +'<br>';
	    }

	    return div;
	};
	legend.addTo(map);*/
  
}

function addDomain(lat,long,domain){
	L.marker([lat, long]).addTo(map)
    .bindPopup(domain.domainName)
    .openPopup();
}

function addDomains(arrayOfDomain){
	if (arrayOfDomain){
		console.log(arrayOfDomain);
		for (i=0 ; i < arrayOfDomain.length ; i++){		
			var coords = arrayOfDomain[i].coords;
			addDomain(coords[0],coords[1],{domainName:arrayOfDomain[i].domainName});
		}
	}
}

function switchFocus(focus){
	focusEnabled = focus;//!focusEnabled;
}

function getMeteoByCoords(lat,lng){
	var latlng = L.latLng(lat, lng);
	getMeteo(latlng);
}

function getMeteo(latlng){
	console.log (latlng);
	jQuery.ajax({url: "https://www.prevision-meteo.ch/services/json/lat="+latlng.lat+"lng="+latlng.lng, success: function(result){
		 console.log (result);
		 meteoResult = result;
		 
 		var div = document.getElementById ('meteoInfo');
 		div.innerHTML =getMeteoPopup();
		PF('meteoPanel').show();
	
	}});
}

function getMeteoPopup(){
	var today = "<div><img src='"+meteoResult.fcst_day_0.icon+"' width='24px' height='24px'/> Aujourd'hui :";			
				
	var hourly = meteoResult.fcst_day_0.hourly_data;
	today = getMeteoTable(today,hourly,meteoResult.fcst_day_0);
	
	for (var i=1 ; i < 5 ; i++){		
		var forecast = meteoResult["fcst_day_"+i];
		today = today+"<div>";
		hourly = forecast.hourly_data;
		today = getMeteoTable(today,hourly,forecast );
		today = today+"</div>";
	}
	return today;
}

function getMeteoTable(today,hourly,data){
	today = today+"<div> "+data.day_long+" : Min. "+data.tmin+" Max. "+data.tmax+"</div>";;
	var table = '<table class="meteoDayTable" width="100%"><tr>';
	for (var i = 0 ; i < 24 ; i=i+4){ 
		var hour = hourly[i+'H00'];
		var icon = hour.ICON;
		var temp = hour.TMP2m;
		table = table+'<td><table><tr><td>'+i+'H00</td></tr>';
		table = table+'<tr><td><img src="'+icon+'" width="12px" height="12px""/></td></tr>';
		table = table+'<tr><td>'+temp+'°C</td></tr></table></td>';
		
		
	}
	table = table+'</tr></table>';
	today=today+'<div>'+table+'</div>';
	return today;
}

function setLayer(layerName){
	baseMaps[layerName].bringToFront();
}

function manualPrint () {
	
	var mapDiv = document.getElementById('map');
	
	domtoimage.toPng(mapDiv,{ width: 800, height:600 })
	.then(function (dataUrl) {
		var link = document.createElement('a');
		link.download = "exportedMap" + '.png';
		link.href = dataUrl;
		link.click();
	});
}

function showDatum (region, inData){
	if (region){
		console.log ("Loading region: "+region);
		
		data =inData;
		//data.push(inData);
		//console.log (data);
		showData (region);
	}
	
}

function removeDatum(region){
	 console.log (overlayMaps);
	 overlayMaps[getCode(region)].remove();
	 console.log ("Removed "+getCode(region));
}

function showData (region){
	var originalData = data;	
	if (originalData){
		var preparedData =  {};	
		var denomination;
		if (region == "sols"){
			// {""properties": perimeter": 157782.0, "soil_id": 15403, "area":
			//{"326742000.0, "soil": 618, "smu": 337242, "geo_point_2d":
			//	{"[48.92016172972839, 3.533430469265583]}},type": "Feature",
			
			originalData = data[0].features;
			//console.log (originalData);
			
		}
		
			for (i = 0 ; i < originalData.length ; i++){
				
				
				
				if (region == "sols"){
				
					denomination = originalData[i].properties.smu;
					//console.log (denomination);
				}
				else denomination = originalData[i].properties.denomination;
				
				var pData =  preparedData[denomination];
				if (!pData){
					preparedData[denomination] = [];
				}
				preparedData[denomination].push ( originalData[i]);
				//console.log (preparedData[denomination]);
			}
			//var overlayMaps[region] = {};
			var lat;
			var lng;
			var localized;
			for (var key in preparedData){
				
				  var color = getRandomColor();
				  var mapKey = "<span style='background-color:"+color+"'>"+key+"</span>";
				  console.log (preparedData[key][0]);
				 if (!localized){
					 lng  = preparedData[key][0].properties.longitude;
					 if (lng){
						 lat = preparedData[key][0].properties.latitude;
						 localized = true;
					 }
				 }
				  var cities = L.layerGroup([ L.geoJSON(preparedData[key], {
					    style: function (feature) {			         	
					    		return {color: color,stroke:true,fillRule:"nonzero"};			    	
					    }
					}).bindPopup(function (layer) {
					    return "Appellation : "+layer.feature.properties.appellation
					    +"<br/> Dénomination : "+layer.feature.properties.denomination
					    +"<br/> ID Dénomination : "+layer.feature.properties.id_denom
					    +"<br/> ID App : "+layer.feature.properties.id_app
					    +"<br/> ID : "+layer.feature.properties.id
					    +"<br/> Type : "+layer.feature.properties.type_ig
					    +"<br/> Insee (new) : "+layer.feature.properties.new_insee
					    +"<br/> Commune (new) : "+layer.feature.properties.new_nomcom
					    +"<br/> Insee (old) : "+layer.feature.properties.old_insee
					    +"<br/> Commune (old) : "+layer.feature.properties.old_nomcom
					    +"<br/> Crinao : "+layer.feature.properties.crinao;
					    +"<br/> Description : "+layer.feature.properties.description;
					})]);
				
				 
				  overlayMaps[getCode(region)] = cities;
				  cities.addTo(map); 
				  if (localized  && focusEnabled) map.setView(new L.LatLng(lat, lng), 8);
				  localized = false;
			}	
		
			
	}
	
}

function getCode (region){
	console.log (region);
	if (region.id) return region.id;
	else return region;
}

function loadSols (){
	data = [];
	loadFile ("http://localhost:8080/data/sols.json", false);
	
}

function loadFile (fileName, isClearData){
	if (isClearData) data = [];
	 console.log ("Loading file: "+fileName);
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
	  if (this.readyState == 4 && this.status == 200) {
		  console.log ("load...");
	    data.push(JSON.parse(this.responseText));
	    console.log ("Ready!");
	   
	    //document.getElementById("demo").innerHTML = myObj.name;
	  }
	  
	};
	console.log ("Sending request");
	xmlhttp.open("GET", fileName, false);
	console.log ("REquest prepared");
	xmlhttp.send();
	console.log ("REquest sent");
}

function addPoint(data, map) {
	var longitude = data.properties.longitude;
	var latitude = data.properties.latitude;
	console.log("put data " + data);
	try {
		L.marker([ latitude, longitude ]).addTo(map).bindPopup(
				'Location type: ' + data.properties.locType + '<br> Name: '
						+ data.properties.name + '<br> Description: '
						+ data.properties.geoDescription);
	} catch (exception) {
		console.log("Error on mark");
		console.log(exception);
	}

	// .openPopup();
}





function getRandomColor() {
    var letters = '0123456789ABCDEF';
    var color = '#';
    for (var i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }
