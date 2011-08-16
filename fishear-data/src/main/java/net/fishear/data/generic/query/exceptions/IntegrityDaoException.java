package net.fishear.data.generic.query.exceptions;

import net.fishear.exceptions.AppException;

public class IntegrityDaoException extends AppException {

	private static final long serialVersionUID = 1L;
	private String tableName;
    private String columnName;

    public IntegrityDaoException(String message, Exception e, Object... params) {
        super(message, e, params);
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
