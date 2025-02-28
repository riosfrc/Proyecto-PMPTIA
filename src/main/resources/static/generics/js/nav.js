const btnMenu = document.querySelector("#btnMenu");
const menu = document.querySelector("#menu");

btnMenu.addEventListener("click",function(){
    menu.classList.toggle("mostrar-menu")
});

const subMenuBtn = document.querySelectorAll(".btn-submenu");
for (let i = 0; i < subMenuBtn.length; i++) {
    subMenuBtn[i].addEventListener("click",function(e){
        if(window.innerWidth < 768){
			e.preventDefault();
            const subMenu = this.nextElementSibling;
            const height = subMenu.scrollHeight;
            if(subMenu.classList.contains("desplegar")){
                subMenu.classList.remove("desplegar");
                subMenu.removeAttribute("style");
            } else{
                subMenu.classList.add("desplegar");
                subMenu.style.height = height + "px";
            }
        }
    });
}