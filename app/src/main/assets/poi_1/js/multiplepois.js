var ident1=-1;
var ident2=-1;
var ident3=-1;
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
    edificiosDesc : [
                "Facultad de Derecho",
                "Oficina de Becas y Atención Socioeconómica",

                "Esta biblioteca forma parte del Sistema de Bibliotecas, Documentación e Información"
                +" (SIBDI) de la Universidad de Costa Rica, que tiene como misión apoyar los programas"
                +" sustantivos de Docencia, Investigación, Acción Social y Administración, mediante la"
                +" adquisición, organización, almacenamiento, acceso y recuperación efectiva de recursos de"
                +" información y la prestación de servicios de calidad, acordes con las nuevas tecnologías y"
                +" orientaciones de los procesos de enseñanza-aprendizaje, que estimulen la creatividad en el"
                +" quehacer científico y promueven la asimilación, transformación y generación del conocimiento."
                +"\n\nContiene materiales bibliográficos sobre:"
                +"\n\t\tCiencias Económicas."
                +"\n\t\tIngeniería."
                +"\n\t\tArquitectura."
                +"\n\t\tInformática."
                +"\n\t\tFísica."
                +"\n\t\tMatemática."
                +"\n\t\tGeología."
                +"\n\nHorarios:"
                +"\n\t\tLunes a Viernes: 7:00 a.m. a 8:55 p.m."
                +"\n\t\tSábados: 8:00 a.m. a 6:00 p.m."
                +"\n\t\tDomingos y feriados: cerrado"
                +"\n\nTeléfono: (506) 2511-4461"
                +"\nWeb: http://sibdi.ucr.ac.cr",

                "La Escuela de Arquitectura fue fundada en 1971, y su papel, además de la formación de profesionales, es desarrollar una visión crítica"
                 +" sobre el acontecer arquitectónico, que le permita al profesional conservar la identidad cultural de su país, preservar su medio ecológico"
                 +" y desarrollar una arquitectura contextualizada, con respecto a las necesidades socioeconómicas de la nación, en el momento histórico actual."
                 +"\n\nCarreras:"
                 +"\n\t\tArquitectura."
                 +"\n\nTeléfono: (506) 2511-6881"
                 +"\nCorreo: arquis@ucr.ac.cr"
                 +"\nFacebook: https://www.facebook.com/arquitectura.ucr"
                 +"\nWeb: http://www.arquis.ucr.ac.cr/",

                "Comedor universitario",
                "Facultad de Ingeniería",
                "Escuela de Física y Matemáticas",
                "Escuela de Estudios Generales",
                "Biblioteca Carlos Monge",
                "Sección de Educación Preescolar",

                "La Facultad de Letras se fundó en marzo de 1941 y se encuentra integrada por las Escuelas de Lenguas Modernas,"
                 +" Filología, Lingüística y Literatura y Filosofía, el Instituto de Investigaciones Lingüísticas (INIL), el Instituto"
                 +" de Investigaciones Filosóficas (INIF) y varios programas de posgrado en lenguas modernas y clásicas, literaturas, lingüística y filosofía."
                 +"\n\nCarreras:"
                 +"\n\t\tFilología Española"
                 +"\n\t\tFilología Clásica"
                 +"\n\t\tFilosofía"
                 +"\n\t\tInglés"
                 +"\n\t\tLengua Inglesa"
                 +"\n\t\tInglés a Distancia"
                 +"\n\t\tFrancés"
                 +"\n\nTeléfono: (506) 2511-8384"
                 +"\nCorreo: facultad.letras@ucr.ac.cr"
                 +"\nFacebook: https://www.facebook.com/Letrasucr"
                 +"\nWeb: http://letras.ucr.ac.cr",

                "Centro de Informática",

                "Escuela fundada en noviembre de 1967, cuyo promotor fue César Dóndoli, ayudado por varios políticos y geólogos, como"
                +" el Geólogo Gabriel Dengo como principal misión se propone desarrollar el conocimiento geocientífico de la región de América Central."
                +"\n\nCarreras:"
                +"\n\t\tGeología."
                +"\n\nTeléfono: (506) 2511-5625"
                +"\nMuseo+UCR: http://museo.ucr.ac.cr/rfb/geologia.html"
                +"\nCorreo: geologia@ucr.ac.cr"
                +"\nFacebook: https://www.facebook.com/ECG.UCR/"
                +"\nWeb: http://www.geologia.ucr.ac.cr/",

                "La Facultad de Ciencias Económicas de la Universidad de Costa Rica inició actividades el 3 de Mayo de 1943. Hoy en día es"
                +" la segunda facultad más grande de la Universidad de Costa Rica y está conformada por cuatro importantes escuelas: Administración"
                +" de Negocios, Administración Pública, Economía y Estadística."
                +"\n\nCarreras:"
                +"\n\t\tContaduría Pública."
                +"\n\t\tDirección de Empresas."
                +"\n\t\tAdministración Aduanera."
                +"\n\t\tAdministración Pública."
                +"\n\t\tEconomía y Estadística."
                +"\n\nTeléfono: (506) 2511-4360"
                +"\nMuseo+UCR: http://museo.ucr.ac.cr/rfb/economicas.html"
                +"\nCorreo: decanato.ce@ucr.ac.cr"
                +"\nFacebook: https://www.facebook.com/Facultad-de-Ciencias-Econ%C3%B3micas-UCR-444744115214/"
                +"\nWeb: http://www.fce.ucr.ac.cr/",

                "La Escuela de Ciencias de la Computación e Informática de la Universidad de Costa Rica se fundó en 1981, como resultado de la"
                 +" fusión de dos programas distintos pero relacionados, el de Bachillerato en Informática y el de Bachillerato y Licenciatura en Computación."
                +"\n\nCarreras:"
                +"\n\t\tComputación e Informática."
                +"\n\nTeléfono: (506) 2511-8000"
                +"\nCorreo: secretaria@ecci.ucr.ac.cr"
                +"\nFacebook: https://www.facebook.com/ECCIOficial"
                +"\nWeb: http://www.ecci.ucr.ac.cr",

                "Facultad de Odontología",
                "Facultad de Medicina",
                "Facultad de Farmacia",
                "Facultad de Microbiología",
                "Escuela de Biología",
                "Escuela de Química",
                "Escuela de Artes Musicales",
                "Escuela de Bellas Artes",
                "Facultad de Educación",
                "Bosque Leonel Oviedo",
                "Mariposario",
                "Plaza 24 de abril",
                "El Pretil"],

    latitud : [9.93639, 9.9355, 9.93601, 9.93486, 9.93724, 9.93595, 9.93648, 9.93612,
                9.93603, 9.93867, 9.9386, 9.937654,
                9.938080, 9.937046, 9.937932, 9.938255, 9.938637, 9.938880, 9.937950, 9.937640, 9.937178, 9.937408,
                9.936547, 9.936083, 9.937604, 9.937249, 9.936307, 9.935916],

    longuitud : [-84.05386, -84.05422, -84.0527, -84.05261, -84.05309, -84.05194, -84.05157,
                -84.05047, -84.05105, -84.0536,
                -84.05286, -84.052356, -84.052452, -84.051656, -84.051992, -84.051683, -84.050404, -84.049967, -84.049292,
                -84.049444, -84.048932,-84.048154, -84.048270, -84.048764, -84.050606, -84.050314, -84.050855, -84.050618 ],



    //Numero de llamados a location
    altitud : 1200,
	// you may request new data from server periodically, however: in this sample data is only requested once
	isRequestingData: false,
	// true once data was fetched
	initiallyLoadedData: false,

	// true once data was fetched
	initiallyLoadedData: false,

	// different POI-Marker assets
	markerDrawable_idle: null,
	markerDrawable_selected: null,
	markerDrawable_directionIndicator: null,

	// list of AR.GeoObjects that are currently shown in the scene / World
	markerList: [],

	// The last selected marker
	currentMarker: null,

	// called to inject new POI data
	loadPoisFromJsonData: function loadPoisFromJsonDataFn(id1, id2, id3) {
	    if(id1!=ident1 && id1!=ident2 && id1!=ident3 || id2!=ident1 && id2!=ident2 && id2!=ident3 || id3!=ident1 && id3!=ident2 && id3!=ident3){
	        ident1=id1;
            ident2=id2;
            ident3=id3;
            if(World.markerList.length == 3) {
                World.markerList[0].markerObject.destroy();
                World.markerList[1].markerObject.destroy();
                World.markerList[2].markerObject.destroy();
                World.markerList.pop();
                World.markerList.pop();
                World.markerList.pop();
            }
            //AR.context.destroyAll()



            // start loading marker assets
            World.markerDrawable_idle = new AR.ImageResource("assets/marker_idle.png");
            World.markerDrawable_selected = new AR.ImageResource("assets/marker_selected.png");
            World.markerDrawable_directionIndicator = new AR.ImageResource("assets/indi.png");


            // loop through POI-information and create an AR.GeoObject (=Marker) per POI

            var singlePoi = {
                "id": id1,
                "latitude": parseFloat(World.latitud[id1]),
                "longitude": parseFloat(World.longuitud[id1]),
                "altitude": parseFloat(World.altitud),  //"altitude": parseFloat(0),
                "title": World.edificios[id1],
                "description": World.edificiosDesc[id1]
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
                "altitude": parseFloat(World.altitud),
                "title": World.edificios[id2],
                "description": World.edificiosDesc[id2]
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
                "altitude": parseFloat(World.altitud),
                "title": World.edificios[id3],
                "description": World.edificiosDesc[id3]
            };

                /*
                    To be able to deselect a marker while the user taps on the empty screen,
                    the World object holds an array that contains each marker.
                */
            World.markerList.push(new Marker(singlePoi));



            World.updateStatusMessage(3 + ' places loaded');
        }
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
	/*
    		It may make sense to display POI details in your native style.
    		In this sample a very simple native screen opens when user presses the 'More' button in HTML.
    		This demoes the interaction between JavaScript and native code.
    	*/
    	// user clicked "More" button in POI-detail panel -> fire event to open native screen
    	onPoiDetailMoreButtonClicked: function onPoiDetailMoreButtonClickedFn() {
    		var currentMarker = World.currentMarker;
    		var architectSdkUrl = "architectsdk://markerselected?id=" + encodeURIComponent(currentMarker.poiData.id) + "&title=" + encodeURIComponent(currentMarker.poiData.title) + "&description=" + encodeURIComponent(currentMarker.poiData.description);
    		/*
    			The urlListener of the native project intercepts this call and parses the arguments.
    			This is the only way to pass information from JavaSCript to your native code.
    			Ensure to properly encode and decode arguments.
    			Note: you must use 'document.location = "architectsdk://...' to pass information from JavaScript to native.
    			! This will cause an HTTP error if you didn't register a urlListener in native architectView !
    		*/
    		document.location = architectSdkUrl;
    	},

	// location updates, fired every time you call architectView.setLocation() in native environment
	locationChanged: function locationChangedFn(lat, lon, alt, acc) {
        if(alt < 1000){
        alt = 1200;
        }
		World.altitud= alt; // comentado
        //alert(World.altitud);
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

		World.currentMarker = marker;

		/*
			In this sample a POI detail panel appears when pressing a cam-marker (the blue box with title & description),
			compare index.html in the sample's directory.
		*/
		// update panel values
		$("#poi-detail-title").html(marker.poiData.title);
		$("#poi-detail-description").html(marker.poiData.description.substr(0,150)+"...");

		// distance and altitude are measured in meters by the SDK. You may convert them to miles / feet if required.
		//var distanceToUserValue = (marker.distanceToUser > 999) ? ((marker.distanceToUser / 1000).toFixed(2) + " km") : (Math.round(marker.distanceToUser) + " m");

		//$("#poi-detail-distance").html(distanceToUserValue);

		// show panel
		$("#panel-poidetail").panel("open", 123);

		$(".ui-panel-dismiss").unbind("mousedown");

		// deselect AR-marker when user exits detail screen div.
		$("#panel-poidetail").on("panelbeforeclose", function(event, ui) {
			World.currentMarker.setDeselected(World.currentMarker);
		});
	},

	// screen was clicked but no geo-object was hit
	onScreenClick: function onScreenClickFn() {
		/*if (World.currentMarker) {
			World.currentMarker.setDeselected(World.currentMarker);
		}*/
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

	},
		targetName2Augmentations :
    	{
    		"Arqui" : [
    			{
    				"type": "video",
    				"uri": "Videos/Arquitectura.mp4",
    				"scale": 0.80,
    				"offsetX": -0.5,
    				"offsetY": -0.3,
    				"autoplay": true,
    				"isTransparent": true
    			}
    		],
    		"Cenizaro" : [
    			{
    				"type": "video",
    				"uri": "Videos/Cenizaro.mp4",
    				"scale": 0.80,
    				"offsetX": -0.5,
    				"offsetY": -0.3,
    				"autoplay": true,
    				"isTransparent": true
    			}
    		],
    		"ECCI" : [
    			{
    				"type": "video",
    				"uri": "Videos/Computacion.mp4",
    				"scale": 0.80,
    				"offsetX": -0.5,
    				"offsetY": -0.3,
    				"autoplay": true,
    				"isTransparent": true
    			}
    		],
    		"Economicas" : [
    			{
    				"type": "video",
    				"uri": "Videos/Economicas.mp4",
    				"scale": 0.80,
    				"offsetX": -0.5,
    				"offsetY": -0.3,
    				"autoplay": true,
    				"isTransparent": true
    			}
    		],
    		"Facio" : [
    			{
    				"type": "video",
    				"uri": "Videos/Rodrigo Facio.mp4",
    				"scale": 0.80,
    				"offsetX": -0.5,
    				"offsetY": -0.3,
    				"autoplay": true,
    				"isTransparent": true
    			}
    		],
    		"Fernando" : [
    			{
    				"type": "video",
    				"uri": "Videos/Fernando.mp4",
    				"scale": 0.80,
    				"offsetX": -0.5,
    				"offsetY": -0.3,
    				"autoplay": true,
    				"isTransparent": true
    			}
    		],
    		"Geo" : [
    			{
    				"type": "video",
    				"uri": "Videos/Geologia.mp4",
    				"scale": 0.80,
    				"offsetX": -0.5,
    				"offsetY": -0.3,
    				"autoplay": true,
    				"isTransparent": true
    			}
    		],
    		"Joaquin" : [
    			{
    				"type": "video",
    				"uri": "Videos/Joaquin Jimenez.mp4",
    				"scale": 0.80,
    				"offsetX": -0.5,
    				"offsetY": -0.3,
    				"autoplay": true,
    				"isTransparent": true
    			}
    		],
    		"Letras" : [
    			{
    				"type": "video",
    				"uri": "Videos/Letras.mp4",
    				"scale": 0.80,
    				"offsetX": -0.5,
    				"offsetY": -0.3,
    				"autoplay": true,
    				"isTransparent": true
    			}
    		],
    		"Letras2" : [
    			{
    				"type": "video",
    				"uri": "Videos/Letras.mp4",
    				"scale": 0.80,
    				"offsetX": -0.5,
    				"offsetY": -0.3,
    				"autoplay": true,
    				"isTransparent": true
    			}
    		],
    		"Monge" : [
    			{
    				"type": "video",
    				"uri": "Videos/Carlos Monge.mp4",
    				"scale": 0.80,
    				"offsetX": -0.5,
    				"offsetY": -0.3,
    				"autoplay": true,
    				"isTransparent": true
    			}
    		],
    		"Ojoche" : [
    			{
    				"type": "video",
    				"uri": "Videos/Ojoche.mp4",
    				"scale": 0.80,
    				"offsetX": -0.5,
    				"offsetY": -0.3,
    				"autoplay": true,
    				"isTransparent": true
    			}
    		],
    		"Omar" : [
    			{
    				"type": "video",
    				"uri": "Videos/Omar Dengo.mp4",
    				"scale": 0.80,
    				"offsetX": -0.5,
    				"offsetY": -0.3,
    				"autoplay": true,
    				"isTransparent": true
    			}
    		],
    		"Tinoco" : [
    			{
    				"type": "video",
    				"uri": "Videos/Biblioteca Tinoco.mp4",
    				"scale": 0.80,
    				"offsetX": -0.5,
    				"offsetY": -0.3,
    				"autoplay": true,
    				"isTransparent": true
    			}
    		],
    		"Yolanda" : [
    			{
    				"type": "video",
    				"uri": "Videos/Yolanda Oreamuno.mp4",
    				"scale": 0.80,
    				"offsetX": -0.5,
    				"offsetY": -0.3,
    				"autoplay": true,
    				"isTransparent": true
    			}
    		]
    	},

    	init: function() {

        		this.tracker = new AR.ClientTracker("assets/project.wtc", {
        			onLoaded: function() {
        				console.info("tracker loaded");
        			}
        		});

        		this.lastCamDrawables = [];

        		this.trackable = new AR.Trackable2DObject(this.tracker, "*", {
        			drawables: {
        				cam: []
        			},
        			onEnterFieldOfVision: function (targetName) {
        				if (this.lastTargetName!==targetName) {
        					this.lastTargetName=targetName;

        					// destroy old augmentations
        					for (var i=World.trackable.drawables.cam.length-1; i>=0; i--) {
        						World.trackable.drawables.cam[i].enabled = false;
        						World.trackable.drawables.cam[i].destroy();
        						World.trackable.drawables.removeCamDrawable(i);
        					}

        					// create new augmentations
        					var augmentationsToCreate = World.targetName2Augmentations[targetName];
        					for (var i=0; i<augmentationsToCreate.length; i++) {
        						var drawableCreated;
        						switch(augmentationsToCreate[i].type) {
        						    case "video":
        						        drawableCreated = new AR.VideoDrawable(augmentationsToCreate[i].uri, augmentationsToCreate[i].scale,
        						        {
        									offsetX: augmentationsToCreate[i].offsetX,
        									offsetY: augmentationsToCreate[i].offsetY,
        									isTransparent: augmentationsToCreate[i].isTransparent
        								});
        								if (augmentationsToCreate[i].autoplay) {
        									drawableCreated.play(-1);
        								}

        						        break;
        						    default:
        						        break;
        						}

        						if (drawableCreated) {
        							World.trackable.drawables.addCamDrawable(drawableCreated);
        						}

        					}

        				} else {
        					for (var i=World.trackable.drawables.cam.length-1; i>=0; i--) {
        						World.trackable.drawables.cam[i].enabled = true;
        						if (World.trackable.drawables.cam[i].resume) {
        							World.trackable.drawables.cam[i].resume();
        						}
        					}
        				}
        			},
        			onExitFieldOfVision: function onExitFieldOfVisionFn() {
        				/*for (var i=World.trackable.drawables.cam.length-1; i>=0; i--) {
        					World.trackable.drawables.cam[i].enabled = false;
        					if (World.trackable.drawables.cam[i].pause) {
        						World.trackable.drawables.cam[i].pause();
        					}
        				}*/
        			}
        		});
        	}

};

/*
	Set a custom function where location changes are forwarded to. There is also a possibility to set AR.context.onLocationChanged to null. In this case the function will not be called anymore and no further location updates will be received.
*/
World.init();
AR.context.onLocationChanged = World.locationChanged;

/*
	To detect clicks where no drawable was hit set a custom function on AR.context.onScreenClick where the currently selected marker is deselected.
*/
AR.context.onScreenClick = World.onScreenClick;