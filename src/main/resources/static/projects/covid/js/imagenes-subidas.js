const containerImages = document.getElementById('container-images');
const deleteImages = document.getElementById('delete-images');

window.addEventListener('load', () => {
	fetch('http://localhost:8090/covid/form/getImages', {
		method: 'GET'
	})
	.then(function (response) {
		 return response.json();
	})
	.then(function (data) {
		createImages(data);
	})
	.catch(function (err) {
		 console.log(err);
	});
});

function createImages(data) {
	console.log(data);
	let fragment = new DocumentFragment();
	for(let image of data){
		let img = document.createElement('img');
		img.setAttribute('src', image['ruta'] + image['nombre']);
		fragment.appendChild(img);
	}
	containerImages.appendChild(fragment);
}

deleteImages.addEventListener('click', function(e){
	e.preventDefault();
	fetch('http://localhost:8090/covid/form/deleteImages', {
		method: 'DELETE',
		}) 
		.then(function (response) {
			return response.text();
		}) 
		.then(function (data) {
			console.log(data);
			removeImages();
		}) 
		.catch(function (err) {
		 	console.log(err);
		});
});

function removeImages(){
	containerImages.innerHTML = '';
}