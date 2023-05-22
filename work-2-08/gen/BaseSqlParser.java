// Generated from C:/Users/15868/Dev/idea-projects/Work/work-2-08/src/main/antlr4\BaseSql.g4 by ANTLR 4.12.0
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class BaseSqlParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.12.0", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, INSERT=6, INTO=7, VALUES=8, UPDATE=9, 
		SET=10, WHERE=11, DELETE=12, FROM=13, AND=14, OR=15, NOT=16, IS=17, IN=18, 
		LIKE=19, BETWEEN=20, ESCAPE=21, PLUS=22, MINUS=23, LR_BRACKET=24, RR_BRACKET=25, 
		EQ=26, COMMA=27, SEMI=28, WS=29, ID_LITERAL=30, STRING_LITERAL=31, DECIMAL_LITERAL=32, 
		FLOAT_LITERAL=33, REAL_LITERAL=34, NULL_=35, TRUE=36, FALSE=37, DOT=38;
	public static final int
		RULE_sqlDML = 0, RULE_comparison_operator = 1, RULE_schema_name = 2, RULE_correlation_name = 3, 
		RULE_table_name = 4, RULE_column_name = 5, RULE_simple_column_name_list = 6, 
		RULE_simple_column_name = 7, RULE_id_ = 8, RULE_values_expression = 9, 
		RULE_value = 10, RULE_expression = 11, RULE_primitive_expression = 12, 
		RULE_where_clause = 13, RULE_boolean_expression = 14, RULE_character_expression = 15, 
		RULE_literal = 16, RULE_sign = 17, RULE_string = 18, RULE_insert = 19, 
		RULE_update = 20, RULE_delete = 21;
	private static String[] makeRuleNames() {
		return new String[] {
			"sqlDML", "comparison_operator", "schema_name", "correlation_name", "table_name", 
			"column_name", "simple_column_name_list", "simple_column_name", "id_", 
			"values_expression", "value", "expression", "primitive_expression", "where_clause", 
			"boolean_expression", "character_expression", "literal", "sign", "string", 
			"insert", "update", "delete"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'<'", "'>'", "'<='", "'>='", "'<>'", null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, "'+'", 
			"'-'", "'('", "')'", "'='", "','", "';'", null, null, null, null, null, 
			null, "'NULL'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, "INSERT", "INTO", "VALUES", "UPDATE", 
			"SET", "WHERE", "DELETE", "FROM", "AND", "OR", "NOT", "IS", "IN", "LIKE", 
			"BETWEEN", "ESCAPE", "PLUS", "MINUS", "LR_BRACKET", "RR_BRACKET", "EQ", 
			"COMMA", "SEMI", "WS", "ID_LITERAL", "STRING_LITERAL", "DECIMAL_LITERAL", 
			"FLOAT_LITERAL", "REAL_LITERAL", "NULL_", "TRUE", "FALSE", "DOT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "BaseSql.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public BaseSqlParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SqlDMLContext extends ParserRuleContext {
		public InsertContext insert() {
			return getRuleContext(InsertContext.class,0);
		}
		public TerminalNode EOF() { return getToken(BaseSqlParser.EOF, 0); }
		public UpdateContext update() {
			return getRuleContext(UpdateContext.class,0);
		}
		public DeleteContext delete() {
			return getRuleContext(DeleteContext.class,0);
		}
		public SqlDMLContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sqlDML; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).enterSqlDML(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).exitSqlDML(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BaseSqlVisitor ) return ((BaseSqlVisitor<? extends T>)visitor).visitSqlDML(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SqlDMLContext sqlDML() throws RecognitionException {
		SqlDMLContext _localctx = new SqlDMLContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_sqlDML);
		try {
			setState(53);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INSERT:
				enterOuterAlt(_localctx, 1);
				{
				setState(44);
				insert();
				setState(45);
				match(EOF);
				}
				break;
			case UPDATE:
				enterOuterAlt(_localctx, 2);
				{
				setState(47);
				update();
				setState(48);
				match(EOF);
				}
				break;
			case DELETE:
				enterOuterAlt(_localctx, 3);
				{
				setState(50);
				delete();
				setState(51);
				match(EOF);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Comparison_operatorContext extends ParserRuleContext {
		public TerminalNode EQ() { return getToken(BaseSqlParser.EQ, 0); }
		public Comparison_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comparison_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).enterComparison_operator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).exitComparison_operator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BaseSqlVisitor ) return ((BaseSqlVisitor<? extends T>)visitor).visitComparison_operator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Comparison_operatorContext comparison_operator() throws RecognitionException {
		Comparison_operatorContext _localctx = new Comparison_operatorContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_comparison_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(55);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 67108926L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Schema_nameContext extends ParserRuleContext {
		public Id_Context id_() {
			return getRuleContext(Id_Context.class,0);
		}
		public Schema_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schema_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).enterSchema_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).exitSchema_name(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BaseSqlVisitor ) return ((BaseSqlVisitor<? extends T>)visitor).visitSchema_name(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Schema_nameContext schema_name() throws RecognitionException {
		Schema_nameContext _localctx = new Schema_nameContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_schema_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(57);
			id_();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Correlation_nameContext extends ParserRuleContext {
		public Id_Context id_() {
			return getRuleContext(Id_Context.class,0);
		}
		public Correlation_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_correlation_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).enterCorrelation_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).exitCorrelation_name(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BaseSqlVisitor ) return ((BaseSqlVisitor<? extends T>)visitor).visitCorrelation_name(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Correlation_nameContext correlation_name() throws RecognitionException {
		Correlation_nameContext _localctx = new Correlation_nameContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_correlation_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59);
			id_();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Table_nameContext extends ParserRuleContext {
		public Id_Context id_() {
			return getRuleContext(Id_Context.class,0);
		}
		public Schema_nameContext schema_name() {
			return getRuleContext(Schema_nameContext.class,0);
		}
		public TerminalNode DOT() { return getToken(BaseSqlParser.DOT, 0); }
		public Table_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_table_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).enterTable_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).exitTable_name(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BaseSqlVisitor ) return ((BaseSqlVisitor<? extends T>)visitor).visitTable_name(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Table_nameContext table_name() throws RecognitionException {
		Table_nameContext _localctx = new Table_nameContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_table_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(64);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(61);
				schema_name();
				setState(62);
				match(DOT);
				}
				break;
			}
			setState(66);
			id_();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Column_nameContext extends ParserRuleContext {
		public Id_Context id_() {
			return getRuleContext(Id_Context.class,0);
		}
		public TerminalNode DOT() { return getToken(BaseSqlParser.DOT, 0); }
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public Correlation_nameContext correlation_name() {
			return getRuleContext(Correlation_nameContext.class,0);
		}
		public Column_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_column_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).enterColumn_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).exitColumn_name(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BaseSqlVisitor ) return ((BaseSqlVisitor<? extends T>)visitor).visitColumn_name(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Column_nameContext column_name() throws RecognitionException {
		Column_nameContext _localctx = new Column_nameContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_column_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(74);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				{
				setState(70);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
				case 1:
					{
					setState(68);
					table_name();
					}
					break;
				case 2:
					{
					setState(69);
					correlation_name();
					}
					break;
				}
				setState(72);
				match(DOT);
				}
				break;
			}
			setState(76);
			id_();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Simple_column_name_listContext extends ParserRuleContext {
		public List<Simple_column_nameContext> simple_column_name() {
			return getRuleContexts(Simple_column_nameContext.class);
		}
		public Simple_column_nameContext simple_column_name(int i) {
			return getRuleContext(Simple_column_nameContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(BaseSqlParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(BaseSqlParser.COMMA, i);
		}
		public Simple_column_name_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simple_column_name_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).enterSimple_column_name_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).exitSimple_column_name_list(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BaseSqlVisitor ) return ((BaseSqlVisitor<? extends T>)visitor).visitSimple_column_name_list(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Simple_column_name_listContext simple_column_name_list() throws RecognitionException {
		Simple_column_name_listContext _localctx = new Simple_column_name_listContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_simple_column_name_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(78);
			simple_column_name();
			setState(83);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(79);
				match(COMMA);
				setState(80);
				simple_column_name();
				}
				}
				setState(85);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Simple_column_nameContext extends ParserRuleContext {
		public Id_Context id_() {
			return getRuleContext(Id_Context.class,0);
		}
		public Simple_column_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simple_column_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).enterSimple_column_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).exitSimple_column_name(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BaseSqlVisitor ) return ((BaseSqlVisitor<? extends T>)visitor).visitSimple_column_name(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Simple_column_nameContext simple_column_name() throws RecognitionException {
		Simple_column_nameContext _localctx = new Simple_column_nameContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_simple_column_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(86);
			id_();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Id_Context extends ParserRuleContext {
		public TerminalNode ID_LITERAL() { return getToken(BaseSqlParser.ID_LITERAL, 0); }
		public Id_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_id_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).enterId_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).exitId_(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BaseSqlVisitor ) return ((BaseSqlVisitor<? extends T>)visitor).visitId_(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Id_Context id_() throws RecognitionException {
		Id_Context _localctx = new Id_Context(_ctx, getState());
		enterRule(_localctx, 16, RULE_id_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88);
			match(ID_LITERAL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Values_expressionContext extends ParserRuleContext {
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(BaseSqlParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(BaseSqlParser.COMMA, i);
		}
		public Values_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_values_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).enterValues_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).exitValues_expression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BaseSqlVisitor ) return ((BaseSqlVisitor<? extends T>)visitor).visitValues_expression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Values_expressionContext values_expression() throws RecognitionException {
		Values_expressionContext _localctx = new Values_expressionContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_values_expression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(90);
			value();
			setState(95);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(91);
				match(COMMA);
				setState(92);
				value();
				}
				}
				setState(97);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ValueContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).exitValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BaseSqlVisitor ) return ((BaseSqlVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_value);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(98);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public Primitive_expressionContext primitive_expression() {
			return getRuleContext(Primitive_expressionContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public Comparison_operatorContext comparison_operator() {
			return getRuleContext(Comparison_operatorContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BaseSqlVisitor ) return ((BaseSqlVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 22;
		enterRecursionRule(_localctx, 22, RULE_expression, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(101);
			primitive_expression();
			}
			_ctx.stop = _input.LT(-1);
			setState(109);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ExpressionContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_expression);
					setState(103);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(104);
					comparison_operator();
					setState(105);
					expression(2);
					}
					} 
				}
				setState(111);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Primitive_expressionContext extends ParserRuleContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public Id_Context id_() {
			return getRuleContext(Id_Context.class,0);
		}
		public Primitive_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primitive_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).enterPrimitive_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).exitPrimitive_expression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BaseSqlVisitor ) return ((BaseSqlVisitor<? extends T>)visitor).visitPrimitive_expression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Primitive_expressionContext primitive_expression() throws RecognitionException {
		Primitive_expressionContext _localctx = new Primitive_expressionContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_primitive_expression);
		try {
			setState(114);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PLUS:
			case MINUS:
			case STRING_LITERAL:
			case DECIMAL_LITERAL:
			case FLOAT_LITERAL:
			case REAL_LITERAL:
			case NULL_:
			case TRUE:
			case FALSE:
				enterOuterAlt(_localctx, 1);
				{
				setState(112);
				literal();
				}
				break;
			case ID_LITERAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(113);
				id_();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Where_clauseContext extends ParserRuleContext {
		public TerminalNode WHERE() { return getToken(BaseSqlParser.WHERE, 0); }
		public Boolean_expressionContext boolean_expression() {
			return getRuleContext(Boolean_expressionContext.class,0);
		}
		public Where_clauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_where_clause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).enterWhere_clause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).exitWhere_clause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BaseSqlVisitor ) return ((BaseSqlVisitor<? extends T>)visitor).visitWhere_clause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Where_clauseContext where_clause() throws RecognitionException {
		Where_clauseContext _localctx = new Where_clauseContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_where_clause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(116);
			match(WHERE);
			setState(117);
			boolean_expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Boolean_expressionContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode AND() { return getToken(BaseSqlParser.AND, 0); }
		public TerminalNode OR() { return getToken(BaseSqlParser.OR, 0); }
		public TerminalNode NOT() { return getToken(BaseSqlParser.NOT, 0); }
		public Comparison_operatorContext comparison_operator() {
			return getRuleContext(Comparison_operatorContext.class,0);
		}
		public TerminalNode IS() { return getToken(BaseSqlParser.IS, 0); }
		public TerminalNode NULL_() { return getToken(BaseSqlParser.NULL_, 0); }
		public List<Character_expressionContext> character_expression() {
			return getRuleContexts(Character_expressionContext.class);
		}
		public Character_expressionContext character_expression(int i) {
			return getRuleContext(Character_expressionContext.class,i);
		}
		public TerminalNode LIKE() { return getToken(BaseSqlParser.LIKE, 0); }
		public TerminalNode ESCAPE() { return getToken(BaseSqlParser.ESCAPE, 0); }
		public StringContext string() {
			return getRuleContext(StringContext.class,0);
		}
		public TerminalNode BETWEEN() { return getToken(BaseSqlParser.BETWEEN, 0); }
		public TerminalNode IN() { return getToken(BaseSqlParser.IN, 0); }
		public TerminalNode LR_BRACKET() { return getToken(BaseSqlParser.LR_BRACKET, 0); }
		public TerminalNode RR_BRACKET() { return getToken(BaseSqlParser.RR_BRACKET, 0); }
		public List<TerminalNode> COMMA() { return getTokens(BaseSqlParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(BaseSqlParser.COMMA, i);
		}
		public Boolean_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_boolean_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).enterBoolean_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).exitBoolean_expression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BaseSqlVisitor ) return ((BaseSqlVisitor<? extends T>)visitor).visitBoolean_expression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Boolean_expressionContext boolean_expression() throws RecognitionException {
		Boolean_expressionContext _localctx = new Boolean_expressionContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_boolean_expression);
		int _la;
		try {
			setState(175);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(119);
				expression(0);
				setState(120);
				match(AND);
				setState(121);
				expression(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(123);
				expression(0);
				setState(124);
				match(OR);
				setState(125);
				expression(0);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(127);
				match(NOT);
				setState(128);
				expression(0);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(129);
				expression(0);
				setState(130);
				comparison_operator();
				setState(131);
				expression(0);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(133);
				expression(0);
				setState(134);
				match(IS);
				setState(136);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NOT) {
					{
					setState(135);
					match(NOT);
					}
				}

				setState(138);
				match(NULL_);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(140);
				character_expression();
				setState(142);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NOT) {
					{
					setState(141);
					match(NOT);
					}
				}

				setState(144);
				match(LIKE);
				setState(145);
				character_expression();
				setState(148);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ESCAPE) {
					{
					setState(146);
					match(ESCAPE);
					setState(147);
					string();
					}
				}

				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(150);
				expression(0);
				setState(152);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NOT) {
					{
					setState(151);
					match(NOT);
					}
				}

				setState(154);
				match(BETWEEN);
				setState(155);
				expression(0);
				setState(156);
				match(AND);
				setState(157);
				expression(0);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(159);
				expression(0);
				setState(161);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NOT) {
					{
					setState(160);
					match(NOT);
					}
				}

				setState(163);
				match(IN);
				setState(164);
				match(LR_BRACKET);
				setState(165);
				expression(0);
				setState(170);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(166);
					match(COMMA);
					setState(167);
					expression(0);
					}
					}
					setState(172);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(173);
				match(RR_BRACKET);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Character_expressionContext extends ParserRuleContext {
		public StringContext string() {
			return getRuleContext(StringContext.class,0);
		}
		public Character_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_character_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).enterCharacter_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).exitCharacter_expression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BaseSqlVisitor ) return ((BaseSqlVisitor<? extends T>)visitor).visitCharacter_expression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Character_expressionContext character_expression() throws RecognitionException {
		Character_expressionContext _localctx = new Character_expressionContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_character_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(177);
			string();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LiteralContext extends ParserRuleContext {
		public StringContext string() {
			return getRuleContext(StringContext.class,0);
		}
		public TerminalNode DECIMAL_LITERAL() { return getToken(BaseSqlParser.DECIMAL_LITERAL, 0); }
		public SignContext sign() {
			return getRuleContext(SignContext.class,0);
		}
		public TerminalNode REAL_LITERAL() { return getToken(BaseSqlParser.REAL_LITERAL, 0); }
		public TerminalNode FLOAT_LITERAL() { return getToken(BaseSqlParser.FLOAT_LITERAL, 0); }
		public TerminalNode TRUE() { return getToken(BaseSqlParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(BaseSqlParser.FALSE, 0); }
		public TerminalNode NULL_() { return getToken(BaseSqlParser.NULL_, 0); }
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).exitLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BaseSqlVisitor ) return ((BaseSqlVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_literal);
		int _la;
		try {
			setState(191);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(179);
				string();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(181);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==PLUS || _la==MINUS) {
					{
					setState(180);
					sign();
					}
				}

				setState(183);
				match(DECIMAL_LITERAL);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(185);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==PLUS || _la==MINUS) {
					{
					setState(184);
					sign();
					}
				}

				setState(187);
				_la = _input.LA(1);
				if ( !(_la==FLOAT_LITERAL || _la==REAL_LITERAL) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(188);
				match(TRUE);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(189);
				match(FALSE);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(190);
				match(NULL_);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SignContext extends ParserRuleContext {
		public TerminalNode PLUS() { return getToken(BaseSqlParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(BaseSqlParser.MINUS, 0); }
		public SignContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sign; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).enterSign(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).exitSign(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BaseSqlVisitor ) return ((BaseSqlVisitor<? extends T>)visitor).visitSign(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SignContext sign() throws RecognitionException {
		SignContext _localctx = new SignContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_sign);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(193);
			_la = _input.LA(1);
			if ( !(_la==PLUS || _la==MINUS) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StringContext extends ParserRuleContext {
		public TerminalNode STRING_LITERAL() { return getToken(BaseSqlParser.STRING_LITERAL, 0); }
		public StringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_string; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).enterString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).exitString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BaseSqlVisitor ) return ((BaseSqlVisitor<? extends T>)visitor).visitString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringContext string() throws RecognitionException {
		StringContext _localctx = new StringContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_string);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(195);
			match(STRING_LITERAL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InsertContext extends ParserRuleContext {
		public TerminalNode INSERT() { return getToken(BaseSqlParser.INSERT, 0); }
		public TerminalNode INTO() { return getToken(BaseSqlParser.INTO, 0); }
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public List<TerminalNode> LR_BRACKET() { return getTokens(BaseSqlParser.LR_BRACKET); }
		public TerminalNode LR_BRACKET(int i) {
			return getToken(BaseSqlParser.LR_BRACKET, i);
		}
		public Simple_column_name_listContext simple_column_name_list() {
			return getRuleContext(Simple_column_name_listContext.class,0);
		}
		public List<TerminalNode> RR_BRACKET() { return getTokens(BaseSqlParser.RR_BRACKET); }
		public TerminalNode RR_BRACKET(int i) {
			return getToken(BaseSqlParser.RR_BRACKET, i);
		}
		public TerminalNode VALUES() { return getToken(BaseSqlParser.VALUES, 0); }
		public Values_expressionContext values_expression() {
			return getRuleContext(Values_expressionContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(BaseSqlParser.SEMI, 0); }
		public InsertContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_insert; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).enterInsert(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).exitInsert(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BaseSqlVisitor ) return ((BaseSqlVisitor<? extends T>)visitor).visitInsert(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InsertContext insert() throws RecognitionException {
		InsertContext _localctx = new InsertContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_insert);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(197);
			match(INSERT);
			setState(198);
			match(INTO);
			setState(199);
			table_name();
			setState(200);
			match(LR_BRACKET);
			setState(201);
			simple_column_name_list();
			setState(202);
			match(RR_BRACKET);
			setState(203);
			match(VALUES);
			setState(204);
			match(LR_BRACKET);
			setState(205);
			values_expression();
			setState(206);
			match(RR_BRACKET);
			setState(208);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SEMI) {
				{
				setState(207);
				match(SEMI);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UpdateContext extends ParserRuleContext {
		public TerminalNode UPDATE() { return getToken(BaseSqlParser.UPDATE, 0); }
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public TerminalNode SET() { return getToken(BaseSqlParser.SET, 0); }
		public List<Column_nameContext> column_name() {
			return getRuleContexts(Column_nameContext.class);
		}
		public Column_nameContext column_name(int i) {
			return getRuleContext(Column_nameContext.class,i);
		}
		public List<TerminalNode> EQ() { return getTokens(BaseSqlParser.EQ); }
		public TerminalNode EQ(int i) {
			return getToken(BaseSqlParser.EQ, i);
		}
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(BaseSqlParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(BaseSqlParser.COMMA, i);
		}
		public Where_clauseContext where_clause() {
			return getRuleContext(Where_clauseContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(BaseSqlParser.SEMI, 0); }
		public UpdateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_update; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).enterUpdate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).exitUpdate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BaseSqlVisitor ) return ((BaseSqlVisitor<? extends T>)visitor).visitUpdate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UpdateContext update() throws RecognitionException {
		UpdateContext _localctx = new UpdateContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_update);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(210);
			match(UPDATE);
			setState(211);
			table_name();
			setState(212);
			match(SET);
			setState(213);
			column_name();
			setState(214);
			match(EQ);
			setState(215);
			value();
			setState(223);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(216);
				match(COMMA);
				setState(217);
				column_name();
				setState(218);
				match(EQ);
				setState(219);
				value();
				}
				}
				setState(225);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(227);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(226);
				where_clause();
				}
			}

			setState(230);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SEMI) {
				{
				setState(229);
				match(SEMI);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DeleteContext extends ParserRuleContext {
		public TerminalNode DELETE() { return getToken(BaseSqlParser.DELETE, 0); }
		public TerminalNode FROM() { return getToken(BaseSqlParser.FROM, 0); }
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public Where_clauseContext where_clause() {
			return getRuleContext(Where_clauseContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(BaseSqlParser.SEMI, 0); }
		public DeleteContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_delete; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).enterDelete(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BaseSqlListener ) ((BaseSqlListener)listener).exitDelete(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BaseSqlVisitor ) return ((BaseSqlVisitor<? extends T>)visitor).visitDelete(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeleteContext delete() throws RecognitionException {
		DeleteContext _localctx = new DeleteContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_delete);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(232);
			match(DELETE);
			setState(233);
			match(FROM);
			setState(234);
			table_name();
			setState(236);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(235);
				where_clause();
				}
			}

			setState(239);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SEMI) {
				{
				setState(238);
				match(SEMI);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 11:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001&\u00f2\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0003\u00006\b\u0000\u0001\u0001"+
		"\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0003\u0004A\b\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0005\u0001\u0005\u0003\u0005G\b\u0005\u0001\u0005\u0001\u0005"+
		"\u0003\u0005K\b\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0005\u0006R\b\u0006\n\u0006\f\u0006U\t\u0006\u0001\u0007"+
		"\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0005\t^\b\t\n\t"+
		"\f\ta\t\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0001\u000b\u0005\u000bl\b\u000b\n\u000b\f\u000b"+
		"o\t\u000b\u0001\f\u0001\f\u0003\fs\b\f\u0001\r\u0001\r\u0001\r\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0003\u000e\u0089\b\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0003\u000e\u008f\b\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0003\u000e\u0095\b\u000e"+
		"\u0001\u000e\u0001\u000e\u0003\u000e\u0099\b\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0003\u000e"+
		"\u00a2\b\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0005\u000e\u00a9\b\u000e\n\u000e\f\u000e\u00ac\t\u000e\u0001\u000e\u0001"+
		"\u000e\u0003\u000e\u00b0\b\u000e\u0001\u000f\u0001\u000f\u0001\u0010\u0001"+
		"\u0010\u0003\u0010\u00b6\b\u0010\u0001\u0010\u0001\u0010\u0003\u0010\u00ba"+
		"\b\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0003\u0010\u00c0"+
		"\b\u0010\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0003\u0013\u00d1\b\u0013\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0005\u0014\u00de"+
		"\b\u0014\n\u0014\f\u0014\u00e1\t\u0014\u0001\u0014\u0003\u0014\u00e4\b"+
		"\u0014\u0001\u0014\u0003\u0014\u00e7\b\u0014\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0001\u0015\u0003\u0015\u00ed\b\u0015\u0001\u0015\u0003\u0015\u00f0"+
		"\b\u0015\u0001\u0015\u0000\u0001\u0016\u0016\u0000\u0002\u0004\u0006\b"+
		"\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*\u0000"+
		"\u0003\u0002\u0000\u0001\u0005\u001a\u001a\u0001\u0000!\"\u0001\u0000"+
		"\u0016\u0017\u00fe\u00005\u0001\u0000\u0000\u0000\u00027\u0001\u0000\u0000"+
		"\u0000\u00049\u0001\u0000\u0000\u0000\u0006;\u0001\u0000\u0000\u0000\b"+
		"@\u0001\u0000\u0000\u0000\nJ\u0001\u0000\u0000\u0000\fN\u0001\u0000\u0000"+
		"\u0000\u000eV\u0001\u0000\u0000\u0000\u0010X\u0001\u0000\u0000\u0000\u0012"+
		"Z\u0001\u0000\u0000\u0000\u0014b\u0001\u0000\u0000\u0000\u0016d\u0001"+
		"\u0000\u0000\u0000\u0018r\u0001\u0000\u0000\u0000\u001at\u0001\u0000\u0000"+
		"\u0000\u001c\u00af\u0001\u0000\u0000\u0000\u001e\u00b1\u0001\u0000\u0000"+
		"\u0000 \u00bf\u0001\u0000\u0000\u0000\"\u00c1\u0001\u0000\u0000\u0000"+
		"$\u00c3\u0001\u0000\u0000\u0000&\u00c5\u0001\u0000\u0000\u0000(\u00d2"+
		"\u0001\u0000\u0000\u0000*\u00e8\u0001\u0000\u0000\u0000,-\u0003&\u0013"+
		"\u0000-.\u0005\u0000\u0000\u0001.6\u0001\u0000\u0000\u0000/0\u0003(\u0014"+
		"\u000001\u0005\u0000\u0000\u000116\u0001\u0000\u0000\u000023\u0003*\u0015"+
		"\u000034\u0005\u0000\u0000\u000146\u0001\u0000\u0000\u00005,\u0001\u0000"+
		"\u0000\u00005/\u0001\u0000\u0000\u000052\u0001\u0000\u0000\u00006\u0001"+
		"\u0001\u0000\u0000\u000078\u0007\u0000\u0000\u00008\u0003\u0001\u0000"+
		"\u0000\u00009:\u0003\u0010\b\u0000:\u0005\u0001\u0000\u0000\u0000;<\u0003"+
		"\u0010\b\u0000<\u0007\u0001\u0000\u0000\u0000=>\u0003\u0004\u0002\u0000"+
		">?\u0005&\u0000\u0000?A\u0001\u0000\u0000\u0000@=\u0001\u0000\u0000\u0000"+
		"@A\u0001\u0000\u0000\u0000AB\u0001\u0000\u0000\u0000BC\u0003\u0010\b\u0000"+
		"C\t\u0001\u0000\u0000\u0000DG\u0003\b\u0004\u0000EG\u0003\u0006\u0003"+
		"\u0000FD\u0001\u0000\u0000\u0000FE\u0001\u0000\u0000\u0000GH\u0001\u0000"+
		"\u0000\u0000HI\u0005&\u0000\u0000IK\u0001\u0000\u0000\u0000JF\u0001\u0000"+
		"\u0000\u0000JK\u0001\u0000\u0000\u0000KL\u0001\u0000\u0000\u0000LM\u0003"+
		"\u0010\b\u0000M\u000b\u0001\u0000\u0000\u0000NS\u0003\u000e\u0007\u0000"+
		"OP\u0005\u001b\u0000\u0000PR\u0003\u000e\u0007\u0000QO\u0001\u0000\u0000"+
		"\u0000RU\u0001\u0000\u0000\u0000SQ\u0001\u0000\u0000\u0000ST\u0001\u0000"+
		"\u0000\u0000T\r\u0001\u0000\u0000\u0000US\u0001\u0000\u0000\u0000VW\u0003"+
		"\u0010\b\u0000W\u000f\u0001\u0000\u0000\u0000XY\u0005\u001e\u0000\u0000"+
		"Y\u0011\u0001\u0000\u0000\u0000Z_\u0003\u0014\n\u0000[\\\u0005\u001b\u0000"+
		"\u0000\\^\u0003\u0014\n\u0000][\u0001\u0000\u0000\u0000^a\u0001\u0000"+
		"\u0000\u0000_]\u0001\u0000\u0000\u0000_`\u0001\u0000\u0000\u0000`\u0013"+
		"\u0001\u0000\u0000\u0000a_\u0001\u0000\u0000\u0000bc\u0003\u0016\u000b"+
		"\u0000c\u0015\u0001\u0000\u0000\u0000de\u0006\u000b\uffff\uffff\u0000"+
		"ef\u0003\u0018\f\u0000fm\u0001\u0000\u0000\u0000gh\n\u0001\u0000\u0000"+
		"hi\u0003\u0002\u0001\u0000ij\u0003\u0016\u000b\u0002jl\u0001\u0000\u0000"+
		"\u0000kg\u0001\u0000\u0000\u0000lo\u0001\u0000\u0000\u0000mk\u0001\u0000"+
		"\u0000\u0000mn\u0001\u0000\u0000\u0000n\u0017\u0001\u0000\u0000\u0000"+
		"om\u0001\u0000\u0000\u0000ps\u0003 \u0010\u0000qs\u0003\u0010\b\u0000"+
		"rp\u0001\u0000\u0000\u0000rq\u0001\u0000\u0000\u0000s\u0019\u0001\u0000"+
		"\u0000\u0000tu\u0005\u000b\u0000\u0000uv\u0003\u001c\u000e\u0000v\u001b"+
		"\u0001\u0000\u0000\u0000wx\u0003\u0016\u000b\u0000xy\u0005\u000e\u0000"+
		"\u0000yz\u0003\u0016\u000b\u0000z\u00b0\u0001\u0000\u0000\u0000{|\u0003"+
		"\u0016\u000b\u0000|}\u0005\u000f\u0000\u0000}~\u0003\u0016\u000b\u0000"+
		"~\u00b0\u0001\u0000\u0000\u0000\u007f\u0080\u0005\u0010\u0000\u0000\u0080"+
		"\u00b0\u0003\u0016\u000b\u0000\u0081\u0082\u0003\u0016\u000b\u0000\u0082"+
		"\u0083\u0003\u0002\u0001\u0000\u0083\u0084\u0003\u0016\u000b\u0000\u0084"+
		"\u00b0\u0001\u0000\u0000\u0000\u0085\u0086\u0003\u0016\u000b\u0000\u0086"+
		"\u0088\u0005\u0011\u0000\u0000\u0087\u0089\u0005\u0010\u0000\u0000\u0088"+
		"\u0087\u0001\u0000\u0000\u0000\u0088\u0089\u0001\u0000\u0000\u0000\u0089"+
		"\u008a\u0001\u0000\u0000\u0000\u008a\u008b\u0005#\u0000\u0000\u008b\u00b0"+
		"\u0001\u0000\u0000\u0000\u008c\u008e\u0003\u001e\u000f\u0000\u008d\u008f"+
		"\u0005\u0010\u0000\u0000\u008e\u008d\u0001\u0000\u0000\u0000\u008e\u008f"+
		"\u0001\u0000\u0000\u0000\u008f\u0090\u0001\u0000\u0000\u0000\u0090\u0091"+
		"\u0005\u0013\u0000\u0000\u0091\u0094\u0003\u001e\u000f\u0000\u0092\u0093"+
		"\u0005\u0015\u0000\u0000\u0093\u0095\u0003$\u0012\u0000\u0094\u0092\u0001"+
		"\u0000\u0000\u0000\u0094\u0095\u0001\u0000\u0000\u0000\u0095\u00b0\u0001"+
		"\u0000\u0000\u0000\u0096\u0098\u0003\u0016\u000b\u0000\u0097\u0099\u0005"+
		"\u0010\u0000\u0000\u0098\u0097\u0001\u0000\u0000\u0000\u0098\u0099\u0001"+
		"\u0000\u0000\u0000\u0099\u009a\u0001\u0000\u0000\u0000\u009a\u009b\u0005"+
		"\u0014\u0000\u0000\u009b\u009c\u0003\u0016\u000b\u0000\u009c\u009d\u0005"+
		"\u000e\u0000\u0000\u009d\u009e\u0003\u0016\u000b\u0000\u009e\u00b0\u0001"+
		"\u0000\u0000\u0000\u009f\u00a1\u0003\u0016\u000b\u0000\u00a0\u00a2\u0005"+
		"\u0010\u0000\u0000\u00a1\u00a0\u0001\u0000\u0000\u0000\u00a1\u00a2\u0001"+
		"\u0000\u0000\u0000\u00a2\u00a3\u0001\u0000\u0000\u0000\u00a3\u00a4\u0005"+
		"\u0012\u0000\u0000\u00a4\u00a5\u0005\u0018\u0000\u0000\u00a5\u00aa\u0003"+
		"\u0016\u000b\u0000\u00a6\u00a7\u0005\u001b\u0000\u0000\u00a7\u00a9\u0003"+
		"\u0016\u000b\u0000\u00a8\u00a6\u0001\u0000\u0000\u0000\u00a9\u00ac\u0001"+
		"\u0000\u0000\u0000\u00aa\u00a8\u0001\u0000\u0000\u0000\u00aa\u00ab\u0001"+
		"\u0000\u0000\u0000\u00ab\u00ad\u0001\u0000\u0000\u0000\u00ac\u00aa\u0001"+
		"\u0000\u0000\u0000\u00ad\u00ae\u0005\u0019\u0000\u0000\u00ae\u00b0\u0001"+
		"\u0000\u0000\u0000\u00afw\u0001\u0000\u0000\u0000\u00af{\u0001\u0000\u0000"+
		"\u0000\u00af\u007f\u0001\u0000\u0000\u0000\u00af\u0081\u0001\u0000\u0000"+
		"\u0000\u00af\u0085\u0001\u0000\u0000\u0000\u00af\u008c\u0001\u0000\u0000"+
		"\u0000\u00af\u0096\u0001\u0000\u0000\u0000\u00af\u009f\u0001\u0000\u0000"+
		"\u0000\u00b0\u001d\u0001\u0000\u0000\u0000\u00b1\u00b2\u0003$\u0012\u0000"+
		"\u00b2\u001f\u0001\u0000\u0000\u0000\u00b3\u00c0\u0003$\u0012\u0000\u00b4"+
		"\u00b6\u0003\"\u0011\u0000\u00b5\u00b4\u0001\u0000\u0000\u0000\u00b5\u00b6"+
		"\u0001\u0000\u0000\u0000\u00b6\u00b7\u0001\u0000\u0000\u0000\u00b7\u00c0"+
		"\u0005 \u0000\u0000\u00b8\u00ba\u0003\"\u0011\u0000\u00b9\u00b8\u0001"+
		"\u0000\u0000\u0000\u00b9\u00ba\u0001\u0000\u0000\u0000\u00ba\u00bb\u0001"+
		"\u0000\u0000\u0000\u00bb\u00c0\u0007\u0001\u0000\u0000\u00bc\u00c0\u0005"+
		"$\u0000\u0000\u00bd\u00c0\u0005%\u0000\u0000\u00be\u00c0\u0005#\u0000"+
		"\u0000\u00bf\u00b3\u0001\u0000\u0000\u0000\u00bf\u00b5\u0001\u0000\u0000"+
		"\u0000\u00bf\u00b9\u0001\u0000\u0000\u0000\u00bf\u00bc\u0001\u0000\u0000"+
		"\u0000\u00bf\u00bd\u0001\u0000\u0000\u0000\u00bf\u00be\u0001\u0000\u0000"+
		"\u0000\u00c0!\u0001\u0000\u0000\u0000\u00c1\u00c2\u0007\u0002\u0000\u0000"+
		"\u00c2#\u0001\u0000\u0000\u0000\u00c3\u00c4\u0005\u001f\u0000\u0000\u00c4"+
		"%\u0001\u0000\u0000\u0000\u00c5\u00c6\u0005\u0006\u0000\u0000\u00c6\u00c7"+
		"\u0005\u0007\u0000\u0000\u00c7\u00c8\u0003\b\u0004\u0000\u00c8\u00c9\u0005"+
		"\u0018\u0000\u0000\u00c9\u00ca\u0003\f\u0006\u0000\u00ca\u00cb\u0005\u0019"+
		"\u0000\u0000\u00cb\u00cc\u0005\b\u0000\u0000\u00cc\u00cd\u0005\u0018\u0000"+
		"\u0000\u00cd\u00ce\u0003\u0012\t\u0000\u00ce\u00d0\u0005\u0019\u0000\u0000"+
		"\u00cf\u00d1\u0005\u001c\u0000\u0000\u00d0\u00cf\u0001\u0000\u0000\u0000"+
		"\u00d0\u00d1\u0001\u0000\u0000\u0000\u00d1\'\u0001\u0000\u0000\u0000\u00d2"+
		"\u00d3\u0005\t\u0000\u0000\u00d3\u00d4\u0003\b\u0004\u0000\u00d4\u00d5"+
		"\u0005\n\u0000\u0000\u00d5\u00d6\u0003\n\u0005\u0000\u00d6\u00d7\u0005"+
		"\u001a\u0000\u0000\u00d7\u00df\u0003\u0014\n\u0000\u00d8\u00d9\u0005\u001b"+
		"\u0000\u0000\u00d9\u00da\u0003\n\u0005\u0000\u00da\u00db\u0005\u001a\u0000"+
		"\u0000\u00db\u00dc\u0003\u0014\n\u0000\u00dc\u00de\u0001\u0000\u0000\u0000"+
		"\u00dd\u00d8\u0001\u0000\u0000\u0000\u00de\u00e1\u0001\u0000\u0000\u0000"+
		"\u00df\u00dd\u0001\u0000\u0000\u0000\u00df\u00e0\u0001\u0000\u0000\u0000"+
		"\u00e0\u00e3\u0001\u0000\u0000\u0000\u00e1\u00df\u0001\u0000\u0000\u0000"+
		"\u00e2\u00e4\u0003\u001a\r\u0000\u00e3\u00e2\u0001\u0000\u0000\u0000\u00e3"+
		"\u00e4\u0001\u0000\u0000\u0000\u00e4\u00e6\u0001\u0000\u0000\u0000\u00e5"+
		"\u00e7\u0005\u001c\u0000\u0000\u00e6\u00e5\u0001\u0000\u0000\u0000\u00e6"+
		"\u00e7\u0001\u0000\u0000\u0000\u00e7)\u0001\u0000\u0000\u0000\u00e8\u00e9"+
		"\u0005\f\u0000\u0000\u00e9\u00ea\u0005\r\u0000\u0000\u00ea\u00ec\u0003"+
		"\b\u0004\u0000\u00eb\u00ed\u0003\u001a\r\u0000\u00ec\u00eb\u0001\u0000"+
		"\u0000\u0000\u00ec\u00ed\u0001\u0000\u0000\u0000\u00ed\u00ef\u0001\u0000"+
		"\u0000\u0000\u00ee\u00f0\u0005\u001c\u0000\u0000\u00ef\u00ee\u0001\u0000"+
		"\u0000\u0000\u00ef\u00f0\u0001\u0000\u0000\u0000\u00f0+\u0001\u0000\u0000"+
		"\u0000\u00185@FJS_mr\u0088\u008e\u0094\u0098\u00a1\u00aa\u00af\u00b5\u00b9"+
		"\u00bf\u00d0\u00df\u00e3\u00e6\u00ec\u00ef";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}