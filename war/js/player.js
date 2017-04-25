var ytState;

function onPlayerStateChange(newState) {
	ytState=newState;
	switch(ytState){
		case -1: document.getElementById("playPause").src = "images/commands/play.png";
			break;
		case 0: document.getElementById("playPause").src = "images/commands/play.png";
			loadVideo(myStringArray[counterTemp]);
			break;
		case 1: document.getElementById("playPause").src = "images/commands/pause.png";
			break;
		case 2: document.getElementById("playPause").src = "images/commands/play.png";
			break;
		case 3: document.getElementById("playPause").src = "images/commands/pause.png";
			break;
		}
}

function togglePlayback(ytplayer) {
	if (ytplayer) {
		if(ytState == 1) ytplayer.pauseVideo();
		else ytplayer.playVideo();
	}
}

function InitVideoPlayer() {
	var params = { allowScriptAccess: "always" };
	var atts = { id: "ytPlayer" };
	swfobject.embedSWF("http://www.youtube.com/apiplayer?" +
				"&enablejsapi=1&playerapiid=mojplayer&version=3", 
					   "videoBox", "480", "295", "8", null, null, params, atts);
}

function loadVideo(videoID) {
	ytplayer = document.getElementById("ytPlayer");
	ytplayer.loadVideoById({'videoId': videoID});
	counterTemp = counterTemp +1;
}

function onYouTubePlayerReady(playerId) {
	ytplayer = document.getElementById("ytPlayer");
	ytplayer.addEventListener("onStateChange", "onPlayerStateChange");
	ytplayer.cueVideoById("PCyRqHRtQcQ");
}