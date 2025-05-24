package com.tarosuke777.hms.thymeleaf.dialect;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.processor.AbstractStandardExpressionAttributeTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

public class LineBreakProcessor extends AbstractStandardExpressionAttributeTagProcessor {

    private static final String ATTRIBUTE_NAME = "linebreak";

    public LineBreakProcessor(String dialectPrefix, int precedence) {
        super(TemplateMode.HTML, dialectPrefix, ATTRIBUTE_NAME, precedence, true);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
            AttributeName attributeName, String attributeValue, Object expressionResult,
            IElementTagStructureHandler structureHandler) {

        if (expressionResult == null) {
            return;
        }

        final String textWithLineBreaks = expressionResult.toString().replace("\n", "<br/>");
        final String textWithLineBreaksAndCarriageReturns = textWithLineBreaks.replace("\r", "");

        final IModelFactory modelFactory = context.getModelFactory();
        final IModel model = modelFactory.createModel();
        model.add(modelFactory.createText(textWithLineBreaksAndCarriageReturns));
        structureHandler.setBody(model, false);

    }
}
