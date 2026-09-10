package com.axiom.i18n.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class I18nSymbolProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return with(environment) {
            I18nSymbolProcessor(
                configs = Configs(
                    packageName = options[ARG_PACKAGE_NAME] ?: DEFAULT_PACKAGE_NAME,
                    moduleName = options[ARG_MODULE_NAME].orEmpty(),
                    internalVisibility = options[ARG_INTERNAL_VISIBILITY].toBoolean(),
                    generateStringsProperty = options[ARG_GENERATE_STRINGS_PROPERTY].toBoolean()
                ),
                codeGenerator = codeGenerator,
                logger = logger
            )
        }
    }

    private companion object {
        const val ARG_PACKAGE_NAME = "axiom.i18n.packageName"
        const val ARG_MODULE_NAME = "axiom.i18n.moduleName"
        const val ARG_INTERNAL_VISIBILITY = "axiom.i18n.internalVisibility"
        const val ARG_GENERATE_STRINGS_PROPERTY = "axiom.i18n.generateStringsProperty"

        const val DEFAULT_PACKAGE_NAME = "com.axiom.i18n"
    }
}
