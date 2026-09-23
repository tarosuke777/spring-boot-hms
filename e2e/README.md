# Playwright E2E

## Setup

```sh
npm install
npx playwright install chromium
```

Start the Spring Boot application on `http://localhost:8080`, then run the
sample test. The test uses the embedded `admin` / `password` credentials:

```sh
npm test
```

Set `TARGET_PAGE_PATH` to capture a different page. The default is `/top`.
The screenshot is written to `screenshots/target-page.png`.

To verify test discovery without starting the application:

```sh
npm test -- --list
```

The TOP page visual regression test compares the page with the committed
baseline image:

```sh
npm test -- --grep "TOP画面のスナップショットが一致する"
```

When the TOP page intentionally changes, update the baseline with:

```sh
npm test -- --grep "TOP画面のスナップショットが一致する" --update-snapshots
```
