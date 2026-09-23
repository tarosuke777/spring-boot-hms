import { expect, test, type Page } from "@playwright/test";
import { mkdir } from "node:fs/promises";

const loginAsAdmin = async (page: Page) => {
  await page.goto("/login");
  await page.getByPlaceholder("Name").fill("admin");
  await page.getByPlaceholder("Password").fill("password");
  await page.locator('input[type="submit"]').click();
  await expect(page).toHaveURL(/\/top$/);
};

test("ログイン後の対象画面を撮影する", async ({ page }) => {
  const targetPagePath = process.env.TARGET_PAGE_PATH ?? "/top";

  await loginAsAdmin(page);

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

test("TOP画面のスナップショットが一致する", async ({ page }) => {
  await loginAsAdmin(page);

  await expect(page).toHaveScreenshot("top-page.png", {
    fullPage: true,
    mask: [
      page.locator("#day-countdown"),
      page.locator("#current-time"),
      page.locator("span.text-muted.fw-bold").filter({
        hasText: /^\d{4}-\d{2}-\d{2}/,
      }),
    ],
  });
});
