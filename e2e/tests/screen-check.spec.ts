import { expect, test } from "@playwright/test";
import { mkdir } from "node:fs/promises";

test("ログイン後の対象画面を撮影する", async ({ page }) => {
  const userName = "admin";
  const password = "password";
  const targetPagePath = process.env.TARGET_PAGE_PATH ?? "/top";

  if (!userName || !password) {
    throw new Error(
      "E2E_USERNAME と E2E_PASSWORD にログイン情報を指定してください。"
    );
  }

  await page.goto("/login");
  await page.getByPlaceholder("Name").fill(userName);
  await page.getByPlaceholder("Password").fill(password);
  await page.locator('input[type="submit"]').click();
  await expect(page).toHaveURL(/\/top$/);

  await page.goto(targetPagePath);
  await expect(page).toHaveURL(
    new URL(targetPagePath, "http://localhost:8080").toString()
  );
  await mkdir("screenshots", { recursive: true });
  await page.screenshot({
    path: "screenshots/target-page.png",
    fullPage: true,
  });
});
