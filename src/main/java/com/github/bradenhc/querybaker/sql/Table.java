package com.github.bradenhc.querybaker.sql;

import java.util.ArrayList;
import java.util.List;

import com.github.bradenhc.querybaker.api.IQueryBuilder;

public class Table implements IQueryBuilder {

	private String mName;
	private String mAlias;
	private List<Column> mColumns = new ArrayList<>();

	public Table(String name, List<Column> columns) {
		this(name);
		mColumns = columns;
	}

	public Table(String name) {
		mName = name;
	}

	public static Table create(String name) {
		return new Table(name);
	}

	public static Table create(String name, List<Column> columns) {
		return new Table(name, columns);
	}

	public Table alias(String alias) {
		if (mAlias != null) {
			// We are resetting/removing the alias. Remove the alias from all the column
			// names
			for (Column c : mColumns) {
				c.alias(null);
			}
		}
		if (alias != null) {
			// We are setting the alias for the first time
			for (Column c : mColumns) {
				c.alias(alias);
			}
		}
		mAlias = alias;
		return this;
	}

	public String alias() {
		return mAlias;
	}

	public Select select() {
		return new Select(this);
	}

	public Insert insert() {
		return new Insert(this);
	}

	public Update update() {
		return new Update(this);
	}

	public Delete delete() {
		return new Delete(this);
	}

	public String drop() {
		return "DROP TABLE " + mName;
	}

	public String truncate() {
		return "TRUNCATE TABLE " + mName;
	}

	public List<Column> columns() {
		return mColumns;
	}

	public Table columns(Column... columns) {
		for (Column c : columns) {
			if (mAlias != null) {
				c.alias(mAlias);
			}
			mColumns.add(c);
		}
		return this;
	}

	public Table name(String name) {
		mName = name;
		return this;
	}

	public String name() {
		return mName;
	}

	@Override
	public String build() {
		StringBuilder sb = new StringBuilder("CREATE TABLE ");
		sb.append(mName).append(" (");
		boolean first = true;
		for (Column c : mColumns) {
			if (first) {
				first = false;
				sb.append(c.toString());
			} else {
				sb.append(", ").append(c.toString());
			}
		}
		sb.append(");");
		return sb.toString();
	}

}