$(document).ready(function () {
    let slides = $(".slide-img")
    let currentSlideIndex = 0
    let loadingContent = $(".loading-content")
    let loadingContentW = screen.width
    slides.eq(currentSlideIndex).addClass("current-slide")

    $(".prev-button").click(function () {
        if (slides.length > 0) {
            slides.eq(currentSlideIndex).removeClass("current-slide")
            currentSlideIndex = (currentSlideIndex - 1 + slides.length) % slides.length;
            slides.eq(currentSlideIndex).addClass("current-slide")
            goLeft()
        }
    })

    $(".next-button").click(function () {
        if (slides.length > 0) {
            slides.eq(currentSlideIndex).removeClass("current-slide")
            currentSlideIndex = (currentSlideIndex + 1 + slides.length) % slides.length;
            slides.eq(currentSlideIndex).addClass("current-slide")
            goRight()
        }
    })

    function nextSlide() {
        if (slides.length > 0) {
            slides.eq(currentSlideIndex).removeClass("current-slide")
            currentSlideIndex = (currentSlideIndex + 1 + slides.length) % slides.length;
            slides.eq(currentSlideIndex).addClass("current-slide")
        }
    }

    setInterval(nextSlide, 5000);
})