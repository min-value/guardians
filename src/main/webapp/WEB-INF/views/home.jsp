<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>신한 가디언즈</title>
    <link rel="stylesheet" href="/assets/css/home/home.css">
</head>
<body>
    <%@ include file="/WEB-INF/views/include/header.jsp" %>
    <div class="banner-container">
        <div class="banner-slide fade">
            <img src="/assets/img/home/homeImg1.png" alt="배너 1">
        </div>
        <div class="banner-slide fade">
            <img src="/assets/img/home/homeImg2.png" alt="배너 2">
        </div>
        <div class="banner-slide fade">
            <img src="/assets/img/home/homeImg3.png" alt="배너 3">
        </div>
    </div>
    <div class="trophy-container">
        <img src="/assets/img/home/trophy1.png" alt="트로피 1">
        <img src="/assets/img/home/trophy2.png" alt="트로피 2">
        <img src="/assets/img/home/trophy3.png" alt="트로피 3">
    </div>

    <script>
        let currentSlide = 0;
        const slides = document.querySelectorAll(".banner-slide");

        function showSlide(index) {
            slides.forEach((slide, i) => {
                slide.classList.remove("active");
                if (i === index) {
                    slide.classList.add("active");
                }
            });
        }

        function nextSlide() {
            currentSlide = (currentSlide + 1) % slides.length;
            showSlide(currentSlide);
        }

        showSlide(currentSlide);
        setInterval(nextSlide, 3000);
    </script>
</body>
</html>
