const titleSelect = document.querySelector("#game-video-title");
const nameSelect = document.querySelector("#game-video-name");
const errorMessage = document.querySelector("#game-video-filter-error");

if (titleSelect && nameSelect && errorMessage) {
  let latestRequest = 0;

  titleSelect.addEventListener("change", async () => {
    const requestId = ++latestRequest;
    const namesUrl = new URL(
      titleSelect.dataset.namesUrl,
      window.location.href
    );
    if (titleSelect.value) {
      namesUrl.searchParams.set("title", titleSelect.value);
    }

    nameSelect.replaceChildren(new Option("すべての名前", ""));
    nameSelect.disabled = true;
    errorMessage.hidden = true;
    errorMessage.textContent = "";

    try {
      const response = await fetch(namesUrl);
      if (!response.ok) {
        throw new Error(`名前候補の取得に失敗しました (${response.status})`);
      }

      const names = await response.json();
      if (requestId !== latestRequest) {
        return;
      }
      for (const name of names) {
        nameSelect.add(new Option(name, name));
      }
      nameSelect.disabled = false;
    } catch (error) {
      if (requestId !== latestRequest) {
        return;
      }
      console.error(error);
      errorMessage.textContent =
        "名前候補を取得できませんでした。ページを再読み込みしてください。";
      errorMessage.hidden = false;
    }
  });
}
