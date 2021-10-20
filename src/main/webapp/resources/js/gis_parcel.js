


function initGISParcel(mapId) {
	
	var baseMaps;

	var controlLayer = {};
	var baseMapLayer = [];
	var overlayMaps=[];
	var legend ;

	var meteoResult;
	var focusEnabled = true;
	
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
		//attribution : osmAttrib
	});
	
	var reliefLayer = new L.TileLayer("https://maps-for-free.com/../layer/relief/z{z}/row{y}/{z}_{x}-{y}.jpg", {
		maxZoom : 18,
		//attribution : osmAttrib
	});
	
	var topoMap = new L.TileLayer("https://{s}.tile.opentopomap.org/{z}/{x}/{y}.png", {
		maxZoom : 18,
		//attribution : osmAttrib
	});
	
	var landUrl = 'http://{s}.tile.thunderforest.com/landscape/{z}/{x}/{y}.png';
	var thunAttrib = '&copy; '+osmUrl+' Contributors & '+landUrl;
	var landMap = new L.TileLayer(landUrl, {
		maxZoom : 18,
		//attribution : thunAttrib
	});
	
	baseMaps = {
		    "osm": osm,
		    //"relief": reliefLayer,
		    //"topoMap":topoMap,
		    //"landMap":landMap
	};

	console.log('MapId: '+mapId);
	var map = L.map(mapId,{center:[ nolayLat, nolayLong], zoom:14, zoomControl: false });	
	
	 
	
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
	
	
	
	this.showParcelData = function (data){
	var originalData = data;	
	if (originalData){
		var preparedData =  {};	
		var denomination;
		
		
			for (i = 0 ; i < originalData.length ; i++){
				
				
				
				denomination = originalData[i].properties.denomination;
				
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
				 var features = preparedData[key];
				 
				 	//console.log('Geometry => ',features[0].geometry);
					var center = turf.center(features[0].geometry);
					//console.log('center =>',center.geometry.coordinates);
					map.panTo(new L.LatLng(center.geometry.coordinates[1], center.geometry.coordinates[0]));
				 
				  var cities = L.layerGroup([ L.geoJSON(preparedData[key], {
					    style: function (feature) {			         	
					    		return {color: "#B60505",stroke:true,fillRule:"nonzero"};			    	
					    }
					}).bindPopup(function (layer) {
					    return "Appellation : "+layer.feature.properties.appellation
					    +"<br/> Dénomination : "+layer.feature.properties.denomination
					    +"<br/> ID Dénomination : "+layer.feature.properties.id_denom
					    +"<br/> ID App : "+layer.feature.properties.id_app
					    +"<br/> ID : "+layer.feature.properties.id
					    +"<br/> Wineyard ID : "+layer.feature.properties.wineyardId
					    +"<br/> Type : "+layer.feature.properties.type_ig
					    +"<br/> Insee (new) : "+layer.feature.properties.new_insee
					    +"<br/> Commune (new) : "+layer.feature.properties.new_nomcom
					    +"<br/> Insee (old) : "+layer.feature.properties.old_insee
					    +"<br/> Commune (old) : "+layer.feature.properties.old_nomcom
					    +"<br/> Crinao : "+layer.feature.properties.crinao
					    +"<br/> Description : "+layer.feature.properties.description
					    //+"<br/> <input type='button' onClick='addToDomain2 ({featureId:\""+layer.feature.properties.id+"\"})' value='ajouter au domaine' />"
					    +"<br/> <input type='button' onClick='addToDomain2 ([{name:\"featureId\",value:\""+layer.feature.properties.id+"\"}])' value='ajouter au domaine' />"
					    //+"<br/> <input type='button' onClick='console.log("+layer.feature.properties.id+")' value='ajouter au domaine' />";
					})]);
				
				 
				  //overlayMaps[getCode(region)] = cities;
				  cities.addTo(map); 
				  if (localized  && focusEnabled) map.setView(new L.LatLng(lat, lng), 8);
				  localized = false;
			}	
		
			
	}
	
}

return this;
}


