document
  .getElementById("passkey-login-btn")
  .addEventListener("click", async () => {
    const errorElement = document.getElementById("passkey-error");
    errorElement.style.display = "none";

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
      // 1. チャレンジ（Options）をサーバーから取得
      const optionsRes = await fetch("/hms/webauthn/authenticate/options", {
        method: "POST",
        headers: headers,
      });

      if (!optionsRes.ok) {
        const errorText = await optionsRes.text();
        console.error("HTTP Error:", optionsRes.status, errorText);
        throw new Error("オプションの取得に失敗しました");
      }
      const options = await optionsRes.json();

      // 2. ブラウザの Passkey 認証プロンプトを表示
      const credential = await navigator.credentials.get({
        publicKey: PublicKeyCredential.parseRequestOptionsFromJSON(options),
      });

      if (!credential) {
        throw new Error("クレデンシャルの取得に失敗しました");
      }

      // 3. 認証結果を Spring Security へ送信
      const loginRes = await fetch("/hms/login/webauthn", {
        method: "POST",
        headers: headers,
        body: JSON.stringify(credential.toJSON()),
      });

      if (loginRes.ok) {
        // ログイン成功時に /top へリダイレクト（SecurityConfigで指定した先）
        window.location.href = "/hms/top";
      } else {
        errorElement.innerText = "パスキー認証に失敗しました。";
        errorElement.style.display = "block";
      }
    } catch (err) {
      console.error("Passkey Error:", err);
      // ユーザーがキャンセルした場合などをケア
      if (err.name !== "NotAllowedError") {
        errorElement.innerText = "パスキーの呼び出しに失敗しました。";
        errorElement.style.display = "block";
      }
    }
  });
