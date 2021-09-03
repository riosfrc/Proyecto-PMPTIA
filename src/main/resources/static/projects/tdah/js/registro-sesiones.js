const pathname = window.location.pathname;
const splitPathname = pathname.split('/');
const idPaciente = splitPathname[splitPathname.length - 2];

function addSessions(sessions) {
	let count = 1;
	let sessionHtml = '';
	if(sessions.length > 0) {
		sessionHtml = `
			<div class="sesiones-sidebar">
                <div class="button-new-sesion">
            		<a href="/tdah/patient/${idPaciente}/new-session"><span><i class="fas fa-video"></i></span> Nueva sesión</a>
            	</div>
            	<div class="button-sort-sesion">
            		<button>Ordenar por <span><i class="fas fa-caret-down"></i></span></button>
            	</div>
            </div>
            <div class="sesiones-grabadas">`;
            
		sessions.forEach(session => {
			sessionHtml += `
			    <div class="cajita1">
			         <!--CONTIENE EL VIDEO Y DATOS DEL PACIENTE DEL LADO IZQUIERDO-->
			        <div class="cajita1-left">
				        <div class="sesion"> 
				        
				            <p>Número de sesión</p><br>
				            <p class="numero_sesion"> ${count} </p>
				        
				       	</div>
			            <video width="200px" class="fm-video video-js vjs-big-play-centered" data-setup="{}" controls id="fm-video" loop >
			                <source src="${session.ruta}${session.nombre}" type="video/mp4">
			            </video>
			            
			            <div class="cajita1-left">
			                <div class="info">   
			                    
			                	<p>Nombre: ${session.nombrePaciente} ${session.apellidoPaciente}</p><br>
			                	<p>Edad: ${session.edadPaciente}</p><br>
			                	<p>Fecha: ${session.fecha}</p><br>
			                    
			                </div> 
			        	</div>
			        </div>
			        
			        <!--CONTIENE LADO DERECHO-->
			        <div class="cajita1-right">
			            <div class="borrar_caja1">
			                <div class="icon-borrar" data-id="${session.nombre}"> 
			                	<i class="fas fa-trash-alt"></i>
			                </div>
			                <label>Borrar</label>
			            </div>
			        </div>             
			    </div>`;
		    
		    count++;
		});
		
		sessionHtml += `</div>`;
		const sessionsContainer = document.getElementById('registro-sesiones');
		sessionsContainer.innerHTML = sessionHtml;
		
		const iconsDelete = document.querySelectorAll('.fa-trash-alt');
		iconsDelete.forEach(iconDelete => {
			iconDelete.addEventListener('click', e => {
				let dataId = e.target.parentElement.getAttribute('data-id');
				let formData = new FormData();
				formData.append('name', dataId)
				requestDeleteSession(formData);
			});
		});
	} else {
		sessionHtml = `
			<div class="sesiones-sidebar">
				<p>No tiene ninguna sesión registrada hasta ahora.</p>
				</br>
            	</br>
                <div class="button-new-sesion">
            		<a href="/tdah/patient/${idPaciente}/new-session"><span><i class="fas fa-video"></i></span> Nueva sesión</a>
            	</div>
            	</br>
            	</br>
            	</br>
            	</br>
            </div>
		`;
		const sessionsContainer = document.getElementById('registro-sesiones');
		sessionsContainer.innerHTML = sessionHtml;
	}
}


function requestGetAllSessions() {
	fetch(`http://localhost:8090/tdah/session/${idPaciente}/getAll`, {
		method: 'GET'
	})
	.then(res => res.json())
	.then(data => {
		console.log(data)
		addSessions(data);
	})
	.catch(err => console.error(err))
};

requestGetAllSessions();


function requestDeleteSession(formData) {
	fetch('http://localhost:8090/tdah/session/delete', {
		method: 'DELETE',
		body: formData
	})
	.then(res => res.json())
	.then(data => {
		console.log(data);
		requestGetAllSessions();
	})
	.catch(err => console.log(err))
}
