//Validacion radio button
const boton_datos= document.querySelector('.boton_datos');
const boton_A_patologicos = document.querySelector(".boton_patologicos");
const boton_A_nopatologicos= document.querySelector(".boton_nopatologicos");
const boton_antecedentes_pre = document.querySelector(".boton_antecedentes_pre");
const boton_historia_desarrollo = document.querySelector(".boton_historia_desarrollo");
const boton_antecedentes = document.querySelector(".boton_antecedentes_escolares");
const boton_dinamica = document.querySelector (".boton_dinamica");

//DATOS PACIENTES ///
boton_datos.addEventListener("click", function(event){
  let hasError = false;
if(!document.querySelector('input[name="sexo"]:checked')) {
  alert('Elija un sexo');
  hasError = true;
  }
  if(!document.querySelector('input[name="estado-civil"]:checked')) {
    alert('Elija estado civil ');
    hasError = true;
    }
    

if(hasError) event.preventDefault();
});

//ANTECEDENTES NO PATOLOGICOS 
boton_A_nopatologicos.addEventListener("click", function(event){
  let hasError = false;
if(!document.querySelector('input[name="hogar"]:checked')) {
  alert('Elija donde vive');
  hasError = true;
  }
  if(!document.querySelector('input[name="lugar"]:checked')) {
  alert('Elija de quien es el lugar donde vive');
  hasError = true;
  }
  if(!document.querySelector('input[name="habitacion"]:checked')) {
    alert('Elija la habitacion ');
    hasError = true;
    }
    if(!document.querySelector('input[name="actividades"]:checked')) {
      alert('Elija si realiza alguna actividad');
      hasError = true;
      }
      if(!document.querySelector('input[name="amigos"]:checked')) {
        alert('Elija si tiene amigos');
        hasError = true;
        }
        if(!document.querySelector('input[name="salirAmigos"]:checked')) {
        alert('Elija la opcion si sale con amigos ');
        hasError = true;
        }

if(hasError) event.preventDefault();
});

//ANTECEDENTES PATOLÓGICOS///
boton_A_patologicos.addEventListener("click", function(event){
  let hasError = false;

  if(!document.querySelector('input[name="estaEnfermo"]:checked')) {
    alert('Elija si esta enfermo');
    hasError = true;
    }
    if(!document.querySelector('input[name="secuelas"]:checked')) {
      alert('Elija si tiene secuelas');
      hasError = true;
      }
      if(!document.querySelector('input[name="Lentes"]:checked')) {
        alert('Elija si usa lentes');
        hasError = true;
        }   
        if(!document.querySelector('input[name="aparatosOrtopedicos"]:checked')) {
          alert('Elija si usa aparatos ortopédicos');
          hasError = true;
          }    
  if(hasError) event.preventDefault();
});

//ANTECEDENTES PRE-PERI Y POSTNATALES///
boton_antecedentes_pre.addEventListener("click", function(event){
  let hasError = false;

  if(!document.querySelector('input[name="situacion"]:checked')) {
    alert('Error, rellena el campo situacion');
    hasError = true;
    }
    if(!document.querySelector('input[name="embarazoDeseado"]:checked')) {
      alert('Error, rellena el campo embarazoDeseado');
      hasError = true;
     }

  if(hasError) event.preventDefault();
});

//HISTORIA DEL DESARROLLO////
boton_historia_desarrollo.addEventListener("click", function(event){
  let hasError = false;

  if(!document.querySelector('input[name="Lloro"]:checked')) {
    alert('Elija uns opción');
    hasError = true;
    }
      if(!document.querySelector('input[name="Respiro"]:checked')) {
      alert('Elija una opción');
      hasError = true;
      }
        if(!document.querySelector('input[name="Incubadora"]:checked')) {
         alert('Elija si estubo o no en incubadora');
         hasError = true;
         }

  if(hasError) event.preventDefault();
});

//ANTECEDENTES ESCOLARES////
boton_antecedentes.addEventListener("click", function(event){
  let hasError = false;

  if(!document.querySelector('input[name="guarderia"]:checked')) {
    alert('Elija si estuvo en guarderia');
    hasError = true;
    }
      if(!document.querySelector('input[name="preescolar"]:checked')) {
      alert('Elija si estuvo en preescolar');
      hasError = true;
      }
        if(!document.querySelector('input[name="primaria"]:checked')) {
        alert('Elija si estuvo en primaria');
        hasError = true;
        }
          if(!document.querySelector('input[name="educacion_especial"]:checked')) {
          alert('Elija si estuvo en educacion especial');
          hasError = true;
          }
            if(!document.querySelector('input[name="Escolar"]:checked')) {
            alert('Elija si ha perdido algun grado escolar');
            hasError = true;
            }
              if(!document.querySelector('input[name="faltaclases"]:checked')) {
              alert('Elija si falta mucho a clases');
              hasError = true;
              }
              if(!document.querySelector('input[name="Cambioescuela"]:checked')) {
                alert('Elija si ha cambiado de escuela');
                hasError = true;
               }
                if(!document.querySelector('input[name="Aprende"]:checked')) {
                alert('Elija si aprende con facilidad');
                hasError = true;
                }
                  if(!document.querySelector('input[name="lee"]:checked')) {
                  alert('Elija como leé');
                  hasError = true;
                  }
                  if(!document.querySelector('input[name="Escribir"]:checked')) {
                    alert('Elija si le cuesta trabajo escribir');
                    hasError = true;
                   }


  if(hasError) event.preventDefault();
});

//DINAMICA FAMILIAR
boton_dinamica.addEventListener("click", function(event){
  let hasError = false;

  if(!document.querySelector('input[name="Suceso"]:checked')) {
    alert('Elija si algún suceso familiar ha afectado especialmente al niño(a)');
    hasError = true;
    }
    

  if(hasError) event.preventDefault();
});
//DINAMICA FAMILIAR