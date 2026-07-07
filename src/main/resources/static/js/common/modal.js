// document全体に対して1つだけリスナーを登録する（安全・汎用的）
document.addEventListener('click', (event) => {

    const target = event.target.closest('.modal-trigger');
    if (!target) return; // なければ何もしない

    const modalBody = target.getAttribute('data-modal-text');
    const modalBodyElm = document.getElementById('globalModalBody');
    if (modalBodyElm && modalBody) {
        modalBodyElm.textContent = modalBody;
    }

    const modalTitle = target.getAttribute('data-modal-title');
    const modalTitleElm = document.getElementById('globalModalTitle');
    if (modalTitleElm && modalTitle) {
        modalTitleElm.textContent = modalTitle;
    }
});