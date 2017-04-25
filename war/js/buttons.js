function toggleLogin(username, password) {
	var userfield = document.getElementById(username);
	var pwordfield= document.getElementById(password);
	if (userfield.is(':visible')) {
		$('#'+userfield).fadeOut('fast');
		$('#'+pwordfield).fadeout('fast');
	}
	else{
		$('#'+userfield).fadeIn('fast');
		$('#'+pwordfield).fadeIn('fast');
	}
}

function toggleLoginClean(){
		$('#navBar').hide();
		$('#loginFields').fadeIn('fast');
		$('#userField').fadeIn('fast');
		$('#passField').fadeIn('fast');
}

//$("#navBar").click(toggleLoginClean());