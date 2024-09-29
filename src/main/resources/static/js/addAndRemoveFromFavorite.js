document.addEventListener('DOMContentLoaded', function() {

    function attachEventListeners() {
        const forms = document.querySelectorAll("form[id^='addToFavoriteForm'], form[id^='removeFromFavoriteForm']");

        forms.forEach(function(form) {
            // Check if the event listener is already attached
            if (!form.hasAttribute('data-listener-attached')) {
                // Attach new event listener
                form.addEventListener('submit', handleFormSubmit);
                // Mark the form as having the event listener attached
                form.setAttribute('data-listener-attached', 'true');
            }
        });
    }

    function handleFormSubmit(event) {
        event.preventDefault();
        let formData = new FormData(this);
        fetch(this.action, {
            method: 'POST',
            body: formData
        }).then(response => {
            if (!response.ok) {
                throw new Error('An error occurred, please try again later.');
            }
            return response.json();
        }).then(data => {
            if (data.success) {
                updateFavoritesMap(data.favoritesMap);
            } else {
                console.error('Error: Response indicates failure', data);
            }
        }).catch(error => {
            console.error('Error:', error);
            alert('An error occurred, please try again later.');
        });
    }

    function updateFavoritesMap(favoritesMap) {
        document.querySelectorAll('[data-recipe-id]').forEach(function(element) {
            const recipeId = element.getAttribute('data-recipe-id');
            const isFavorite = favoritesMap[recipeId];
            const heartIcon = element.querySelector('i');

            if (isFavorite) {
                heartIcon.classList.remove('fa-regular');
                heartIcon.classList.add('fa-solid');
                const addToFavoriteForm = element.querySelector('form[id^="addToFavoriteForm"]');
                if (addToFavoriteForm) {
                    addToFavoriteForm.outerHTML = `
                        <form id="removeFromFavoriteForm" action="/removeFromFavorite" method="post">
                            <input type="hidden" name="recipeId" value="${recipeId}"/>
                            <button type="submit" value="Махни от любими" id="removeFromFavoriteBtn"><i class="fa-solid fa-heart"></i></button>
                        </form>
                    `;
                }
            } else {
                heartIcon.classList.remove('fa-solid');
                heartIcon.classList.add('fa-regular');
                const removeFromFavoriteForm = element.querySelector('form[id^="removeFromFavoriteForm"]');
                if (removeFromFavoriteForm) {
                    removeFromFavoriteForm.outerHTML = `
                        <form id="addToFavoriteForm" action="/addToFavorite" method="post">
                            <input type="hidden" name="recipeId" value="${recipeId}"/>
                            <button type="submit" value="Добави в любими" id="addToFavoriteBtn"><i class="fa-regular fa-heart"></i></button>
                        </form>
                    `;
                }
            }
        });

        // Re-attach event listeners after updating the DOM
        attachEventListeners();
    }

    // Initial attachment of event listeners
    attachEventListeners();
});