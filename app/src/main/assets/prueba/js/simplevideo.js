var World = {
	loaded: false,

	init: function initFn() {
		this.createOverlays();
	},

	createOverlays: function createOverlaysFn() {
		// Initialize ClientTracker
		this.tracker = new AR.ClientTracker("assets/prueba.wtc", {
			onLoaded: this.worldLoaded
		});

		/*
			Besides images, text and HTML content you are able to display videos in augmented reality. With the help of AR.VideoDrawables you can add a video on top of any image recognition target (AR.Trackable2DObject) or have it displayed at any geo location (AR.GeoObject). Like any other drawable you can position, scale, rotate and change the opacity of the video drawable.

			The video we use for this example is "video.mp4". As with all resources the video can be loaded locally from the application bundle or remotely from any server. In this example the video file is already bundled with the application.

			The URL and the size are required when creating a new AR.VideoDrawable. Optionally the offsetX and offsetY parameters are set to position the video on the target. The values for the offsets are in SDUs. If you want to know more about SDUs look up the code reference.
		*/
		var video1 = new AR.VideoDrawable("http://res.cloudinary.com/eniacs/video/upload/ac_aac,br_410,c_scale,h_290,vc_h264,w_480/v1477362262/video_1_pyfrre.mp4", 0.40, {
			offsetY: -0.3,
		});

	 	 var video2 = new AR.VideoDrawable("https://dl.dropboxusercontent.com/s/3ofeje5c5b73128/video_2.mp4?dl=0", 0.40, {
        			offsetY: -0.3,
        		});

        var video3 = new AR.VideoDrawable("https://dl.dropboxusercontent.com/s/6agxjowj3ounho5/video_3.mp4?dl=0", 0.40, {
        			offsetY: -0.3,
        		});

        var video4 = new AR.VideoDrawable("https://dl.dropboxusercontent.com/s/2ifyop7541hgbpo/video_4.mp4?dl=0", 0.40, {
        			offsetY: -0.3,
        		});

      var video5 = new AR.VideoDrawable("https://dl.dropboxusercontent.com/s/y9qto9bexpnfciu/video_5.mp4?dl=0", 0.40, {
        			offsetY: -0.3,
        		});

        var video6 = new AR.VideoDrawable("https://dl.dropboxusercontent.com/s/k3b52wszg97orh2/video_6.mp4?dl=0", 0.40, {
        			offsetY: -0.3,
        		});

		/*
			Adding the video to the image target is straight forward and similar like adding any other drawable to an image target.

			Note that this time we use "*" as target name. That means that the AR.Trackable2DObject will respond to any target that is defined in the target collection. You can use wildcards to specify more complex name matchings. E.g. 'target_?' to reference 'target_1' through 'target_9' or 'target*' for any targets names that start with 'target'.

			To start the video immediately after the target is recognized we call play inside the onEnterFieldOfVision trigger. Supplying -1 to play tells the Wikitude SDK to loop the video infinitely. Choose any positive number to re-play it multiple times.
		*/
		var pageOne = new AR.Trackable2DObject(this.tracker, "v1", {
			drawables: {
				cam: [video1]
			},
			onEnterFieldOfVision: function onEnterFieldOfVisionFn() {
				video.play(-1);
			}
		});

		var pageTwo = new AR.Trackable2DObject(this.tracker, "v2", {
        			drawables: {
        				cam: [video2]
        			},
        			onEnterFieldOfVision: function onEnterFieldOfVisionFn() {
        				video.play(-1);
        			}
        		});

       var pageThree = new AR.Trackable2DObject(this.tracker, "v3", {
        			drawables: {
        				cam: [video3]
        			},
        			onEnterFieldOfVision: function onEnterFieldOfVisionFn() {
        				video.play(-1);
        			}
        		});

         var pageFour = new AR.Trackable2DObject(this.tracker, "v4", {
        			drawables: {
        				cam: [video4]
        			},
        			onEnterFieldOfVision: function onEnterFieldOfVisionFn() {
        				video.play(-1);
        			}
        		});

        var pageFive = new AR.Trackable2DObject(this.tracker, "v5", {
        			drawables: {
        				cam: [video5]
        			},
        			onEnterFieldOfVision: function onEnterFieldOfVisionFn() {
        				video.play(-1);
        			}
        		});

        var pageSix = new AR.Trackable2DObject(this.tracker, "v6", {
        			drawables: {
        				cam: [video6]
        			},
        			onEnterFieldOfVision: function onEnterFieldOfVisionFn() {
        				video.play(-1);
        			}
        		});
	},

	worldLoaded: function worldLoadedFn() {
	/*	var cssDivInstructions = " style='display: table-cell;vertical-align: middle; text-align: right; width: 50%; padding-right: 15px;'";
		var cssDivSurfer = " style='display: table-cell;vertical-align: middle; text-align: left; padding-right: 15px; width: 38px'";
		var cssDivBiker = " style='display: table-cell;vertical-align: middle; text-align: left; padding-right: 15px;'";
		document.getElementById('loadingMessage').innerHTML =
            "<div" + cssDivInstructions + ">Scan Target &#35;1 (surfer) or &#35;2 (biker):</div>" +
            "<div" + cssDivSurfer + "><img src='assets/surfer.png'></img></div>" +
            "<div" + cssDivBiker + "><img src='assets/bike.png'></img></div>";*/

		// Remove Scan target message after 10 sec.
		setTimeout(function() {
			var e = document.getElementById('loadingMessage');
			e.parentElement.removeChild(e);
		}, 10000);
	}
};

World.init();
