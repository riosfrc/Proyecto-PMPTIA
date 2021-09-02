const datosPersonales = document.getElementById("datos-personales");
const antecedentesNoPatologicos = document.getElementById("antecedentes-personales-no-patologicos");
const antecedentesPatologicos = document.getElementById("antecedentes-personales-patologicos");
const antecedentesPostnatales = document.getElementById("antecedentes-pre-peri-y-postnatales");
const historiaDelDesarrollo = document.getElementById("historia-del-desarrollo");
const antecedentesEscolares = document.getElementById("antecedentes-escolares");
const dinamicaFamiliar = document.getElementById("dinamica-familiar");
const aspectoDePersonalidad = document.getElementById("aspecto-de-personalidad-emocionales");
const enviarFormulario = document.getElementById("enviar-formulario");
const section = document.querySelector(".section");
const sectionNames = document.querySelectorAll(".section__name");

document.body.style.overflowY = "scroll";

section.addEventListener("click",(e)=>{
    if(e.target.classList.contains("section__name")){
        datosPersonales.style.display="none";
        antecedentesNoPatologicos.style.display="none";
        antecedentesPatologicos.style.display="none";
        antecedentesPostnatales.style.display="none";
        historiaDelDesarrollo.style.display="none";
        antecedentesEscolares.style.display="none";
        dinamicaFamiliar.style.display="none";
        aspectoDePersonalidad.style.display="none";
        enviarFormulario.style.display="none";
        for (sectionName of sectionNames) {
            sectionName.classList.remove("section__name--selected");
        }
        if(e.target.classList.contains("section__datos-personales")){
            datosPersonales.style.display="block";
            e.target.classList.add("section__name--selected");
        }
        else if(e.target.classList.contains("section__antecedentes-personales-no-patologicos")){
            antecedentesNoPatologicos.style.display="block";
            e.target.classList.add("section__name--selected");
        }
        else if(e.target.classList.contains("section__antecedentes-personales-patologicos")){
            antecedentesPatologicos.style.display="block";
            e.target.classList.add("section__name--selected");
        }
        else if(e.target.classList.contains("section__antecedentes-pre-peri-y-postnatales")){
            antecedentesPostnatales.style.display="block";
            e.target.classList.add("section__name--selected");
        }
        else if(e.target.classList.contains("section__historia-del-desarrollo")){
            historiaDelDesarrollo.style.display="block";
            e.target.classList.add("section__name--selected");
        }
        else if(e.target.classList.contains("section__antecedentes-escolares")){
            antecedentesEscolares.style.display="block";
            e.target.classList.add("section__name--selected");
        }
        else if(e.target.classList.contains("section__dinamica-familiar")){
            dinamicaFamiliar.style.display="block";
            e.target.classList.add("section__name--selected");
        }
        else if(e.target.classList.contains("section__aspecto-de-personalidad-emocionales")){
            aspectoDePersonalidad.style.display="block";
            e.target.classList.add("section__name--selected");
        }
        else if(e.target.classList.contains("section__enviar-formulario")){
            enviarFormulario.style.display="block";
            e.target.classList.add("section__name--selected");
        }
    }
});