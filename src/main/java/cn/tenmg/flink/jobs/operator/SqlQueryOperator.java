package cn.tenmg.flink.jobs.operator;

import java.util.Map;

import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.tenmg.dsl.NamedScript;
import cn.tenmg.dsl.utils.DSLUtils;
import cn.tenmg.flink.jobs.context.FlinkJobsContext;
import cn.tenmg.flink.jobs.model.SqlQuery;
import cn.tenmg.flink.jobs.utils.SQLUtils;

/**
 * SQL操作执行器
 * 
 * @author 赵伟均 wjzhao@aliyun.com
 *
 * @since 1.1.0
 */
public class SqlQueryOperator extends AbstractSqlOperator<SqlQuery> {

	private static final Logger log = LogManager.getLogger(SqlQueryOperator.class);

	@Override
	Object execute(StreamTableEnvironment tableEnv, SqlQuery sqlQuery, Map<String, Object> params) throws Exception {
		NamedScript namedScript = DSLUtils.parse(sqlQuery.getScript(), params);
		String saveAs = sqlQuery.getSaveAs(), statement = SQLUtils.toSQL(namedScript);
		log.info(statement);
		Table table = tableEnv.sqlQuery(statement);
		String defaultCatalog = FlinkJobsContext.getDefaultCatalog(tableEnv);
		if (!defaultCatalog.equals(tableEnv.getCurrentCatalog())) {
			tableEnv.useCatalog(defaultCatalog);
		}
		tableEnv.createTemporaryView(saveAs, table);
		return table;
	}

}