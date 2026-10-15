## 🔄 開発ワークフロー（ワークツリー対応版）

タスクの指示を受けたら、コードを書き始める前に必ず以下のステップを順に実行してください。

### Step 1: 事前準備（Worktree作成 & ブランチ作成 & 固有TODOファイルの作成）
作業を開始する前に、元のリポジトリ（ベースリポジトリ）の `main` ブランチを起点として、**専用の作業フォルダ（Worktree）と機能ブランチを同時に作成**します。これにより、ユーザーの既存の作業環境を破壊することなく、完全に並行してタスクを進めることができます。

1. **git worktree による作業フォルダとブランチの同時作成:**
   ベースリポジトリ側で `main` ブランチを最新状態（`git pull` 等）に更新した上で、以下のコマンドを実行し、別フォルダにタスク用の機能ブランチ（`feature/チケット名-概要` など）を展開すること。
   ```bash
   # mainブランチを最新化
   git checkout main && git pull
   
   # 別フォルダを作成し、同時に新ブランチを切り替えて展開
   # (例: ../project-チケット名)
   git worktree add \
     ../project-チケット名 \
     -b feature/チケット名-概要
   ```
   *以降の Step 2〜3 の作業は、すべてこの生成された新しい作業フォルダ内で行うこと。*

2. **固有のTODOファイルの作成:** 新しく作成されたプロジェクトルート（生成先フォルダ）に、作業ごとに固有のファイル名（例: `TODO_チケット名.md`）でタスク管理ファイルを作成すること。
   * 各タスクは中断・再開ができるよう、必ずチェックボックス `- [ ]` 形式で細分化して記述すること。
   * **タスクには必ず「UT（単体テスト）の作成・実行」を含めること。**
   * 進捗に合わせて、リアルタイムにチェック `- [x]` を入れること。

### Step 2: 【マルチエージェント・サイクル】の実行
実装フェーズでは、以下の「ジュニア」と「シニア」のサイクルを、シニアから「LGTM」が出るまで**エージェント自身で自律的にループ**させてください。

#### 👶 ロール1：ジュニアエンジニア（実装 ＆ UT作成担当）
* 作成したTODOファイルのタスクに基づき、愚直かつ迅速に機能コードを実装または修正する。
* **必ず対象コードに対するUT（単体テスト）を同時に作成し、テストがすべてパスすることを確認すること。**
* 実装とテスト実行が完了したら、思考プロセスまたはログに出力：
  > **[ジュニア]** タスクxxの実装およびUT作成、テストパスを確認しました。シニアのコードレビューを要求します。

#### 👨‍🏫 ロール2：シニアエンジニア（超厳格レビュー担当）
* ジュニアが書いたコードおよび**作成されたUTの内容**に対し、以下の観点で**妥協なく厳しいレビュー**を行う。
  * 可読性、保守性、バグの可能性、エッジケースの考慮
  * **UTのテストケースが十分か、異常系や境界値が網羅されているか**
  * プロジェクトの既存アーキテクチャやコード規約との整合性
* **修正が必要な場合:** 具体的な問題点（コードやテストの不備）と改善案を指摘し、ジュニア（ロール1）に修正を差し戻す。
  > **[シニア]** レビュー結果：〇〇の考慮が漏れています（またはテストケースが不足しています）。〜〜のように修正してください。
* **問題がない場合（LGTM）:**
  承認を出してサイクルを終了し、次のTODOに進む。
  > **[シニア] LGTM.** この実装およびテスト内容で問題ありません。次のタスクへ進んでください。

### Step 3: プルリクエスト（PR）の作成 & TODOファイルの削除 & Worktreeのクリーンアップ
すべてのTODOファイルのタスク（実装とUT）が完了し、シニアの最終LGTMが得られたら、以下のクリーンアップと送信処理を行ってください。

