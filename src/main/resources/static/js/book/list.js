// テーブル全体に対して1つだけリスナーを登録する手法
document.addEventListener("DOMContentLoaded", () => {
    const table = document.querySelector('table'); // テーブル要素
    table.addEventListener('click', (event) => {
        // クリックされた要素が .note-trigger か確認
        const target = event.target.closest('.note-trigger');
        if (target) {
            const noteText = target.getAttribute('data-note');
            document.getElementById('modalNoteBody').textContent = noteText;
        }
    });
});