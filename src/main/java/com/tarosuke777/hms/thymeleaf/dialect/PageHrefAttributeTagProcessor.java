package com.tarosuke777.hms.thymeleaf.dialect;


import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;


public class PageHrefAttributeTagProcessor extends AbstractAttributeTagProcessor {

  private static final String ATTRIBUTE_NAME = "page-href";

  public PageHrefAttributeTagProcessor(String dialectPrefix, int precedence) {
    super(TemplateMode.HTML, dialectPrefix, null, false, ATTRIBUTE_NAME, true, precedence, true);
  }

  @Override
  protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
      AttributeName attributeName, String attributeValue,
      IElementTagStructureHandler structureHandler) {

    // 属性値（式や数値文字列）を評価してページ番号を取得
    int pageNumber = 0; // デフォルト値

    try {
      // 式を評価
      Object result = StandardExpressions.getExpressionParser(context.getConfiguration())
          .parseExpression(context, attributeValue).execute(context);

      if (result instanceof Number number) {
        pageNumber = number.intValue();
      } else if (result != null) {
        pageNumber = Integer.parseInt(result.toString());
      }
    } catch (Exception e) {
      // null評価やパースエラー時はデフォルト(0)にフォールバック
      pageNumber = 0;
    }

    // 現在のURLをベースに page パラメータを差し替えたURLを生成
    String targetUrl = ServletUriComponentsBuilder.fromCurrentRequest()
        .replaceQueryParam("page", pageNumber).build().encode().toUriString();

    // HTMLタグに href 属性を書き込み/更新
    structureHandler.setAttribute("href", targetUrl);

  }

}
