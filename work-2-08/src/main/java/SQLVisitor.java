import org.antlr.v4.runtime.tree.TerminalNode;

public class SQLVisitor extends BaseSqlBaseVisitor<SQLVisitor.SQLMetaData> {

    private SQLMetaData sqlMetaData = new SQLMetaData();

    @Override
    public SQLMetaData visitTable_name(BaseSqlParser.Table_nameContext ctx) {
        sqlMetaData.tableName = ctx.getText();
        return super.visitTable_name(ctx);
    }

    @Override
    protected SQLMetaData defaultResult() {
        return sqlMetaData;
    }

    @Override
    public SQLMetaData visitInsert(BaseSqlParser.InsertContext ctx) {
        sqlMetaData.sqlType = "insert";
        return super.visitInsert(ctx);
    }

    @Override
    public SQLMetaData visitDelete(BaseSqlParser.DeleteContext ctx) {
        sqlMetaData.sqlType = "delete";
        return super.visitDelete(ctx);
    }

    @Override
    public SQLMetaData visitUpdate(BaseSqlParser.UpdateContext ctx) {
        sqlMetaData.sqlType = "update";
        return super.visitUpdate(ctx);
    }

    static class SQLMetaData {

        String sqlType;
        String tableName;
    }
}
