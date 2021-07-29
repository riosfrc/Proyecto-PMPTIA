const formulario = document.getElementById('formulario');
const inputs = document.querySelectorAll('#formulario input');

const validarFormulario = (e) =>{
    if(e.target.value===null || e.target.value===''){
       
       e.target .classList.add('input-group-error');
    }else {
        e.target .classList.remove('input-group-error');
        
    }

}

inputs.forEach((input) =>{
    input.addEventListener('blur',validarFormulario);
});