function addRows(data) {
  for(let image of data){
	  // Get a reference to the table
	  let tableRef = document.getElementById('my-table');
	
	  // Insert a row at the end of the table
	  let newRow = tableRef.insertRow(-1);
	  
	  // Insert a cell in the row at index 0
	  let newCell1 = newRow.insertCell(0);
	  let newCell2 = newRow.insertCell(1);
	  let newCell3 = newRow.insertCell(2);
	  let newCell4 = newRow.insertCell(3);
	  let newCell5 = newRow.insertCell(4);
	  let newCell6 = newRow.insertCell(5);
	  let newCell7 = newRow.insertCell(6);
	  let newCell8 = newRow.insertCell(7);
	  let newCell9 = newRow.insertCell(8);
	  
	  // Append a text node to the cell
	  let imagen = document.createElement("img")
	  imagen.src= image['rutaImagen'] + image['nombreImagen'];
	  newCell1.appendChild(imagen);
	  
	  let newText2 = document.createTextNode(image['tipoImagen']);
	  newCell2.appendChild(newText2);
		
	  let newText3 = document.createTextNode(image['enfermedad']);
	  newCell3.appendChild(newText3);
	
	  let newText4 = document.createTextNode(image['faseEnfermedad']);
	  newCell4.appendChild(newText4);
	  
	  let newText5 = document.createTextNode(image['edad']);
	  newCell5.appendChild(newText5);
	
	  let newText6 = document.createTextNode(image['peso'] + ' kg');
	  newCell6.appendChild(newText6);
	  
	  let newText7 = document.createTextNode(image['sexo']);
	  newCell7.appendChild(newText7);
	  
	  if(image['saturacionOxigeno'] === ""){
		let newText8 = document.createTextNode('No especificado');
	    newCell8.appendChild(newText8);
	  } else{
		let newText8 = document.createTextNode(image['saturacionOxigeno'] + ' %');
	    newCell8.appendChild(newText8);
	  }
	  
	  newCell9.innerHTML = `<p class="icon-edit"><i class="fas fa-edit"></i></p> <p class="icon-delete"><i class="fas fa-trash-alt"></i></p>`;
	  
  }
}

const requestGetImages = () => {
	fetch('http://localhost:8090/covid/admin/getImages', {
		 method: 'GET',
		})
		.then(function (response) {
			 return response.json();
		})
		.then(function (data) {
			 console.log(data);
			 addRows(data);
		})
		.catch(function (err) {
			 console.log(err);
		});
}

window.addEventListener('load', requestGetImages);
