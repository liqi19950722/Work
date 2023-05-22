// Generated from C:/Users/15868/Dev/idea-projects/Work/work-2-08/src/main/antlr4\BaseSql.g4 by ANTLR 4.12.0
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link BaseSqlParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface BaseSqlVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link BaseSqlParser#sqlDML}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSqlDML(BaseSqlParser.SqlDMLContext ctx);
	/**
	 * Visit a parse tree produced by {@link BaseSqlParser#comparison_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparison_operator(BaseSqlParser.Comparison_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link BaseSqlParser#schema_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchema_name(BaseSqlParser.Schema_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link BaseSqlParser#correlation_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCorrelation_name(BaseSqlParser.Correlation_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link BaseSqlParser#table_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_name(BaseSqlParser.Table_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link BaseSqlParser#column_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_name(BaseSqlParser.Column_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link BaseSqlParser#simple_column_name_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_column_name_list(BaseSqlParser.Simple_column_name_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link BaseSqlParser#simple_column_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_column_name(BaseSqlParser.Simple_column_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link BaseSqlParser#id_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId_(BaseSqlParser.Id_Context ctx);
	/**
	 * Visit a parse tree produced by {@link BaseSqlParser#values_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValues_expression(BaseSqlParser.Values_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link BaseSqlParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(BaseSqlParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link BaseSqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(BaseSqlParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link BaseSqlParser#primitive_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitive_expression(BaseSqlParser.Primitive_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link BaseSqlParser#where_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhere_clause(BaseSqlParser.Where_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link BaseSqlParser#boolean_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolean_expression(BaseSqlParser.Boolean_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link BaseSqlParser#character_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharacter_expression(BaseSqlParser.Character_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link BaseSqlParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(BaseSqlParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link BaseSqlParser#sign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSign(BaseSqlParser.SignContext ctx);
	/**
	 * Visit a parse tree produced by {@link BaseSqlParser#string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(BaseSqlParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by {@link BaseSqlParser#insert}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert(BaseSqlParser.InsertContext ctx);
	/**
	 * Visit a parse tree produced by {@link BaseSqlParser#update}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate(BaseSqlParser.UpdateContext ctx);
	/**
	 * Visit a parse tree produced by {@link BaseSqlParser#delete}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDelete(BaseSqlParser.DeleteContext ctx);
}