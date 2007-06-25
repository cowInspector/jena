package com.hp.hpl.jena.sdb.layout2.hash;

import com.hp.hpl.jena.sdb.layout2.TableDescNodes;
import com.hp.hpl.jena.sdb.sql.SDBConnection;
import com.hp.hpl.jena.sdb.store.TableDesc;

public class TupleLoaderHashPGSQL extends TupleLoaderHashBase {

	public TupleLoaderHashPGSQL(SDBConnection connection, TableDesc tableDesc,
			int chunkSize) {
		super(connection, tableDesc, chunkSize);
	}
	
	public String[] getNodeColTypes() {
		return new String[] {"BIGINT", "TEXT", "VARCHAR(10)", "VARCHAR("+ TableDescNodes.DatatypeUriLength+ ")", "INT"};
	}
	
	public String getTupleColType() {
		return "BIGINT";
	}
	
	public String[] getCreateTempTable() {
		return new String[] { "CREATE TEMPORARY TABLE" , "ON COMMIT DELETE ROWS" };
	}
	
	@Override
	public String getClearTempNodes() {
		return null;
	}
	
	@Override
	public String getClearTempTuples() {
		return null;
	}
	
	@Override
	public String getNodeLoader() {
		return super.getNodeLoader() + hashCode();

	}
	
	@Override
	public String getTupleLoader() {
		return super.getTupleLoader() + hashCode();
	}
}
