package cn.tenmg.flink.jobs.operator.support;

import java.util.HashSet;
import java.util.Set;

import cn.tenmg.dsl.utils.StringUtils;
import cn.tenmg.flink.jobs.context.FlinkJobsContext;
import cn.tenmg.flink.jobs.model.Operate;
import cn.tenmg.flink.jobs.operator.AbstractOperator;

/**
 * 支持SQL保留关键字的操作器抽象类。已废弃，将在下一个版本移除，请使用SQLUtils.wrapIfReservedKeywords替换
 * 
 * @author June wjzhao@aliyun.com
 *
 * @param <T>
 *            操作类型
 * @since 1.1.5
 */
@Deprecated
public abstract class SqlReservedKeywordSupport<T extends Operate> extends AbstractOperator<T> {

	protected static final String SQL_RESERVED_KEYWORD_WRAP_PREFIX = "`", SQL_RESERVED_KEYWORD_WRAP_SUFFIX = "`";

	protected static final Set<String> sqlReservedKeywords = new HashSet<String>();

	static {
		addReservedKeywords(FlinkJobsContext.getProperty("sql.reserved.keywords"));
		addReservedKeywords(FlinkJobsContext.getProperty("sql.custom.keywords"));
	}

	protected static String wrapIfReservedKeywords(String word) {
		if (word != null && sqlReservedKeywords.contains(word.toUpperCase())) {
			return SQL_RESERVED_KEYWORD_WRAP_PREFIX + word + SQL_RESERVED_KEYWORD_WRAP_SUFFIX;
		}
		return word;
	}

	private static void addReservedKeywords(String keywords) {
		if (StringUtils.isNotBlank(keywords)) {
			String[] words = keywords.split(",");
			for (int i = 0; i < words.length; i++) {
				sqlReservedKeywords.add(words[i].trim().toUpperCase());
			}
		}
	}

}