// implementation of AR-Experience (aka "World")
var World = {

	//Vector de locations.
	edificios : [
                "Facultad de Derecho","Oficina de Becas",
                "Biblioteca Tinoco","Escuela de Arquitectura","Comedor universitario","Facultad de Ingeniería",
                "Escuela de Física y Matemáticas","Escuela de Generales","Biblioteca Carlos Monge",
                "Educación Preescolar","Facultad de Letras","Centro de Informática","Escuela de Geología",
                "Ciencias Económicas","Computación e Informática","Facultad de Odontología","Facultad de Medicina",
                "Facultad de Farmacia","Facultad de Microbiología","Escuela de Biología","Escuela de Química",
                "Escuela de Artes Musicales","Escuela de Bellas Artes","Facultad de Educación","Bosque Leonel Oviedo",
                "Mariposario","Plaza 24 de abril","El Pretil"],


    latitud : [9.93639, 9.9355, 9.93601, 9.93486, 9.93724, 9.93595, 9.93648, 9.93612,
                9.93603, 9.93867, 9.9386, 9.937654,
                9.938080, 9.937046, 9.937932, 9.938255, 9.938637, 9.938880, 9.937950, 9.937640, 9.937178, 9.937408,
                9.936547, 9.936083, 9.937604, 9.937249, 9.936307, 9.935916],
                
    longuitud : [-84.05386, -84.05422, -84.0527, -84.05261, -84.05309, -84.05194, -84.05157,
                -84.05047, -84.05105, -84.0536,
                -84.05286, -84.052356, -84.052452, -84.051656, -84.051992, -84.051683, -84.050404, -84.049967, -84.049292,
                -84.049444, -84.048932,-84.048154, -84.048270, -84.048764, -84.050606, -84.050314, -84.050855, -84.050618 ],



    //Numero de llamados a location
    num : 0,

	// true once data was fetched
	initiallyLoadedData: false,

	// different POI-Marker assets
	markerDrawable_idle: null,
	markerDrawable_selected: null,

	// list of AR.GeoObjects that are currently shown in the scene / World
	markerList: [],

	// The last selected marker
	currentMarker: null,

	// called to inject new POI data
	loadPoisFromJsonData: function loadPoisFromJsonDataFn(id1, id2, id3) {
		World.markerList.pop();
		 World.markerList.pop();
		 World.markerList.pop();

		// start loading marker assets
		World.markerDrawable_idle = new AR.ImageResource("assets/marker_idle.png");
		World.markerDrawable_selected = new AR.ImageResource("assets/marker_selected.png");

		// loop through POI-information and create an AR.GeoObject (=Marker) per POI
		
		var singlePoi = {
			"id": id2,
			"latitude": parseFloat(World.latitud[id1]),
			"longitude": parseFloat(World.longuitud[id1]),
			"altitude": parseFloat(0),
			"title": World.edificios[id1],
			"description": World.edificios[id1]
		};

			/*
				To be able to deselect a marker while the user taps on the empty screen, 
				the World object holds an array that contains each marker.
			*/
		World.markerList.push(new Marker(singlePoi));


		singlePoi = {
			"id": id2,
			"latitude": parseFloat(World.latitud[id2]),
			"longitude": parseFloat(World.longuitud[id2]),
			"altitude": parseFloat(0),
			"title": World.edificios[id2],
			"description": World.edificios[id2]
		};

			/*
				To be able to deselect a marker while the user taps on the empty screen, 
				the World object holds an array that contains each marker.
			*/
		World.markerList.push(new Marker(singlePoi));


		singlePoi = {
			"id": id3,
			"latitude": parseFloat(World.latitud[id3]),
			"longitude": parseFloat(World.longuitud[id3]),
			"altitude": parseFloat(0),
            "title": World.edificios[id3],
            "description": World.edificios[id3]
		};

			/*
				To be able to deselect a marker while the user taps on the empty screen, 
				the World object holds an array that contains each marker.
			*/
		World.markerList.push(new Marker(singlePoi));



		World.updateStatusMessage(3 + ' places loaded');
	},

	// updates status message shon in small "i"-button aligned bottom center
	updateStatusMessage: function updateStatusMessageFn(message, isWarning) {

		var themeToUse = isWarning ? "e" : "c";
		var iconToUse = isWarning ? "alert" : "info";

		$("#status-message").html(message);
		$("#popupInfoButton").buttonMarkup({
			theme: themeToUse
		});
		$("#popupInfoButton").buttonMarkup({
			icon: iconToUse
		});
	},

	// location updates, fired every time you call architectView.setLocation() in native environment
	locationChanged: function locationChangedFn(lat, lon, alt, acc) {

		/*
			The custom function World.onLocationChanged checks with the flag World.initiallyLoadedData if the function was already called. With the first call of World.onLocationChanged an object that contains geo information will be created which will be later used to create a marker using the World.loadPoisFromJsonData function.
		*/
		//if (!World.initiallyLoadedData) {
			/* 
				requestDataFromLocal with the geo information as parameters (latitude, longitude) creates different poi data to a random location in the user's vicinity.
			*/

		//	World.initiallyLoadedData = true;
		//}
	},

	// fired when user pressed maker in cam
	onMarkerSelected: function onMarkerSelectedFn(marker) {

		// deselect previous marker
		if (World.currentMarker) {
			if (World.currentMarker.poiData.id == marker.poiData.id) {
				return;
			}
			World.currentMarker.setDeselected(World.currentMarker);
		}

		// highlight current one
		marker.setSelected(marker);
		World.currentMarker = marker;
	},

	// screen was clicked but no geo-object was hit
	onScreenClick: function onScreenClickFn() {
		if (World.currentMarker) {
			World.currentMarker.setDeselected(World.currentMarker);
		}
	},

	// request POI data
	requestDataFromLocal: function requestDataFromLocalFn(lat, lon, indice) {
		/*var poiData = [];
		++num;


		World.updateStatusMessage('Latitude es '+ lat + '. Longitud es ' + lon + '. Y el indice es ' + indice '.');

		if(num > 3){
			//AR.context.destroyAll();
			poiData.length = 0;
			// empty list of visible markers
			World.markerList = [];
			num = 1;

		}
		
		poiData.push({
				"id": (i + 1),
				"longitude": (lon),
				"latitude": (lat),
				"name": edificios[indice]
			});
		World.loadPoisFromJsonData(poiData);*/

	}

};

/* 
	Set a custom function where location changes are forwarded to. There is also a possibility to set AR.context.onLocationChanged to null. In this case the function will not be called anymore and no further location updates will be received. 
*/
AR.context.onLocationChanged = World.locationChanged;

/*
	To detect clicks where no drawable was hit set a custom function on AR.context.onScreenClick where the currently selected marker is deselected.
*/
AR.context.onScreenClick = World.onScreenClick;