1. **TODOファイルの削除:** 今回の作業用に作成したTODOファイル（`TODO_チケット名.md`）を必ず削除すること（コミットに含めない、または削除コミットを行う）。
2. **PRの作成:** 変更をリモートリポジトリにプッシュし、マージ先（`main` ブランチ）に対する**プルリクエスト（PR）を自動で作成（または下書き作成）**し、ユーザーに報告すること。
3. **Worktreeの削除（環境のクリーンアップ）:** PR作成完了後、ベースリポジトリ側（または適切なディレクトリ）から以下のコマンドを実行し、不要になった作業フォルダをクリーンアップすること。
   ```bash
   git worktree remove ../project-チケット名
   ```

### Step 4: 中断時の状態保存
ユーザーから中断の指示が入った場合、またはセッションが切れる場合は、必ずTODOファイルを最新状態に更新し、現在の「Step」と「どのロールのフェーズか」を明記して終了すること。

### Step 5: UI改修時の画像・動画取得
画面表示やユーザー操作に影響する改修を行った場合は、実装とテストの確認に加えて、改修箇所が分かる画像と操作動画をPlaywrightで取得すること。

1. 次の準備コマンドはリポジトリルートで実行する。Playwrightは `e2e/` のロックファイルに従ってインストールし、Chromiumも用意する。

   ```sh
   cd e2e
   npm ci
   npx playwright install chromium
   cd ..
   ```

   Linux上でPlaywrightのブラウザー依存ライブラリが不足している場合は、`cd e2e && npx playwright install --with-deps chromium` を実行する（OSパッケージのインストール権限が必要）。

2. 別のターミナルでリポジトリルートからアプリケーションを起動し、ログイン画面が応答するまで待つ。ローカルプロファイルとH2データベースは既定で使用される。

   ```sh
   ./gradlew bootRun
   ```

   ```sh
   for i in $(seq 1 60); do
     if curl --silent --fail http://localhost:8080/login -o /dev/null; then
       break
     fi
     sleep 2
   done
   curl --fail --silent --show-error http://localhost:8080/login -o /dev/null
   ```

   撮影後はアプリケーションを起動したターミナルで `Ctrl+C` を押して停止する。

3. リポジトリルートのターミナルで一時ディレクトリを作り、Playwrightでログインから対象画面までを録画する。以下はこのプロジェクトの `admin` / `password` でログインしてTOPをフルページ撮影する例。別画面や別の操作を撮影する場合は、URL、操作、期待する画面を改修内容に合わせて変更する。

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
   test -s "$CAPTURE_DIR/top.png" && test -s "$CAPTURE_DIR/login-to-top.webm"
   ls -lh "$CAPTURE_DIR/top.png" "$CAPTURE_DIR/login-to-top.webm"
   ```

4. `gh` CLIで対象リポジトリに認証済みであることを確認し、GitHubのユーザー添付機能へ画像と動画をアップロードする。次のコマンドの `REPO` と `PR_URL` は実際の対象に置き換える。認証トークンをコマンド本文やログへ出力しないこと。

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

   `curl` はGitHub.com用のURLを使用している。GitHub Enterprise Serverでは、そのインスタンスのユーザー添付アップロードURLに置き換える。

5. PR本文の既存内容を保持したまま、画像はMarkdown画像、動画はURL単独行で追加する。本文を更新した後、同じPRから本文を取得して両方のURLが含まれていることを確認する。

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

6. PR本文に実際の画像と動画が表示され、両方の添付URLが確認できた後にだけ、一時メディアを削除する。撮影・アップロード・PR更新のいずれかが失敗した場合はエラーを明記し、確認前に成果物を削除しない。

   ```sh
   rm -- "$CAPTURE_DIR/top.png" "$CAPTURE_DIR/login-to-top.webm"
   rmdir -- "$CAPTURE_DIR"
   unset CAPTURE_DIR TOKEN IMAGE_URL VIDEO_URL REPO_ID
   ```
