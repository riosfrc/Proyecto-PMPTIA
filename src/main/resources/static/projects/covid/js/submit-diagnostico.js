(function () {

	'use strict'
	var previewImages = document.getElementById('preview-images');
	var file = document.getElementById('file');
	var form = document.getElementById('form');
	var btnFile = document.getElementById('btnFile');
	var btnSubmit = document.getElementById('submit');
	var iconUpload = document.getElementById("icon-upload");
	var formData = new FormData();
	
	// Da click al input[type='file'] cuando se presiona btnFile
	btnFile.addEventListener("click",()=>{
		file.click();
	});
	
	// Genera el thumbnail_id aleatoreamente, llama a la funcion createThumbnail() y añade los archivos al formData
	file.addEventListener('change', function (e) {
		for ( var i = 0; i < file.files.length; i++ ) {
			var randomNo = Math.floor( Math.random() * 30000 ) + '_' + Date.now();
			var extension = file.files[i].name.split('.').pop();
			var thumbnail_id = randomNo + '.' + extension;
			createThumbnail(file, i, thumbnail_id);
			formData.append(thumbnail_id, file.files[i]);
		}
		e.target.value = '';
	});
	
	form.addEventListener('submit', (e) => {
		e.preventDefault();
		btnSubmit.disabled = true;
		
		var values = [];
		var keys = [];
		// Guarda el contenido del formData en el array values
		for(var key of formData.keys()) {
			values.push(formData.get(key));
			keys.push(key);
		}
		
		// Borra todo el contenido del formData
		for(var key of keys) {
			formData.delete(key);
		}
		
		// Carga el contenido del array al formData en el formato deseado
		for(var index in values) {
			formData.append('file[]', values[index], keys[index].split('.').pop());
		}
		
		formData.append("edad", document.querySelector('input[name="edad"]').value);
		formData.append("peso", document.querySelector('input[name="peso"]').value);
		let sexoValue = "";
		if(document.getElementById('masculino').checked) sexoValue = document.getElementById('masculino').value;
		if(document.getElementById('femenino').checked) sexoValue = document.getElementById('femenino').value;
		formData.append("sexo", sexoValue);
		formData.append("tipoImagen", document.querySelector('select[name="tipoImagen"]').value);
		
		 fetch('http://localhost:8090/covid/diagnostico/save', {
		 	method: 'POST',
		 	body: formData
		 }) .then(function (response) {
		 	return response.text();
		 }) .then(function (data) {
		 	clearFormDataAndThumbnails();
		 	form.reset();
		 	iconDisplay();
		 	btnSubmit.disabled = false;
		 	console.log(data);
		 }) .catch(function (err) {
		 	console.log(err);
		 });
	});
	
	// Crea el thumbnail para la previsualización de la imagenes subidas
	var createThumbnail = function (file, iterator, thumbnail_id) {
		var thumbnail = document.createElement('div');
		thumbnail.classList.add('thumbnail', thumbnail_id);
		thumbnail.dataset.id = thumbnail_id;
		thumbnail.setAttribute('style', `background-image: url(${ URL.createObjectURL( file.files[iterator] ) })`);
		previewImages.appendChild(thumbnail);
		createCloseButton(thumbnail_id);
		iconDisplay();
	}
	
	// Genera el botón de eliminar a los thumbnails
	var createCloseButton = function (thumbnail_id) {
		var closeButton = document.createElement('div');
		closeButton.classList.add('close-button');
		closeButton.innerText = 'x';
		document.getElementsByClassName(thumbnail_id)[0].appendChild(closeButton);
	}
	
	// Borra todo el contenido del formData y los thumbnails
	var clearFormDataAndThumbnails = function () {
		for ( var key of formData.keys() ) {
			formData.delete(key);
		}
		document.querySelectorAll('.thumbnail').forEach(function (thumbnail) {
			thumbnail.remove();
		});
	}
	
	// Elimina el thumbnail y la imagen del formData cuando se presiona el botón de eliminar
	document.getElementById('preview-images').addEventListener('click', function (e) {
		if ( e.target.classList.contains('close-button') ) {
			e.target.parentNode.remove();
			formData.delete(e.target.parentNode.dataset.id);
			iconDisplay();
		}
	});
	
	// Mostrar y ocultar icon images
	var iconDisplay = function () {
		if(previewImages.hasChildNodes()){
			iconUpload.style.display = "none";
		} else{
			iconUpload.style.display = "";
		}
	}
})();