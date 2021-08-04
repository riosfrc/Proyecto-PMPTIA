const boton_datos= document.querySelector('.boton_datos');
const boton_A_nopatologicos= document.querySelector(".boton_nopatologicos");
const boton_historia_desarrollo = document.querySelector(".boton_historia_desarrollo");
var familiar_responable = document.querySelector('#familiar_responable');
var escolarida_paciente= document.querySelector('#escolarida_paciente');
var escolaridad_padre= document.querySelector('#escolaridad_padre');
var escolaridad_madre= document.querySelector('#escolaridad_madre');
var donde_conoce= document.querySelector('#donde_conoce');
var cuantos_amigostiene= document.querySelector('#cuantos_amigostiene');
var tipo_nacimiento = document.querySelector('#tipo_nacimiento');

//DATOS DEL PACIENTE
boton_datos.addEventListener("click", function(event){
    
        if(familiar_responable.value==""){
	            alert("Selecciona un familiar responsable");
  		event.preventDefault();
        }
        
        if(escolarida_paciente.value==""){
          alert("Selecciona una escolaridad del paciente");
  		event.preventDefault();
         }

         if(escolaridad_padre.value==""){
          alert("Seleccione una escolaridad del padre");
  		event.preventDefault();
         }

         if(escolaridad_madre.value==""){
          alert("Seleccione una escolaridad de la madre");
  		event.preventDefault();
         }
    
});




//ANTECEDENTES NOPATOLÓGICOS/////

boton_A_nopatologicos.addEventListener("click", function(event){
    
  if(donde_conoce.value==""){
        alert("Selecciona de donde conoce sus amigos");
event.preventDefault();
  }
  
  if(cuantos_amigostiene.value==""){
    alert("Selecciona cuantos amigos tiene");
event.preventDefault();
   }



});
//HISTORA DEL DESARROLLO ///

boton_historia_desarrollo.addEventListener("click", function(event){
    
  if(tipo_nacimiento.value==""){
        alert("Selecciona el tipo de nacimiento");
		event.preventDefault();
  }
  


});