const notificationContainer = document.getElementById('notificationContainer');
const notificationAnchor = document.getElementById('notificationContainerAnchor');

notificationAnchor.addEventListener('click', function (event) {
    event.stopPropagation();
    notificationContainer.classList.toggle('hide');
});

document.addEventListener('click', function (event) {
    if (!notificationContainer.classList.contains('hide') && !notificationContainer.contains(event.target) && event.target !== notificationAnchor) {
        notificationContainer.classList.add('hide');
    }
});