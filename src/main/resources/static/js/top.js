document
  .getElementById("passkey-register-btn")
  .addEventListener("click", async (e) => {
    const btn = e.currentTarget;
    const optionsUrl = btn.dataset.optionsUrl;
    const registerUrl = btn.dataset.registerUrl;

    const statusMsg = document.getElementById("passkey-status-msg");
    statusMsg.style.display = "none";

    // CSRFトークンの取得
    const token = document
      .querySelector("meta[name='_csrf']")
      ?.getAttribute("content");
    const header = document
      .querySelector("meta[name='_csrf_header']")
      ?.getAttribute("content");

    const headers = {
      "Content-Type": "application/json",
    };
    if (token && header) {
      headers[header] = token;
    }

    try {
      // 1. チャレンジ（登録用Options）をサーバーから取得
      const optionsRes = await fetch(optionsUrl, {
        method: "POST",
        headers: headers,
      });

      if (!optionsRes.ok) {
        throw new Error("登録用オプションの取得に失敗しました");
      }
      const options = await optionsRes.json();

      // 2. ブラウザで TouchID / FaceID 等の生成プロンプトを起動
      const credential = await navigator.credentials.create({
        publicKey: PublicKeyCredential.parseCreationOptionsFromJSON(options),
      });

      if (!credential) {
        throw new Error("クレデンシャルの生成に失敗しました");
      }

      // 3. 公開鍵情報をサーバーへ送信して保存（WebAuthnService.save が呼ばれます）
      const regRes = await fetch(registerUrl, {
        method: "POST",
        headers: headers,
        body: JSON.stringify({
          publicKey: {
            credential: credential.toJSON(),
            label: "My Key", // 任意の識別用ラベル（省略可能）
          },
        }),
      });

      if (regRes.ok) {
        statusMsg.className = "alert alert-success py-2";
        statusMsg.innerText =
          "パスキーの登録が完了しました！次回からパスキーでログインできます。";
        statusMsg.style.display = "block";
      } else {
        throw new Error("パスキーの保存に失敗しました");
      }
    } catch (err) {
      console.error("Passkey Register Error:", err);
      if (err.name !== "NotAllowedError") {
        statusMsg.className = "alert alert-danger py-2";
        statusMsg.innerText = "パスキーの登録に失敗しました。";
        statusMsg.style.display = "block";
      }
    }
  });
