const selectButton = document.querySelector("#selectGameVideoButton");
const targetType = document.querySelector("#targetType");
const targetId = document.querySelector("#gameVideoTargetId");
const targetLabel = document.querySelector("#gameVideoTargetLabel");
const popupError = document.querySelector("#gameVideoPopupError");

if (selectButton && targetType && targetId && targetLabel && popupError) {
  let selectionWindow = null;

  selectButton.addEventListener("click", () => {
    if (selectionWindow && !selectionWindow.closed) {
      selectionWindow.focus();
      return;
    }

    selectionWindow = window.open(
      "/gameVideo/list?select=true",
      "gameVideoSelector",
      "width=1280,height=900,scrollbars=yes,resizable=yes"
    );
    if (!selectionWindow) {
      popupError.textContent =
        "選択画面を開けませんでした。ブラウザーのポップアップ設定を確認してください。";
      popupError.hidden = false;
      return;
    }
    popupError.hidden = true;
  });

  window.addEventListener("message", (event) => {
    if (
      event.origin !== window.location.origin ||
      event.source !== selectionWindow ||
      targetType.value !== "GAME_VIDEO"
    ) {
      return;
    }

    const selection = event.data;
    const id = Number(selection?.id);
    if (
      selection?.type !== "game-video-selected" ||
      !Number.isSafeInteger(id) ||
      id < 1 ||
      typeof selection.label !== "string" ||
      !selection.label
    ) {
      popupError.textContent =
        "ゲーム動画を受け取れませんでした。選択画面からもう一度選択してください。";
      popupError.hidden = false;
      return;
    }

    targetId.value = String(id);
    targetLabel.value = selection.label;
    popupError.hidden = true;
    selectionWindow = null;
  });
}
