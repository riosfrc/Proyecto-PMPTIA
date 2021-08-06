function addRows(data) {
	let htmlRow = '';
	const thead = document.querySelector('thead');
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
	const tbody = document.querySelector('tbody');
	tbody.innerHTML = '';
	var nameImages = [];
	for(row of data) {
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
	}
	tbody.innerHTML = htmlRow;
	dt.parse(nameImages);
}

(function requestGetImages() {
	fetch('http://localhost:8090/covid/admin/getImages', {
			method: 'GET',
		})
		.then(function (response) {
			 return response.json();
		})
		.then(function (data) {
			 addRows(data);
		})
		.catch(function (err) {
			 console.log(err);
		});
})();

function requestDeleteImages(dataDelete) {
	fetch('http://localhost:8090/covid/admin/deleteImages', {
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

function deleteRow(elements) {
	for(element of elements) {
		dt.removeSelected(element.id);
		dt.items = dt.items.filter(item => item.id !== element.id);
	}
}

function deleteAction(){
    const elements = dt.getSelected();
    const json = '{item:'+JSON.stringify(elements)+'}';
    console.log(json);

    const formData = new FormData();
    formData.append('nameImages', json);

    requestDeleteImages(formData);
}

class DataTable{

    element; // Representa toda la tabla
    headers;
    items;
    copyItems; // Copia de los items para la busqueda
    selected; // Arreglo con todos los items seleccionados
    pagination; // Calcular las páginas
    numberOfEntries; // Número de entradas que se desea mostrar por página
    headerButtons; // Acciones en la tabla

    constructor(selector, headerButtons){
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