// Generated from C:/Users/15868/Dev/idea-projects/Work/work-2-08/src/main/antlr4\BaseSql.g4 by ANTLR 4.12.0
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link BaseSqlParser}.
 */
public interface BaseSqlListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link BaseSqlParser#sqlDML}.
	 * @param ctx the parse tree
	 */
	void enterSqlDML(BaseSqlParser.SqlDMLContext ctx);
	/**
	 * Exit a parse tree produced by {@link BaseSqlParser#sqlDML}.
	 * @param ctx the parse tree
	 */
	void exitSqlDML(BaseSqlParser.SqlDMLContext ctx);
	/**
	 * Enter a parse tree produced by {@link BaseSqlParser#comparison_operator}.
	 * @param ctx the parse tree
	 */
	void enterComparison_operator(BaseSqlParser.Comparison_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link BaseSqlParser#comparison_operator}.
	 * @param ctx the parse tree
	 */
	void exitComparison_operator(BaseSqlParser.Comparison_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link BaseSqlParser#schema_name}.
	 * @param ctx the parse tree
	 */
	void enterSchema_name(BaseSqlParser.Schema_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link BaseSqlParser#schema_name}.
	 * @param ctx the parse tree
	 */
	void exitSchema_name(BaseSqlParser.Schema_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link BaseSqlParser#correlation_name}.
	 * @param ctx the parse tree
	 */
	void enterCorrelation_name(BaseSqlParser.Correlation_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link BaseSqlParser#correlation_name}.
	 * @param ctx the parse tree
	 */
	void exitCorrelation_name(BaseSqlParser.Correlation_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link BaseSqlParser#table_name}.
	 * @param ctx the parse tree
	 */
	void enterTable_name(BaseSqlParser.Table_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link BaseSqlParser#table_name}.
	 * @param ctx the parse tree
	 */
	void exitTable_name(BaseSqlParser.Table_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link BaseSqlParser#column_name}.
	 * @param ctx the parse tree
	 */
	void enterColumn_name(BaseSqlParser.Column_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link BaseSqlParser#column_name}.
	 * @param ctx the parse tree
	 */
	void exitColumn_name(BaseSqlParser.Column_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link BaseSqlParser#simple_column_name_list}.
	 * @param ctx the parse tree
	 */
	void enterSimple_column_name_list(BaseSqlParser.Simple_column_name_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link BaseSqlParser#simple_column_name_list}.
	 * @param ctx the parse tree
	 */
	void exitSimple_column_name_list(BaseSqlParser.Simple_column_name_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link BaseSqlParser#simple_column_name}.
	 * @param ctx the parse tree
	 */
	void enterSimple_column_name(BaseSqlParser.Simple_column_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link BaseSqlParser#simple_column_name}.
	 * @param ctx the parse tree
	 */
	void exitSimple_column_name(BaseSqlParser.Simple_column_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link BaseSqlParser#id_}.
	 * @param ctx the parse tree
	 */
	void enterId_(BaseSqlParser.Id_Context ctx);
	/**
	 * Exit a parse tree produced by {@link BaseSqlParser#id_}.
	 * @param ctx the parse tree
	 */
	void exitId_(BaseSqlParser.Id_Context ctx);
	/**
	 * Enter a parse tree produced by {@link BaseSqlParser#values_expression}.
	 * @param ctx the parse tree
	 */
	void enterValues_expression(BaseSqlParser.Values_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link BaseSqlParser#values_expression}.
	 * @param ctx the parse tree
	 */
	void exitValues_expression(BaseSqlParser.Values_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link BaseSqlParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(BaseSqlParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link BaseSqlParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(BaseSqlParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link BaseSqlParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(BaseSqlParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link BaseSqlParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(BaseSqlParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link BaseSqlParser#primitive_expression}.
	 * @param ctx the parse tree
	 */
	void enterPrimitive_expression(BaseSqlParser.Primitive_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link BaseSqlParser#primitive_expression}.
	 * @param ctx the parse tree
	 */
	void exitPrimitive_expression(BaseSqlParser.Primitive_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link BaseSqlParser#where_clause}.
	 * @param ctx the parse tree
	 */
	void enterWhere_clause(BaseSqlParser.Where_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link BaseSqlParser#where_clause}.
	 * @param ctx the parse tree
	 */
	void exitWhere_clause(BaseSqlParser.Where_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link BaseSqlParser#boolean_expression}.
	 * @param ctx the parse tree
	 */
	void enterBoolean_expression(BaseSqlParser.Boolean_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link BaseSqlParser#boolean_expression}.
	 * @param ctx the parse tree
	 */
	void exitBoolean_expression(BaseSqlParser.Boolean_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link BaseSqlParser#character_expression}.
	 * @param ctx the parse tree
	 */
	void enterCharacter_expression(BaseSqlParser.Character_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link BaseSqlParser#character_expression}.
	 * @param ctx the parse tree
	 */
	void exitCharacter_expression(BaseSqlParser.Character_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link BaseSqlParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(BaseSqlParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link BaseSqlParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(BaseSqlParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link BaseSqlParser#sign}.
	 * @param ctx the parse tree
	 */
	void enterSign(BaseSqlParser.SignContext ctx);
	/**
	 * Exit a parse tree produced by {@link BaseSqlParser#sign}.
	 * @param ctx the parse tree
	 */
	void exitSign(BaseSqlParser.SignContext ctx);
	/**
	 * Enter a parse tree produced by {@link BaseSqlParser#string}.
	 * @param ctx the parse tree
	 */
	void enterString(BaseSqlParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by {@link BaseSqlParser#string}.
	 * @param ctx the parse tree
	 */
	void exitString(BaseSqlParser.StringContext ctx);
	/**
	 * Enter a parse tree produced by {@link BaseSqlParser#insert}.
	 * @param ctx the parse tree
	 */
	void enterInsert(BaseSqlParser.InsertContext ctx);
	/**
	 * Exit a parse tree produced by {@link BaseSqlParser#insert}.
	 * @param ctx the parse tree
	 */
	void exitInsert(BaseSqlParser.InsertContext ctx);
	/**
	 * Enter a parse tree produced by {@link BaseSqlParser#update}.
	 * @param ctx the parse tree
	 */
	void enterUpdate(BaseSqlParser.UpdateContext ctx);
	/**
	 * Exit a parse tree produced by {@link BaseSqlParser#update}.
	 * @param ctx the parse tree
	 */
	void exitUpdate(BaseSqlParser.UpdateContext ctx);
	/**
	 * Enter a parse tree produced by {@link BaseSqlParser#delete}.
	 * @param ctx the parse tree
	 */
	void enterDelete(BaseSqlParser.DeleteContext ctx);
	/**
	 * Exit a parse tree produced by {@link BaseSqlParser#delete}.
	 * @param ctx the parse tree
	 */
	void exitDelete(BaseSqlParser.DeleteContext ctx);
}