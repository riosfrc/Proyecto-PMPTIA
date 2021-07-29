const slider = document.getElementById("slider");
let sliderSection = document.querySelectorAll(".slider__section");
let sliderSectionLast = sliderSection[sliderSection.length -1];
const btnLeft = document.getElementById("btn-left");
const btnRight = document.getElementById("btn-right");

slider.insertAdjacentElement('afterbegin', sliderSectionLast);

function next(){
    let sliderSectionFirst = document.querySelectorAll(".slider__section")[0];
    slider.style.marginLeft = "-200%";
    slider.style.transition = "all 0.9s ease-in";
    setTimeout(function(){
        slider.style.transition = "none";
        slider.insertAdjacentElement('beforeend', sliderSectionFirst);
        slider.style.marginLeft = "-100%";
    }, 900);
}

function previous(){
    let sliderSection = document.querySelectorAll(".slider__section");
    let sliderSectionLast = sliderSection[sliderSection.length -1];
    slider.style.marginLeft = "0";
    slider.style.transition = "all 0.9s ease-in";
    setTimeout(function(){
        slider.style.transition = "none";
        slider.insertAdjacentElement('afterbegin', sliderSectionLast);
        slider.style.marginLeft = "-100%";
    }, 900);
}

btnRight.addEventListener('click', function(){
    next();
    clearInterval(timer);
    timer = setInterval(function(){
        next();
    }, delay);
});

btnLeft.addEventListener('click', function(){
    previous();
    clearInterval(timer);
    timer = setInterval(function(){
        next();
    }, delay);
});

let delay = 6000;
let timer = setInterval(function(){
    next();
}, delay);