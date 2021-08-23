let selectInput = '';
let dt;

function addRows(data) {
	selectInput === 'Detecciones' ? selectInput = 'Detecciones' : selectInput = 'Repositorio';
	console.log(selectInput);
	const thead = document.querySelector('thead');
	if(selectInput === 'Repositorio'){
		thead.innerHTML = `
			<tr>
                <th></th>
                <th>Imagen</th>
                <th>Tipo de imagen</th>
                <th>Enfermedad</th>
                <th>Fase</th>
                <th>Fecha</th>
                <th>Edad</th>
                <th>Peso</th>
                <th>Sexo</th>
                <th>SpO2</th>
            </tr>`;
    } else {
		thead.innerHTML = `
			<tr>
                <th></th>
                <th>Imagen de entrada</th>
                <th>Imagen de salida</th>
                <th>Prediagnóstico</th>
                <th>Tipo de imagen</th>
                <th>Fecha</th>
                <th>Edad</th>
                <th>Peso</th>
                <th>Sexo</th>
                <th>Confiabilidad</th>
            </tr>`;
	}

	const tbody = document.querySelector('tbody');
	tbody.innerHTML = '';
	let htmlRow = '';
	var nameImages = [];
	for(row of data) {
		if(selectInput === 'Repositorio'){
			nameImages.push(row.nombreImagen)
			htmlRow += `
				<tr>
	                <td><img src="${row.rutaImagen + row.nombreImagen}"/></td>
	                <td>${row.tipoImagen}</td>
	                <td>${row.enfermedad}</td>
	                <td>${row.faseEnfermedad}</td>
	                <td>${row.fecha}</td>
	                <td>${row.edad}</td>
	                <td>${row.peso} kg</td>
	                <td>${row.sexo}</td>
	                <td>${row.saturacionOxigeno} %</td>
	            </tr>`;
        } else {
			nameImages.push(row.nombreImagenDeteccion)
			htmlRow += `
				<tr>
	                <td><img src="${row.rutaImagen + row.nombreImagenDeteccion}"/></td>
	                <td><img src="${row.rutaImagen + row.nombreImagenMapa}"/></td>
	                <td>${row.prediagnostico}</td>
	                <td>${row.tipoImagen}</td>
	                <td>${row.fecha}</td>
	                <td>${row.edad}</td>
	                <td>${row.peso} kg</td>
	                <td>${row.sexo}</td>
	                <td>${row.confiabilidad} %</td>
	            </tr>`;
		}
	}
	tbody.innerHTML = htmlRow;
	dt = new DataTable('#datatable', [
            {
                id: 'bEditar',
                text: 'Editar',
                icon: '<i class="fas fa-edit"></i>',
                action: function () {
					if(dt.getSelected().length === 0) {
						alert('Marque una fila para editar');
					} 
					else if(dt.getSelected().length > 1){
						alert('Solo debe estar una fila marcada para editar');
					}
					else{
						if(selectInput === 'Repositorio'){
							console.log('Funcion editar repositorio');
							editActionRepositorio();
						} else {
							console.log('Funcion editar detecciones');
							editActionDetecciones();
						}
                    }
                }

            },
            {
                id: 'bEliminar',
                text: 'Eliminar',
                icon: '<i class="fas fa-trash-alt"></i>',
                action: function() {
					if(dt.getSelected().length === 0) {
						alert('Marque una o más filas para eliminar');
					} else {
						if(selectInput === 'Repositorio'){
							console.log('Funcion eliminar repositorio');
	                		deleteActionRepositorio();
						} else {
							console.log('Funcion eliminar detecciones');
							deleteActionDetecciones();
						}
					}
                }
            }
        ], selectInput);
	dt.parse(nameImages);
}

async function requestGetImagesRepository() {
		try {
        const response = await fetch('http://localhost:8090/covid/admin/getImagesRepository', {
            method: 'GET',
        });
        const data = await response.json();
        addRows(data);
    } catch (err) {
        console.log(err);
    }
};

requestGetImagesRepository();

async function requestGetImagesDetection() {
	try {
        const response = await fetch('http://localhost:8090/covid/admin/getImagesDetection', {
            method: 'GET',
        });
        const data = await response.json();
        addRows(data);
    } catch (err) {
        console.log(err);
    }
};

