/**
 * metaタグからCSRFトークンを取得してヘッダーオブジェクトを返す
 */
export function getCsrfHeaders() {
  const token = document
    .querySelector("meta[name='_csrf']")
    ?.getAttribute("content");
  const header = document
    .querySelector("meta[name='_csrf_header']")
    ?.getAttribute("content");
  return token && header ? { [header]: token } : {};
}

/**
 * ステータスメッセージを表示する
 */
export function showStatusMessage(element, message, type = "danger") {
  if (!element) return;
  element.className = `alert alert-${type} py-2`;
  element.innerText = message;
  element.style.display = "block";
}

/**
 * WebAuthn (パスキー) のブラウザサポートチェック
 */
export function isWebAuthnSupported() {
  return !!window.PublicKeyCredential;
}
