/*
 * (c) Copyright 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.sdb.util;

import com.hp.hpl.jena.shared.PrefixMapping;

import com.hp.hpl.jena.sparql.ARQConstants;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.algebra.OpPrefixesUsed;
import com.hp.hpl.jena.sparql.algebra.OpVisitorBase;
import com.hp.hpl.jena.sparql.algebra.OpWalker;
import com.hp.hpl.jena.sparql.algebra.op.OpExt;
import org.openjena.atlas.io.IndentedWriter;

import com.hp.hpl.jena.query.Query;

import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.sdb.compiler.OpSQL;
import com.hp.hpl.jena.sdb.engine.QueryEngineSDB;



/** Print utilities */

public class PrintSDB
{
    public static String divider = "----------------" ;
    
    public static void print(Store store, Query query, QueryEngineSDB queryEngine)
    {
        if ( queryEngine == null )
            queryEngine = new QueryEngineSDB(store, query) ;
        Op op = queryEngine.getPlan().getOp() ;
        System.out.println(op.toString(query.getPrefixMapping())) ;
    }

    public static void print(Op op)
    { print(op, null) ; }

    
    public static void print(Op op, PrefixMapping pmap)
    {
        if ( pmap == null )
            pmap = OpPrefixesUsed.used(op, ARQConstants.getGlobalPrefixMap()) ;
        System.out.print(op.toString(pmap)) ;
    }
    
    public static void printSQL(Op op)
    {
        OpWalker.walk(op, new PrintSQL()) ;
    }
    
    public static void printSqlNodes(Op op)
    {
        OpWalker.walk(op, new PrintSqlNodes()) ;
    }

    static class PrintSqlNodes extends OpVisitorBase
    {
        boolean first = true ;
        private IndentedWriter out ;
        PrintSqlNodes(IndentedWriter out)
        { this.out = out ; }

        PrintSqlNodes()
        { this.out = new IndentedWriter(System.out) ; }
        
        @Override
        public void visit(OpExt op)
        {
            if ( ! ( op instanceof OpSQL ) )
            {
                super.visit(op) ;
                return ;
            }
            OpSQL opSQL = (OpSQL)op ;
            if ( ! first )
                out.println(divider) ;
            opSQL.output(out) ;
            out.ensureStartOfLine() ;
            out.flush();
            first = false ;
        }
    }

    
    static class PrintSQL extends OpVisitorBase
    {
        boolean first = true ;
        private IndentedWriter out ;
        PrintSQL(IndentedWriter out)
        { this.out = out ; }

        PrintSQL()
        { this.out = new IndentedWriter(System.out) ; }
        
        @Override
        public void visit(OpExt op)
        {
            if ( ! ( op instanceof OpSQL ) )
            {
                super.visit(op) ;
                return ;
            }
            OpSQL opSQL = (OpSQL)op ;
            
            
            if ( ! first )
                out.println(divider) ;
            out.print(opSQL.toSQL()) ;
            out.ensureStartOfLine() ;
            out.flush();

            first = false ;
        }
    }
    
}

/*
 * (c) Copyright 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */