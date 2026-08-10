import {
  getCsrfHeaders,
  showStatusMessage,
  isWebAuthnSupported,
} from "./utils.js";

export function initPasskeyRegister() {
  const regBtn = document.getElementById("passkey-register-btn");
  if (!regBtn) return; // 画面上にボタンがなければ何もしない

  regBtn.addEventListener("click", async (e) => {
    const btn = e.currentTarget;
    const { optionsUrl, registerUrl } = btn.dataset;
    const statusMsg = document.getElementById("passkey-status-msg");

    if (statusMsg) statusMsg.style.display = "none";

    if (!isWebAuthnSupported()) {
      showStatusMessage(
        statusMsg,
        "お使いのブラウザはパスキー登録に対応していません。",
        "danger"
      );
      return;
    }

    const headers = { "Content-Type": "application/json", ...getCsrfHeaders() };

    try {
      // 1. 登録用Optionsを取得
      const optionsRes = await fetch(optionsUrl, { method: "POST", headers });
      if (!optionsRes.ok) throw new Error("Options fetch failed");
      const options = await optionsRes.json();

      // 2. クレデンシャル作成
      const credential = await navigator.credentials.create({
        publicKey: PublicKeyCredential.parseCreationOptionsFromJSON(options),
      });
      if (!credential) throw new Error("Credential creation failed");

      // 3. 登録実行
      const regRes = await fetch(registerUrl, {
        method: "POST",
        headers,
        body: JSON.stringify({
          publicKey: { credential: credential.toJSON(), label: "My Key" },
        }),
      });

      if (!regRes.ok) throw new Error("Register save failed");

      // 4. 成功メッセージの表示
      showStatusMessage(
        statusMsg,
        "パスキーの登録が完了しました！次回からパスキーでログインできます。",
        "success"
      );
    } catch (err) {
      console.error("Passkey Register Error:", err);
      if (err.name !== "NotAllowedError") {
        showStatusMessage(
          statusMsg,
          "パスキーの登録に失敗しました。",
          "danger"
        );
      }
    }
  });
}
