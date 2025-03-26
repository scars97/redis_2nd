package com.example.common.config

import org.hibernate.boot.model.FunctionContributions
import org.hibernate.dialect.MySQLDialect
import org.hibernate.query.ReturnableType
import org.hibernate.query.sqm.function.NamedSqmFunctionDescriptor
import org.hibernate.query.sqm.function.SqmFunctionRegistry
import org.hibernate.query.sqm.produce.function.StandardArgumentsValidators
import org.hibernate.sql.ast.SqlAstNodeRenderingMode
import org.hibernate.sql.ast.SqlAstTranslator
import org.hibernate.sql.ast.spi.SqlAppender
import org.hibernate.sql.ast.tree.SqlAstNode

open class MySqlFullTextDialect: MySQLDialect() {

    override fun initializeFunctionRegistry(functionContributions: FunctionContributions) {
        super.initializeFunctionRegistry(functionContributions)

        val functionRegistry: SqmFunctionRegistry = functionContributions.functionRegistry
        functionRegistry.register("MATCH", MatchFunction)
    }

    object MatchFunction : NamedSqmFunctionDescriptor(
        "MATCH",
        false,
        StandardArgumentsValidators.exactly(2),
        null
    ) {
        override fun render(
            sqlAppender: SqlAppender,
            arguments: List<out SqlAstNode>,
            returnType: ReturnableType<*>?,
            translator: SqlAstTranslator<*>
        ) {
            sqlAppender.appendSql("MATCH(")
            translator.render(arguments[0], SqlAstNodeRenderingMode.DEFAULT)
            sqlAppender.appendSql(") AGAINST (")
            translator.render(arguments[1], SqlAstNodeRenderingMode.DEFAULT)
            sqlAppender.appendSql(" IN NATURAL LANGUAGE MODE)")
        }
    }
}