function editActionRepositorio(){
	const elements = dt.getSelected();
	const valuesElement = elements[0].values;
	let img = valuesElement[0];
	let nombreImagen = img.split("/")[img.split("/").length - 2].replace(/['"]/g, '');
	let tipoImagen = valuesElement[1];
	let enfermedad = valuesElement[2];
	let faseEnfermedad = valuesElement[3];
	let edad = valuesElement[5];
	let peso = valuesElement[6].split(' ')[0];
	let sexo = valuesElement[7];
	let SpO2 = valuesElement[8].split(' ')[0];
	console.log(valuesElement);
	let modalHtml = `
		<h1>Editar Imagen Seleccionada</h1>
		${img}
		<form id="form">
				<input type="hidden" name="nombreImagen" value="${nombreImagen}" required>
                <div class="input-group"> 
                    <label class="input-group__label" for="">Edad del paciente*:<abbr title="Este campo es obligatorio" aria-label="requeried"></abbr></label>
                    <div class="input-group__input">
                        <input type="text" name="edad" value="${edad}" maxlength="2" required>
                    </div>
                </div>

                <div class="input-group">
                    <label class="input-group__label" for="">Peso del paciente (kg)*:<abbr title="Este campo es obligatorio" aria-label="requeried"></abbr></label>
                    <div class="input-group__input">
                        <input type="text" name="peso" value="${peso}" maxlength="5" required>
                    </div>
                </div>
                
                    <div class="input-group">
                        <label  class="input-group__label">Sexo del paciente*:<abbr title="Este campo es obligatorio" aria-label="required"></abbr></label>
                        <div class="input-group__input">
                            <input type="radio" required name="sexo" id="masculino" value="Masculino"> Masculino
                            <input type="radio" required name="sexo" id="femenino" value="Femenino"> Femenino
                        </div>
                    </div>

                <div class="input-group">
                    <label class="input-group__label" for="">Saturación de oxígeno (%):</label>
                    <div class="input-group__input">
                        <input type="text" name="saturacionOxigeno" value="${SpO2}" maxlength="5">
                    </div>
                </div>

                <div class="input-group">
                    <label class="input-group__label">Enfermedad*:<abbr title="Este campo es obligatorio" aria-label="required"></abbr></label>
                    <div class="input-group__input">
                        <select name="enfermedad" id="combo_enfermedad" class="combo_box" required>
                            <option value="COVID-19">COVID-19</option>
                            <option value="Neumonía viral">Neumonía Viral</option>
                            <option value="Neumonía bacteriana">Neumonía Bacteriana</option>
                            <option value="Sano">Sano</option>
                        </select>
                    </div>
                </div>

            <div class="input-group">
                <label class="input-group__label">Fase de la enfermedad*:<abbr title="Este campo es obligatorio" aria-label="required"></abbr></label>
                <div class="input-group__input">
                       <select name="faseEnfermedad" id="combo_faseEnfermedad" class="combo_box" required>
                        <option value="Moderada">Moderada</option>
                        <option value="Severa">Severa</option>
                        <option value="Crítica">Crítica</option>
                        <option value="Desconocida">Desconocida</option>
                    </select>
                </div>
            </div>

            <div class="input-group">  
                <label class="input-group__label">Tipo de imagen*:<abbr title="Este campo es obligatorio" aria-label="required"></abbr></label>
                <div class="input-group__input">
                    <select name="tipoImagen" id="combo_imagen" class="combo_box" required>
                        <option value="Radiografía de toráx"><label>Radiografía de toráx</label></option>
                        <option value="Tomografía computarizada"><label>Tomografía computarizada</label></option>
                    </select>
                </div>
            </div>
               
            <div class="modal-buttons">
		        <button id="close">Cancelar</button>
				<button id="save">Guardar</button>
			</div>
        </form>
		`;
	showModal(modalHtml);
	document.querySelector(`.input-group__input > input[value="${sexo}"]`).checked = true;
	document.getElementById('combo_enfermedad').value = enfermedad;
	document.getElementById('combo_faseEnfermedad').value = faseEnfermedad;
	document.getElementById('combo_imagen').value = tipoImagen;
}

function requestUpdateImageRepositorio(){
	let dataUpdate = new FormData();
	dataUpdate.append("nombreImagen", document.querySelector('input[name="nombreImagen"]').value)
	dataUpdate.append("edad", document.querySelector('input[name="edad"]').value);
	dataUpdate.append("peso", document.querySelector('input[name="peso"]').value);
	let sexoValue = "";
	if(document.getElementById('masculino').checked) sexoValue = document.getElementById('masculino').value;
	if(document.getElementById('femenino').checked) sexoValue = document.getElementById('femenino').value;
	dataUpdate.append("sexo", sexoValue);
	dataUpdate.append("saturacionOxigeno", document.querySelector('input[name="saturacionOxigeno"]').value);
	dataUpdate.append("enfermedad", document.querySelector('select[name="enfermedad"]').value);
	dataUpdate.append("faseEnfermedad", document.querySelector('select[name="faseEnfermedad"]').value);
	dataUpdate.append("tipoImagen", document.querySelector('select[name="tipoImagen"]').value);
	
	fetch('http://localhost:8090/covid/admin/updateImageRepository', {
		method: 'PUT',
		body: dataUpdate
	})
	.then(res => {
		res.text();
	})
	.then(async data => {
		console.log(data);
		let actualPage = dt.pagination.actual - 1;
		await requestGetImagesRepository();
		let liButtons = dt.element.querySelectorAll('.pages li');
		liButtons[actualPage].firstElementChild.click();
	})
	.catch(err => {
		console.error(err);
	});
}

function editActionDetecciones(){
	const elements = dt.getSelected();
	const valuesElement = elements[0].values;
	let imgEntrada = valuesElement[0];
	let nombreImagen = imgEntrada.split("/")[imgEntrada.split("/").length - 2].replace(/['"]/g, '');
	let imgSalida = valuesElement[1];
	let diagnostico = valuesElement[2];
	let tipoImagen = valuesElement[3];
	let edad = valuesElement[5];
	let peso = valuesElement[6].split(' ')[0];
	let sexo = valuesElement[7];
	let confiabilidad = valuesElement[8].split(' ')[0];
	
	let modalHtml = `
		<h1>Editar Imagen Seleccionada</h1>
		${imgEntrada}
		${imgSalida}
		<form id="form">
			<input type="hidden" name="nombreImagen" value="${nombreImagen}" required>
        	<div class="input-group"> 
                <label class="input-group__label" for="">Edad del paciente*:<abbr title="Este campo es obligatorio" aria-label="required"></abbr></label>
                <div class="input-group__input">
                    <input type="text" name="edad" value="${edad}" maxlength="2" required>
                </div>
            </div>
            <div class="input-group">
                <label class="input-group__label" for="">Peso del paciente (kg)*:<abbr title="Este campo es obligatorio" aria-label="required"></abbr></label>
                <div class="input-group__input">
                    <input type="text" name="peso" value="${peso}" maxlength="5" required>
                </div>
            </div>
            <div class="input-group">
                <label class="input-group__label" for="">Sexo del paciente*:<abbr title="Este campo es obligatorio" aria-label="required"></abbr></label>
                <div class="input-group__input">
                    <input type="radio" name="sexo" id="masculino" value="Masculino"> Masculino
                    <input type="radio" name="sexo" id="femenino" value="Femenino"> Femenino
                </div>
            </div>
            <div class="input-group"> 
                <label class="input-group__label" for="">Confiabilidad (%)*:<abbr title="Este campo es obligatorio" aria-label="required"></abbr></label>
                <div class="input-group__input">
                    <input type="text" name="confiabilidad" value="${confiabilidad}" maxlength="6" required>
                </div>
            </div>
            <div class="input-group">
                    <label class="input-group__label">Diagnóstico*:<abbr title="Este campo es obligatorio" aria-label="required"></abbr></label>
                    <div class="input-group__input">
                        <select name="enfermedad" id="combo_diagnostico" class="combo_box" required>
                            <option value="COVID-19">COVID-19</option>
                            <option value="Neumonía viral">Neumonía Viral</option>
                            <option value="Neumonía bacteriana">Neumonía Bacteriana</option>
                            <option value="Sano">Sano</option>
                        </select>
                    </div>
                </div>
            <div class="input-group">  
                <label class="input-group__label">Tipo de imagen*:<abbr title="Este campo es obligatorio" aria-label="required"></abbr></label>
                <div class="input-group__input">
                    <select name="tipoImagen" id="combo_imagen" class="combo_box" required>
                        <option value="Radiografía de toráx"><label>Radiografía de toráx</label></option>
                        <option value="Tomografía computarizada"><label>Tomografía computarizada</label></option>
                    </select>
                </div>
            </div>
            <div class="modal-buttons">
		        <button id="close">Cancelar</button>
				<button id="save">Guardar</button>
			</div>
        </form>
	`;
	
	showModal(modalHtml);
	document.querySelector(`.input-group__input > input[value="${sexo}"]`).checked = true;
	document.getElementById('combo_diagnostico').value = diagnostico;
	document.getElementById('combo_imagen').value = tipoImagen;
}

function requestUpdateImageDetecciones(){
	let formData = new FormData();
	formData.append("nombreImagen", document.querySelector('input[name="nombreImagen"]').value);
	formData.append("edad", document.querySelector('input[name="edad"]').value);
	formData.append("peso", document.querySelector('input[name="peso"]').value);
	let sexoValue = "";
	if(document.getElementById('masculino').checked) sexoValue = document.getElementById('masculino').value;
	if(document.getElementById('femenino').checked) sexoValue = document.getElementById('femenino').value;
	formData.append("sexo", sexoValue);
	formData.append("confiabilidad", document.querySelector('input[name="confiabilidad"]').value);
	formData.append("enfermedad", document.querySelector('select[name="enfermedad"]').value);
	formData.append("tipoImagen", document.querySelector('select[name="tipoImagen"]').value);
	
	fetch('http://localhost:8090/covid/admin/updateImageDetection', {
		method: 'PUT',
		body: formData
	})
	.then(res => {
		res.text();
	})
	.then(async data => {
		console.log(data);
		let actualPage = dt.pagination.actual - 1;
		await requestGetImagesDetection();
		let liButtons = dt.element.querySelectorAll('.pages li');
		liButtons[actualPage].firstElementChild.click();
	})
	.catch(err => {
		console.error(err);
	});
}

function deleteRow(elements) {
	for(element of elements) {
		dt.removeSelected(element.id);
		dt.items = dt.items.filter(item => item.id !== element.id);
	}
}

function requestDeleteImagesRepositorio(dataDelete) {
	fetch('http://localhost:8090/covid/admin/deleteImagesRepositorio', {
			method: 'DELETE',
			body: dataDelete
		})
		.then(function (response) {
			return response.json();
		})
		.then(function (elements) {
			console.log(elements);
			let actualPage = dt.pagination.actual - 1;
			deleteRow(elements);
			dt.maketable();
			let liButtons = dt.element.querySelectorAll('.pages li');
			if(liButtons[actualPage] === undefined) {
				liButtons[liButtons.length - 1].firstChild.click();
			} else {
				liButtons[actualPage].firstChild.click();
			}
		})
		.catch(function (err) {
			 console.log(err);
		});
}

function deleteActionRepositorio(){
    const elements = dt.getSelected();
    const json = '{item:'+JSON.stringify(elements)+'}';
    console.log(json);

    const formData = new FormData();
    formData.append('nameImages', json);

    requestDeleteImagesRepositorio(formData);
}

function requestDeleteImagesDetecciones(dataDelete) {
	fetch('http://localhost:8090/covid/admin/deleteImagesDetecciones', {
			method: 'DELETE',
			body: dataDelete
		})
		.then(function (response) {
			return response.json();
		})
		.then(function (elements) {
			console.log(elements);
			let actualPage = dt.pagination.actual - 1;
			deleteRow(elements);
			dt.maketable();
			let liButtons = dt.element.querySelectorAll('.pages li');
			if(liButtons[actualPage] === undefined) {
				liButtons[liButtons.length - 1].firstChild.click();
			} else {
				liButtons[actualPage].firstChild.click();
			}
		})
		.catch(function (err) {
			 console.log(err);
		});
}

function deleteActionDetecciones() {
	const elements = dt.getSelected();
    const json = '{item:'+JSON.stringify(elements)+'}';
    console.log(json);

    const formData = new FormData();
    formData.append('nameImages', json);

    requestDeleteImagesDetecciones(formData);
}

class DataTable{
	
	selectInput;
    element; // Representa toda la tabla
    headers;
    items;
    copyItems; // Copia de los items para la busqueda
    selected; // Arreglo con todos los items seleccionados
    pagination; // Calcular las páginas
    numberOfEntries; // Número de entradas que se desea mostrar por página
    headerButtons; // Acciones en la tabla

    constructor(selector, headerButtons, selectInput){
		this.selectInput = selectInput;
        this.element = document.querySelector(selector);
        this.headers = [];
        this.items = [];
        this.pagination = {total: 0, // Número de items que tiene la tabla
            noItemsPerPage: 0, // Número de items por pagina
            noPages: 0, // Número de paginas
            actual: 0,
            pointer: 0,
            diff: 0,
            lastPageBeforeDots:0,
            noButtonsBeforeDots: 4
        };

        this.selected = [];
        this.numberOfEntries = 5; // Número de entradas que se van a mostrar al inicio
        this.headerButtons = headerButtons;
    }

    parse(nameImages){
        const headers = [...this.element.querySelector('thead tr').children]; // Información del header
        const trs = [...this.element.querySelector('tbody').children]; //  Información de las filas

        headers.forEach(element => {
            this.headers.push(element.textContent)
        });
	
		let index = 0;
        trs.forEach(tr => {
            const cells = [...tr.children];

            const item = {
                id: nameImages[index],
                values: []
            };
            
            index++;
            
            cells.forEach(cell => {
				if(cell.children.length > 0) {
					const image = [...cell.children][0].getAttribute('src');
					if(image !== null) {
						item.values.push(`<img src='${image}'/>`);
					}
	        	} else {
	            	item.values.push(cell.textContent);
	            }
            });
            this.items.push(item);
        });

        console.log(this.items);

        this.maketable();
    }

    maketable(){
        this.copyItems = [...this.items];

        this.initPagination(this.items.length, this.numberOfEntries); // Validación de las páginas

        const container = document.createElement('div');
        container.id = this.element.id;
        this.element.innerHTML = '';
        this.element.replaceWith(container);
        this.element = container;

        this.createHTML(); // Crea la estructura básica
        this.renderHeaders(); // Contruye los headers de los datos
        this.renderRows(); // Contruye las filas de la tabla
        this.renderPagesButtons(); // Crea los botones para la paginación
        this.renderHeaderButtons(); // Añadir los botones de acción
        this.renderSearch(); 
        this.renderSelectEntries(); // Crea los elementos que se seleccionaron
    }

    initPagination(total, entries){
        this.pagination.total = total;
        this.pagination.noItemsPerPage = entries;
        this.pagination.noPages = Math.ceil(this.pagination.total / this.pagination.noItemsPerPage);
        this.pagination.actual = 1;
        this.pagination.pointer = 0;
        this.pagination.diff = this.pagination.noItemsPerPage - (this.pagination.total % this.pagination.noItemsPerPage);
    }

    createHTML(){
        this.element.innerHTML = `
        <div class="datatable-container">
            <div class="header-tools">
                <div class="tools">
                    <ul id="header-buttons-container">
                    </ul>
                </div>
                <div class="select">
                	<select class="select-input" id="select-input">
                		<option value="Repositorio">Repositorio</option>
                		<option value="Detecciones">Detecciones</option>
                	</select>
                </div>
            </div>
            <table class="datatable">
                <thead>
                    <tr>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
            <div class="footer-tools">
                <div class="list-items">
                    Mostrar
                    <select class="n-entries" name="n-entries" id="n-entries" class="n-entries">
                        <option value="5">5</option>
                        <option value="10">10</option>
                        <option value="15">15</option>
                    </select>
                    entradas
                </div>
                
                <div class="pages">
                </div>
            </div>
        </div>`;
    }

    renderHeaders(){
		const select = document.getElementById('select-input');
		select.value = this.selectInput;
		select.addEventListener('change', (e) => {
			selectInput = e.target.value;
			if(selectInput === "Repositorio") requestGetImagesRepository();
			else requestGetImagesDetection();
			
		});

        this.element.querySelector('thead tr').innerHTML = '';
        this.headers.forEach(header => {
            this.element.querySelector('thead tr').innerHTML += `<th>${header}</th>`;
        });
    }

    renderRows(){
        this.element.querySelector('tbody').innerHTML = '';

        let i = 0;
        const {pointer, total} = this.pagination;
        const limit = this.pagination.actual * this.pagination.noItemsPerPage;

        for(i = pointer; i < limit; i++) {
            if(i === total) break;

            const{id, values} = this.copyItems[i];
            const checked = this.isChecked(id);

            let data = '';

            data += `<td class="table-checkbox">
                        <input type="checkbox" class="datatable-checkbox" data-id="${id}" ${checked ? "checked" : ""}>
                    </td>`;

            values.forEach(cell => {
                data += `<td>${cell}</td>`;
            });

            this.element.querySelector('tbody').innerHTML += `<tr>${data}</tr>`

            // Listener para el checkbox
            document.querySelectorAll('.datatable-checkbox').forEach(checkbox => {
                checkbox.addEventListener('click', e => {
                    const element = e.target;
                    const id = element.getAttribute('data-id');

                    if(element.checked) {
                        const item = this.getItem(id);
                        this.selected.push(item);
                    } else {
                        this.removeSelected(id);
                    }
                });
            });
        }
    }

    isChecked(id){
        const items = this.selected;
        let res = false;

        if(items.length === 0) return false;

        items.forEach(item => {
            if(item.id === id) res = true;
        });

        return res;
    }

    getItem(id) {
        const res = this.items.filter(item => item.id === id);
        if(res.length === 0) return null;
        return res[0];
    }

    removeSelected(id) {
        const res = this.selected.filter(item => item.id !== id);
        this.selected = [...res];
    }

    renderPagesButtons() {
        const pagesContainer = this.element.querySelector('.pages');
        let pages = '';

        const buttonsToShow = this.pagination.noButtonsBeforeDots;
        const actualIndex = this.pagination.actual;

        let limI = Math.max(actualIndex - 2, 1);
        let limS = Math.min(actualIndex + 2, this.pagination.noPages);
        const missinButtons = buttonsToShow - (limS - limI);

        if(Math.max(limI - missinButtons, 0)) {
            limI = limI - missinButtons;
        } else if(Math.min(limS + missinButtons, this.pagination.noPages) !== this.pagination.noPages) {
            limS = limS + missinButtons;
        }

        if(limS < (this.pagination.noPages - 2)) {
            pages += this.getIteratedButtons(limI, limS);
            pages += `<li>...</li>`;
            pages += this.getIteratedButtons(this.pagination.noPages - 1, this.pagination.noPages);
        } else {
            pages += this.getIteratedButtons(limI, this.pagination.noPages);
        }

        pagesContainer.innerHTML = `<ul>${pages}</ul>`;

        this.element.querySelectorAll('.pages li button').forEach(button => {
            button.addEventListener('click', e => {
                this.pagination.actual = parseInt(e.target.getAttribute('data-page'));
                this.pagination.pointer = (this.pagination.actual * this.pagination.noItemsPerPage) - this.pagination.noItemsPerPage;
                this.renderRows();
                this.renderPagesButtons();
            });
        });
    }

    getIteratedButtons(start, end) {
        let res = '';
        for(let i = start; i <= end; i++) {
            if(i === this.pagination.actual) {
                res += `<li><span class="active">${i}</span></li>`;
            } else {
                res += `<li><button data-page="${i}">${i}</button></li>`;
            }
        }

        return res;
    }

    renderHeaderButtons(){
        let html = '';
        const buttonsContainer = this.element.querySelector('#header-buttons-container');
        const headerButtons = this.headerButtons;

        headerButtons.forEach(button => {
            html += `<li><button id="${button.id}">${button.icon} ${button.text}</button></li>`;
        });

        buttonsContainer.innerHTML = html;

        headerButtons.forEach(button => {
            document.querySelector('#' + button.id).addEventListener('click', button.action);
        });
    }

    renderSearch(){}

    renderSelectEntries() {
        const select = this.element.querySelector('#n-entries');

        const html = [5,10,15].reduce((acc, item) => {
            return acc += `<option value="${item}" ${this.numberOfEntries === item ? 'selected' : ''}>${item}</option>`; // ARREGLAR
        }, '');

        select.innerHTML = html;

        this.element.querySelector('#n-entries').addEventListener('change', e => {
            const numberOfEntries = parseInt(e.target.value);
            this.numberOfEntries = numberOfEntries;

            this.initPagination(this.copyItems.length, this.numberOfEntries);
            this.renderRows();
            this.renderPagesButtons();
        });
    }

    getSelected(){
        return this.selected;
    }
}

// Ventana modal
const modalContainer = document.getElementById('modal-container');
const modalBody = document.getElementById('modal-body');

const showModal = function(modalHtml) {
	modalBody.innerHTML = modalHtml;
	modalContainer.classList.add('show');
	window.addEventListener('click', closeModal);
	document.getElementById('close').addEventListener('click', () => {
		modalContainer.classList.remove('show');
	});
	const form = document.getElementById('form');
	form.addEventListener('submit', e => {
		e.preventDefault();
		modalContainer.classList.remove('show');
		if(selectInput === 'Repositorio') requestUpdateImageRepositorio();
		if(selectInput === 'Detecciones') requestUpdateImageDetecciones();
	});
}
	
const closeModal = function(e){
	if(e.target === modalContainer){
		modalContainer.classList.remove('show');
		window.removeEventListener('click', closeModal);
	}
}