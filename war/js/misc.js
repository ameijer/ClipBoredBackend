/* Different ways to handle events, one works for internet explorer, the other for everything else*/
function addEvent(element, evnt, funct){
	if(element.attachEvent)
		return element.attachEvent('on'+evnt, funct);
	else
		return element.addEventListener(evnt, funct, false);
}