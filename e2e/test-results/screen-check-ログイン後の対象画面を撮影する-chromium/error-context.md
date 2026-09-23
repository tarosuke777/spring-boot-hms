# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: screen-check.spec.ts >> ログイン後の対象画面を撮影する
- Location: tests/screen-check.spec.ts:3:5

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:8080/login
Call log:
  - navigating to "http://localhost:8080/login", waiting until "load"

```

# Test source

```ts
  1  | import { test } from "@playwright/test";
  2  | 
  3  | test("ログイン後の対象画面を撮影する", async ({ page }) => {
  4  |   const userName = process.env.E2E_USERNAME;
  5  |   const password = process.env.E2E_PASSWORD;
  6  |   const targetPagePath = process.env.TARGET_PAGE_PATH ?? "/top";
  7  | 
  8  |   if (!userName || !password) {
  9  |     throw new Error(
  10 |       "E2E_USERNAME と E2E_PASSWORD にログイン情報を指定してください。"
  11 |     );
  12 |   }
  13 | 
> 14 |   await page.goto("/login");
     |              ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:8080/login
  15 |   await page.getByPlaceholder("Name").fill(userName);
  16 |   await page.getByPlaceholder("Password").fill(password);
  17 |   await page.locator('input[type="submit"]').click();
  18 | 
  19 |   await page.goto(targetPagePath);
  20 |   await page.screenshot({
  21 |     path: "screenshots/target-page.png",
  22 |     fullPage: true,
  23 |   });
  24 | });
  25 | 
```