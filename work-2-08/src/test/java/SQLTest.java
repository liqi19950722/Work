import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SQLTest {
    @Test
    public void should_get_insert_and_table_name() {
        String sql = """
                INSERT INTO test(id, name, age) VALUES(1, "张三", 18);
                """;
        SQLVisitor.SQLMetaData accept = getSqlMetaData(sql);
        assertEquals("insert", accept.sqlType);
        assertEquals("test", accept.tableName);
    }

    @Test
    public void should_get_delete_and_table_name() {
        String sql = """
                DELETE FROM test WHERE id = 1
                """;
        SQLVisitor.SQLMetaData accept = getSqlMetaData(sql);
        assertEquals("delete", accept.sqlType);
        assertEquals("test", accept.tableName);
    }

    @Test
    public void should_get_update_and_table_name() {
        String sql = """
                UPDATE test SET name="张三" WHERE id=1;
                """;
        SQLVisitor.SQLMetaData accept = getSqlMetaData(sql);
        assertEquals("update", accept.sqlType);
        assertEquals("test", accept.tableName);
    }

    private static SQLVisitor.SQLMetaData getSqlMetaData(String sql) {
        var baseSqlLexer = new BaseSqlLexer(CharStreams.fromString(sql));
        CommonTokenStream tokens = new CommonTokenStream(baseSqlLexer);
        var parser = new BaseSqlParser(tokens);
        ParseTree tree = parser.sqlDML();

        return tree.accept(new SQLVisitor());
    }
}
