package com.tarosuke777.hms.thymeleaf.dialect;

import java.util.HashSet;
import java.util.Set;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

/**
 * HMSカスタムThymeleafダイアレクト。 このダイアレクトは、カスタム属性プロセッサーを登録します。
 */
public class HmsDialect extends AbstractProcessorDialect {

    private static final String DIALECT_NAME = "Hms Dialect";
    private static final String DIALECT_PREFIX = "hms";

    public HmsDialect() {
        super(DIALECT_NAME, DIALECT_PREFIX, StandardDialect.PROCESSOR_PRECEDENCE);
    }

    /**
     * このダイアレクトが提供するプロセッサーのセットを返します。
     * 
     * @param dialectPrefix ダイアレクトのプレフィックス
     * @return プロセッサーのセット
     */
    @Override
    public Set<IProcessor> getProcessors(final String dialectPrefix) {
        final Set<IProcessor> processors = new HashSet<>();
        processors.add(new LineBreakProcessor(dialectPrefix, getDialectProcessorPrecedence()));
        return processors;
    }

}
