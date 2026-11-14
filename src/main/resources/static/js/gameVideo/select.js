const selectionError = document.querySelector("#game-video-selection-error");

document.addEventListener("click", (event) => {
  if (!(event.target instanceof Element)) {
    return;
  }

  const button = event.target.closest("[data-game-video-id]");
  if (!(button instanceof HTMLButtonElement)) {
    return;
  }

  if (!window.opener || window.opener.closed) {
    selectionError.textContent =
      "登録画面が見つかりません。登録画面から選択画面を開き直してください。";
    selectionError.hidden = false;
    return;
  }

  const id = Number(button.dataset.gameVideoId);
  const title = button.dataset.gameVideoTitle;
  const name = button.dataset.gameVideoName;
  const registeredAt = button.dataset.gameVideoRegisteredAt;
  if (!Number.isSafeInteger(id) || id < 1 || !title || !name || !registeredAt) {
    selectionError.textContent =
      "ゲーム動画を選択できませんでした。ページを再読み込みしてください。";
    selectionError.hidden = false;
    return;
  }

  window.opener.postMessage(
    {
      type: "game-video-selected",
      id,
      label: `${title} / ${name} / ${registeredAt}`,
    },
    window.location.origin
  );
  window.close();
});
