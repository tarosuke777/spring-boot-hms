document.addEventListener("DOMContentLoaded", (event) => {
    const container = document.getElementById("task-container");
    let draggedItem = null;

    // ドラッグ開始
    container.addEventListener("dragstart", (e) => {
        draggedItem = e.target.closest(".col-md-4");
        // ドラッグ中の要素を半透明にするなどの視覚効果
        setTimeout(() => {
            draggedItem.style.opacity = "0.5";
        }, 0);
        e.dataTransfer.setData("text/plain", draggedItem.id);
    });

    // ドラッグ終了
    container.addEventListener("dragend", (e) => {
        if (draggedItem) {
            draggedItem.style.opacity = "";
            draggedItem = null;
        }
    });

    // ドロップオーバー (ドロップを許可するために必要)
    container.addEventListener("dragover", (e) => {
        e.preventDefault();
    });

    // ドロップ
    container.addEventListener("drop", (e) => {
        e.preventDefault();
        const dropTarget = e.target.closest(".col-md-4");
        if (dropTarget && draggedItem) {
            // ドロップされた場所に基づいて要素を挿入
            const boundingBox = dropTarget.getBoundingClientRect();
            const center = boundingBox.left + boundingBox.width / 2;
            if (e.clientX > center) {
                // 右側にドロップ
                container.insertBefore(draggedItem, dropTarget.nextSibling);
            } else {
                // 左側にドロップ
                container.insertBefore(draggedItem, dropTarget);
            }

            // ここでサーバーに新しい順序を送信するロジックを実装
            // 例: const newOrder = Array.from(container.children).map(child => child.id);
            // fetch('/api/update-order', { method: 'POST', body: JSON.stringify(newOrder) });
        }
    });
});
