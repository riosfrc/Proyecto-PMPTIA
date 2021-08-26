function addSessions(sessions) {
	let count = 1;
	let sessionHtml = '';
	sessions.forEach(session => {
		sessionHtml += `
	    <div class="cajita1">
	         <!--CONTIENE EL VIDEO Y DATOS DEL PACIENTE DEL LADO IZQUIERDO-->
	        <div class="cajita1-left">
		        <div class="sesion"> 
		        
		            <p> Numero se sesión: </p><br>
		            <p class="numero_sesion"> ${count} </p>
		        
		       	</div>
	            <video width="200px" class="fm-video video-js vjs-big-play-centered" data-setup="{}" controls id="fm-video" loop >
	                <source src="${session.ruta}${session.nombre}" type="video/mp4">
	            </video>
	            
	            <div class="cajita1-left">
	                <div>   
	                    
	                <p>Nombre: ${session.nombrePaciente} ${session.apellidoPaciente}</p><br>
	                <p>Edad: ${session.edadPaciente}</p><br>
	                <p>Fecha: ${session.fecha}</p><br>
	                    
	                </div> 
	        	</div>
	        </div>
	        
	        <!--CONTIENE LADO DERECHO-->
	        <div class="cajita1-right">
	        
	            <div class="descargar_caja1">
	                <div class="icon-descargar1"> <i class="fas fa-download"></i> </div>
	                <label>Descargar</label>
	            </div>
	            <div class="borrar_caja1">
	                <div class="icon-borrar"> <i class="fas fa-trash-alt"></i>  </div>
	                <label>Borrar</label>
	            </div>
	            
	        </div>
	                       
	    </div>
	    <div class="division"></div>`;
	    
	    count++;
	});

	const sessionsContainer = document.querySelector(".contenedor");
	sessionsContainer.innerHTML = sessionHtml;
}


(function requestGetAllSessions() {
	fetch('http://localhost:8090/tdah/session/getAll', {
		method: 'GET'
	})
	.then(res => res.json())
	.then(data => {
		console.log(data)
		addSessions(data);
	})
	.catch(err => console.error(err))
})();