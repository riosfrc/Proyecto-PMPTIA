(function () {

	'use strict'
	var previewImages = document.getElementById('preview-images');
	var file = document.getElementById('file');
	var form = document.getElementById('form');
	var btnSubmit = document.getElementById('submit');
	var btnFile = document.getElementById('btnFile');
	var iconUpload = document.getElementById("icon-upload");
	var formData = new FormData();
	
	// Da click al input[type='file'] cuando se presiona btnFile
	btnFile.addEventListener("click",()=>{
		file.click();
	});
	
	// Genera el thumbnail_id aleatoreamente, llama a la funcion createThumbnail() y añade los archivos al formData
	file.addEventListener('change', function (e) {
		for ( var i = 0; i < file.files.length; i++ ) {
			var randomNo = Math.floor(100000 + Math.random() * 900000) + '_' + Date.now();
			var extension = file.files[i].name.split('.').pop();
			var thumbnail_id = randomNo + '.' + extension;
			createThumbnail(file, i, thumbnail_id);
			console.log(thumbnail_id);
			formData.append(thumbnail_id, file.files[i]);
		}
		e.target.value = '';
	});
	
	form.addEventListener('submit', function (e) {
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
			console.log(values[index]);
			formData.append('file[]', values[index], keys[index]);
		}
		
		formData.append("edad", document.querySelector('input[name="edad"]').value);
		formData.append("peso", document.querySelector('input[name="peso"]').value);
		let sexoValue = "";
		if(document.getElementById('masculino').checked) sexoValue = document.getElementById('masculino').value;
		if(document.getElementById('femenino').checked) sexoValue = document.getElementById('femenino').value;
		formData.append("fecha", dateNow());
		formData.append("sexo", sexoValue);
		formData.append("saturacionOxigeno", document.querySelector('input[name="saturacionOxigeno"]').value);
		formData.append("enfermedad", document.querySelector('select[name="enfermedad"]').value);
		formData.append("faseEnfermedad", document.querySelector('select[name="faseEnfermedad"]').value);
		formData.append("tipoImagen", document.querySelector('select[name="tipoImagen"]').value);
		
		 fetch('http://localhost:8090/covid/form/save', {
		 	method: 'POST',
		 	body: formData
		 }) .then(function (response) {
		 	return response.text();
		 }) .then(function (data) {
			if(data.includes('200 OK'))
				showModal(data);
			else
				console.log(data);
		 	clearFormDataAndThumbnails();
		 	form.reset();
		 	iconDisplay();
		 	btnSubmit.disabled = false;
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
		const keys = [];
		for(let key of formData.keys()) {
    		keys.push(key);
		}
		for(let id in keys) {
    		formData.delete(keys[id]);
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
	
	// Obtener fecha de subida de la imagen en el formato yyyy/mm/dd
	function dateNow(){
		var date = new Date();

		var dd = date.getDate();
		var mm = date.getMonth() + 1;
		var yyyy = date.getFullYear();

		if (dd < 10) dd = '0' + dd;
		if (mm < 10) mm = '0' + mm;

		date = yyyy + '/' + mm + '/' + dd;
		return date;
    }
	
	// Ventana modal
	const modal_container = document.getElementById('modal_container');
	const close = document.getElementById('close');

	const showModal = function(data) {
		document.getElementById('textData').textContent = data.replace('200 OK\n', '');
	    modal_container.classList.add('show');
	    window.addEventListener('click', closeModal);
	}
	
	function closeModal(e){
		if(e.target === modal_container){
			modal_container.classList.remove('show');
			window.removeEventListener('click', closeModal);
		}
	}

	close.addEventListener('click', () => {
	  modal_container.classList.remove('show');
	});
	
})();