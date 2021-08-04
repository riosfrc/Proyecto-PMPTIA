const formulario = document.getElementById('formulario');
const inputs = document.querySelectorAll('#formulario input[type="text"]');
const botonDatos = document.querySelector('.boton_datos');
const botonNoPatologicos = document.querySelector(".boton_nopatologicos");
const botonPatologicos = document.querySelector(".boton_patologicos");
const botonAntecedentesPre = document.querySelector(".boton_antecedentes_pre");
const botonHistoriaDesarrollo = document.querySelector(".boton_historia_desarrollo");
const botonAntecedentes = document.querySelector(".boton_antecedentes_escolares");
const botonDinamica = document.querySelector (".boton_dinamica");

const validarFormulario = (e) => {
    if(e.target.value===null || e.target.value==='') {
       e.target.classList.add('input-group-error');
       e.target.setAttribute ("error","true");
    } else {
        e.target .classList.remove('input-group-error');
        e.target.setAttribute ("error","false");
    }
};

const validarClickEventInputsText = (inicio, final) => {
	for(index=inicio; index < inputs.length; index++) {
		
        if(inputs[index].getAttribute("error") === "true" || inputs[index].getAttribute("error") === null) {
            inputs[index].classList.add('input-group-error');
        }
        
        if(index == final) break;
	}
}


botonDatos.addEventListener("click",() => {
   	validarClickEventInputsText(0,16);
});

botonNoPatologicos.addEventListener("click",() => {
      validarClickEventInputsText(17,53);
});

botonPatologicos.addEventListener("click",() => {
      validarClickEventInputsText(54,69);
});

botonAntecedentesPre.addEventListener("click",() => {
      validarClickEventInputsText(70,81);
});

botonHistoriaDesarrollo.addEventListener("click",() => {
      validarClickEventInputsText(82,93);
});
botonAntecedentes.addEventListener("click",() => {
      validarClickEventInputsText(94,126);
});
botonDinamica.addEventListener("click",() => {
      validarClickEventInputsText(127,131);
});


inputs.forEach((input) =>{
    input.addEventListener('blur',validarFormulario);
    input.addEventListener('keyup',validarFormulario);
});