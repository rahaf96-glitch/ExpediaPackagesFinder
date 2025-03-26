document.getElementById('searchBtn').addEventListener('click', function() {
    let origin = document.getElementById('originCity').value;
    let destination = document.getElementById('destinationCity').value;

    fetch(`https://expediapackagesfinder-4.onrender.com/api/packages?origin=${origin}&destination=${destination}`)
        .then(response => response.json())
        .then(data => {
            let resultDiv = document.getElementById('package-cards');
            resultDiv.innerHTML = '';

            if (Array.isArray(data) && data.length > 0) {
                data.forEach(package => {
                    let imageUrl = package.hotelImageUrl ? package.hotelImageUrl : 'https://via.placeholder.com/400';
                    let price = package.formattedTotalPriceValue ? package.formattedTotalPriceValue : 'N/A';
                    let hotelStarRating = package.hotelStarRating ? package.hotelStarRating : 0;

                    let starRatingHtml = '';
                    for (let i = 0; i < 5; i++) {
                        if (i < hotelStarRating) {
                            starRatingHtml += `<i class="fas fa-star" style="color: #FFD700;"></i>`;
                        } else {
                            starRatingHtml += `<i class="fas fa-star" style="color: #ccc;"></i>`;
                        }
                    }

                    resultDiv.innerHTML += `
                    <div class="col-md-4 mb-4">
                        <div class="card package-card">
                            <img src="${imageUrl}" alt="Package Image">
                            <div class="package-card-body">
                                <h5 class="package-card-title">${package.hotelInfo ? package.hotelInfo.hotelName : 'No hotel info'}</h5>
                                <p class="package-card-text">${package.destination ? package.destination.city : 'No destination info'}</p>
                                <p class="package-card-text">Travel Dates: ${package.offerDateRange ? package.offerDateRange.formattedTravelStartDate : 'N/A'} to ${package.offerDateRange ? package.offerDateRange.formattedTravelEndDate : 'N/A'}</p>
                                <p class="package-card-text">Rating: ${starRatingHtml}</p>
                                <p class="package-card-text">Total Price: ${price}</p>
                                <a href="#" class="cta-button">View Details</a>
                            </div>
                        </div>
                    </div>
                `;
                });
            } else {
                resultDiv.innerHTML = "<p style='text-align: center; font-size: 18px; margin: 0; padding: 20px;'>No packages found for this destination.</p>";

            }

            scrollToResults();
        })
        .catch(error => console.error('Error fetching data:', error));
});

function scrollToResults() {
    document.getElementById('results').scrollIntoView({
        behavior: 'smooth',
        block: 'start'
    });
}


window.onscroll = function() {
    let scrollUpBtn = document.getElementById('scrollUpBtn');
    if (document.body.scrollTop > 100 || document.documentElement.scrollTop > 100) {
        scrollUpBtn.style.display = "block";
    } else {
        scrollUpBtn.style.display = "none";
    }
};

function scrollToTop() {
    window.scrollTo({
        top: 0,
        behavior: "smooth"
    });
}