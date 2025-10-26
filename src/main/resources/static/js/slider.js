// Image Slider for Homepage
let slideIndex = 0;
let slideTimer;

// Auto start slider when page loads
document.addEventListener('DOMContentLoaded', function() {
    showSlides();
    startAutoSlide();
});

function startAutoSlide() {
    slideTimer = setInterval(function() {
        changeSlide(1);
    }, 5000); // Change slide every 5 seconds
}

function stopAutoSlide() {
    clearInterval(slideTimer);
}

function changeSlide(n) {
    stopAutoSlide();
    slideIndex += n;
    showSlides();
    startAutoSlide();
}

function currentSlide(n) {
    stopAutoSlide();
    slideIndex = n - 1;
    showSlides();
    startAutoSlide();
}

function showSlides() {
    let slides = document.getElementsByClassName("hero-slide");
    let dots = document.getElementsByClassName("indicator");
    
    if (slideIndex >= slides.length) {
        slideIndex = 0;
    }
    if (slideIndex < 0) {
        slideIndex = slides.length - 1;
    }
    
    // Hide all slides
    for (let i = 0; i < slides.length; i++) {
        slides[i].classList.remove("active");
    }
    
    // Remove active from all dots
    for (let i = 0; i < dots.length; i++) {
        dots[i].classList.remove("active");
    }
    
    // Show current slide
    if (slides[slideIndex]) {
        slides[slideIndex].classList.add("active");
    }
    
    // Activate current dot
    if (dots[slideIndex]) {
        dots[slideIndex].classList.add("active");
    }
}

// Pause slider on hover
document.addEventListener('DOMContentLoaded', function() {
    const slider = document.querySelector('.hero-slider');
    if (slider) {
        slider.addEventListener('mouseenter', stopAutoSlide);
        slider.addEventListener('mouseleave', startAutoSlide);
    }
});
