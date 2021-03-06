
var Ajax;
if (Ajax && (Ajax != null)) {
	Ajax.Responders.register({
		onCreate: function() {
			if($('tailSpinner') && Ajax.activeRequestCount>0)
				Effect.Appear('tailSpinner',{duration:0.5,queue:'end'});
		},
		onComplete: function() {
			if($('tailSpinner') && Ajax.activeRequestCount==0)
				Effect.Fade('tailSpinner',{duration:0.5,queue:'end'});
		}
	});
}

/**
 * A class for streaming a log file into a div or other element
 * @param logFileName the logfile to stream or tail
 * @param initialOffset the offset at which to begin streaming (eg, length of already displayed content)
 * @param elementToUpdate the container for the log content
 * @param divElementToScroll the div element to scroll with the updates (could be same as element to update)
 * @param errorMsg text to display if the incremental update reports an error)
 */
function LogStreamer(logFileName, direction,initialOffset, elementToUpdate, divElementToScroll, errorMsg,url) {

	this.logData = { "model" : {"fileName": logFileName,"direction": direction, "startIndex": 0, "endIndex": initialOffset}};
	this.contentElement = elementToUpdate;
	this.scrollingElement = divElementToScroll;
	this.errorMsg = errorMsg;
	this.url=url;
	this.fetchUpdates = function(logStreamer) {

		new Ajax.Request(url, {
			logStreamer: logStreamer,
			method:'get',
			requestHeaders: {Accept: 'application/json'},
			parameters: {fileName: logStreamer.logData.model.fileName,direction:logStreamer.logData.model.direction,startIndex: logStreamer.logData.model.endIndex },
			onSuccess: function(transport){
				logStreamer.logData = transport.responseText.evalJSON(true);
				appendText = "";
				if (logStreamer.logData.model.error) {
					appendText = (logStreamer.errorMsg) ? logStreamer.errorMsg : "\n\n** " + logStreamer.logData.model.error + " **";
					logStreamer.stop();
				}
				else {
					appendText = logStreamer.logData.model.content;
				}
				//if IE get content from innerText instead of innderHTML and add <pre> for formatting.
				if (Prototype.Browser.IE) {
					var content =logStreamer.contentElement.innerText;
					var lastindex=content.lastIndexOf("<br>");
					if(appendText.length!=0){ 
						var newContent= "<PRE>" + content.substring(lastindex)+ appendText+ "</PRE>";
						logStreamer.contentElement.update(newContent);
					}

				}  
				else {
					var content =logStreamer.contentElement.textContent;
					var lastindex=content.lastIndexOf("<br>");
					if(appendText.length!=0){
						var newContent=content.substring(lastindex)+ appendText ;
						logStreamer.contentElement.update(newContent);
					}
				}  
				//auto scroll to end of the line
				//logStreamer.scrollingElement.scrollTop = logStreamer.scrollingElement.scrollHeight;
			}
		})     
	}
	this.periodicUpdater = null;
	this.start = function() {
	//	logStreamer.contentElement.innerHTML="Tailing is Started.<br/>";
		var fetchUpdates = this.fetchUpdates.curry(this);
		//delay for 5 seconds and fetch updates
		this.periodicUpdater = new PeriodicalExecuter(fetchUpdates, 5);
	}
	this.stop = function() {
		logStreamer.contentElement.innerHTML =logStreamer.contentElement.innerHTML+"<br/>Tailing is Stopped.<br/>";
		if (this.periodicUpdater) this.periodicUpdater.stop();
	}
}




