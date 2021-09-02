// Ventana modal
const modal_container = document.getElementById('modal_container');
const close = document.getElementById('close');

const showModal = function() {
	modal_container.classList.add('show');
	window.addEventListener('click', closeModal);
};

function closeModal(e) {
	if (e.target === modal_container) {
		modal_container.classList.remove('show');
		window.removeEventListener('click', closeModal);
	}
}

close.addEventListener('click', () => {
	modal_container.classList.remove('show');
});

showModal();
