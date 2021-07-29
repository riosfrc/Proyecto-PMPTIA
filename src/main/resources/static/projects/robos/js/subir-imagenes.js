const checkboxEscenasNormales = document.getElementById('checkbox-escenas-normales');
const checkboxEscenasAntesDeCrimen = document.getElementById('checkbox-escenas-antes-de-crimen');
const checkboxEscenasDuranteElCrimen = document.getElementById('checkbox-escenas-durante-el-crimen');
const checkboxEscenasDespuesDelCrimen = document.getElementById('checkbox-escenas-despues-del-crimen');

const escenasNormales = document.getElementById('escenas-normales');
const escenasAntesDeCrimen = document.getElementById('escenas-antes-de-crimen');
const escenasDuranteElCrimen = document.getElementById('escenas-durante-el-crimen');
const escenasDespuesDelCrimen = document.getElementById('escenas-despues-del-crimen');

const boton = document.querySelector('.btn-form');

checkboxEscenasNormales.addEventListener('change', (e) => {
    if(e.target.checked) {
        escenasNormales.style.display = 'block';
    } else {
        escenasNormales.style.display = 'none';
    }
});

checkboxEscenasAntesDeCrimen.addEventListener('change', (e) => {
    if(e.target.checked) {
        escenasAntesDeCrimen.style.display = 'block'; 
    } else {
        escenasAntesDeCrimen.style.display = 'none';
    }
});

checkboxEscenasDuranteElCrimen.addEventListener('change', (e) => {
    if(e.target.checked) {
        escenasDuranteElCrimen.style.display = 'block'; 
    } else {
        escenasDuranteElCrimen.style.display = 'none';
    }
});
    
checkboxEscenasDespuesDelCrimen.addEventListener('change', (e) => {
    if(e.target.checked) {
        escenasDespuesDelCrimen.style.display = 'block'; 
    } else { 
        escenasDespuesDelCrimen.style.display = 'none'; 
    }
});

document.getElementById('checkbox').addEventListener('change', () => {
    if(checkboxEscenasNormales.checked || checkboxEscenasAntesDeCrimen.checked || checkboxEscenasDuranteElCrimen.checked || checkboxEscenasDespuesDelCrimen.checked){
        boton.style.display = 'flex';
    } else {
        boton.style.display = 'none';
    }
});