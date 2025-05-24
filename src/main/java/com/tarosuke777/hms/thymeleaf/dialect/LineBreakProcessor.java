package com.tarosuke777.hms.thymeleaf.dialect;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.processor.AbstractStandardExpressionAttributeTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * テキスト内の改行文字（\n）をHTMLの&lt;br/&gt;タグに変換するThymeleaf属性プロセッサー。<br>
 * 属性名 {@code hms:linebreak} で使用されます。
 */
public class LineBreakProcessor extends AbstractStandardExpressionAttributeTagProcessor {

    private static final String ATTRIBUTE_NAME = "linebreak";

    public LineBreakProcessor(String dialectPrefix, int precedence) {
        super(TemplateMode.HTML, dialectPrefix, ATTRIBUTE_NAME, precedence, true);
    }

    /**
     * 属性が適用された要素を処理します。 属性値の評価結果を取得し、テキスト内の改行文字（\n）を&lt;br/&gt;タグに変換し、
     * キャリッジリターン（\r）を削除した上で、要素のボディとして設定します。
     *
     * @param context テンプレートコンテキスト
     * @param tag 処理対象のタグ
     * @param attributeName 処理対象の属性名
     * @param attributeValue 処理対象の属性値
     * @param expressionResult 属性値の評価結果
     * @param structureHandler 要素の構造ハンドラー
     */
    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
            AttributeName attributeName, String attributeValue, Object expressionResult,
            IElementTagStructureHandler structureHandler) {

        if (expressionResult == null) {
            return;
        }

        final String rawText = expressionResult.toString();
        String[] lines = rawText.split("\\R", -1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            sb.append(org.unbescape.html.HtmlEscape.escapeHtml5(lines[i]));
            if (i < lines.length - 1) {
                sb.append("<br/>"); // Add <br/> tag for line breaks.
            }
        }
        final String processedHtmlContent = sb.toString();

        final IModelFactory modelFactory = context.getModelFactory();
        final IModel model = modelFactory.createModel();
        model.add(modelFactory.createText(processedHtmlContent));
        structureHandler.setBody(model, false);

    }
}
