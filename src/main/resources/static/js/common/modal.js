// テーブル全体に対して1つだけリスナーを登録する手法
document.addEventListener("DOMContentLoaded", () => {
    const table = document.querySelector('table'); // テーブル要素
    table.addEventListener('click', (event) => {
        // クリックされた要素が .modal-trigger か確認
        const target = event.target.closest('.modal-trigger');
        if (target) {
            const modalText = target.getAttribute('data-modal-text');
            document.getElementById('globalModalBody').textContent = modalText;
        }
    });
});