# Playwright E2E

## Setup

```sh
cd e2e
npm ci
npx playwright install chromium
cd ..
```

On Linux, if Chromium reports missing system libraries, install its browser
dependencies with `cd e2e && npx playwright install --with-deps chromium`.
This requires permission to install operating-system packages.

## UI改修の画像・動画をPRに添付する

画像・動画はリポジトリに追加せず、一時ディレクトリに作成してGitHubへ
アップロードします。以下のコマンド例は、アプリを起動し、`admin` /
`password` でログインしてTOP画面をフルページ撮影し、ログイン操作をWebMで
録画します。別の改修を撮影する場合は、スクリプトのURL、操作、期待する画面を
変更してください。コマンド例はbash向けです。

1. リポジトリルートからアプリを起動します。ローカルプロファイルとH2 DBを
   使用します。別ターミナルで続くコマンドを実行してください。

   ```sh
   ./gradlew bootRun
   ```

2. リポジトリルートのターミナルでログイン画面の応答を待ちます。成功しない
   場合は撮影に進まず、起動ログを確認してください。

   ```sh
   set -euo pipefail
   ready=false
   for i in $(seq 1 60); do
     if curl --silent --fail http://localhost:8080/login -o /dev/null; then
       ready=true
       break
     fi
     sleep 2
   done
   if [ "$ready" != true ]; then
     echo "Spring Boot did not become ready at http://localhost:8080/login" >&2
     exit 1
   fi
   ```

3. 同じリポジトリルートのターミナルで一時ディレクトリを作成し、画像と動画を
   取得します。動画はブラウザーコンテキストを閉じて保存が完了した後に名前を
   変更します。

   ```sh
   set -euo pipefail
   CAPTURE_DIR="$(mktemp -d)"
   export CAPTURE_DIR
   cd e2e
   node <<'NODE'
   const fs = require("node:fs/promises");
   const path = require("node:path");
   const { chromium } = require("@playwright/test");

   (async () => {
     const outputDir = process.env.CAPTURE_DIR;
     if (!outputDir) throw new Error("CAPTURE_DIR is required");

     const browser = await chromium.launch();
     let context;
     let video;
     try {
       context = await browser.newContext({
         viewport: { width: 1280, height: 900 },
         recordVideo: {
           dir: outputDir,
           size: { width: 1280, height: 900 },
         },
       });
       const page = await context.newPage();
       await page.goto("http://localhost:8080/login");
       await page.getByPlaceholder("Name").fill("admin");
       await page.getByPlaceholder("Password").fill("password");
       await page.locator('input[type="submit"]').click();
       await page.waitForURL("**/top");
       await page.waitForTimeout(1000);
       await page.screenshot({
         path: path.join(outputDir, "top.png"),
         fullPage: true,
       });
       video = page.video();
     } finally {
       if (context) await context.close();
       await browser.close();
     }

     if (!video) throw new Error("Playwright did not create a video");
     await fs.rename(
       await video.path(),
       path.join(outputDir, "login-to-top.webm")
     );
   })().catch((error) => {
     console.error(error);
     process.exitCode = 1;
   });
   NODE
   cd ..
   test -s "$CAPTURE_DIR/top.png"
   test -s "$CAPTURE_DIR/login-to-top.webm"
   ls -lh "$CAPTURE_DIR/top.png" "$CAPTURE_DIR/login-to-top.webm"
   ```

