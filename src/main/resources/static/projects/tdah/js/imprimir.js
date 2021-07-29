document.addEventListener("DOMContentLoaded", () =>{

    const $boton = document.querySelector("#btn-Crear-PDF");

    $boton.addEventListener("click", () => {

        
        var dp = document.getElementById("datos-personales");
        var apnp = document.getElementById("antecedentes-personales-no-patologicos");
        var app = document.getElementById("antecedentes-personales-patologicos");
        var appyp = document.getElementById("antecedentes-pre-peri-y-postnatales");
        var hdd = document.getElementById("historia-del-desarrollo");
        var ae = document.getElementById("antecedentes-escolares");
        var df = document.getElementById("dinamica-familiar");
        var adp = document.getElementById("aspecto-de-personalidad-emocionales");


                html2pdf(dp,{
                    margin: 0.25,
                    filename: 'formulario.pdf',
                    image: {
                        type: 'jpg',
                        quality: 0.98
                    },
                    html2canvas: {
                        scale: 2,
                        letterRendering: true,
                    },
                    jsPDF: {
                        unit: "in",
                        format: "a2",
                        ortientation: 'landscape'
                    }
                })
                .save()
                .catch(err => console.log(err))
                .finally()
                .then(() => {
                    console.log("Guardado")
            })

    });
});