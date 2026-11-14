import { expect, test } from "@playwright/test";

test("企業一覧では企業名の列を備考と選考履歴より広く表示する", async ({
  page,
}) => {
  await page.goto("/login");
  await page.getByPlaceholder("Name").fill("admin");
  await page.getByPlaceholder("Password").fill("password");
  await page.locator('input[type="submit"]').click();
  await expect(page).toHaveURL(/\/top$/);

  await page.goto("/company/list");

  const [companyNameWidth, noteWidth, historyWidth] = await page
    .locator("table thead th")
    .evaluateAll((headers) =>
      headers.map((header) => header.getBoundingClientRect().width)
    );

  expect(companyNameWidth).toBeGreaterThan(noteWidth * 1.5);
  expect(companyNameWidth).toBeGreaterThan(historyWidth * 1.5);
});
