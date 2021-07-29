const iconFolders = document.querySelectorAll('.far');

iconFolders.forEach(function(iconFolder) {
    iconFolder.addEventListener('mouseover', function() {
        iconFolder.classList.toggle('fa-folder');
        iconFolder.classList.toggle('fa-folder-open');
    });
    iconFolder.addEventListener('mouseout', function() {
        iconFolder.classList.toggle('fa-folder');
        iconFolder.classList.toggle('fa-folder-open');
    });
});