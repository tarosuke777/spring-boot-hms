import {
  getCsrfHeaders,
  showStatusMessage,
  isWebAuthnSupported,
} from "./utils.js";

export function initPasskeyLogin() {
  const loginBtn = document.getElementById("passkey-login-btn");
  if (!loginBtn) return; // 画面上にボタンがなければ何もしない

  loginBtn.addEventListener("click", async (e) => {
    const btn = e.currentTarget;
    const { optionsUrl, loginUrl, redirectUrl } = btn.dataset;
    const errorElement = document.getElementById("passkey-error");

    if (errorElement) errorElement.style.display = "none";

    if (!isWebAuthnSupported()) {
      showStatusMessage(
        errorElement,
        "お使いのブラウザはパスキー認証に対応していません。"
      );
      return;
    }

    const headers = { "Content-Type": "application/json", ...getCsrfHeaders() };

    try {
      // 1. チャレンジを取得
      const optionsRes = await fetch(optionsUrl, { method: "POST", headers });
      if (!optionsRes.ok) throw new Error("Options fetch failed");
      const options = await optionsRes.json();

      // 2. 認証プロンプト呼び出し
      const credential = await navigator.credentials.get({
        publicKey: PublicKeyCredential.parseRequestOptionsFromJSON(options),
      });
      if (!credential) throw new Error("Credential creation failed");

      // 3. 認証実行
      const loginRes = await fetch(loginUrl, {
        method: "POST",
        headers,
        body: JSON.stringify(credential.toJSON()),
      });

      if (!loginRes.ok) {
        showStatusMessage(errorElement, "パスキー認証に失敗しました。");
        return;
      }

      // 4. リダイレクト
      window.location.href = redirectUrl;
    } catch (err) {
      console.error("Passkey Login Error:", err);
      if (err.name !== "NotAllowedError") {
        showStatusMessage(errorElement, "パスキーの呼び出しに失敗しました。");
      }
    }
  });
}
