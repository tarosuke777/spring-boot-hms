// document全体に対して1つだけリスナーを登録する（安全・汎用的）
document.addEventListener('click', (event) => {

    const target = event.target.closest('.modal-trigger');
    if (!target) return; // なければ何もしない

    const modalText = target.getAttribute('data-modal-text') || '';
    const modalBody = document.getElementById('globalModalBody');

    if (modalBody) {
        modalBody.textContent = modalText;
    }
});