import { initPasskeyLogin } from "./login.js";
import { initPasskeyRegister } from "./register.js";

// DOMContentLoaded または Vite/Webpackのビルド後出力ファイル指定に合わせて初期化
document.addEventListener("DOMContentLoaded", () => {
  initPasskeyLogin();
  initPasskeyRegister();
});
