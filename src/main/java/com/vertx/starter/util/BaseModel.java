package com.vertx.starter.util;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class BaseModel {

	/**
	 * the name of the class represent the name of the table
	 */
	private String table_name = null;
//	private Future<SQLConnection> dbFuture = Future.future();

//	public Future<SQLConnection> getConnection() {
//		return dbFuture;
//	}

	private HashMap<String, Object> saveRecord = new HashMap<>();

	public BaseModel() {
		this.table_name = this.getClassName();
	}

	protected abstract HashMap<String, String> getColumns();

	protected abstract String getConstraint();

	/**
	 * this function creates a new table when it don't exist.
	 * 
	 * @return
	 *
	 * @return boolean
	 * @throws SQLException when something goes wrong
	 */
	public Future<Void> create() {
		Future<Void> future = Future.future();

		StringBuilder stm = new StringBuilder("CREATE TABLE IF NOT EXISTS " + getTable_name() + " (");
		HashMap<String, String> columns = getColumns();
		for (Object key : columns.keySet()) {
			stm.append(key).append(" ").append(columns.get(key)).append(",");
		}
		// get and add constrain;
		stm.append(getConstraint()).append(");");

		Constant.postgreSQLClient.getConnection(ar -> {
			SQLConnection conn = ar.result();
			conn.execute(stm.toString(), res -> {
				conn.close();
				future.complete(res.result());
				future.fail(res.cause());
			});
		});

		return future;
	}

	public Future<Void> exist() {
		Future<Void> future = Future.future();
		Constant.postgreSQLClient.getConnection(ar -> {
			SQLConnection conn = ar.result();
			conn.execute("SELECT CASE WHEN exists((SELECT * FROM information_schema.tables WHERE 'table_name' = \'"
					+ this.table_name + "\')) THEN 1 ELSE 0 END", rs -> {
						conn.close();
						future.complete(rs.result());
						future.fail(rs.cause());
					});
		});
		return future;
	}

	protected abstract String getClassName();
//	{
//		String name = this.getClass().getName();
//		String[] arrName = StringUtils.split(name, ".");
//		return (arrName[arrName.length - 1]).toLowerCase();
//	}

	private String getTable_name() {
		return this.table_name;
	}

	/**
	 * This function insert the records in the saveRecord HashMap. it first verifies
	 * that the all the columns are provided and then the insertion is not any other
	 * column not provided is set to null
	 *
	 * @return true when the insertion was done correctly and false otherwise
	 */
	public Future<ResultSet> save(boolean returnId) {
		Future<ResultSet> future = Future.future();

		StringBuilder insertStr = new StringBuilder("INSERT INTO " + getTable_name() + " ");
		StringBuilder keyStr = new StringBuilder("(");
		StringBuilder valueStr = new StringBuilder("(");

		for (Object key : saveRecord.keySet()) {
			keyStr.append(key).append(",");
			valueStr.append("?").append(",");
		}
		keyStr.replace(keyStr.length() - 1, keyStr.length(), ")");
		valueStr.replace(valueStr.length() - 1, valueStr.length(), ")");

		insertStr.append(keyStr.toString()).append(" VALUES ").append(valueStr.toString());
		if(returnId)
			insertStr.append(" RETURNING *;");
		else insertStr.append(";");
		

		// add values
		Set<String> keys = saveRecord.keySet();
		String[] keysArr = keys.toArray(new String[keys.size()]);
		JsonArray sqlParam = new JsonArray();
		for (int i = 0; i < keysArr.length; i++) {
			sqlParam.add(saveRecord.get(keysArr[i]));
		}

		// empty the saveRecord
		saveRecord = new HashMap<>();
		Constant.postgreSQLClient.getConnection(ar -> {
			SQLConnection conn = ar.result();
			conn.queryWithParams(insertStr.toString(), sqlParam, rs -> {
				conn.close();
				future.complete(rs.result());
				future.fail(rs.cause());
			});
		});
		return future;
	}

	public Future<Void> save(String str) {
		Future<Void> future = Future.future();
		Constant.postgreSQLClient.getConnection(ar -> {
			SQLConnection conn = ar.result();
			conn.execute("INSERT INTO " + getTable_name() + " " + str + ";", rs -> {
				conn.close();
				future.complete(rs.result());
				future.fail(rs.cause());
			});
		});
		return future;
	}

	public void set(String column, Object value) {
		this.saveRecord.put(column, value);
	}

	public boolean save(HashMap<String, String> record) {
		throw new NotImplementedException();
	}

	public Future<Void> save(String[] record) {
		Future<Void> future = Future.future();

		StringBuilder insertStr = new StringBuilder("INSERT INTO " + getTable_name() + " (");
		for (String val : record) {
			insertStr.append(val).append(",");
		}

		insertStr.replace(insertStr.length() - 1, insertStr.length(), ");");

		Constant.postgreSQLClient.getConnection(ar -> {
			SQLConnection conn = ar.result();
			conn.execute(insertStr.toString(), rs -> {
				conn.close();
				future.complete(rs.result());
				future.fail(rs.cause());
			});
		});
		return future;
	}

	public Future<ResultSet> find(String query) {
		Future<ResultSet> future = Future.future();
		Constant.postgreSQLClient.getConnection(ar -> {
			SQLConnection conn = ar.result();
			conn.query(query, row -> {
				conn.close();
				future.complete(row.result());
				future.fail(row.cause());
			});
		});
		return future;
	}

	public Future<ResultSet> find(String[] columns, String where, int limit) {
		Future<ResultSet> future = Future.future();
		StringBuilder findSQL = new StringBuilder("SELECT ");
		if (columns == null || columns.length == 0)
			findSQL.append("* ");
		else {
			for (String column : columns) {
				findSQL.append(column).append(",");
			}
			findSQL.delete(findSQL.length() - 1, findSQL.length()); // remove the last ,
		}

		findSQL.append(" FROM ").append(getTable_name());
		if (where != null) {
			findSQL.append(" WHERE ").append(where);
		}
		if (limit > 0) {
			findSQL.append(" LIMIT ").append(limit);
		}

		Constant.postgreSQLClient.getConnection(ar -> {
			SQLConnection conn = ar.result();
			conn.query(findSQL.toString(), row -> {
				conn.close();
				future.complete(row.result());
				future.fail(row.cause());
			});
		});
		return future;
	}

	public Future<ResultSet> findOne(String[] columns, String where) {
		Future<ResultSet> future = Future.future();
		StringBuilder findSQL = new StringBuilder("SELECT ");
		if (columns == null || columns.length == 0)
			findSQL.append("* ");
		else {
			for (String column : columns) {
				findSQL.append(column).append(",");
			}
			findSQL.delete(findSQL.length() - 1, findSQL.length()); // remove the last ,
		}

		findSQL.append(" FROM ").append(getTable_name());
		if (where != null) {
			findSQL.append(" WHERE ").append(where);
		}

//	        System.out.println(findSQL.toString());
		Constant.postgreSQLClient.getConnection(ar -> {
			SQLConnection conn = ar.result();
			conn.query(findSQL.toString(), row -> {
				conn.close();
				future.complete(row.result());
				future.fail(row.cause());
			});
		});
		return future;
	}

	public Future<Void> remove(String where) {
		Future<Void> future = Future.future();
		StringBuilder removeSQL = new StringBuilder("DELETE FROM ").append(getTable_name()).append(" WHERE ");
		if (where == null)
			throw new TypeNotPresentException("where need to be a valid where clause", null);
		else
			removeSQL.append(where);

		Constant.postgreSQLClient.getConnection(ar -> {
			SQLConnection conn = ar.result();
			conn.execute(removeSQL.toString(), res -> {
				conn.close();
				if (res.succeeded())
					future.complete();
				else
					future.fail(res.cause());
			});
		});
		return future;
	}

	public Future<UpdateResult> update(HashMap<String, ?> record, String id) {
		Future<UpdateResult> future = Future.future();

		if (record == null || record.size() == 0 || id.equals("0")) {
			throw new TypeNotPresentException("where need to be a valid where clause", null);
		}

		JsonArray params = new JsonArray();

		StringBuilder updateSQL = new StringBuilder("UPDATE ").append(getTable_name()).append(" SET ");
		Set<String> keys = record.keySet();
		for (String key : keys) {
			updateSQL.append(key).append(" = ").append("?").append(",");
			params.add(record.get(key));
		}

		updateSQL.delete(updateSQL.length() - 1, updateSQL.length()); // remove the last ,
		updateSQL.append(" WHERE id = ").append(id).append(";");
		Constant.postgreSQLClient.getConnection(ar -> {
			SQLConnection conn = ar.result();
			conn.updateWithParams(updateSQL.toString(), params, res -> {
				conn.close();
				future.complete(res.result());
				future.fail(res.cause());
			});
		});
		return future;
	}

	public Future<Void> truncate() {
		Future<Void> future = Future.future();
		Constant.postgreSQLClient.getConnection(ar -> {
			SQLConnection conn = ar.result();
			conn.execute("TRUNCATE " + getTable_name(), res -> {
				conn.close();
				if (res.succeeded())
					future.complete();
				else
					future.fail(res.cause());
			});
		});
		return future;
	}

//	    public boolean update(String query, int id) throws SQLException {
//	        StringBuilder updateSQL = new StringBuilder("UPDATE ").append(getTable_name()).append(query);
//	        PreparedStatement stm = this.connection.prepareStatement(updateSQL.toString());
//	    }
}