4. `gh` CLIが対象リポジトリに認証済みであることを確認し、ユーザー添付機能に
   画像と動画をアップロードします。リポジトリ名とPR URLを対象に置き換えます。
   アップロードURLが返らない場合は、PR更新やファイル削除に進まないでください。

   ```sh
   set -euo pipefail
   gh auth status
   REPO='OWNER/REPO'
   PR_URL='https://github.com/OWNER/REPO/pull/123'
   REPO_ID="$(gh api "repos/$REPO" --jq .id)"
   case "$REPO_ID" in
     ''|*[!0-9]*) echo "Invalid repository ID for $REPO" >&2; exit 1 ;;
   esac
   TOKEN="$(gh auth token)"

   upload_attachment() {
     file="$1"
     name="$(basename -- "$file")"
     mime="$2"
     curl --fail-with-body --silent --show-error -X POST \
       'https://uploads.github.com/user-attachments/assets' \
       --url-query "name=$name" \
       --url-query "content_type=$mime" \
       --url-query "repository_id=$REPO_ID" \
       -H 'Content-Type: application/octet-stream' \
       -H 'X-GitHub-Api-Version: 2022-11-28' \
       -H "Authorization: Bearer $TOKEN" \
       --data-binary "@$file" |
       node -e '
         let body = "";
         process.stdin.on("data", (chunk) => body += chunk);
         process.stdin.on("end", () => {
           try {
             const url = JSON.parse(body).url;
             if (typeof url !== "string" || !url.startsWith("https://")) {
               throw new Error("GitHub did not return an attachment URL");
             }
             process.stdout.write(url);
           } catch (error) {
             console.error(error.message);
             process.exitCode = 1;
           }
         });
       '
   }

   IMAGE_URL="$(upload_attachment "$CAPTURE_DIR/top.png" image/png)"
   VIDEO_URL="$(upload_attachment "$CAPTURE_DIR/login-to-top.webm" video/webm)"
   printf 'Image: %s\nVideo: %s\n' "$IMAGE_URL" "$VIDEO_URL"
   ```

   GitHub Enterprise Serverでは、インスタンスに対応する添付アップロードURLを
   使用してください。この例のURLはGitHub.com用です。

5. 既存のPR本文を保ったまま、画像はMarkdown画像として、動画はURL単独の行として
   追加します。更新後にPR本文を再取得し、両方の添付URLを確認します。

   ```sh
   set -euo pipefail
   BODY_FILE="$(mktemp)"
   gh pr view "$PR_URL" --repo "$REPO" --json body --jq .body > "$BODY_FILE"
   cat >> "$BODY_FILE" <<EOF

   ### UI改修のスクリーンショット

   ![ログイン後のTOP画面]($IMAGE_URL)

   ### UI操作動画

   $VIDEO_URL
   EOF
   gh pr edit "$PR_URL" --repo "$REPO" --body-file "$BODY_FILE"
   gh pr view "$PR_URL" --repo "$REPO" --json body --jq .body > "$BODY_FILE"
   grep -F "$IMAGE_URL" "$BODY_FILE"
   grep -F "$VIDEO_URL" "$BODY_FILE"
   rm -- "$BODY_FILE"
   ```

6. PR本文をブラウザーで開き、画像が表示され、動画を再生できることを確認して
   から一時ディレクトリを削除します。表示・再生またはURL確認ができない場合は
   削除せず、原因を調査してください。アップロードとPR更新に成功したメディアは
   GitHub上に保存され、ローカルには残しません。

   ```sh
   rm -- "$CAPTURE_DIR/top.png" "$CAPTURE_DIR/login-to-top.webm"
   rmdir -- "$CAPTURE_DIR"
   unset CAPTURE_DIR TOKEN IMAGE_URL VIDEO_URL REPO_ID
   ```

撮影後はアプリケーションを起動したターミナルで `Ctrl+C` を押して停止します。

## Existing E2E tests

Start the Spring Boot application on `http://localhost:8080`, then run the
sample test. The test uses the embedded `admin` / `password` credentials:

```sh
cd e2e && npm test
```

Set `TARGET_PAGE_PATH` to capture a different page. The default is `/top`.
The screenshot is written to `e2e/screenshots/target-page.png`.

To verify test discovery without starting the application:

```sh
cd e2e && npm test -- --list
```

The TOP page visual regression test compares the page with the committed
baseline image:

```sh
cd e2e && npm test -- top.spec.ts --grep "TOP画面のスナップショットが一致する"
```

When the TOP page intentionally changes, update the baseline with:

```sh
cd e2e && npm test -- top.spec.ts --grep "TOP画面のスナップショットが一致する" --update-snapshots
```
